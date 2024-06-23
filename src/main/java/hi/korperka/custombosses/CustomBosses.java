package hi.korperka.custombosses;

import co.aikar.commands.BukkitCommandManager;
import hi.korperka.custombosses.bosses.entityimage.EntityImagesStorage;
import hi.korperka.custombosses.commands.SummonBoss;
import hi.korperka.custombosses.config.DragonConfig;
import hi.korperka.custombosses.config.PhantomConfig;
import hi.korperka.custombosses.listeners.ImmunitiesListener;
import hi.korperka.custombosses.listeners.MiscListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import pl.mikigal.config.ConfigAPI;
import pl.mikigal.config.style.CommentStyle;
import pl.mikigal.config.style.NameStyle;

@Getter
public final class CustomBosses extends JavaPlugin {
    private PhantomConfig phantomConfig;
    private DragonConfig dragonConfig;

    @Override
    public void onEnable() {
        phantomConfig = ConfigAPI.init(PhantomConfig.class,
                NameStyle.CAMEL_CASE,
                CommentStyle.INLINE,
                true,
                this);
        dragonConfig = ConfigAPI.init(DragonConfig.class,
                NameStyle.CAMEL_CASE,
                CommentStyle.INLINE,
                true,
                this);

        getServer().getPluginManager().registerEvents(new ImmunitiesListener(), this);
        getServer().getPluginManager().registerEvents(new MiscListener(), this);

        BukkitCommandManager manager = new BukkitCommandManager(this);
        manager.registerCommand(new SummonBoss());
    }

    @Override
    public void onDisable() {
        EntityImagesStorage.getBossEntities().forEach((entityImage, entities) -> entities.forEach(Entity::remove));
    }

    public static CustomBosses getInstance() {
        return CustomBosses.getPlugin(CustomBosses.class);
    }

    public void sendGlobalMessage(String message) {
        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(message));
        getLogger().info(message);
    }
}
