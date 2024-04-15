
package scs.azacord.uinterface;

import java.lang.Thread;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import scs.azacord.discord.Discord;
import scs.azacord.service.Cache;
import scs.azacord.service.Systemcall;
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

                switch (input) {

                    case 127: {
                        String v = getValue();
                        if (v.length() > 0)
                            setValue(v.substring(0, v.length() - 1));
                        else System.out.print("  \b\b\b\b");
                    break; }

                    case 13: onReturn(); break;

                    case 27: Action.quit(); break;

                    default: setValue(getValue() + (char)input); break;
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private static void onReturn () {

        if (getValue().length() == 0) { System.out.print("  \b\b\b\b"); return; }

        if (getValue().charAt(0) == '/') scs.azacord.action.Command.exec(getValue());
        else Cache.queueOutgoingMessage(Cache.getCurrentChannelId(), getValue().replace("\\n","\n"));

        setValue("");
    }
}
