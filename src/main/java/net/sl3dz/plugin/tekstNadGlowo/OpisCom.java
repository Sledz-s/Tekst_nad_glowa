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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class OpisCom implements CommandExecutor, TabCompleter {

    private final double player_x = 0;
    private final double player_y = 2.5;
    private final double player_z = 0;

    private final JavaPlugin plugin;

    private final Map<UUID, PlayerDescription> activeDescriptions = new HashMap<>();

    private record PlayerDescription(TextDisplay display, BukkitRunnable task) {}

    public OpisCom(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Tylko gracz może użyć tej komendy!");
            return true;
        }

        UUID playerUUID = player.getUniqueId();

        if (args.length == 0) {
            if (removeActiveDescription(playerUUID)) {
                player.sendMessage(ChatColor.YELLOW + "Twój opis został usunięty.");
            } else {
                player.sendMessage(ChatColor.RED + "Nie masz obecnie aktywnego opisu. Użyj: /opis <text>");
            }
            return true;
        }

        removeActiveDescription(playerUUID);

        String rawText = String.join(" ", args);
        String formattedText = ChatColor.translateAlternateColorCodes('&', rawText);

        Location spawnLoc = player.getLocation().add(player_x, player_y, player_z);
        TextDisplay textDisplay = (TextDisplay) player.getWorld().spawnEntity(spawnLoc, EntityType.TEXT_DISPLAY);

        textDisplay.setText(formattedText);
        textDisplay.setBillboard(TextDisplay.Billboard.CENTER);


        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline() || !textDisplay.isValid()) {
                    if (textDisplay.isValid()) {
                        textDisplay.remove();
                    }
                    activeDescriptions.remove(playerUUID);
                    this.cancel();
                    return;
                }

                Location currentLoc = player.getLocation().add(player_x, player_y, player_z);
                textDisplay.teleport(currentLoc);
            }
        };

        activeDescriptions.put(playerUUID, new PlayerDescription(textDisplay, runnable));
        runnable.runTaskTimer(plugin, 0L, 1L);

        player.sendMessage(ChatColor.GREEN + "Ustawiono nowy opis!");
        return true;
    }

    private boolean removeActiveDescription(UUID playerUUID) {
        PlayerDescription current = activeDescriptions.remove(playerUUID);
        if (current != null) {
            current.task().cancel();
            if (current.display().isValid()) {
                current.display().remove();
            }
            return true;
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return Collections.emptyList();
    }
}