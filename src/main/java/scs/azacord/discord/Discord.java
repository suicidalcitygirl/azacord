
package scs.azacord.discord;

import java.lang.Thread;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.guild.GuildCreateEvent;

import scs.azacord.service.Config;
import scs.azacord.service.Cache;
import scs.azacord.module.MessageCache;
import scs.azacord.uinterface.Display;

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

        Display.append("System", "Connecting to Discord..");

        String token = Config.getToken();
        DiscordClient client = DiscordClient.create(token);
        GatewayDiscordClient gateway = client.login().block();

        gateway.on(MessageCreateEvent.class).subscribe(event -> {
            Events.onMessageCreateEvent(event);
        });
        gateway.on(GuildCreateEvent.class).subscribe(event -> {
            Events.onGuildCreateEvent(event);
        });

        Display.append("System", "Connected succesfully!");

        while (!shouldStop) { try {

            Thread.sleep(500);

            if (Cache.outgoingMessageCount() != 0) {

                MessageCache messageObject = Cache.popOutgoingMessage();

                if (messageObject.targetChannelId == "") continue;

                Cache.Discord.getChannelById(
                    messageObject.targetChannelId
                ).createMessage(messageObject.content).block();
            }

        } catch (Exception e) {} }

        gateway.logout().block();
        running = false;
    }
}
