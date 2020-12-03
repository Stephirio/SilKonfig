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

public class BlockBreak implements Listener {

    private final Main plugin;

    public BlockBreak(Main plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        Player player = event.getPlayer();
        Block broken_block = event.getBlock();
        if (player.getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH)) {
            if (plugin.getConfig().contains(broken_block.getWorld().getName())) {

                ConfigurationSection drops_config = plugin.getConfig().getConfigurationSection(broken_block.getWorld()
                        .getName()).getConfigurationSection("drops");

                String previous_block = broken_block.getBlockData().getMaterial().toString();
                if (drops_config.contains(previous_block)) {
                    if (player.hasPermission("silkonfig.break." + previous_block)) {
                        broken_block.getWorld().getBlockAt(broken_block.getLocation()).setType(Material.AIR);
                        event.getBlock().getDrops().clear();

                        for (String drop : drops_config.getConfigurationSection(previous_block).getKeys(true)) {
                            if (!drop.equals("messages"))
                                broken_block.getWorld().dropItemNaturally(event.getBlock().getLocation(),
                                        new ItemStack(Material.valueOf((String) drop), drops_config
                                                .getConfigurationSection(previous_block).getInt(drop)));
                        }

                        for (Object message : drops_config.getConfigurationSection(previous_block).getList("messages"))
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', (String) message));

                    } else {
                        if (plugin.getConfig().getString("no-permission-action").equalsIgnoreCase("prevent breaking")) {
                            event.setCancelled(true);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("no-permission")));
                        }
                    }
                }
            }
        }
    }

}
