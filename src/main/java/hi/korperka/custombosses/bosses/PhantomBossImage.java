package hi.korperka.custombosses.bosses;

import hi.korperka.custombosses.CustomBosses;
import hi.korperka.custombosses.bosses.entityimage.EntityImage;
import hi.korperka.custombosses.bosses.entityimage.EntityImagesStorage;
import hi.korperka.custombosses.config.PhantomConfig;
import hi.korperka.custombosses.enums.Immunity;
import lombok.experimental.SuperBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@SuperBuilder
public class PhantomBossImage extends EntityImage<Phantom> implements Listener {
    private CustomBosses plugin;
    private BossBar bossBar;
    private List<Integer> phantomsSpawnHealth;
    private int spawnHealth;
    private int spawnHealthIndex;
    private EntityImage<Enemy> summonedPhantom;

    protected PhantomBossImage(EntityImageBuilder<Phantom, ?, ?> b) {
        super(b);
    }

    @Nullable
    @Override
    public Phantom create(Location location) {
        plugin = CustomBosses.getInstance();
        plugin.getLogger().info(String.format("Фантом заспавнился на локации %s;%s;%s;%s", (location.getWorld() == null) ? "" : location.getWorld().getName(), location.getX(), location.getY(), location.getZ()));
        plugin.getPhantomConfig().setPhantomDefeated(false);
        return super.create(location);
    }

    public PhantomBossImage registerListener() {
        phantomsSpawnHealth = plugin.getPhantomConfig().getPhantomsSpawnHealth();
        phantomsSpawnHealth.sort(Collections.reverseOrder());
        spawnHealthIndex = 0;
        spawnHealth = phantomsSpawnHealth.get(spawnHealthIndex);
        summonedPhantom = EntityImage.builder().type(EntityType.PHANTOM).immunities(List.of(Immunity.FIRE)).build();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        bossBar = Bukkit.createBossBar(plugin.getPhantomConfig().getPhantomCustomName(), BarColor.RED, BarStyle.SOLID);
        return this;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (EntityImagesStorage.getEntityImage(entity) == this) {
            plugin.getPhantomConfig().setPhantomDefeated(true);
            plugin.sendGlobalMessage(plugin.getPhantomConfig().getPhantomDeathMessage());
        }

        bossBar.removeAll();
    }

    public void updateBossBar(double healthPercentage, Location bossLocation) {
        bossBar.setProgress(Math.max(0, Math.min(1, healthPercentage / 100)));
        bossBar.setTitle(plugin.getPhantomConfig().getPhantomCustomName());

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getLocation().distance(bossLocation) <= 64) {
                bossBar.addPlayer(player);
            } else {
                bossBar.removePlayer(player);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if (EntityImagesStorage.getEntityImage(entity) != this) {
            return;
        }

        PhantomConfig phantomConfig = plugin.getPhantomConfig();
        double health = ((Enemy) entity).getHealth();
        AttributeInstance maxHealthAttribute = ((Enemy) entity).getAttribute(Attribute.GENERIC_MAX_HEALTH);

        if (maxHealthAttribute == null) {
            return;
        }

        World world = entity.getWorld();
        Random spawnRandom = new Random();
        Location entityLocation = entity.getLocation();
        Location spawnLocation = new Location(world,
                entityLocation.getX() + spawnRandom.nextInt(5) * (spawnRandom.nextBoolean() ? -1 : 1),
                entityLocation.getY() + spawnRandom.nextInt(5) * (spawnRandom.nextBoolean() ? -1 : 1),
                entityLocation.getZ() + spawnRandom.nextInt(5) * (spawnRandom.nextBoolean() ? -1 : 1)
        );
        double maxHealth = maxHealthAttribute.getBaseValue();
        double healthPercentage = (health / maxHealth) * 100;
        int maxCount = phantomConfig.getPhantomSpawnMaxCount();
        int minCount = phantomConfig.getPhantomSpawnMinCount();

        if (healthPercentage <= spawnHealth) {
            if(spawnHealthIndex >= phantomsSpawnHealth.size()) {
                return;
            }
            spawnHealthIndex++;
            spawnHealth = phantomsSpawnHealth.get(spawnHealthIndex - 1);
            Random random = new Random();
            if (random.nextInt(100) < phantomConfig.getPhantomsSpawnChance()) {
                int spawnCount = random.nextInt(maxCount - minCount + 1) + minCount;
                for (int i = 0; i <= spawnCount; i++) {
                        summonedPhantom.create(spawnLocation);
                }
            }
        }

        updateBossBar(healthPercentage, entityLocation);
    }
}