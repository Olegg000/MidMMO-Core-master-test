package astaro.midmmo.core.registries;

import astaro.midmmo.Midmmo;
import astaro.midmmo.core.items.FirstLevelSword;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemRegistry {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Midmmo.MODID);

    public static final DeferredHolder<Item, Item> FIRST_LEVEL_SWORD = ITEMS.registerItem(
            "first_level_sword",
            props -> {
                ItemAttributeModifiers modifiers = ItemAttributeModifiers.builder()
                        .add(
                                StatsRegistry.PHYSICAL_DAMAGE,
                                new AttributeModifier(
                                        ResourceLocation.fromNamespaceAndPath(Midmmo.MODID, "base_physical_damage"),
                                        5.0,
                                        AttributeModifier.Operation.ADD_VALUE
                                ),
                                EquipmentSlotGroup.MAINHAND
                        )
                        .add(
                                Attributes.ATTACK_SPEED,
                                new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, -2.4, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        )
                        .add(
                                StatsRegistry.STRENGTH,
                                StatsRegistry.modifyDoubleAttribute("first_sword_strength", 3.0),
                                EquipmentSlotGroup.MAINHAND
                        )
                        .add(
                                StatsRegistry.DEXTERITY,
                                StatsRegistry.modifyDoubleAttribute("first_sword_dexterity", 1.0),
                                EquipmentSlotGroup.MAINHAND
                        )
                        .build();

                props.durability(59).attributes(modifiers);

                return new FirstLevelSword(props);
            }
    );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}