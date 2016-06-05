package uk.snowshock35.goldentreasures.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLilyPad;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import uk.snowshock35.goldentreasures.GoldenTreasures;
import uk.snowshock35.goldentreasures.references.ReferencesModBlocks;
import uk.snowshock35.goldentreasures.references.ReferencesModInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.snowshock35.goldentreasures.references.ReferencesConfigInfo;

import java.util.List;
import java.util.Random;


public class BlockGoldenLilypad extends BlockGoldenTreasures {

    public static final int SECONDS_PER_TICK = 20;
    private static final Logger LOGGER = LogManager.getLogger(ReferencesModInfo.MOD_ID + ".GoldenLilypad");

    public BlockGoldenLilypad() {
        super(Material.plants);
        float var3 = 0.5F;
        float var4 = 0.015625F;
        this.setTickRandomly(false);
        this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, var4, 0.5F + var3);
        this.setStepSound(BlockLilyPad.soundTypeGrass);
        this.setBlockName(ReferencesModBlocks.GOLDEN_LILYPAD);
        this.setBlockTextureName(ReferencesModBlocks.GOLDEN_LILYPAD);
        this.setCreativeTab(GoldenTreasures.CREATIVE_TAB);
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random par5Random) {
        LOGGER.debug("Ticking lilypad at [{},{},{}]", x, y, z);
        final Block block = world.getBlock(x, y, z);
        LOGGER.debug("Block is [{}]", block);
        this.growCropsNearby(world, x, y, z);
        world.scheduleBlockUpdate(x, y, z, block, secondsBetweenGrowthTicks() * 20);
    }

    @Override
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta) {
        final int superResult = super.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, meta);
        world.scheduleBlockUpdate(x, y, z, this, secondsBetweenGrowthTicks() * SECONDS_PER_TICK);
        return superResult;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        world.spawnParticle("mobSpell", x + 0.5D + rand.nextGaussian() / 8, y, z + 0.5D + rand.nextGaussian() / 8, 0.0D, 0.9D, 0.5D);
    }

    private int secondsBetweenGrowthTicks() {
        return ReferencesConfigInfo.GoldenLilypad.SECONDS_BETWEEN_GROWTH_TICKS;
    }

    private int tileRange() {
        return ReferencesConfigInfo.GoldenLilypad.TILE_RANGE;
    }

    private int fullPotencyRange() {
        return ReferencesConfigInfo.GoldenLilypad.FULL_POTENCY_RANGE;
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
    public boolean canBlockStay(World world, int x, int y, int z) {
        return y >= 0 && y < 256 && world.getBlock(x, y - 1, z).getMaterial() == Material.water && world.getBlockMetadata(x, y - 1, z) == 0;
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1));
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World world, int x, int y, int z)
    {
        return super.canPlaceBlockAt(world, x, y, z) && this.canBlockStay(world, x, y, z);
    }

}
