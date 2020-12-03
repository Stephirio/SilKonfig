package stephirio.silkonfig;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import stephirio.silkonfig.commands.Reload;
import stephirio.silkonfig.events.BlockBreak;
public final class Main extends JavaPlugin {

    public static String plugin_prefix = ChatColor.GREEN + "[" + ChatColor.GOLD + "SilKonfig" + ChatColor.GREEN + "]";
    
    public void severeLog(String string) { System.out.println(plugin_prefix + " " + ChatColor.RED + string); }
    public void warningLog(String string) { System.out.println(plugin_prefix + " " + ChatColor.YELLOW + string); }
    public void successLog(String string) { System.out.println(plugin_prefix + " " + ChatColor.GREEN + string); }

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        successLog("Loading libraries...");
        successLog("Registering events...");
        getServer().getPluginManager().registerEvents(new BlockBreak(this), this);
        getCommand("silkonfig").setExecutor(new Reload(this));
        successLog("Developer: Stephirio");
        successLog("Plugin started!");
    }

}
