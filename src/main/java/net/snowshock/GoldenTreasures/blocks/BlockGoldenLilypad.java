package net.snowshock.GoldenTreasures.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockLilyPad;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.snowshock.GoldenTreasures.GoldenTreasures;
import net.snowshock.GoldenTreasures.references.ReferencesModBlocks;
import net.snowshock.GoldenTreasures.references.ReferencesModInfo;
import net.snowshock.GoldenTreasures.utils.ContentHelper;

import java.util.List;
import java.util.Random;

public class BlockGoldenLilypad extends BlockFlower {
    public BlockGoldenLilypad() {
        super(0);
        float var3 = 0.5F;
        float var4 = 0.015625F;
        this.setTickRandomly(false);
        this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, var4, 0.5F + var3);
        this.setStepSound(BlockLilyPad.soundTypeGrass);
        this.setBlockName(ReferencesModBlocks.GOLDEN_LILYPAD);
        this.setBlockTextureName(ReferencesModBlocks.GOLDEN_LILYPAD);
        this.setCreativeTab(GoldenTreasures.CREATIVE_TAB);
    }

    @Override
    public String getUnlocalizedName() {
        return String.format("tile.%s%s", ReferencesModInfo.MOD_ID + ":", getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon(String.format("%s", getUnwrappedUnlocalizedName(this.getUnlocalizedName())));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return blockIcon;
    }

    protected String getUnwrappedUnlocalizedName(String unlocalizedName) {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random par5Random) {

        this.growCropsNearby(world, x, y, z);
    }

    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        world.spawnParticle("mobSpell", x + 0.5D + rand.nextGaussian() / 8, y, z + 0.5D + rand.nextGaussian() / 8, 0.0D, 0.9D, 0.5D);
    }

    private int secondsBetweenGrowthTicks() {
        return 100;
    }

    private int tileRange() {
        return 4;
    }

    private int fullPotencyRange() {
        return 0;
    }

    public void growCropsNearby(World world, int xO, int yO, int zO) {
        for (int xD = -tileRange(); xD <= tileRange(); xD++) {
            for (int yD = -1; yD <= tileRange(); yD++) {
                for (int zD = -tileRange(); zD <= tileRange(); zD++) {
                    int x = xO + xD;
                    int y = yO + yD;
                    int z = zO + zD;

                    double distance = Math.sqrt(Math.pow(x - xO, 2) + Math.pow(y - yO, 2) + Math.pow(z - zO, 2));
                    distance -= fullPotencyRange();
                    distance = Math.min(1D, distance);
                    double distanceCoefficient = 1D - (distance / tileRange());

                    Block block = world.getBlock(x, y, z);

                    if (block instanceof IPlantable || block instanceof IGrowable) {
                        if (!(block instanceof BlockGoldenLilypad)) {
                            //it schedules the next tick.
                            world.scheduleBlockUpdate(x, y, z, block, (int) (distanceCoefficient * (float) secondsBetweenGrowthTicks() * 20F));
                            block.updateTick(world, x, y, z, world.rand);
                        }
                    }
                }
            }
        }
        world.scheduleBlockUpdate(xO, yO, zO, world.getBlock(xO, yO, zO), secondsBetweenGrowthTicks() * 20);
    }

    @Override
    public int getRenderType() {
        return 23;
    }

    @Override
    public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity) {
        if (par7Entity == null || !(par7Entity instanceof EntityBoat)) {
            super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
        }
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
        return AxisAlignedBB.getBoundingBox(par2 + minX, par3 + minY, par4 + minZ, par2 + maxX, par3 + maxY, par4 + maxZ);

    }

    @Override
    protected boolean canPlaceBlockOn(Block block) {
        return block != null && ContentHelper.areBlocksEqual(block, Blocks.water);
    }

    @Override
    public boolean canBlockStay(World world, int x, int y, int z) {
        return y >= 0 && y < 256 && world.getBlock(x, y - 1, z).getMaterial() == Material.water && world.getBlockMetadata(x, y - 1, z) == 0;
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1));
    }
}
