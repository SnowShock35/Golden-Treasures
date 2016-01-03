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
import net.snowshock.goldentreasures.references.ReferencesModItems;

import static net.snowshock.goldentreasures.references.ReferencesConfigInfo.GoldenFeather.*;


/**
 * Created by Xeno on 5/15/14.
 */
public class ItemGoldenFeather extends ItemGoldenTreasures {

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
    public void onUpdate(ItemStack ist, World world, Entity e, int i, boolean f) {
        int potency = LEAPING_POTENCY;
        if (potency == 0) return;
        potency -= 1;
        if (e instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) e;
            player.addPotionEffect(new PotionEffect(Potion.jump.id, 2, potency, true));
        }
    }
}
