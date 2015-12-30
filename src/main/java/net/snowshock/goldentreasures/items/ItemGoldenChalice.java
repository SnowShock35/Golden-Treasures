package net.snowshock.goldentreasures.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.snowshock.goldentreasures.references.ReferencesModItems;

public class ItemGoldenChalice extends ItemGoldenTreasuresTogglable {
    public ItemGoldenChalice() {
        super();
        this.setUnlocalizedName(ReferencesModItems.GOLDEN_CHALICE);
        this.setMaxStackSize(1);
        this.setMaxDamage(0);
        canRepair = false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.common;
    }
}