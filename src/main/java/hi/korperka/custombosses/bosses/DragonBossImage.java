package hi.korperka.custombosses.bosses;

import hi.korperka.custombosses.CustomBosses;
import hi.korperka.custombosses.bosses.entityimage.EntityImage;
import hi.korperka.custombosses.bosses.entityimage.EntityImagesStorage;
import lombok.experimental.SuperBuilder;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@SuperBuilder
public class DragonBossImage extends EntityImage<EnderDragon> implements Listener {
    CustomBosses plugin = CustomBosses.getInstance();

    protected DragonBossImage(EntityImageBuilder<EnderDragon, ?, ?> b) {
        super(b);
    }

    public DragonBossImage registerListener() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        return this;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity damager = event.getDamager();
        if (EntityImagesStorage.getEntityImage(damager) != this) {
            return;
        }

        if(entity instanceof Player) {
            ((Player) entity).damage(plugin.getDragonConfig().getDragonAdditionalDamage());
        }
    }
}
