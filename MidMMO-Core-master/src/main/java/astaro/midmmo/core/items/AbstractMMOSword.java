package astaro.midmmo.core.items;
import net.minecraft.world.item.SwordItem;

public abstract class AbstractMMOSword extends SwordItem implements ILevelRequirement, IWeaponData {
    private final int requiredLevel;
    private final float baseDamage;

    public AbstractMMOSword(Properties properties, int requiredLevel, float baseDamage) {
        super(properties);
        this.requiredLevel = requiredLevel;
        this.baseDamage = baseDamage;
    }

    @Override
    public int getRequiredLevel() {
        return this.requiredLevel;
    }

    @Override
    public float getBaseDamage() {
        return this.baseDamage;
    }
}