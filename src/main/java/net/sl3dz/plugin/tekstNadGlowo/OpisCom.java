package net.sl3dz.plugin.tekstNadGlowo;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class OpisCom implements CommandExecutor, TabCompleter {

    private final double player_x = 0;
    private final double player_y = 2.5;
    private final double player_z = 0;

    private final JavaPlugin plugin;

    public OpisCom(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Tylko gracz może użyć tej komendy!");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("Użycie: /opis [tekst]");
            return true;
        }

        String rawText = String.join(" ", args);
        String formattedText = ChatColor.translateAlternateColorCodes('&', rawText);

        Location spawnLoc = player.getLocation().add(player_x, player_y, player_z);
        TextDisplay textDisplay = (TextDisplay) player.getWorld().spawnEntity(spawnLoc, EntityType.TEXT_DISPLAY);

        textDisplay.setText(formattedText);
        textDisplay.setBillboard(TextDisplay.Billboard.CENTER);

        new BukkitRunnable() {
            private int ticksElapsed = 0;
            private final int maxTicks = (20 * 10);

            @Override
            public void run() {
                if (!player.isOnline() || !textDisplay.isValid() || ticksElapsed >= maxTicks) {
                    if (textDisplay.isValid()) {
                        textDisplay.remove();
                    }
                    this.cancel();
                    return;
                }

                Location currentLoc = player.getLocation().add(player_x, player_y, player_z);
                textDisplay.teleport(currentLoc);
                ticksElapsed++;
            }
        }.runTaskTimer(plugin, 0L, 1L);

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return Collections.emptyList();
    }
}