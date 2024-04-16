
package scs.azacord.action;

import java.time.Instant;

import discord4j.common.util.Snowflake;

import scs.azacord.uinterface.Display;
import scs.azacord.service.Cache;
import scs.azacord.service.Systemcall;

public class Command {

    public static void exec (String input) {

        Display.append("System", "Executing: " + input);

        String[] args = input.contains(" ") ? input.split(" ") : new String[]{input};

        switch (args[0]) {

            case "/ls": listChannels(); break;

            case "/join": case "/j": setChannel(args); break;

            case "/exit": case "/quit": Action.quit(); break;

            case "/clear": Display.clear(); break;

            case "/time": case "/date": printDate(); break;

            default: Display.append("System", "Unknown Command!"); break;
        }
    }

    private static void listChannels () {

        Display.append("::CHANNEL LIST::");
        var channels = Cache.Discord.getChannels();
        for (int i = 0; i < channels.length; ++i)
            Display.append(i + ": " + channels[i].getName());
    }

    private static void setChannel (String[] args) {

        if (args.length < 2) return;

        int index = -1;
        try { index = Integer.parseInt(args[1]);
        } catch (Exception e) { return; }

        Cache.setCurrentChannelId(
            Cache.Discord.getChannels()[index].getId().asString()
        );
        Cache.setCurrentChannelName(
            Cache.Discord.getChannels()[index].getName()
        );

        Display.clear();

        Snowflake now = Snowflake.of(Instant.now());
        var messages = Cache.Discord.getChannelById(Cache.getCurrentChannelId())
            .getMessagesBefore(now).take(100).collectList().block();
        for (int i = messages.size() - 1; i >= 0; --i) {
            Display.append(
                messages.get(i).getAuthor().get().getUsername(),
                messages.get(i).getContent()
            );
            for (var attachement : messages.get(i).getAttachments()) {
                Display.append(" ─ " + attachement.getUrl());
            }
        }
    }

    private static void printDate () {

        Display.append(Systemcall.getDateTime());
    }
}
