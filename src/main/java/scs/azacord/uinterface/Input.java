
package scs.azacord.uinterface;

import java.lang.Thread;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import scs.azacord.discord.Discord;
import scs.azacord.service.Cache;
import scs.azacord.service.Systemcall;
import scs.azacord.service.Audio;
import scs.azacord.action.Action;

public class Input {

    private static String value = "";
    public static synchronized String getValue () { return value; }
    public static synchronized void setValue (String v) { value = v; }

    public static void start () {

        mainThread = new Thread () {
            public void run () { loop(); }
        };
        mainThread.start();
    }
    public static void stop () {

        mainThread.interrupt();
        Systemcall.canonicalEnable();
    }

    private static Thread mainThread;

    public static void loop () {

        if (Systemcall.canonicalDisable() != 0) {

            System.out.println("FAILED TO DISABLE CANONICAL TERMINAL!");
            System.out.println("EXIT!");
            System.exit(-3);
        }

        BufferedReader bufferedReader
            = new BufferedReader(new InputStreamReader(System.in));
        int input = 0;

        try {

            while (!mainThread.isInterrupted()) {

                if (bufferedReader.ready())
                    input = bufferedReader.read();
                else { Thread.sleep(1); continue; }

                Audio.playType();

                switch (input) {

                    case 127: {
                        String v = getValue();
                        if (v.length() > 0)
                            setValue(v.substring(0, v.length() - 1));
                        else System.out.print("  \b\b\b\b");
                    break; }

                    case 13: typeStop(); onReturn(); break;

                    case 27: Action.quit(); break;

                    default: setValue(getValue() + (char)input); typeStart(); break;
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private static String processMentions (String message) {

        String[] words = message.contains(" ") ? message.split(" ") : new String[]{message};
        for (String word : words) {
            if (word.equals("")) continue;
            if (word.charAt(0) == '@' && word.length() > 1) {
                var user = Cache.Discord.getUserByName(word.substring(1));
                if (user != null) message = message.replace(word, "<@" + user.getId().asString() + ">");
            }
        }
        return message;
    }

    private static String processFormatting (String message) { return message.replace("\\n","\n"); }

    private static void onReturn () {

        if (getValue().length() == 0) { System.out.print("  \b\b\b\b"); return; }

        if (getValue().charAt(0) == '/') scs.azacord.action.Command.exec(getValue());
        else Cache.queueOutgoingMessage(
            Cache.getCurrentChannelId(),
            processFormatting(processMentions(getValue()))
        );

        setValue("");
    }

    private static boolean typingActive = false;
    private static void typeStart () { try {

        if (typingActive) return;
        if (Cache.getCurrentChannelId().equals("")) return;
        if (getValue().charAt(0) == '/') return;

        try { Cache.Discord.getChannelById(Cache.getCurrentChannelId()).type().block();
        } catch (Exception e) {}
        typingActive = true;

    } catch (Exception e) {} }
    private static void typeStop () { typingActive = false; }
}
