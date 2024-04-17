
package scs.azacord;

import java.lang.Thread;

import scs.azacord.service.Config;
import scs.azacord.service.Audio;
import scs.azacord.discord.Discord;
import scs.azacord.uinterface.Input;
import scs.azacord.uinterface.Display;

public class Main {

    public static void main (String[] args) {

        if (!Config.loadConfig()) {

            System.out.println("Failed to load Config file!");
            System.out.println("'" + Config.getConfigPath() + "' has been generated.");
            return;
        }

        Audio.playStart();

        Input.start();
        Discord.start();

        while (Discord.isRunning()) {

            Display.tick();

            try { Thread.sleep(10); } catch (Exception e) {}
        }
    }
}
