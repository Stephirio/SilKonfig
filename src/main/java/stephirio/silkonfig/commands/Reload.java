package stephirio.silkonfig.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import stephirio.silkonfig.Main;


/** This class manages the /silkonfig reload command. */
public class Reload implements CommandExecutor {

    private Main plugin;
    public Reload(Main plugin) { this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args[0].length() > 0 && args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        plugin.getConfig().getString("reload-message")));
            } else plugin.successLog("The configuration files have been reloaded.");
        }

        return false;
    }
}
