
//color codes copied from here:
//https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
//then i added some stuff for config to disable and enable

package scs.azacord.service;

public class ConsoleColors {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    public static String Reset () { return Config.getUseColors() ? ANSI_RESET : ""; }
    public static String Black () { return Config.getUseColors() ? ANSI_BLACK : ""; }
    public static String Red () { return Config.getUseColors() ? ANSI_RED : ""; }
    public static String Green () { return Config.getUseColors() ? ANSI_GREEN : ""; }
    public static String Yellow () { return Config.getUseColors() ? ANSI_YELLOW : ""; }
    public static String Blue () { return Config.getUseColors() ? ANSI_BLUE : ""; }
    public static String Purple () { return Config.getUseColors() ? ANSI_PURPLE : ""; }
    public static String Cyan () { return Config.getUseColors() ? ANSI_CYAN : ""; }
    public static String White () { return Config.getUseColors() ? ANSI_WHITE : ""; }

    public static String Black_BG () { return Config.getUseColors() ? ANSI_BLACK_BACKGROUND : ""; }
    public static String Red_BG () { return Config.getUseColors() ? ANSI_RED_BACKGROUND : ""; }
    public static String Green_BG () { return Config.getUseColors() ? ANSI_GREEN_BACKGROUND : ""; }
    public static String Yellow_BG () { return Config.getUseColors() ? ANSI_YELLOW_BACKGROUND : ""; }
    public static String Blue_BG () { return Config.getUseColors() ? ANSI_BLUE_BACKGROUND : ""; }
    public static String Purple_BG () { return Config.getUseColors() ? ANSI_PURPLE_BACKGROUND : ""; }
    public static String Cyan_BG () { return Config.getUseColors() ? ANSI_CYAN_BACKGROUND : ""; }
    public static String White_BG () { return Config.getUseColors() ? ANSI_WHITE_BACKGROUND : ""; }
}
