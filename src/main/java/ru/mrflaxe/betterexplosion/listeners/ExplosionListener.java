package ru.mrflaxe.betterexplosion.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public class ExplosionListener implements Listener {
    
    private final Plugin plugin;
    private final Random r;
    
    public ExplosionListener(Plugin plugin) {
        this.plugin = plugin;
        r = new Random();
    }
    
    @EventHandler
    public void onEntityExplose(EntityExplodeEvent e) {
        Entity exploser = e.getEntity();
        
        List<Block> toRemove = new ArrayList<>();
        
        for(Block b : e.blockList()) {
            Location blockLoc = b.getLocation();
            
            if(blockLoc.getBlockY() > exploser.getLocation().getBlockY()) {
                if(b.isEmpty() && b.isLiquid()) return;
                if(b.getType() == Material.TNT) return;
                
                toRemove.add(b);
                
                Material type = b.getType();
                
                if(type.toString().endsWith("_LOG")) b.setType(Material.COAL_BLOCK);
                if(type.toString().startsWith("GRASS") || type == Material.DIRT) b.setType(Material.COARSE_DIRT);
                
                BlockData data = b.getBlockData();
                b.setType(Material.AIR);
                FallingBlock fallingBlock = blockLoc.getWorld().spawnFallingBlock(blockLoc.add(0.5, 0.5, 0.5), data);
                
                Location exploserLoc = exploser.getLocation();
                double distance = getDistance(blockLoc, exploserLoc);
                Vector vector = getVector(exploserLoc, blockLoc);
                
                vector.multiply(3);
                vector.multiply(1/distance);
                
                fallingBlock.setVelocity(vector);
            }
            
            if(r.nextInt(10) > 5) {
                Block block = blockLoc.getWorld().getBlockAt(blockLoc);

                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    block.setType(Material.FIRE);
                }, 1);
                
            }
        }
        
        toRemove.forEach(b -> e.blockList().remove(b));
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if(!e.getCause().equals(DamageCause.ENTITY_EXPLOSION)) return;
        e.setCancelled(true);
        
        Entity damaged = e.getEntity();
        Entity damager = e.getDamager();
        
        Location playerLoc = damaged.getLocation();
        Location damagerLoc = damager.getLocation();
        
        double distance = getDistance(playerLoc, damagerLoc);
        Vector vector = getVector(damagerLoc, playerLoc.add(0,1,0));
        
        vector.multiply(5);
        vector.multiply(1/distance);
        
        damaged.setVelocity(vector);
    }
    
    /**
     * 
     * 
     * @param firstLoc - location of explosed object
     * @param secondLoc - location of victim object
     * @return
     */
    private Vector getVector(Location firstLoc, Location secondLoc) {
        float firstX = firstLoc.getBlockX();
        float firstY = firstLoc.getBlockY();
        float firstZ = firstLoc.getBlockZ();
        
        float secondX = secondLoc.getBlockX();
        float secondY = secondLoc.getBlockY();
        float secondZ = secondLoc.getBlockZ();
        
        float differenceX = secondX - firstX;
        float differenceY = secondY - firstY;
        float differenceZ = secondZ - firstZ;
        
        float summ = Math.abs(differenceZ) + Math.abs(differenceY) + Math.abs(differenceX);
        
        float vectorX = differenceX / summ;
        float vectorY = differenceY / summ;
        float vectorZ = differenceZ / summ;
        
        return new Vector(vectorX, vectorY, vectorZ);
    }
    
    private double getDistance(Location firstLoc, Location secondLoc) {
        double deltaX = Math.abs(firstLoc.getX() - secondLoc.getX());
        double deltaZ = Math.abs(firstLoc.getZ() - secondLoc.getZ());

        return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaZ, 2));
    }
    
    public void registerEvent() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
}
