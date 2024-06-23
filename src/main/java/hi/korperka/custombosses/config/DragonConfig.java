package hi.korperka.custombosses.config;

import pl.mikigal.config.Config;
import pl.mikigal.config.annotation.Comment;
import pl.mikigal.config.annotation.ConfigName;

@ConfigName("dragonconfig.yml")
public interface DragonConfig extends Config {
    @Comment("Начальное здоровье дракона")
    default int getDragonHealth() { return 600; }
    @Comment("Здоровье, которое будет добавлено дракону за одного игрока в энде")
    default int getDragonHealthForPlayer() { return 100; }
    @Comment("Дополнительный урон, который будет нанесён игроку при ударе от эндер дракона")
    default double getDragonAdditionalDamage() { return 4; }
    @Comment("Кол-во ударов, нужное для поломки кристала")
    default int getCrystalHitTimes() { return 15; }
}
