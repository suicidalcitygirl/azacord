
package scs.azacord.service;

public class Audio {

    public static void playPing() {

        playSound(Config.getPingSound());
    }
    public static void playType() {

        playSound(Config.getTypeSound());
    }

    private static void playSound (String filePath) {

        if (filePath.equals("")) return;

        Systemcall.playSound(filePath);
    }
}
