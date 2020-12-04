package stephirio.silkonfig.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import stephirio.silkonfig.Main;
import stephirio.silkonfig.utils.ProbabilityCollection;

import java.util.ArrayList;

public class BlockBreak implements Listener {

    private final Main plugin;
    public BlockBreak(Main plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block broken_block = event.getBlock();

        if (player.getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH)) {  // if broken with silk touch

            if (plugin.getConfig().contains(broken_block.getWorld().getName())) {  //if in the right world
                ConfigurationSection drops_config = plugin.getConfig().getConfigurationSection(broken_block.getWorld()
                        .getName()).getConfigurationSection("drops");
                String previous_block = broken_block.getBlockData().getMaterial().toString();

                if (drops_config.contains(previous_block)) {
                    if (player.hasPermission("silkonfig.break." + previous_block)) {

                        broken_block.getWorld().getBlockAt(broken_block.getLocation()).setType(Material.AIR);
                        event.getBlock().getDrops().clear();
                        ConfigurationSection previous_block_config =
                                drops_config.getConfigurationSection(previous_block);

                        ProbabilityCollection<String> collection = new ProbabilityCollection<>();
                        for (String drop : previous_block_config.getKeys(true)) {
                            if (!drop.equals("messages"))
                                collection.add(drop, Integer.parseInt(previous_block_config.getList(drop).get(0)
                                        .toString().replace("%", "")));
                        }
                        String dropped_item = collection.get();

                        broken_block.getWorld().dropItemNaturally(event.getBlock().getLocation(),
                                new ItemStack(Material.valueOf(dropped_item),
                                        (Integer) previous_block_config.getList(dropped_item).get(1)));

                        if (drops_config.getConfigurationSection(previous_block).getList("messages") != null) {
                            for (Object message : drops_config.getConfigurationSection(previous_block)
                                    .getList("messages"))
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                        plugin.getConfig().getString("prefix") + (String) message));
                        }
                    } else {
                        if (plugin.getConfig().getString("no-permission-action")
                                .equalsIgnoreCase("prevent breaking")) {
                            event.setCancelled(true);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                                    plugin.getConfig().getString("prefix") +
                                    plugin.getConfig().getString("no-permission")));
                        }
                    }
                }
            }
        }
    }

}
