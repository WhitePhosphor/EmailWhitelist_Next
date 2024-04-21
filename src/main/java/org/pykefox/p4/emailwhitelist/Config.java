package org.pykefox.p4.emailwhitelist;

import org.bukkit.plugin.Plugin;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class Config {
    private final Plugin plugin;

    Config(Plugin plugin) {
        this.plugin = plugin;
    }

    public static String emailAddress;
    public static String passcode;
    public static String hostAddress;
    public static String port;
    public static String emailTitle;
    public static String emailContent;
    public static String trigger;

    public void loadConfig() {
        plugin.saveDefaultConfig();
        FileConfiguration config = plugin.getConfig();

        emailAddress = config.getString("general.email-address");
        passcode = config.getString("general.passcode");
        hostAddress = config.getString("general.host-address");
        port = config.getString("general.port");
        emailTitle = config.getString("general.email-title");
        emailContent = config.getString("general.email-content");
        trigger = config.getString("general.trigger");

    }
}
