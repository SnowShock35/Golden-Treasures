package net.snowshock.goldentreasures.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.snowshock.goldentreasures.references.ReferencesModItems;

public class ItemGoldenFood extends ItemGoldenTreasures {
    public ItemGoldenFood() {
        super();
        this.setUnlocalizedName(ReferencesModItems.GOLDEN_FOOD);
        this.setMaxStackSize(64);
        this.setMaxDamage(0);
        canRepair = false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack, int pass) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.common;
    }

}
