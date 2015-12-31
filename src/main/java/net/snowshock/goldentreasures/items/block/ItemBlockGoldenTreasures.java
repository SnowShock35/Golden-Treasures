package net.snowshock.goldentreasures.items.block;

import com.google.common.collect.ImmutableMap;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.snowshock.goldentreasures.items.IHeldBlockAction;
import net.snowshock.goldentreasures.utils.LanguageHelper;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ItemBlockGoldenTreasures extends ItemBlock {
    private final Block block;
    //defaults to only showing the tooltip when shift is pressed. you can override this behavior at the item level by setting the item's showTooltipsAlways bool to true.
    private boolean showTooltipsAlways = false;

    public ItemBlockGoldenTreasures(Block block) {
        super(block);
        this.block = block;
    }

    /**
     * Just a call to formatTooltip(). If you are overriding this function, call
     * formatTooltip() directly and DO NOT call super.addInformation().
     */
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean whatDoesThisEvenDo) {
        this.formatTooltip(null, stack, list);
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
    public void formatTooltip(ImmutableMap<String, String> toFormat, ItemStack stack, List list) {
        if (showTooltipsAlways() || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
            LanguageHelper.formatTooltip(this.getUnlocalizedNameInefficiently(stack) + ".tooltip", toFormat, stack, list);
    }


    @Override
    @SideOnly(Side.CLIENT)
    public String getItemStackDisplayName(ItemStack stack) {
        return LanguageHelper.getLocalization(this.getUnlocalizedNameInefficiently(stack) + ".name");
    }

    protected boolean showTooltipsAlways() {
        return this.showTooltipsAlways;
    }

    protected void showTooltipsAlways(boolean b) {
        this.showTooltipsAlways = b;
    }


    @Override
    public void onUpdate(ItemStack ist, World world, Entity entity, int i, boolean f) {
        if (block instanceof IHeldBlockAction) {
            ((IHeldBlockAction) block).doHeldItemUpdate(ist, world, entity, i, f);
        }
    }
}
