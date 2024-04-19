
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

            case "/join": case "/j": setChannel(args); break;

            case "/exit": case "/quit": Action.quit(); break;

            case "/clear": Display.clear(); break;

            case "/time": case "/date": printDate(); break;

            case "/upload": case "/attach": case "/a": attach(args); break;

            case "/colors": case "/color": colorTest(); break;

            case "/togglesound": toggleSound(); break;

            default: Display.append(ConsoleColors.Red() + "Unknown Command!" + ConsoleColors.Reset()); break;
        }
    }

    private static void listChannels () {

        Display.append("::CHANNEL LIST::");
        var channels = Cache.Discord.getChannels();
        for (int i = 0; i < channels.length; ++i)
            Display.append(
                ConsoleColors.White() + i + ConsoleColors.Reset() + ": "
                + channels[i].getGuild().block().getName()
                + "::" + channels[i].getName()
            );
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

    private static void printDate () {

        Display.append(Systemcall.getDateTime());
    }

    private static void attach (String[] args) {

        if (args.length < 2) return;

        if (Config.getDisableAttachements()) {
            Display.append(ConsoleColors.Red() + "Attachements are disabled!" + ConsoleColors.Reset());
            return;
        }

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
}
