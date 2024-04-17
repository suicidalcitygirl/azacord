
package scs.azacord.service;

public class Audio {

    public static void playPing() {

        playSound(Config.getPingSound());
    }
    public static void playType() {

        playSound(Config.getTypeSound());
    }
    public static void playStart() {

        playSound(Config.getStartSound());
    }
    public static void playSwitchChannel() {

        playSound(Config.getSwitchChannelSound());
    }
    public static void playTypeAlert() {

        playSound(Config.getTypeAlertSound());
    }
    public static void playQuit() {

        playSoundWait(Config.getQuitSound());
    }

    private static void playSound (String filePath) {

        if (filePath.equals("")) return;
        if (filePath.equals("NULL")) return;

        Systemcall.playSound(filePath);
    }
    private static void playSoundWait (String filePath) {

        if (filePath.equals("")) return;
        if (filePath.equals("NULL")) return;

        Systemcall.playSoundWait(filePath);
    }
}
