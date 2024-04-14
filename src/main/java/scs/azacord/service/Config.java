
package scs.azacord.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class Config {

    private static String token; public static String getToken () { return token; }


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
                    }
                }

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

                + "\ntoken="
            );

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
