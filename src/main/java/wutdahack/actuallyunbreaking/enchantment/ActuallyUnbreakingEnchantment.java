package wutdahack.actuallyunbreaking.enchantment;

import amymialee.noenchantcap.NoEnchantCap;
import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.enchantment.*;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import wutdahack.actuallyunbreaking.AUConfig;

import java.util.Random;

// this enchantment is an UnbreakingEnchantment like enchantment
public class ActuallyUnbreakingEnchantment extends Enchantment {

    static AUConfig config = AutoConfig.getConfigHolder(AUConfig.class).getConfig();

    public ActuallyUnbreakingEnchantment(Rarity rarityIn, EnchantmentTarget typeIn, EquipmentSlot[] slots) {
        super(rarityIn, typeIn, slots);
    }

    public int getMinPower(int level) {
        return 5 + (level - 1) * 8;
    }

    public int getMaxPower(int level) {
        return super.getMinPower(level) + 50;
    }

    public int getMaxLevel() {
        if (config.maxLevelOnly) {
            if (FabricLoader.getInstance().isModLoaded("noenchantcap")) {
                return NoEnchantCap.config.unbreakingCap;
            } else {
                return 3;
            }
        } else if (!config.maxLevelOnly) {
            return 1;
        }
        return 1;
    }

    public boolean isAcceptableItem(ItemStack stack) {
        return stack.isDamageable() || super.isAcceptableItem(stack);
    }

    // items can't have mending and unbreaking together
    public boolean canAccept(Enchantment other) {
        return !(other instanceof MendingEnchantment) && super.canAccept(other);
    }

    public static boolean preventDamage(ItemStack stack, Random random) {

        int level = EnchantmentHelper.getLevel(ModEnchantments.UNBREAKING, stack);

        if (config.maxLevelOnly) {
            if (FabricLoader.getInstance().isModLoaded("noenchantcap")) {
                if (level == NoEnchantCap.config.unbreakingCap) {
                    return true;
                } else if (level < NoEnchantCap.config.unbreakingCap) {
                    UnbreakingEnchantment.shouldPreventDamage(stack, level, random);
                }
            } else {
                if (level == 3) {
                    return true;
                } else if (level < 3) {
                    UnbreakingEnchantment.shouldPreventDamage(stack, level, random);
                }
            }
        }  else if (!config.maxLevelOnly && level > 0) {
            return true;
        }

        return false;
    }


}
