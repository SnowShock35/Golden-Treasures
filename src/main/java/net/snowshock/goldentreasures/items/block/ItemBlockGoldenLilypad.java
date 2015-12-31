package net.snowshock.goldentreasures.items.block;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.snowshock.goldentreasures.init.InitModBlocks;

import static net.snowshock.goldentreasures.references.ReferencesConfigInfo.GoldenLilypad.SECONDS_BETWEEN_GROWTH_TICKS;

public class ItemBlockGoldenLilypad extends ItemBlockGoldenTreasures {

    public ItemBlockGoldenLilypad(Block block) {
        super(block);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.epic;
    }


    /**
     * Called whenever this item is equipped and the right mouse button is
     * pressed. Args: itemStack, world, entityPlayer
     */
    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World world, EntityPlayer par3EntityPlayer) {
        MovingObjectPosition var4 = this.getMovingObjectPositionFromPlayer(world, par3EntityPlayer, true);

        if (world.isRemote)
            return par1ItemStack;

        if (var4 == null)
            return par1ItemStack;
        else {
            if (var4.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                int var5 = var4.blockX;
                int var6 = var4.blockY;
                int var7 = var4.blockZ;

                if (!world.canMineBlock(par3EntityPlayer, var5, var6, var7))
                    return par1ItemStack;

                if (!par3EntityPlayer.canPlayerEdit(var5, var6, var7, var4.sideHit, par1ItemStack))
                    return par1ItemStack;

                if (world.getBlock(var5, var6, var7).getMaterial() == Material.water && world.getBlockMetadata(var5,
                        var6, var7) == 0 && world.isAirBlock(var5, var6 + 1, var7)) {
                    world.setBlock(var5, var6 + 1, var7, InitModBlocks.blockGoldenLilypad);
                    world.getBlock(var5, var6 + 1, var7).onBlockPlaced(world, var5, var6 + 1, var7, 0,0, 0, 0, 0);

                    if (!par3EntityPlayer.capabilities.isCreativeMode) {
                        --par1ItemStack.stackSize;
                    }
                }
            }

            return par1ItemStack;
        }
    }
}