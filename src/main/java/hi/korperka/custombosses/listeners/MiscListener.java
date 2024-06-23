package hi.korperka.custombosses.listeners;

import hi.korperka.custombosses.CustomBosses;
import hi.korperka.custombosses.bosses.DragonBossImage;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashMap;
import java.util.Map;

public class MiscListener implements Listener {
    private final CustomBosses plugin;
    private Map<EnderCrystal, Integer> crystalHits;

    public MiscListener() {
        this.plugin = CustomBosses.getInstance();
        this.crystalHits = new HashMap<>();
    }

    @EventHandler
    public void onCrystalDamage(EntityDamageEvent event) {
        if (event.getEntityType() != EntityType.ENDER_CRYSTAL || event.getEntity().getWorld().getEntities().stream().noneMatch(entity -> entity.getType() == EntityType.ENDER_DRAGON)) {
            return;
        }

        EnderCrystal crystal = (EnderCrystal) event.getEntity();

        crystalHits.putIfAbsent(crystal, 0);

        int hits = crystalHits.get(crystal) + 1;
        crystalHits.put(crystal, hits);

        if (hits < plugin.getDragonConfig().getCrystalHitTimes()) {
            event.setCancelled(true);
        } else {
            crystalHits.remove(crystal);
        }
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if(event.getEntity().getType() == EntityType.ENDER_DRAGON) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                EnderDragon creature = (EnderDragon) event.getEntity();

                DragonBossImage.builder()
                        .type(EntityType.ENDER_DRAGON)
                        .health(plugin.getDragonConfig().getDragonHealth() + plugin.getDragonConfig().getDragonHealthForPlayer() * creature.getWorld().getPlayers().size())
                        .build().registerListener().apply(creature);
            }, 5);
        }

        if(!canLimitSpawn(event)) {
            return;
        }

        if(!plugin.getPhantomConfig().getPhantomsSpawnBiomes().contains(event.getLocation().getBlock().getBiome())) {
            event.setCancelled(true);
        }

        World world = event.getEntity().getWorld();
        int phantomCount = 0;
        for (Chunk chunk : world.getLoadedChunks()) {
            for (Entity entity : chunk.getEntities()) {
                if (entity.getType() == EntityType.PHANTOM) {
                    phantomCount++;
                }
            }
        }

        if(phantomCount > plugin.getPhantomConfig().getPhantomsNaturalSpawnCount()) {
            event.setCancelled(true);
        }
    }

    private boolean canLimitSpawn(CreatureSpawnEvent event) {
        return plugin.getPhantomConfig().getPhantomDefeated() &&
                event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.NATURAL &&
                event.getEntity().getType() == EntityType.PHANTOM;
    }
}
