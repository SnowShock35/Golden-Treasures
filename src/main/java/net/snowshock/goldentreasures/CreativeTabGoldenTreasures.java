package net.snowshock.goldentreasures;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class CreativeTabGoldenTreasures extends CreativeTabs {
    public CreativeTabGoldenTreasures(int ID, String langName) {
        super(ID, langName);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getTabIconItem() {
        return Items.gold_ingot;
    }

}
