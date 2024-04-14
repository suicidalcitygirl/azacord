
package scs.azacord.discord;

import discord4j.core.object.entity.Message;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.event.domain.guild.GuildCreateEvent;
import discord4j.core.object.entity.channel.Channel;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.GuildMessageChannel;

import scs.azacord.service.Cache;
import scs.azacord.uinterface.Display;

public class Events {

    public static void onMessageCreateEvent (MessageCreateEvent event) {

        Message message = event.getMessage();
        MessageChannel channel = message.getChannel().block();
        if (message.getAuthor().isEmpty()) return;
        if (channel.getId().asString().equals(Cache.getCurrentChannelId())) {
            Display.append(
                message.getAuthor().get().getUsername(),
                message.getContent().isEmpty() ? "<empty>" : message.getContent()
            );
        }
    }

    public static void onGuildCreateEvent (GuildCreateEvent event) {

        for (var channel : event.getGuild().getChannels().collectList().block()) {

            if (!channel.getType().equals(Channel.Type.GUILD_TEXT)) continue;
            GuildMessageChannel gmChannel = (GuildMessageChannel)channel;

            Cache.Discord.addChannel(gmChannel);
        }
    }
}
