package net.snowshock.goldentreasures.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockTorch;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.World;
import net.snowshock.goldentreasures.GoldenTreasures;
import net.snowshock.goldentreasures.interdiction.InterdictionField;
import net.snowshock.goldentreasures.references.ReferencesConfigInfo;
import net.snowshock.goldentreasures.references.ReferencesModBlocks;
import net.snowshock.goldentreasures.references.ReferencesModInfo;

import java.util.Random;

import static net.snowshock.goldentreasures.utils.LocalizedNameHelper.getUnwrappedUnlocalizedName;

public class BlockGoldenTorch extends BlockTorch {

    private final InterdictionField interdictionField;

    public BlockGoldenTorch() {
        super();
        this.setBlockName(ReferencesModBlocks.GOLDEN_TORCH);
        this.setBlockTextureName(ReferencesModBlocks.GOLDEN_TORCH);
        this.setCreativeTab(GoldenTreasures.CREATIVE_TAB);
        this.setHardness(0.0F);
        this.setLightLevel(1.0F);
        this.setTickRandomly(false);
        this.setStepSound(BlockTorch.soundTypeWood);
        interdictionField = new InterdictionField(ReferencesConfigInfo.GeneralConfigs.PUSH_RADIUS);
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
    public int onBlockPlaced(World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int meta) {
        world.scheduleBlockUpdate(x, y, z, this, tickRate());
        return super.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, meta);
    }

    @Override
    public void updateTick(World world, int x, int y, int z, Random random) {
        super.updateTick(world, x, y, z, random);
        world.scheduleBlockUpdate(x, y, z, this, tickRate());

        // Skip if on the client
        if (world.isRemote)
            return;

        interdictionField.doInterdictionTick(world, x, y, z);
    }

    public int tickRate() {
        return 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        int orientation = world.getBlockMetadata(x, y, z);
        double xOffset = (double) ((float) x + 0.5F);
        double yOffset = (double) ((float) y + 0.7F);
        double zOffset = (double) ((float) z + 0.5F);
        double verticalModifier = 0.2199999988079071D;
        double horizontalModifier = 0.27000001072883606D;

        if (orientation == 1) {
            world.spawnParticle("mobSpell", xOffset - horizontalModifier, yOffset + verticalModifier, zOffset, 0.0D, 0.0D, 0.0D);
            world.spawnParticle("flame", xOffset - horizontalModifier, yOffset + verticalModifier, zOffset, 0.0D, 0.0D, 0.0D);
        } else if (orientation == 2) {
            world.spawnParticle("mobSpell", xOffset + horizontalModifier, yOffset + verticalModifier, zOffset, 0.0D, 0.0D, 0.0D);
            world.spawnParticle("flame", xOffset + horizontalModifier, yOffset + verticalModifier, zOffset, 0.0D, 0.0D, 0.0D);
        } else if (orientation == 3) {
            world.spawnParticle("mobSpell", xOffset, yOffset + verticalModifier, zOffset - horizontalModifier, 0.0D, 0.0D, 0.0D);
            world.spawnParticle("flame", xOffset, yOffset + verticalModifier, zOffset - horizontalModifier, 0.0D, 0.0D, 0.0D);
        } else if (orientation == 4) {
            world.spawnParticle("mobSpell", xOffset, yOffset + verticalModifier, zOffset + horizontalModifier, 0.0D, 0.0D, 0.0D);
            world.spawnParticle("flame", xOffset, yOffset + verticalModifier, zOffset + horizontalModifier, 0.0D, 0.0D, 0.0D);
        } else {
            world.spawnParticle("mobSpell", xOffset, yOffset, zOffset, 0.0D, 0.0D, 0.0D);
            world.spawnParticle("flame", xOffset, yOffset, zOffset, 0.0D, 0.0D, 0.0D);
        }
    }
}
