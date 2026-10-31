package net.sl3dz.plugin.tekstNadGlowo;

import org.bukkit.command.PluginCommand;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class TekstNadGlowo extends JavaPlugin {

    private OpisCom opisCom;

    @Override
    public void onEnable() {
        PluginManager pm = getServer().getPluginManager();
        if (pm.getPermission("tekstnadglowo.opis") == null) {
            pm.addPermission(new Permission("tekstnadglowo.opis", "Pozwala uzywac /opis", PermissionDefault.TRUE));
        }

        PluginCommand command = getCommand("opis");
        if (command != null) {
            this.opisCom = new OpisCom(this);
            command.setExecutor(opisCom);
            command.setTabCompleter(opisCom);
        }
    }

    @Override
    public void onDisable() {
        if (opisCom != null) {
            opisCom.cleanupAll();
        }
    }
}