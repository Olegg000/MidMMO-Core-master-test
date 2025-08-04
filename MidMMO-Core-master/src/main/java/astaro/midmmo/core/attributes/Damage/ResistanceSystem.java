package astaro.midmmo.core.attributes.Damage;

import astaro.midmmo.core.attributes.providers.AttributeProvider;
import net.minecraft.world.entity.LivingEntity;

public class ResistanceSystem {
    public static float applyDefenses(LivingEntity target,
                                      float incomingDamage,
                                      CustomDamageSources type) {
        Double resistance = getResistance(target, type);
        Double penetration = getPenetration(target, type);

        float effectiveResist = (float) Math.max(0, resistance - penetration);
        return incomingDamage * (1 - effectiveResist / 100f);
    }

    private static Double getResistance(LivingEntity target, CustomDamageSources type) {
        AttributeProvider provider = AttributeProvider.createFor(target);
        if (type.isMagical()) return provider.getStat("magic_resist");
        if (type.isCritical()) return provider.getStat("crit_resist");
        if (type.isPhysical()) return provider.getStat("armor");
        return 1.0;
    }

    private static Double getPenetration(LivingEntity attacker, CustomDamageSources type) {
        AttributeProvider provider = AttributeProvider.createFor(attacker);
        if (type.isMagical()) return provider.getStat("resistance_penetration");
        if (type.isPhysical()) return provider.getStat("armor_penetration");
        return 0.1;
    }
}
