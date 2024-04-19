
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

    private static boolean disabled = false;
    public static boolean disabled () { return disabled; }
    public static void toggleSound () { disabled = !disabled; }

    private static void playSound (String filePath) {

        if (disabled) return;
        if (filePath.equals("")) return;
        if (filePath.equals("NULL")) return;

        Systemcall.playSound(filePath);
    }
    private static void playSoundWait (String filePath) {

        if (disabled) return;
        if (filePath.equals("")) return;
        if (filePath.equals("NULL")) return;

        Systemcall.playSoundWait(filePath);
    }
}
