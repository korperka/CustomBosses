package hi.korperka.custombosses.bosses.entityimage;

import lombok.Getter;
import org.bukkit.entity.Entity;

import java.util.*;

public class EntityImagesStorage {
    @Getter
    private static final Map<EntityImage<?>, List<Entity>> bossEntities = new HashMap<>();

    public static void putEntityImage(EntityImage<?> entityImage, Entity entity) {
        bossEntities.computeIfAbsent(entityImage, k -> new ArrayList<>()).add(entity);
    }

    public static EntityImage<?> getEntityImage(Entity entity) {
        return bossEntities.entrySet().stream()
                .filter(entry -> entry.getValue().contains(entity))
                .map(Map.Entry::getKey)
                .findFirst().orElse(null);
    }
}
