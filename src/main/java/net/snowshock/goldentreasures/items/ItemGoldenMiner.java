package net.snowshock.goldentreasures.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.snowshock.goldentreasures.references.ReferencesModItems;


public class ItemGoldenMiner extends ItemGoldenTreasuresTogglable {
    public ItemGoldenMiner() {
        super();
        this.setUnlocalizedName(ReferencesModItems.GOLDEN_MINER);
        this.setMaxStackSize(1);
        canRepair = false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.common;
    }
}
