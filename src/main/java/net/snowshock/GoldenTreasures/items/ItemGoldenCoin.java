package net.snowshock.GoldenTreasures.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.snowshock.GoldenTreasures.references.ReferencesModItems;

public class ItemGoldenCoin extends ItemGoldenTreasures {
    public ItemGoldenCoin() {
        super();
        this.setUnlocalizedName(ReferencesModItems.GOLDEN_COIN);
        this.setMaxStackSize(1);
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
