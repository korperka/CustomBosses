package hi.korperka.custombosses.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Subcommand;
import hi.korperka.custombosses.CustomBosses;
import hi.korperka.custombosses.bosses.PhantomBossImage;
import hi.korperka.custombosses.enums.Immunity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Random;

@CommandAlias("summonboss")
public class SummonBoss extends BaseCommand {
    private final CustomBosses plugin;

    public SummonBoss() {
        this.plugin = CustomBosses.getInstance();
    }

    @Subcommand("phantom")
    public void summonPhantom(Player player, Integer x, Integer y, Integer z) {
        Location location = new Location(player.getWorld(), x, y, z);
        location.getChunk().load();
        location.getChunk().addPluginChunkTicket(plugin);
        Bukkit.broadcastMessage(plugin.getPhantomConfig().getPhantomAppearMessage());
        
        Bukkit.getScheduler().runTaskLater(plugin, () -> PhantomBossImage.builder()
                .type(EntityType.PHANTOM)
                .health(plugin.getPhantomConfig().getPhantomHealth())
                .size(plugin.getPhantomConfig().getPhantomSize())
                .immunities(List.of(Immunity.FIRE))
                .customName(plugin.getPhantomConfig().getPhantomCustomName())
                .customNameVisible(false)
                .build().registerListener().create(location),
                plugin.getPhantomConfig().getPhantomAppearTime() * 20);
    }

    @Subcommand("phantom")
    public void summonPhantomNoLocation(Player player) {
        int spawnOffset = plugin.getPhantomConfig().getPhantomSpawnOffset();
        Random random = new Random();

        int minDistance = 50;

        int xOffset = minDistance + random.nextInt(spawnOffset - minDistance) * (random.nextBoolean() ? -1 : 1);
        int zOffset = minDistance + random.nextInt(spawnOffset - minDistance) * (random.nextBoolean() ? -1 : 1);

        int x = player.getLocation().getBlockX() + xOffset;
        int z = player.getLocation().getBlockZ() + zOffset;
        int y = player.getWorld().getHighestBlockYAt(x, z) + plugin.getPhantomConfig().getPhantomSize() / 2;

        summonPhantom(player, x, y, z);
    }
}
