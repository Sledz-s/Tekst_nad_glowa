package net.sl3dz.plugin.tekstNadGlowo;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final OpisCom opisCom;

    public PlayerQuitListener(OpisCom opisCom) {
        this.opisCom = opisCom;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();


        OpisCom.PlayerDescription desc = opisCom.getActiveDescriptions().remove(player.getUniqueId());

        if (desc != null) {
            desc.task().cancel();
            if (desc.display().isValid()) {
                desc.display().remove();
            }
        }
    }
}