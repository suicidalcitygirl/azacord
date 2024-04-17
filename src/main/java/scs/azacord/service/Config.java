
package scs.azacord.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Config {

    private static String token = ""; public static String getToken () { return token; }
    private static String pingSound = ""; public static String getPingSound () { return pingSound; }
    private static String typeSound = ""; public static String getTypeSound () { return typeSound; }
    private static String startSound = ""; public static String getStartSound () { return startSound; }
    private static String switchChannelSound = ""; public static String getSwitchChannelSound () { return switchChannelSound; }
    private static String typeAlertSound = ""; public static String getTypeAlertSound () { return typeAlertSound; }
    private static String quitSound = ""; public static String getQuitSound () { return quitSound; }


    private static String configPath = System.getProperty("user.home") + "/.config/azacord.conf";
    public static String getConfigPath () { return configPath; }

    public static boolean loadConfig () {

        try {

            File configFile = new File(configPath);
            configFile.getParentFile().mkdirs();

            if (configFile.exists() && !configFile.isDirectory()) {

                String configRaw = Files.readString(configFile.toPath());

                if (!configRaw.contains("\n")) {
                    generate(); return false;
                }

                for (String line : configRaw.split("\n")) {

                    if (line == "") continue;
                    if (line.charAt(0) == '#') continue;
                    if (!line.contains("=")) continue;

                    String[] values = line.split("=");

                    switch (values[0]) {

                        case "token": token = values[1]; break;
                        case "pingSound": pingSound = values[1]; break;
                        case "typeSound": typeSound = values[1]; break;
                        case "startSound": startSound = values[1]; break;
                        case "switchChannelSound": switchChannelSound = values[1]; break;
                        case "typeAlertSound": typeAlertSound = values[1]; break;
                        case "quitSound": quitSound = values[1]; break;
                    }
                }

                if (token.equals(""))
                    Files.writeString(configFile.toPath(), "\ntoken=", StandardOpenOption.APPEND);

                if (pingSound.equals(""))
                    Files.writeString(configFile.toPath(), "\n# default:/opt/azacord/ping1.wav\npingSound=NULL", StandardOpenOption.APPEND);

                if (typeSound.equals(""))
                    Files.writeString(configFile.toPath(), "\n# default: /opt/azacord/type1.mp3\ntypeSound=NULL", StandardOpenOption.APPEND);

                if (startSound.equals(""))
                    Files.writeString(configFile.toPath(), "\n# default: NULL\nstartSound=NULL", StandardOpenOption.APPEND);

                if (switchChannelSound.equals(""))
                    Files.writeString(configFile.toPath(), "\n# default: NULL\nswitchChannelSound=NULL", StandardOpenOption.APPEND);

                if (typeAlertSound.equals(""))
                    Files.writeString(configFile.toPath(), "\n# default: NULL\ntypeAlertSound=NULL", StandardOpenOption.APPEND);

                if (quitSound.equals(""))
                    Files.writeString(configFile.toPath(), "\n# default: NULL\nquitSound=NULL", StandardOpenOption.APPEND);

                return true;

            } else {
                generate(); return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return false;
    }

    private static void generate () {

        try {

            File configFile = new File(configPath);
            configFile.getParentFile().mkdirs();

            Files.writeString(configFile.toPath(),
                "# azacord config file, generated " +
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now())

                + "\n"
                + "\n# bot token to login with"
                + "\ntoken="
                + "\n# sound effects, NULL to disable value, mplayer required"
                + "\n# default:/opt/azacord/ping1.wav"
                + "\npingSound=NULL"
                + "\n# default: /opt/azacord/type1.mp3"
                + "\ntypeSound=NULL"
                + "\n# default: NULL"
                + "\nstartSound=NULL"
                + "\n# default: NULL"
                + "\nswitchChannelSound=NULL"
                + "\n# default: NULL"
                + "\ntypeAlertSound=NULL"
                + "\n# default: NULL"
                + "\nquitSound=NULL"
                + "\n"
                + "\n"
                + "\n"
            );

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
