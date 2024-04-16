
package scs.azacord.service;

import java.util.Vector;

import discord4j.core.object.entity.channel.GuildMessageChannel;
import discord4j.core.object.entity.User;

import scs.azacord.module.MessageCache;

public class Cache {

    // CURRENT CHANNEL ID
    private static String currentChannelId = "";
    public static synchronized String getCurrentChannelId () { return currentChannelId; }
    public static synchronized void setCurrentChannelId (String value) { currentChannelId = value; }
    // CURRENT CHANNEL NAME
    private static String currentChannelName = "";
    public static synchronized String getCurrentChannelName () { return currentChannelName; }
    public static synchronized void setCurrentChannelName (String value) { currentChannelName = value; }

    // OUTGOING MESSAGE QUEUE / MESSAGE SEND QUEUE
    private static Vector<MessageCache> messageSendQueue
        = new Vector<MessageCache>();
    public static synchronized void queueOutgoingMessage (String targetChannelId, String content) {
        MessageCache messageObject = new MessageCache();
        messageObject.targetChannelId = targetChannelId;
        messageObject.content = content;
        messageSendQueue.add(messageObject);
    }
    public static synchronized MessageCache popOutgoingMessage () {
        MessageCache messageObject = messageSendQueue.get(0);
        messageSendQueue.remove(0);
        return messageObject;
    }
    public static synchronized int outgoingMessageCount () {
        return messageSendQueue.size();
    }

    public class Discord {

        // SELF USER CACHE
        private static User selfUser;
        public static synchronized User getSelfUser () {
            return selfUser;
        }
        public static synchronized void setSelfUser (User user) {
            selfUser = user;
        }

        // GUILD CHANNEL CACHE
        private static Vector<GuildMessageChannel> channelCache
            = new Vector<GuildMessageChannel>();
        public static synchronized void addChannel (GuildMessageChannel channel) {
            if (!channelCache.contains(channel)) channelCache.add(channel);
        }
        public static synchronized void removeChannel (GuildMessageChannel subject) {
            int targetIndex = -1;
            for (int i = 0; i < channelCache.size(); ++i)
                if (channelCache.get(0).getId().equals(subject.getId()))
                    { targetIndex = i; break; }
            if (targetIndex != -1)
                channelCache.remove(targetIndex);
        }
        public static synchronized GuildMessageChannel getChannelById (String channelId) {

            for (var channel : channelCache)
                if (channel.getId().asString().equals(channelId))
                    return channel;
            return null;
        }
        public static synchronized GuildMessageChannel[] getChannels () {
            return channelCache.toArray(new GuildMessageChannel[channelCache.size()]);
        }
    }
}
