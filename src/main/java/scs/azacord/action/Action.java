
package scs.azacord.action;

import scs.azacord.discord.Discord;
import scs.azacord.uinterface.Display;
import scs.azacord.uinterface.Input;
import scs.azacord.service.Systemcall;
import scs.azacord.service.Audio;

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
}
