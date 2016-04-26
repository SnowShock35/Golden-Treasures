package net.snowshock.goldentreasures.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

import java.util.List;

import static net.snowshock.goldentreasures.references.ReferencesConfigInfo.GeneralConfigs.NUM_CRAFTING_COMPONENTS;
import static net.snowshock.goldentreasures.references.ReferencesModItems.GOLDEN_CRAFTING_COMPONENT;

public class ItemGoldenCraftingComponent extends ItemGoldenTreasures {

    public static final String ID_TAG = "component_id";

    public IIcon[] icons = new IIcon[NUM_CRAFTING_COMPONENTS];

    public ItemGoldenCraftingComponent() {
        super();
        this.setUnlocalizedName(GOLDEN_CRAFTING_COMPONENT);
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
        for (int id = 0; id < NUM_CRAFTING_COMPONENTS; id++) {
            itemIcon = iconRegister.registerIcon(String.format("%s_%d", unwrapUnlocalizedName(this.getUnlocalizedName()), id));
            icons[id] = itemIcon;
        }
    }

    @Override
    public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem, int useRemaining) {
        return getIconForStack(stack);
    }

    @Override
    public IIcon getIconIndex(ItemStack stack) {
        return getIconForStack(stack);
    }

    protected IIcon getIconForStack(ItemStack stack) {
        if (stack.hasTagCompound()) {
            int id = stack.getTagCompound().getInteger(ID_TAG);

            // Set a ceiling on ingredient number in case the number has been reduced.
            if (id >= NUM_CRAFTING_COMPONENTS)
                id = 0;

            return icons[id];
        } else {
            // Fallback to default, because what else are we going to do?
            return super.getIconIndex(stack);
        }
    }

    @Override
    public void getSubItems(final Item item, final CreativeTabs tab, final List list) {
        for (int id = 0; id < NUM_CRAFTING_COMPONENTS; id++) {
            list.add(createSubItem(item, id));
        }
    }

    protected ItemStack createSubItem(Item item, int number) {
        final ItemStack itemStack = new ItemStack(item, 1);
        itemStack.setTagCompound(new NBTTagCompound());
        itemStack.getTagCompound().setInteger(ID_TAG, number);
        return itemStack;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        if (stack.hasTagCompound()) {
            int id = stack.getTagCompound().getInteger(ID_TAG);
            return this.getUnlocalizedName() + "_" + String.valueOf(id);
        } else {
            return super.getUnlocalizedName(stack);
        }
    }
}
