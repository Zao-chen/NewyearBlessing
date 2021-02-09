package newyearblessing.newyearblessing;

import newyearblessing.newyearblessing.commands.maincommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class NewyearBlessing extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.saveDefaultConfig();
        getConfig().options().copyDefaults();
        getCommand("nyb").setExecutor(new maincommand());
    }

    @Override
    public void onDisable() {
        saveDefaultConfig();
        // Plugin shutdown logic
    }
}
