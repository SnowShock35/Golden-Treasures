package uk.snowshock35.goldentreasures;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import uk.snowshock35.goldentreasures.init.InitModItems;

public class CreativeTabGoldenTreasures extends CreativeTabs {
    public CreativeTabGoldenTreasures(int ID, String langName) {
        super(ID, langName);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getTabIconItem() {
        return InitModItems.golden_staff;
    }

}
