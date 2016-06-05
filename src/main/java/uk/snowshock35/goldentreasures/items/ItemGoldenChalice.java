package uk.snowshock35.goldentreasures.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import uk.snowshock35.goldentreasures.references.ReferencesModItems;
import uk.snowshock35.goldentreasures.utils.ContentHelper;

import static uk.snowshock35.goldentreasures.references.ReferencesConfigInfo.GoldenChalice.HUNGER_SATURATION_MULTIPLIER;

public class ItemGoldenChalice extends ItemGoldenTreasuresTogglable {
    @SideOnly(Side.CLIENT)
    private IIcon iconOverlay;

    public ItemGoldenChalice() {
        super();
        this.setUnlocalizedName(ReferencesModItems.GOLDEN_CHALICE);
        this.setMaxStackSize(1);
        this.setMaxDamage(0);
        canRepair = false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        super.registerIcons(iconRegister);
        iconOverlay = iconRegister.registerIcon(String.format("%s_overlay", unwrapUnlocalizedName(this.getUnlocalizedName())));
    }

    @Override
    public IIcon getIcon(ItemStack ist, int renderPass) {
        final boolean enabled = isEnabled(ist);
        if (!enabled && renderPass == 1)
            return iconOverlay;
        else
            return super.getIcon(ist, renderPass);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.epic;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack) {
        return 16;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack) {
        return EnumAction.drink;
    }

    @Override
    public ItemStack onEaten(ItemStack ist, World world, EntityPlayer player) {
        if (world.isRemote)
            return ist;

            int multiplier = getHungerSaturationMultiplier();
            player.getFoodStats().addStats(1, (float) (multiplier / 2));
            player.attackEntityFrom(DamageSource.drown, multiplier);
            return ist;
    }

    private int getHungerSaturationMultiplier() {
        return HUNGER_SATURATION_MULTIPLIER;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack ist, World world, EntityPlayer player) {
        if (player.isSneaking())
            return super.onItemRightClick(ist, world, player);
        float coeff = 1.0F;
        double xOff = player.prevPosX + (player.posX - player.prevPosX) * coeff;
        double yOff = player.prevPosY + (player.posY - player.prevPosY) * coeff + 1.62D - player.yOffset;
        double zOff = player.prevPosZ + (player.posZ - player.prevPosZ) * coeff;
        boolean isInDrainMode = this.isEnabled(ist);
        MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(world, player, isInDrainMode);

        if (mop == null) {
            if (!this.isEnabled(ist)) {
                player.setItemInUse(ist, this.getMaxItemUseDuration(ist));
            }
            return ist;
        } else {

            if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                int x = mop.blockX;
                int y = mop.blockY;
                int z = mop.blockZ;

                if (!world.canMineBlock(player, x, y, z))
                    return ist;

                if (!player.canPlayerEdit(x, y, z, mop.sideHit, ist))
                    return ist;

                if (this.isEnabled(ist)) {
                    TileEntity tile = world.getTileEntity(x, y, z);
                    if (tile instanceof IFluidHandler) {
                        //it's got infinite water.. it just drains water, nothing more.
                        FluidStack fluid = new FluidStack(FluidRegistry.WATER, 1000);
                        ((IFluidHandler) tile).drain(ForgeDirection.getOrientation(mop.sideHit), fluid, true);

                        return ist;
                    }
                } else {
                    TileEntity tile = world.getTileEntity(x, y, z);
                    if (tile instanceof IFluidHandler) {
                        FluidStack fluid = new FluidStack(FluidRegistry.WATER, 1000);
                        int amount = ((IFluidHandler) tile).fill(ForgeDirection.getOrientation(mop.sideHit), fluid, false);

                        if (amount > 0) {
                            ((IFluidHandler) tile).fill(ForgeDirection.getOrientation(mop.sideHit), fluid, true);
                        }

                        return ist;
                    }
                }

                if (!this.isEnabled(ist)) {
                    if (mop.sideHit == 0) {
                        --y;
                    }

                    if (mop.sideHit == 1) {
                        ++y;
                    }

                    if (mop.sideHit == 2) {
                        --z;
                    }

                    if (mop.sideHit == 3) {
                        ++z;
                    }

                    if (mop.sideHit == 4) {
                        --x;
                    }

                    if (mop.sideHit == 5) {
                        ++x;
                    }

                    if (!player.canPlayerEdit(x, y, z, mop.sideHit, ist))
                        return ist;

                    if (this.tryPlaceContainedLiquid(world, ist, xOff, yOff, zOff, x, y, z))
                        return ist;

                } else {
                    String ident = ContentHelper.getIdent(world.getBlock(x, y, z));
                    if ((ident.equals(ContentHelper.getIdent(Blocks.flowing_water)) || ident.equals(ContentHelper.getIdent(Blocks.water))) && world.getBlockMetadata(x, y, z) == 0) {
                        world.setBlock(x, y, z, Blocks.air);

                        return ist;
                    }
                }
            }

            return ist;
        }
    }

    public boolean tryPlaceContainedLiquid(World world, ItemStack ist, double posX, double posY, double posZ, int x, int y, int z) {
        Material material = world.getBlock(x, y, z).getMaterial();
        if (this.isEnabled(ist))
            return false;
        boolean isNotSolid = !material.isSolid();
        if (!world.isAirBlock(x, y, z) && !isNotSolid)
            return false;
        else {
            if (world.provider.isHellWorld) {
                world.playSoundEffect(posX + 0.5D, posY + 0.5D, posZ + 0.5D, "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

                for (int var11 = 0; var11 < 8; ++var11) {
                    world.spawnParticle("largesmoke", x + Math.random(), y + Math.random(), z + Math.random(), 0.0D, 0.0D, 0.0D);
                }
            } else {
                world.setBlock(x, y, z, Blocks.flowing_water, 0, 3);
            }

            return true;
        }
    }
}