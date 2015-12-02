package net.snowshock.GoldenTreasures;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.snowshock.GoldenTreasures.references.ReferencesModInfo;

public class CreativeTabGoldenTreasures extends CreativeTabs {
    public CreativeTabGoldenTreasures(int ID, String langName) {
        super(ID, langName);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getTabIconItem() {
        return Items.gold_ingot;
    }

    public String getTranslatedTabLable() {
        return ReferencesModInfo.MOD_ID;
    }
}
