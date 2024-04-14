
package scs.azacord.action;

import scs.azacord.discord.Discord;
import scs.azacord.uinterface.Display;
import scs.azacord.uinterface.Input;
import scs.azacord.service.Systemcall;

public class Action {

    public static void quit () {

        Display.append("System", "Exiting...");
        Discord.stop();
        Input.stop();
        Display.tick();

        Discord.waitForStop();
        
        Systemcall.canonicalEnable();
        System.exit(0);
    }
}
