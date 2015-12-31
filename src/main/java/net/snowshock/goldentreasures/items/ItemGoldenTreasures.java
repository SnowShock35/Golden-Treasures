package net.snowshock.goldentreasures.items;

import com.google.common.collect.ImmutableMap;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.snowshock.goldentreasures.GoldenTreasures;
import net.snowshock.goldentreasures.references.ReferencesModInfo;
import net.snowshock.goldentreasures.utils.LanguageHelper;
import net.snowshock.goldentreasures.utils.NBTHelper;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemGoldenTreasures extends Item {
    private boolean showTooltipsAlways = false;

    public ItemGoldenTreasures() {
        super();
        this.setCreativeTab(GoldenTreasures.CREATIVE_TAB);
    }

    @Override
    public String getUnlocalizedName() {
        return String.format("item.%s%s", ReferencesModInfo.MOD_ID + ":", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    public String getUnlocalizedName(ItemStack itemStack) {
        return String.format("item.%s%s", ReferencesModInfo.MOD_ID + ":", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(String.format("%s", getUnwrappedUnlocalizedName(this.getUnlocalizedName())));
    }

    protected String getUnwrappedUnlocalizedName(String unlocalizedName) {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }

    @SideOnly(Side.CLIENT)
    public void formatTooltip(ImmutableMap<String, String> toFormat, ItemStack stack, List list) {
        if(this.showTooltipsAlways() || Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54)) {
            LanguageHelper.formatTooltip(this.getUnlocalizedNameInefficiently(stack) + ".tooltip", toFormat, stack, list);
        }
    }

    protected boolean showTooltipsAlways() {
        return this.showTooltipsAlways;
    }

}
