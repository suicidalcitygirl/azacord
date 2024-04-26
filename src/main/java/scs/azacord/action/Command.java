
package scs.azacord.action;

import java.time.Instant;
import java.io.File;
import java.io.FileInputStream;

import discord4j.common.util.Snowflake;

import scs.azacord.uinterface.Display;
import scs.azacord.service.Cache;
import scs.azacord.service.Systemcall;
import scs.azacord.service.Audio;
import scs.azacord.service.ConsoleColors;
import scs.azacord.service.Config;

public class Command {

    public static void exec (String input) {

        Display.system("Executing: " + input);

        String[] args = input.contains(" ") ? input.split(" ") : new String[]{input};

        switch (args[0]) {

            case "/ls": listChannels(); break;

            case "/dms": case "/pms": listUsers(); break;

            case "/s": case "/k": case "/search": search(args); break;

            case "/join": case "/j": setChannel(args); break;

            case "/dm": case "/pm": setChannelDM(args); break;

            case "/exit": case "/quit": Action.quit(); break;

            case "/clear": Display.clear(); break;

            case "/time": case "/date": printDate(); break;

            case "/upload": case "/attach": case "/a": attach(args); break;

            case "/colors": case "/color": colorTest(); break;

            case "/togglesound": toggleSound(); break;

            case "/profile": case "/pf": case "/p": showProfile(args); break;

            case "/notifications": case "/n": showNotifications(); break;

            case "/togglemessageaction": case "/togglema": toggleMessageAction(); break;

            default: Display.append(ConsoleColors.Red() + "Unknown Command!" + ConsoleColors.Reset()); break;
        }
    }

    private static void listChannels () {

        Display.append(ConsoleColors.White() + "::CHANNEL LIST::" + ConsoleColors.Reset());
        var channels = Cache.Discord.getChannels();
        for (int i = 0; i < channels.length; ++i)
            Display.append(
                ConsoleColors.White() + i + ConsoleColors.Reset() + ": "
                + channels[i].getGuild().block().getName()
                + "::" + channels[i].getName()
            );
    }

    private static void listUsers () {

        var users = Cache.Discord.getUsers();
        String message = "";
        for (int i = 0; i < users.length; ++i)
            message +=
                ConsoleColors.White() + i + ConsoleColors.Reset() + ": "
                + users[i].getUsername() + ", "
            ;

        int consoleWidth = Systemcall.getConsoleWidth();
        if (message.length() > consoleWidth)
            for (int i = 0; i < (int)(message.length() / consoleWidth); ++i)
                Display.append("");

        Display.append(ConsoleColors.White() + "::PRIVATE MESSAGE LIST::" + ConsoleColors.Reset());
        Display.append(message);
    }

    private static void search (String[] args) {

        if (args.length < 2) return;
        String query = args[1];

        var channels = Cache.Discord.getChannels();
        String[] channelList = new String[channels.length];
        for (int i = 0; i < channels.length; ++i)
            channelList[i] =
                ConsoleColors.White() + i + ConsoleColors.Reset() + ": "
                + channels[i].getGuild().block().getName()
                + "::" + channels[i].getName()
            ;

        var users = Cache.Discord.getUsers();
        String[] dmList = new String[users.length];
        for (int i = 0; i < users.length; ++i)
            dmList[i] =
                ConsoleColors.White() + i + ConsoleColors.Reset() + ": "
                + users[i].getUsername()
            ;

        Display.append(ConsoleColors.White() + "::CHANNEL SEARCH RESULTS::" + ConsoleColors.Reset());
        for (String entry : channelList) if (entry.toLowerCase().contains(query.toLowerCase())) Display.append(entry);

        Display.append(ConsoleColors.White() + "::USER DM SEARCH RESULTS::" + ConsoleColors.Reset());
        for (String entry : dmList) if (entry.toLowerCase().contains(query.toLowerCase())) Display.append(entry);
    }

