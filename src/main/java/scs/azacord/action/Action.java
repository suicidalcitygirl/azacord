
package scs.azacord.action;

import scs.azacord.discord.Discord;
import scs.azacord.uinterface.Display;
import scs.azacord.uinterface.Input;
import scs.azacord.service.Systemcall;
import scs.azacord.service.Audio;
import scs.azacord.service.Config;

public class Action {

    public static void quit () {

        Display.system("Exiting...");
        Discord.stop();
        Input.stop();
        Display.tick();

        Discord.waitForStop();

        Audio.playQuit();

        Systemcall.canonicalEnable();
        System.exit(0);
    }

    private static boolean disableMessageAction = false;
    public static void toggleMessageAction () { disableMessageAction = !disableMessageAction; }
    public static boolean getMessageActionDisabled () { return disableMessageAction; }

    public static void performOnMessageAction (String author, String message) {

        if (disableMessageAction) return;

        String reggex = Config.getOnLocalChannelRecvCommand();
        if (reggex.equals("") || reggex.equals("NULL")) return;

        author = author.replace("<","");
        author = author.replace(">","");
        author = author.replace("|","");
        author = author.replace(";","");
        author = author.replace("&","");
        author = author.replace("'","");
        author = author.replace("\"","");
        author = author.replace("$","");
        author = author.replace("!","");

        message = message.replace("<","");
        message = message.replace(">","");
        message = message.replace("|","");
        message = message.replace(";","");
        message = message.replace("&","");
        message = message.replace("'","");
        message = message.replace("\"","");
        message = message.replace("$","");
        message = message.replace("!","");

        reggex = reggex.replace("$1", author);
        reggex = reggex.replace("$2", message);

        Systemcall.executeDetach(new String[]{"/bin/bash","-c",reggex});
    }
}
