package ru.mrflaxe.betterexplosion;

import org.bukkit.plugin.java.JavaPlugin;

import ru.mrflaxe.betterexplosion.listeners.ExplosionListener;
import ru.soknight.lib.configuration.Messages;

public class BetterExplosion extends JavaPlugin {
    
    private Messages messages;
    
    @Override
    public void onEnable() {
        initializeConfigs();
        
        registerListeners();
    }
    
    private void initializeConfigs() {
        messages = new Messages(this, "messages.yml");
        messages.refresh();
    }
    
    private void registerListeners() {
        new ExplosionListener(this).registerEvent();
    }
}