    private static void setChannel (String[] args) {

        if (args.length < 2) return;

        int index = -1;
        try { index = Integer.parseInt(args[1]);
        } catch (Exception e) { return; }

        if (index >= Cache.Discord.getChannels().length) return;

        Cache.setCurrentChannelId(
            Cache.Discord.getChannels()[index].getId().asString()
        );
        Cache.setCurrentChannelName(
            Cache.Discord.getChannels()[index].getName()
        );
        Cache.setCurrentChannelIsDM(false);

        try {

            Display.clear();
            Display.clearTypists();
            Audio.playSwitchChannel();

            Snowflake now = Snowflake.of(Instant.now());
            var messages = Cache.Discord.getChannelById(Cache.getCurrentChannelId())
                .getMessagesBefore(now).take(100).collectList().block();
            for (int i = messages.size() - 1; i >= 0; --i) {
                Display.append(
                    messages.get(i).getAuthor().get().getUsername(),
                    messages.get(i).getContent()
                );
                for (var attachement : messages.get(i).getAttachments()) {
                    Display.append(
                        ConsoleColors.White() + " ─ " +
                        ConsoleColors.Cyan() + attachement.getUrl() + ConsoleColors.Reset()
                    );
                }
            }
            if (messages.size() == 0)
                Display.append(
                    "This beginning of #"
                    + ConsoleColors.White()
                    + Cache.Discord.getChannels()[index].getName()
                    + ConsoleColors.Reset() + "."
                );

        } catch (Exception e) {

            Cache.setCurrentChannelId("");
            Cache.setCurrentChannelName("");

            Display.system(
                ConsoleColors.Red()
                + "Failed to join '" + Cache.Discord.getChannels()[index].getName() + "'"
                + ConsoleColors.Reset()
            );
            Display.append("You have probably tried to join a channel you don't have access to..");
            Display.append("");
            Display.append("    You're not in a very strange place; .. You're nowhere ....");
        }
    }

    private static void setChannelDM (String[] args) {

        if (args.length < 2) return;

        int index = -1;
        try { index = Integer.parseInt(args[1]);
        } catch (Exception e) { return; }

        if (index >= Cache.Discord.getUsers().length) return;

        try {

            var pmChannel = Cache.Discord.getUsers()[index].getPrivateChannel().block();
            Cache.Discord.addPrivateChannel(pmChannel);

            Cache.setCurrentChannelId(pmChannel.getId().asString());
            Cache.setCurrentChannelName(Cache.Discord.getUsers()[index].getUsername());
            Cache.setCurrentChannelIsDM(true);

            Display.clear();
            Display.clearTypists();
            Audio.playSwitchChannel();

            Snowflake now = Snowflake.of(Instant.now());
            var messages = Cache.Discord.getPrivateChannelById(Cache.getCurrentChannelId())
                .getMessagesBefore(now).take(100).collectList().block();
            for (int i = messages.size() - 1; i >= 0; --i) {
                Display.append(
                    messages.get(i).getAuthor().get().getUsername(),
                    messages.get(i).getContent()
                );
                for (var attachement : messages.get(i).getAttachments()) {
                    Display.append(
                        ConsoleColors.White() + " ─ " +
                        ConsoleColors.Cyan() + attachement.getUrl() + ConsoleColors.Reset()
                    );
                }
            }
            if (messages.size() == 0)
                Display.append(
                    "This beginning of your DM with "
                    + ConsoleColors.White()
                    + Cache.Discord.getUsers()[index].getUsername()
                    + ConsoleColors.Reset() + "."
                );

        } catch (Exception e) {

            Cache.setCurrentChannelId("");
            Cache.setCurrentChannelName("");

            Display.system(
                ConsoleColors.Red()
                + "Failed to join DM with '" + Cache.Discord.getUsers()[index].getUsername() + "'"
                + ConsoleColors.Reset()
            );
            Display.append("I don't actualy know exactly this has happened..");
            Display.append("");
            Display.append("    You're not in a very strange place; .. You're nowhere ....");
        }
    }

    private static void printDate () {

        Display.append(Systemcall.getDateTime());
    }

    private static void attach (String[] args) {

        if (args.length < 2) return;

        if (Config.getDisableAttachements()) {
            Display.append(ConsoleColors.Red() + "Attachements are disabled!" + ConsoleColors.Reset());
            return;
        }

        if (args.length > 2) { if (args[1].equals("s") || args[1].equals("-s")) {


            try {
                Cache.Discord.getChannelById(Cache.getCurrentChannelId())
                    .createMessage(messageSpec -> {

                    String filePath = args[2];
                    if (args.length > 3) for (int i = 3; i < args.length; ++i)
                        filePath += " " + args[i];

                    try {
                        File attachementFile = new File(filePath);
                        if (attachementFile.exists() && !attachementFile.isDirectory())
                            messageSpec.addFile(
                                attachementFile.getName(),
                                new FileInputStream(attachementFile)
                            );
                    } catch (Exception e) {}

                }).block();

            } catch (Exception e) {}

        } } else {

            try {
                Cache.Discord.getChannelById(Cache.getCurrentChannelId())
                    .createMessage(messageSpec -> {

                    for (int i = 1; i < args.length; ++i) {
                        try {
                            File attachementFile = new File(args[i]);
                            if (!(attachementFile.exists() && !attachementFile.isDirectory()))
                                continue;

                            messageSpec.addFile(
                                attachementFile.getName(),
                                new FileInputStream(attachementFile)
                            );
                        } catch (Exception e) {}
                    }

                }).block();

            } catch (Exception e) {}
        }
    }

