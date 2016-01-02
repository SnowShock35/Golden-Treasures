package net.snowshock.goldentreasures.items;

import com.google.common.collect.ImmutableMap;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.snowshock.goldentreasures.GoldenTreasures;
import net.snowshock.goldentreasures.references.ReferencesModInfo;
import net.snowshock.goldentreasures.references.ReferencesModItems;
import net.snowshock.goldentreasures.utils.LanguageHelper;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemGoldenFood extends ItemFood {

    private boolean showTooltipsAlways = false;

    public ItemGoldenFood() {
        super(20, 1.0F, false);
        this.setUnlocalizedName(ReferencesModItems.GOLDEN_FOOD);
        this.setCreativeTab(GoldenTreasures.CREATIVE_TAB);
        this.setMaxStackSize(64);
        this.setMaxDamage(0);
        canRepair = false;
    }

    @Override
    public String getUnlocalizedName() {
        return String.format("item.%s%s", ReferencesModInfo.MOD_ID + ":", unwrapUnlocalizedName(super.getUnlocalizedName()));
    }

    public String getUnlocalizedName(ItemStack itemStack) {
        return String.format("item.%s%s", ReferencesModInfo.MOD_ID + ":", unwrapUnlocalizedName(super.getUnlocalizedName()));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(String.format("%s", unwrapUnlocalizedName(this.getUnlocalizedName())));
    }

    protected String unwrapUnlocalizedName(String unlocalizedName) {
        return LanguageHelper.unwrapUnlocalizedName(unlocalizedName);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack, int pass) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.rare;
    }

    @Override
    public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        --par1ItemStack.stackSize;
        par3EntityPlayer.getFoodStats().addStats(10, 1.0F);
        par2World.playSoundAtEntity(par3EntityPlayer, "random.burp", 0.5F, par2World.rand.nextFloat() * 0.1F + 0.9F);
        this.onFoodEaten(par1ItemStack, par2World, par3EntityPlayer);
        return par1ItemStack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack) {
        return 16;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack) {
        return EnumAction.eat;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        if (par3EntityPlayer.canEat(false)) {
            par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
        }

        return par1ItemStack;
    }

    /**
     * Used to format tooltips. Grabs tooltip from language registry with the
     * entry 'item.unlocalizedName.tooltip'. Has support for Handlebars-style
     * templating, and line breaking using '\n'.
     *
     * @param toFormat An ImmutableMap that has all the regex keys and values. Regex
     *                 strings are handled on the tooltip by including '{{regexKey}}'
     *                 with your regex key, of course.
     * @param stack    The ItemStack passed from addInformation.
     * @param list     List of description lines passed from addInformation.
     */
    @SideOnly(Side.CLIENT)
    public void formatTooltip(ImmutableMap<String, String> toFormat, ItemStack stack, List list) {
        if (showTooltipsAlways() || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
            LanguageHelper.formatTooltip(this.getUnlocalizedNameInefficiently(stack) + ".tooltip", toFormat, stack, list);
    }

    /**
     * Just a call to formatTooltip(). If you are overriding this function, call
     * formatTooltip() directly and DO NOT call super.addInformation().
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean whatDoesThisEvenDo) {
        this.formatTooltip(null, stack, list);
    }


    protected boolean showTooltipsAlways() {
        return this.showTooltipsAlways;
    }

    protected void showTooltipsAlways(boolean b) {
        this.showTooltipsAlways = b;
    }

}
