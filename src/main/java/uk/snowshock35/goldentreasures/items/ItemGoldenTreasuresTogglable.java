package uk.snowshock35.goldentreasures.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import uk.snowshock35.goldentreasures.utils.NBTHelper;

public class ItemGoldenTreasuresTogglable extends ItemGoldenTreasures {

    public ItemGoldenTreasuresTogglable() {
        super();
        //Is this really necessary? this.hasSubtypes = true;
    }

    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack, int pass) {
        return this.isEnabled(stack);
    }

    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if(!world.isRemote && player.isSneaking()) {
            this.toggleEnabled(stack);
            player.worldObj.playSoundAtEntity(player, "random.orb", 0.1F, 0.5F * ((player.worldObj.rand.nextFloat() - player.worldObj.rand.nextFloat()) * 0.7F + 1.2F));
            return stack;
        } else {
            return stack;
        }
    }

    public boolean isEnabled(ItemStack ist) {
        return NBTHelper.getBoolean("enabled", ist);
    }

    public void toggleEnabled(ItemStack ist) {
        NBTHelper.setBoolean("enabled", ist, !NBTHelper.getBoolean("enabled", ist));
    }
}
