
package scs.azacord.discord;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.guild.GuildCreateEvent;
import discord4j.core.event.domain.channel.TypingStartEvent;
import discord4j.core.event.domain.lifecycle.ReadyEvent;

import scs.azacord.service.Cache;
import scs.azacord.uinterface.Display;

public class Events {

    public static void onReadyEvent (ReadyEvent event) {

        var self = event.getSelf();
        Cache.Discord.setSelfUser(self);

        Display.append("System", "Session: " + event.getSessionId());
        Display.append("System", "Logged in as '" + self.getUsername() + "'");
    }

    public static void onMessageCreateEvent (MessageCreateEvent event) {

        Message message = event.getMessage();
        MessageChannel channel = message.getChannel().block();
        if (message.getAuthor().isEmpty()) return;
        if (channel.getId().asString().equals(Cache.getCurrentChannelId())) {

            Display.append(
                message.getAuthor().get().getUsername(),
                message.getContent()
            );
            for (var attachement : message.getAttachments()) {
                Display.append(" ─ " + attachement.getUrl());
            }

            Display.removeTyper(message.getAuthor().get().getUsername());
        }
    }

    public static void onGuildCreateEvent (GuildCreateEvent event) {

        for (var channel : event.getGuild().getChannels().collectList().block()) {

            if (!channel.getType().equals(Channel.Type.GUILD_TEXT)) continue;
            GuildMessageChannel gmChannel = (GuildMessageChannel)channel;

            Cache.Discord.addChannel(gmChannel);
        }
    }

    public static void onTypingStartEvent (TypingStartEvent event) {

        String username = event.getUser().block().getUsername();
        if (username.isEmpty()) return;
        if (Cache.Discord.getSelfUser().getId().equals(event.getUser().block().getId()))
            return;

        MessageChannel channel = event.getChannel().block();
        if (channel.getId().asString().equals(Cache.getCurrentChannelId()))
            Display.addTyper(username);
    }
}
