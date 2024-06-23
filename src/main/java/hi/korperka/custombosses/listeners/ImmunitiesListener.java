package hi.korperka.custombosses.listeners;

import hi.korperka.custombosses.bosses.entityimage.EntityImage;
import hi.korperka.custombosses.bosses.entityimage.EntityImagesStorage;
import hi.korperka.custombosses.enums.Immunity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;

public class ImmunitiesListener implements Listener {
    @EventHandler
    public void onEntityCombust(EntityCombustEvent event) {
        EntityImage<?> entityImage = EntityImagesStorage.getEntityImage(event.getEntity());

        if(entityImage == null) {
            return;
        }

        if(entityImage.getImmunities().contains(Immunity.FIRE)) {
            event.setCancelled(true);
        }
    }
}
