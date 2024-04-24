
package scs.azacord.uinterface;

import java.util.Vector;
import java.util.HashMap;
import java.time.Instant;
import java.time.Duration;

import scs.azacord.service.Systemcall;
import scs.azacord.service.Cache;
import scs.azacord.service.ConsoleColors;
import scs.azacord.service.Config;
import scs.azacord.uinterface.Input;

public class Display {

    private static Object mutex = new Object();

    private static Vector<String> screenBuffer = new Vector<String>();

    private static void trimCheck () { synchronized (mutex) {

        int height = Systemcall.getConsoleHeight();

        if (screenBuffer.size() > height + 10) for (int i = 0; i < 10; ++i)
                screenBuffer.remove(0);
    } }

    public static void append (String sender, String message) { synchronized (mutex) {

        if (message.contains("<@" + Cache.Discord.getSelfUser().getId().asString() + ">")) {
            if (Config.getUseColors())
                screenBuffer.add(
                    ConsoleColors.Yellow() + "  @ "
                    +  sender + ConsoleColors.Reset() + "  "
                    + ConsoleColors.White() + message + ConsoleColors.Reset()
                );
            else
                screenBuffer.add("! @ [" + sender + "]: " + message);
        } else {
            if (Config.getUseColors())
                screenBuffer.add(
                    " " + (Config.getUseRedUsernames() ? ConsoleColors.Red() : ConsoleColors.Blue())
                    + sender + ConsoleColors.Reset() + "  " + message
                );
            else
                screenBuffer.add("[" + sender + "]: " + message);
        }
        trimCheck();
    } }

    public static void system (String message) { synchronized (mutex) {

        if (Config.getUseColors())
            screenBuffer.add(
                " " + ConsoleColors.White() + "System" + ConsoleColors.Reset() + "  " + message
            );
        else
            screenBuffer.add("{[System]}: " + message);
        trimCheck();
    } }
    public static void append (String message) { synchronized (mutex) {

        screenBuffer.add("  " + message); trimCheck();
    } }
    public static void clear () { synchronized (mutex) {

        screenBuffer.clear();
    } }

    private static Vector<String> typingUsers = new Vector<String>();
    private static HashMap<String, Instant> typingTimes = new HashMap<String, Instant>();
    public static void addTyper (String typer) { synchronized (mutex) {
        if (typingUsers.contains(typer)) return;
        typingUsers.add(typer);
        typingTimes.put(typer, Instant.now());
    } }
    public static void removeTyper (String typer) { synchronized (mutex) {
        if (!typingUsers.contains(typer)) return;
        typingUsers.remove(typer);
        typingTimes.remove(typer);
    } }
    public static void clearTypists () { synchronized (mutex) {
        typingUsers.clear();
    } }
    public static void checkTypists () { synchronized (mutex) {
        Vector<String> removeCache = new Vector<String>();
        for (String typer : typingUsers) {
            Duration duration = Duration.between(typingTimes.get(typer), Instant.now());
            if (duration.toSeconds() >= 14)
                removeCache.add(typer);
        }
        for (String typer : removeCache) removeTyper(typer);
    } }
    private static String generateTypingStatus (int lengthLimit) { synchronized (mutex) {
        String returnValue = "";
        switch (typingUsers.size()) {
            case 0: break;
            case 1: returnValue = typingUsers.get(0) + " is typing..."; break;
            case 2: returnValue = typingUsers.get(0) + " and " + typingUsers.get(1) + " are typing..."; break;
            default: {
                returnValue = typingUsers.get(0);
                for (int i = 1; i < typingUsers.size() - 1; ++i)
                    returnValue += ", " + typingUsers.get(i);
                returnValue += " and " + typingUsers.get(typingUsers.size() - 1) + " are typing...";
            break; }
        }
        if (returnValue.length() > lengthLimit) {
            returnValue = "Multiple users typing...";
            if (returnValue.length() > lengthLimit)
            returnValue = "...";
        }
        return returnValue;
    } }

    public static void tick () {

        runChecks();

        timer++; if (timer > 100) {
            runOccasionalChecks(); timer = 0; }
    }
    private static int timer;

    private static String inputCache;
    private static int screenBufferCache, typingUserCache, widthCache, heightCache, notificationCache;

    private static void runChecks () {

        if (screenBufferCache != screenBuffer.size()) {
            screenBufferCache = screenBuffer.size(); render();
        }

        if (typingUserCache != typingUsers.size()) {
            typingUserCache = typingUsers.size(); render();
        }

        if (notificationCache != Cache.getNotificationCount()) {
            notificationCache = Cache.getNotificationCount(); render();
        }

        if (inputCache != Input.getValue()) {
            inputCache = Input.getValue(); inputOnlyRender();
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

        checkTypists();
    }

    private static void render () { synchronized (mutex) {

        int width = Systemcall.getConsoleWidth();
        int height = Systemcall.getConsoleHeight();

        Systemcall.canonicalEnable();
        System.out.print("\033[2J\033[1;1H");
        System.out.flush();

        for (String line : screenBuffer)
            System.out.println(line);

        if (screenBuffer.size() < height)
            for (int i = 2; i < height - screenBuffer.size(); ++i)
                System.out.println();

        String notificationCount = String.valueOf(Cache.getNotificationCount());
        if (notificationCount.length() > 2) notificationCount = "99";
        System.out.print(
            notificationCount.equals("0") ? "──"
            : ConsoleColors.Yellow() + "⋅"
            + ConsoleColors.White() + notificationCount + ConsoleColors.Reset()
        );
        System.out.print(notificationCount.length() == 1 ? "──" : "─");

        String channel = Cache.getCurrentChannelName();
        System.out.print(channel);

        for (int i = 0; i < 2; ++i) System.out.print("─");

        String typeStatus = generateTypingStatus(width - channel.length() - 6);
        // typeStatus = typeStatus.length() > 0 ? "┤" + typeStatus + "├" : typeStatus;
        // typeStatus = typeStatus.length() > 0 ? "⋅" + typeStatus + "⋅" : typeStatus;
        typeStatus = typeStatus.length() > 0 ? " " + typeStatus + " " : typeStatus;
        System.out.print(typeStatus);
        for (int i = 0; i < width - channel.length() - 6 - typeStatus.length(); ++i)
            System.out.print("─");

        inputFieldCache = ">: " + (
            inputCache.length() > width - 16
            ? inputCache.substring(inputCache.length() - (width - 16))
            : inputCache
        );
        System.out.print(inputFieldCache);

        Systemcall.canonicalDisable();
    } }

    private static String inputFieldCache = "";

    private static void inputOnlyRender () { synchronized (mutex) {

        int width = Systemcall.getConsoleWidth();
        Systemcall.canonicalEnable();

        if (Config.getInputFieldOverflowFix())
            System.out.print("\r");

        else for (int i = 0; i < inputFieldCache.length() + 2; ++i)
            System.out.print("    \b\b\b\b\b");

        inputFieldCache = ">: " + (
            inputCache.length() > width - 16
            ? inputCache.substring(inputCache.length() - (width - 16))
            : inputCache
        );
        System.out.print(inputFieldCache);

        Systemcall.canonicalDisable();
    } }
}
