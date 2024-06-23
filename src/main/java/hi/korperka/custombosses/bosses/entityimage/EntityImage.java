package hi.korperka.custombosses.bosses.entityimage;

import hi.korperka.custombosses.enums.Immunity;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Enemy;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Slime;

import javax.annotation.Nullable;
import java.util.List;

@SuperBuilder
@Getter
//@AllArgsConstructor
//@NoArgsConstructor
public class EntityImage<T extends Enemy> {
    @NonNull
    private EntityType type;
    private int health;
    @Builder.Default
    private int size = 1;
    private List<Immunity> immunities;
    private String customName;
    @Builder.Default
    private boolean customNameVisible = false;
    @Builder.Default
    private double damageMultiplier = 1;

    @Nullable
    public T create(Location location) {
        World world = location.getWorld();

        if (world == null) {
            return null;
        }

        T entity = (T) world.spawnEntity(location, type);
        apply(entity);
        return entity;
    }

    public void apply(T entity) {
        AttributeInstance healthAttribute = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (health != 0 && healthAttribute != null) {
            healthAttribute.setBaseValue(health);
            entity.setHealth(health);
        }

        AttributeInstance damageAttribute = entity.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
        if(damageAttribute != null) {
            damageAttribute.setBaseValue(damageAttribute.getBaseValue() * damageMultiplier);
        }

        if (entity instanceof Phantom) {
            ((Phantom) entity).setSize(size);
        }

        if (entity instanceof Slime) {
            ((Slime) entity).setSize(size);
        }

        entity.setCustomName(customName);
        entity.setCustomNameVisible(customNameVisible);

        EntityImagesStorage.putEntityImage(this, entity);
    }
}
