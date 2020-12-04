package stephirio.silkonfig;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import stephirio.silkonfig.commands.Reload;
import stephirio.silkonfig.events.BlockBreak;
import stephirio.silkonfig.utils.UpdateChecker;

public final class Main extends JavaPlugin {

    public Integer pluginID = 86369;
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
        new UpdateChecker(this, pluginID).getLatestVersion(version -> {
            if(this.getDescription().getVersion().equalsIgnoreCase(version))
                successLog("Plugin is up to date.");
            else
                warningLog("A new version of SilKonfig is available. Download it now at " +
                        "https://www.spigotmc.org/threads/mcmmoadditions.85779/");
        });
    }

}
