
package scs.azacord.service;

import java.lang.Runtime;
import java.nio.charset.StandardCharsets;

public class Systemcall {

    public static void playSoundWait (String a) {

        try {
            Runtime.getRuntime().exec(
                new String[]{"/bin/sh", "-c", "mplayer '" +a+ "'"}
            ).waitFor();

        } catch (Exception e) { e.printStackTrace(); }
    }

    public static void playSound (String a) {

        try {
            Runtime.getRuntime().exec(
                new String[]{"/bin/sh", "-c", "mplayer '" +a+ "'"}
            );

        } catch (Exception e) { e.printStackTrace(); }
    }

    public static String getDateTime () {

        try {
            var process = Runtime.getRuntime().exec(
                new String[]{"/bin/sh", "-c", "date"}
            );
            process.waitFor();
            byte[] buffer = process.getInputStream().readAllBytes();
            String string = new String(buffer, StandardCharsets.UTF_8);
            return string.substring(0, string.length() - 1);

        } catch (Exception e) { e.printStackTrace(); return ""; }
    }

    public static int getConsoleWidth () {

        try {
            var process = Runtime.getRuntime().exec(
                new String[]{"/bin/sh", "-c", "stty size </dev/tty"}
            );
            process.waitFor();
            byte[] buffer = process.getInputStream().readAllBytes();
            String string = new String(buffer, StandardCharsets.UTF_8);
            string = string.substring(0, string.length() - 1);
            int width = Integer.parseInt(string.split(" ")[1]);
            return width;

        } catch (Exception e) { e.printStackTrace(); return -1; }
    }
    public static int getConsoleHeight () {

        try {
            var process = Runtime.getRuntime().exec(
                new String[]{"/bin/sh", "-c", "stty size </dev/tty"}
            );
            process.waitFor();
            byte[] buffer = process.getInputStream().readAllBytes();
            String string = new String(buffer, StandardCharsets.UTF_8);
            string = string.substring(0, string.length() - 1);
            int height = Integer.parseInt(string.split(" ")[0]);
            return height;

        } catch (Exception e) { e.printStackTrace(); return -1; }
    }

    public static int canonicalDisable () {

        try {
            Runtime.getRuntime().exec(
                new String[]{"/bin/sh", "-c", "stty raw </dev/tty"}
            ).waitFor();

        } catch (Exception e) { return -1; }
        return 0;
    }
    public static int canonicalEnable () {

        try {
            Runtime.getRuntime().exec(
                new String[]{"/bin/sh", "-c", "stty cooked </dev/tty"}
            ).waitFor();

        } catch (Exception e) { return -1; }
        return 0;
    }

    public static void execute (String[] a) {

        try {
            Runtime.getRuntime().exec(a).waitFor();

        } catch (Exception e) { e.printStackTrace(); }
    }
    public static void executeDetach (String[] a) {

        try {
            Runtime.getRuntime().exec(a);

        } catch (Exception e) { e.printStackTrace(); }
    }
}
