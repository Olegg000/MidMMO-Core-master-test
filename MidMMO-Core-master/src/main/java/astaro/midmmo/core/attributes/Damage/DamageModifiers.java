package astaro.midmmo.core.attributes.Damage;

import net.minecraft.world.entity.LivingEntity;

public class DamageModifiers {

    public static float  applyAttackModifiers(LivingEntity attacker, float baseDamage,
                                              CustomDamageSources type) {
        float modifier = baseDamage;
        return modifier;
    }
}
