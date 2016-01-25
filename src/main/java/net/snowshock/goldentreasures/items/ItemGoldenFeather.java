package net.snowshock.goldentreasures.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.snowshock.goldentreasures.GoldenTreasures;
import net.snowshock.goldentreasures.references.ReferencesModInfo;
import net.snowshock.goldentreasures.references.ReferencesModItems;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.snowshock.goldentreasures.references.ReferencesConfigInfo.GoldenFeather.HUNGER_MULTIPLIER;
import static net.snowshock.goldentreasures.references.ReferencesConfigInfo.GoldenFeather.LEAPING_POTENCY;


/**
 * Created by Xeno on 5/15/14.
 */
public class ItemGoldenFeather extends ItemGoldenTreasures {

    private static final Logger LOGGER = LogManager.getLogger(ReferencesModInfo.MOD_ID + ".GoldenTreasures");

    public ItemGoldenFeather() {
        this.setUnlocalizedName(ReferencesModItems.GOLDEN_FEATHER);
        this.setCreativeTab(GoldenTreasures.CREATIVE_TAB);
        this.setMaxStackSize(1);
        canRepair = false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.epic;
    }

    // event driven item, does nothing here.

    // minor jump buff
    @Override
    public void onUpdate(ItemStack ist, World world, Entity e, int i, boolean isHeld) {
        if (world.isRemote)
            return;

        int potency = LEAPING_POTENCY;
        if (potency == 0) return;

        if (e instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) e;
            player.addPotionEffect(new PotionEffect(Potion.jump.id, 2, potency, true));
        }

        float hungerMultiplier = HUNGER_MULTIPLIER;

        EntityPlayer player = null;
        if (e instanceof EntityPlayer) {
            player = (EntityPlayer) e;
        }
        if (player == null)
            return;

        // Buffer of 8 fall distance so we're not depleting hunger for falls that wouldn't have hurt anyway.
        float distanceBuffer = 8F;

        if (player.fallDistance > distanceBuffer && player.getFoodStats().getFoodLevel() > 0) {
            float amount = Math.min(player.fallDistance - distanceBuffer, player.getFoodStats().getFoodLevel());
            LOGGER.trace("Ticking hunger [{}] [{}] {}", player.fallDistance, amount,
                    ReflectionToStringBuilder.toString(player.getFoodStats()));
            player.fallDistance -= amount;
            player.addExhaustion(amount * hungerMultiplier);
            player.fallDistance = player.fallDistance < 0 ? 0 : player.fallDistance;
        }
    }
}
