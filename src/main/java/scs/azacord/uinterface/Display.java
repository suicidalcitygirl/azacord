
package scs.azacord.uinterface;

import java.util.Vector;

import scs.azacord.service.Systemcall;
import scs.azacord.service.Cache;
import scs.azacord.uinterface.Input;

public class Display {

    private static Object mutex = new Object();

    private static Vector<String> screenBuffer = new Vector<String>();

    public static void append (String sender, String message) { synchronized (mutex) {

        screenBuffer.add("[" + sender + "]: " + message);
    } }

    public static void append (String message) { synchronized (mutex) {

        screenBuffer.add(message);
    } }

    public static void clear () { synchronized (mutex) {

        screenBuffer.clear();
    } }

    public static void tick () {

        runChecks();
        timer++; if (timer > 100) {
            runOccasionalChecks(); timer = 0; }

    }
    private static int timer;

    private static String inputCache;
    private static int screenBufferCache, widthCache, heightCache;

    private static void runChecks () {

        if (screenBufferCache != screenBuffer.size()) {
            screenBufferCache = screenBuffer.size(); render();
        }

        if (inputCache != Input.getValue()) {
            inputCache = Input.getValue(); render();
        }
    }

    private static void runOccasionalChecks () {

        int widthResult = Systemcall.getConsoleWidth();
        if (widthCache != widthResult) {
            widthCache = widthResult; render();
        }

        int heightResult = Systemcall.getConsoleHeight();
        if (heightCache != heightResult) {
            heightCache = heightResult; render();
        }
    }

    private static boolean cursorToggle = false;

    private static void render () { synchronized (mutex) {

        int width = Systemcall.getConsoleWidth();
        int height = Systemcall.getConsoleHeight();

        Systemcall.canonicalEnable();
        System.out.print("\033[H\033[2J");
        System.out.flush();

        for (String line : screenBuffer)
            System.out.println(line);

        if (screenBuffer.size() < height)
            for (int i = 2; i < height - screenBuffer.size(); ++i)
                System.out.println();

        String channel = Cache.getCurrentChannelName();
        for (int i = 0; i < 4; ++i) System.out.print("─");
        System.out.print(channel);
        for (int i = 0; i < width - channel.length() - 4; ++i)
            System.out.print("─");

        System.out.print(">: " + inputCache + (cursorToggle?"_":""));

        Systemcall.canonicalDisable();
    } }
}
