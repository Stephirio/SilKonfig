package stephirio.silkonfig.utils;


import org.bukkit.Bukkit;
import stephirio.silkonfig.Main;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

/** Checks if there are new updates of the plugin. */
public class UpdateChecker {

    private final Main plugin;
    private final int resourceId;

    public UpdateChecker(Main plugin, int resourceId) {
        this.plugin  = plugin;
        this.resourceId = resourceId;
    }  // Constructor


    /** Gets the latest version of the plugin */
    public void getLatestVersion(Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" +
                    this.resourceId).openStream();
                 Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                }
            } catch (IOException exception) {
                plugin.getLogger().info("Update checker is broken, can't find an update!" + exception.getMessage());
            }
        });
    }
}