    private static void colorTest () {

        if (!Config.getUseColors()) {

            Display.append("Colors are disabled!");
            Display.append("You can enable them in the Configuration file.");
            Display.append("Located: '" + Config.getConfigPath() + "'.");
            return;
        }

        Display.append(ConsoleColors.Black() + "BLACK" + ConsoleColors.Reset() + "   " + ConsoleColors.Black_BG() + "BLACK" + ConsoleColors.Reset());
        Display.append(ConsoleColors.Red() + "RED" + ConsoleColors.Reset() + "       " + ConsoleColors.Red_BG() + "RED" + ConsoleColors.Reset());
        Display.append(ConsoleColors.Green() + "GREEN" + ConsoleColors.Reset() + "   " + ConsoleColors.Green_BG() + "GREEN" + ConsoleColors.Reset());
        Display.append(ConsoleColors.Yellow() + "YELLOW" + ConsoleColors.Reset() + " " + ConsoleColors.Yellow_BG() + "YELLOW" + ConsoleColors.Reset());
        Display.append(ConsoleColors.Blue() + "BLUE" + ConsoleColors.Reset() + "     " + ConsoleColors.Blue_BG() + "BLUE" + ConsoleColors.Reset());
        Display.append(ConsoleColors.Purple() + "PURPLE" + ConsoleColors.Reset() + " " + ConsoleColors.Purple_BG() + "PURPLE" + ConsoleColors.Reset());
        Display.append(ConsoleColors.Cyan() + "CYAN" + ConsoleColors.Reset() + "     " + ConsoleColors.Cyan_BG() + "CYAN" + ConsoleColors.Reset());
        Display.append(ConsoleColors.White() + "WHITE" + ConsoleColors.Reset() + "   " + ConsoleColors.White_BG() + "WHITE" + ConsoleColors.Reset());
    }

    private static void toggleSound () {

        Audio.toggleSound();
        Display.append("Sounds: " +
            (
                Audio.disabled()
                ? ConsoleColors.Red() + "disabled" + ConsoleColors.Reset()
                : ConsoleColors.Green() + "enabled" + ConsoleColors.Reset()
            )
        );
    }

    private static void toggleMessageAction () {

        Action.toggleMessageAction();
        Display.append("MessageAction: " +
            (
                Action.getMessageActionDisabled()
                ? ConsoleColors.Red() + "disabled" + ConsoleColors.Reset()
                : ConsoleColors.Green() + "enabled" + ConsoleColors.Reset()
            )
        );
    }

    private static void showProfile (String[] args) {

        if (args.length < 2) return;

        var user = Cache.Discord.getUserByName(args[1]);
        if (user == null) return;

        Display.append(
            "GlobalName: " + ConsoleColors.White()
            + user.getGlobalName() + ConsoleColors.Reset()
        );
        Display.append(
            "Username: " + ConsoleColors.White()
            + user.getUsername() + ConsoleColors.Reset()
        );
        Display.append(
            "IsBot: " + ConsoleColors.White()
            + (user.isBot() ? "true" : "false") + ConsoleColors.Reset()
        );
        Display.append(
            "Id: " + ConsoleColors.White()
            + user.getId().asString() + ConsoleColors.Reset()
        );
        Display.append(
            "Avatar: " + ConsoleColors.Cyan()
            + user.getAvatarUrl() + ConsoleColors.Reset()
        );
        Display.append(
            "Banner: " + ConsoleColors.Cyan()
            + (user.getBannerUrl().isEmpty() ? "" : user.getBannerUrl().get())
            + ConsoleColors.Reset()
        );
    }

    private static void showNotifications () {

        if (Cache.getNotificationCount() == 0) {

            Display.append("There are no notifications currently.");
            return;
        }

        for (String notification : Cache.getNotifications()) {

            Display.append(notification);
        }

        Cache.clearNotifications();
    }
}
