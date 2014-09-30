package me.externo6.godapples;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;
    // set the config options
    public long cooldown = 10000;
    public boolean showMessage = true;
    public String messageCooldown = "§cGod Apple Cooldown remaining: {seconds} seconds.";
    public String messageNotAllowed = "§cYou dont have permission to use GodApples.";

    public void onEnable() {
        getLogger();
        instance = this;
        saveDefaultConfig();
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new AListener(), this);
        getCommand("godapples").setExecutor(this);
        reloadConfig();
    }
    @Override
    public void reloadConfig() {
        super.reloadConfig();
        Configuration config = getConfig();
        cooldown = config.getLong("cooldown");
        showMessage = config.getBoolean("showMessage");
        ConfigurationSection msgs = config.getConfigurationSection("messages");
        messageCooldown = msgs.getString("cooldown", messageCooldown);
        messageNotAllowed = msgs.getString("notallowed", messageNotAllowed);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd,
            String commandLabel, String[] args) {
        if (args.length >= 1 && "reload".equalsIgnoreCase(args[0])) {
            reloadConfig();
            sender.sendMessage("[GApples] Reloaded configuration!");
            return true;
        }

        return false;
    }


    public static Main getInstance() {
        return instance;
    }

}