package net.snowshock.goldentreasures.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.ArrayList;
import java.util.List;

import static net.snowshock.goldentreasures.references.ReferencesConfigInfo.GeneralConfigs.NUM_INGREDIENTS;
import static net.snowshock.goldentreasures.references.ReferencesModInfo.MOD_ID;
import static net.snowshock.goldentreasures.references.ReferencesModItems.GOLDEN_INGREDIENT;

public class ItemGoldenIngredient extends ItemGoldenTreasures {

    @SideOnly(Side.CLIENT)
    public IIcon[] icons = new IIcon[NUM_INGREDIENTS];

    public ItemGoldenIngredient() {
        super();
        this.setUnlocalizedName(GOLDEN_INGREDIENT);
        this.setMaxStackSize(64);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
        canRepair = false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(final ItemStack stack) {
        return EnumRarity.rare;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(final ItemStack stack, final int pass) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        for (int meta = 0; meta < NUM_INGREDIENTS; meta++) {
            itemIcon = iconRegister.registerIcon(String.format("%s_%d", unwrapUnlocalizedName(this.getUnlocalizedName()), meta));
            icons[meta] = itemIcon;
        }
    }
//
//    @Override
//    public boolean showDurabilityBar(ItemStack stack) {
//        return false;
//    }
//
//    @Override
//    public boolean isDamaged(ItemStack stack) {
//        return false;
//    }
//
    @Override
    public IIcon getIconFromDamage(int meta) {
        if (meta >= NUM_INGREDIENTS)
            meta = 0;

        return icons[meta];
    }
//
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(final Item item, final CreativeTabs tab, final List list) {
        for (int i = 0; i < NUM_INGREDIENTS; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return this.getUnlocalizedName() + "_" + stack.getItemDamage();
    }
}
