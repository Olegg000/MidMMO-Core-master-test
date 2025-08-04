package astaro.midmmo.core.listeners;

import astaro.midmmo.core.attributes.Damage.CustomDamageSources;
import astaro.midmmo.core.attributes.Damage.DamageSystem;
import astaro.midmmo.core.data.PlayerData;
import astaro.midmmo.core.data.cache.PlayerDataCache;
import astaro.midmmo.core.items.FirstLevelSword;
import astaro.midmmo.core.items.ILevelRequirement;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

import java.util.logging.Level;
import java.util.logging.Logger;

@EventBusSubscriber(modid = "midmmo")
public class DamageListener {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPhysicalDamage(AttackEntityEvent event){

        LivingEntity attacker = event.getEntity();
        LivingEntity target = (LivingEntity) event.getTarget();
        event.setCanceled(true);

        Item heldItem = attacker.getMainHandItem().getItem();
        if (heldItem instanceof ILevelRequirement itemWithRequirement) {
            if (attacker instanceof ServerPlayer serverAttacker) {
                PlayerData playerData = PlayerDataCache.get(serverAttacker.getUUID());
                if (playerData != null) {
                    if (playerData.getPlayerLvl() < itemWithRequirement.getRequiredLevel()) {
                        event.setCanceled(true);
                        serverAttacker.displayClientMessage(Component.literal("Your level is too low to use this."), true);
                        return;
                    }
                } else {
                    event.setCanceled(true);
                    serverAttacker.displayClientMessage(Component.literal("Player data not loaded."), true);
                    return;
                }
            }
        }


        //Create phys.damage

        CustomDamageSources damageSource = new CustomDamageSources.Builder(attacker.level().registryAccess()
                .lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(DamageTypes.PLAYER_ATTACK))
                .causingEntity(attacker)
                .addFlag(CustomDamageSources.DamageFlag.PHYSICAL)
                .build();

        // Create damage system
        DamageSystem system = new DamageSystem(attacker, target, damageSource);
        float damage = system.calculateDmg();

        // Apply damage
        Logger.getLogger(DamageListener.class.getName()).log(Level.INFO, String.valueOf(damage));
        LivingDamageEvent.Pre damageEvent = new LivingDamageEvent.Pre(
                target, new DamageContainer(damageSource, damage)
        );
        NeoForge.EVENT_BUS.post(damageEvent);
        target.hurt(damageSource,damageEvent.getContainer().getNewDamage());


    }
}
