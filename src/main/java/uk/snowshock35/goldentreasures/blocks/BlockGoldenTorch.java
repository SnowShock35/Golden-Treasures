package uk.snowshock35.goldentreasures.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockTorch;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import uk.snowshock35.goldentreasures.GoldenTreasures;
import uk.snowshock35.goldentreasures.interdiction.InterdictionField;
import uk.snowshock35.goldentreasures.items.IHeldBlockAction;
import uk.snowshock35.goldentreasures.references.ReferencesModBlocks;
import uk.snowshock35.goldentreasures.references.ReferencesModInfo;
import uk.snowshock35.goldentreasures.utils.LanguageHelper;

import java.util.Random;

public class BlockGoldenTorch extends BlockTorch implements IHeldBlockAction {

    private final InterdictionField interdictionField;

    public BlockGoldenTorch(InterdictionField interdictionField) {
        super();
        this.interdictionField = interdictionField;
        this.setBlockName(ReferencesModBlocks.GOLDEN_TORCH);
        this.setBlockTextureName(ReferencesModBlocks.GOLDEN_TORCH);
        this.setCreativeTab(GoldenTreasures.CREATIVE_TAB);
        this.setHardness(0.0F);
        this.setLightLevel(1.0F);
        this.setTickRandomly(false);
        this.setStepSound(BlockTorch.soundTypeWood);

    }

    @Override
    public String getUnlocalizedName() {
        return String.format("tile.%s%s", ReferencesModInfo.MOD_ID + ":", LanguageHelper.unwrapUnlocalizedName(super.getUnlocalizedName()));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
        blockIcon = iconRegister.registerIcon(String.format("%s", LanguageHelper.unwrapUnlocalizedName(this.getUnlocalizedName())));
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
        if (isClientSide(world))
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

    @Override
    public void doHeldItemUpdate(ItemStack ist, World world, Entity entity, int i) {
        if (world.isRemote)
            return;

        EntityPlayer player = getEntityPlayer(entity);
        if (player == null)
            return;

        int blockX = MathHelper.floor_double(player.posX);
        int blockY = MathHelper.floor_double(player.posY - player.getYOffset());
        int blockZ = MathHelper.floor_double(player.posZ);

        interdictionField.doInterdictionTick(world, blockX, blockY, blockZ);
    }

    private boolean isClientSide(World world) {
        return world.isRemote;
    }

    /**
     * Converts entity to entityplayer. Will return
     *
     * @return {@code null} if the entity is not an instance of EntityPlayer. Otherwise returns {@code entity}
     */
    private EntityPlayer getEntityPlayer(Entity entity) {
        EntityPlayer player = null;
        if (entity instanceof EntityPlayer) {
            player = (EntityPlayer) entity;
        }
        return player;
    }
}
