
package scs.azacord.discord;

import java.lang.Thread;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.guild.GuildCreateEvent;
import discord4j.core.event.domain.channel.TypingStartEvent;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.gateway.intent.IntentSet;

import scs.azacord.service.Config;
import scs.azacord.service.Cache;
import scs.azacord.service.ConsoleColors;
import scs.azacord.module.MessageCache;
import scs.azacord.uinterface.Display;
import scs.azacord.action.Action;

public class Discord {

    public static void start () {

        shouldStop = false;
        running = true;

        mainThread = new Thread () {
            public void run () { loop(); }
        };
        mainThread.start();
    }
    public static void stop () {

        shouldStop = true;
    }
    public static void waitForStop () {

        stop();

        try {
            while (running) Thread.sleep(10);
        } catch (Exception e) {}
    }

    private static boolean running, shouldStop;
    public static boolean isRunning () { return running; }

    private static Thread mainThread = null;

    public static void loop () {

        Display.system("Connecting to Discord..");

        try {

            String token = Config.getToken();
            GatewayDiscordClient gateway = DiscordClient.create(token)
                .gateway().setEnabledIntents(IntentSet.all()).login().block();

            gateway.on(ReadyEvent.class).subscribe(event -> {
                Events.onReadyEvent(event);
            });
            gateway.on(MessageCreateEvent.class).subscribe(event -> {
                Events.onMessageCreateEvent(event);
            });
            gateway.on(GuildCreateEvent.class).subscribe(event -> {
                Events.onGuildCreateEvent(event);
            });
            gateway.on(TypingStartEvent.class).subscribe(event -> {
                Events.onTypingStartEvent(event);
            });

            Display.system("Connected succesfully!");

            while (!shouldStop) { try {

                Thread.sleep(500);

                if (Cache.outgoingMessageCount() != 0) {

                    MessageCache messageObject = Cache.popOutgoingMessage();

                    if (messageObject.targetChannelId == "") continue;

                    try {

                        if (Cache.getCurrentChannelIsDM())
                            Cache.Discord.getPrivateChannelById(
                                messageObject.targetChannelId
                            ).createMessage(messageObject.content).block();
                        else
                            Cache.Discord.getChannelById(
                                messageObject.targetChannelId
                            ).createMessage(messageObject.content).block();

                    } catch (Exception e) {

                        Display.system(
                            ConsoleColors.Red()
                            + "Failed to send message!"
                            + ConsoleColors.Reset()
                        );
                        Display.system(
                            ConsoleColors.Red()
                            + e.toString()
                            + ConsoleColors.Reset()
                        );
                    }
                }

            } catch (Exception e) {} }

            gateway.logout().block();
            running = false;

        } catch (Exception e) {

            running = false;
            Display.system(ConsoleColors.Red() + e + ConsoleColors.Reset());
            Display.tick();
            Action.quit();
        }
    }
}
