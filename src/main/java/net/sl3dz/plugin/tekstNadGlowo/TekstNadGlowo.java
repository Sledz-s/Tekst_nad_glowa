package net.sl3dz.plugin.tekstNadGlowo;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class TekstNadGlowo extends JavaPlugin {

    @Override
    public void onEnable() {
        PluginCommand command = getCommand("opis");
        if (command != null) {
            OpisCom opisCom = new OpisCom(this);
            command.setExecutor(opisCom);
            command.setTabCompleter(opisCom);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}