package newyearblessing.newyearblessing;

import newyearblessing.newyearblessing.commands.papi;
import newyearblessing.newyearblessing.commands.maincommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import newyearblessing.newyearblessing.metrics.Metrics;
import newyearblessing.newyearblessing.commands.PlaceholderHook;

import java.io.*;
import java.net.URL;

public final class NewyearBlessing extends JavaPlugin {
    private PlaceholderHook placeholderHook;
    @Override
    public void onEnable()
    {
        /*统计部分*/
        int pluginId = 10347; // <-- Replace with the id of your plugin!
        Metrics metrics = new Metrics(this, pluginId);
        Plugin plugin = newyearblessing.newyearblessing.NewyearBlessing.getProvidingPlugin(newyearblessing.newyearblessing.NewyearBlessing.class);
        metrics.addCustomChart(new Metrics.SimplePie("chart_id", () -> "My value"));
        /*papi部分*/
        placeholderHook = new PlaceholderHook();
        new papi().register();
        /*本体初始化部分*/
        this.saveDefaultConfig();
        getConfig().options().copyDefaults();
        getCommand("nyb").setExecutor(new maincommand());
        System.out.println("[NewyearBlessing] 加载成功");
    }
    @Override
    public void onDisable() {
        saveDefaultConfig();
        // Plugin shutdown logic
    }
}
