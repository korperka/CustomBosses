package hi.korperka.custombosses.config;

import org.bukkit.ChatColor;
import org.bukkit.block.Biome;
import pl.mikigal.config.Config;
import pl.mikigal.config.annotation.Comment;
import pl.mikigal.config.annotation.ConfigName;

import java.util.List;

@ConfigName("phantomconfig.yml")
public interface PhantomConfig extends Config {
    @Comment("Здоровье фантома")
    default int getPhantomHealth() { return 1000; }
    @Comment("Размер фантома")
    default int getPhantomSize() { return 30; }
    @Comment("Кастомное имя фантома")
    default String getPhantomCustomName() { return "Фантом"; }
    @Comment("Минимальное кол-во маленьких фантомов в куче")
    default int getPhantomSpawnMinCount() { return 3; }
    @Comment("Максимальное кол-во маленьких фантомов в куче")
    default int getPhantomSpawnMaxCount() { return 6; }
    @Comment("Шанс спавна кучи фантомов в процентах")
    default Integer getPhantomsSpawnChance() {
        return 50;
    }
    @Comment("Проценты здоровья фантома, при котором будут спавнится кучи обычных фантомов")
    default List<Integer> getPhantomsSpawnHealth() {
        return List.of(80, 60, 40, 20, 1);
    }
    @Comment("Был ли фантом побеждён (отвечает за ограничение по спавну фантомов в мире)")
    default Boolean getPhantomDefeated() { return false; }
    void setPhantomDefeated(Boolean defeated);
    @Comment("Оффсет, в котором заспавнится фантом при вводе команды для спавна без указания координат")
    default Integer getPhantomSpawnOffset() { return 250; }
    @Comment("Время в секундах, после истечения которого появится фантом после ввода команды")
    default Integer getPhantomAppearTime() { return 60; }
    @Comment("Сообщение при появлении фантома")
    default String getPhantomAppearMessage() { return ChatColor.RED + "В небе что-то есть"; }
    @Comment("Сообщение при смерти фантома")
    default String getPhantomDeathMessage() { return ChatColor.GREEN + "Небо освобождено, но всё ещё стоит быть осторожным..."; }
    @Comment("Биомы, в которых смогут спавнится фантомы после убийства босса")
    default List<Biome> getPhantomsSpawnBiomes() { return List.of(Biome.BADLANDS, Biome.DESERT); }
    @Comment("Кол-во фантомов в куче, которые смогут натурально заспавнится после убийства босса")
    default Integer getPhantomsNaturalSpawnCount() { return 4; }
    @Comment("Множитель урона фантома")
    default Double getPhantomDamageMultiplier() { return 1.0; }
}
