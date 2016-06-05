package uk.snowshock35.goldentreasures.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import uk.snowshock35.goldentreasures.init.InitModItems;
import uk.snowshock35.goldentreasures.references.ReferencesConfigInfo;
import uk.snowshock35.goldentreasures.references.ReferencesModItems;
import uk.snowshock35.goldentreasures.utils.ContentHelper;
import uk.snowshock35.goldentreasures.utils.InventoryHelper;

import java.util.ArrayList;
import java.util.List;

public class ItemGoldenLantern extends ItemGoldenTreasuresTogglable {
    public ItemGoldenLantern() {
        super();
        this.setUnlocalizedName(ReferencesModItems.GOLDEN_LANTERN);
        this.setMaxStackSize(1);
        this.setMaxDamage(0);
        canRepair = false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.epic;
    }

    public int getRange() {
        return ReferencesConfigInfo.GoldenLantern.PLACEMENT_SCAN_RADIUS;
    }
    // event driven item, does nothing here.

    // minor jump buff
    @Override
    public void onUpdate(ItemStack ist, World world, Entity e, int i, boolean isHeld) {
        if (!isEnabled(ist))
            return;
        if (world.isRemote)
            return;
        if (e instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) e;
            if (e instanceof EntityPlayer) {
                player = (EntityPlayer) e;
            }
            if (player == null)
                return;


            //TODO this is where we'll be placing our algorithm for darkness detection and placing torches!

            //TODO ACTUALLY make this configurable
            // always on for now, takes effect only at a configurable light level

            int playerX = MathHelper.floor_double(player.posX);
            int playerY = MathHelper.floor_double(player.boundingBox.minY);
            int playerZ = MathHelper.floor_double(player.posZ);

            placement:
            for (int xDiff = -getRange(); xDiff <= getRange(); xDiff++) {
                for (int zDiff = -getRange(); zDiff <= getRange(); zDiff++) {
                    for (int yDiff = getRange() / 2; yDiff >= -getRange() / 2; yDiff--) {
                        int x = playerX + xDiff;
                        int y = playerY + yDiff;
                        int z = playerZ + zDiff;
                        if (!player.worldObj.isAirBlock(x, y, z))
                            continue;
                        int lightLevel = player.worldObj.getBlockLightValue(x, y, z);
                        if (lightLevel > ReferencesConfigInfo.GoldenLantern.MIN_LIGHT_LEVEL)
                            continue;
                        if (tryToPlaceTorchAround(ist, x, y, z, player, world))
                            break placement;
                    }
                }
            }

            //attemptPlacementByLookVector(player);

        }
    }

    //
//    public void attemptPlacementByLookVector(EntityPlayer player) {
//        MovingObjectPosition mop = getMovingObjectPositionFromPlayer(player.worldObj, player, false);
//        if (!player.canPlayerEdit(x, y, z, side, ist))
//            return;
//
//    }
//
//    //experimenting with a look vector based version of the lantern to avoid some really annoying stuff I can't figure out because I'm dumb.
//    @Override
//    protected MovingObjectPosition getMovingObjectPositionFromPlayer(World world, EntityPlayer player, boolean weirdBucketBoolean) {
//        float movementCoefficient = 1.0F;
//        float pitchOff = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * movementCoefficient;
//        float yawOff = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * movementCoefficient;
//        double xOff = player.prevPosX + (player.posX - player.prevPosX) * movementCoefficient;
//        double yOff = player.prevPosY + (player.posY - player.prevPosY) * movementCoefficient + 1.62D - player.yOffset;
//        double zOff = player.prevPosZ + (player.posZ - player.prevPosZ) * movementCoefficient;
//        Vec3 playerVector = Vec3.createVectorHelper(xOff, yOff, zOff);
//        float cosTraceYaw = MathHelper.cos(-yawOff * 0.017453292F - (float) Math.PI);
//        float sinTraceYaw = MathHelper.sin(-yawOff * 0.017453292F - (float) Math.PI);
//        float cosTracePitch = -MathHelper.cos(-pitchOff * 0.017453292F);
//        float sinTracePitch = MathHelper.sin(-pitchOff * 0.017453292F);
//        float pythagoraStuff = sinTraceYaw * cosTracePitch;
//        float pythagoraStuff2 = cosTraceYaw * cosTracePitch;
//        double distCoeff = getRange();
//        Vec3 rayTraceVector = playerVector.addVector(pythagoraStuff * distCoeff, sinTracePitch * distCoeff, pythagoraStuff2 * distCoeff);
//        return world.rayTraceBlocks(playerVector, rayTraceVector, weirdBucketBoolean);
//    }
//
    private boolean findAndDrainGoldenStaff(EntityPlayer player) {
        ItemGoldenStaff staffItem = (ItemGoldenStaff) InitModItems.golden_staff;

        // Shortcut out if the staff doesn't exist or isn't enabled.
        if(staffItem == null || !ReferencesConfigInfo.GoldenStaff.ITEM_ENABLED) {
            return false;
        }

        if (player.capabilities.isCreativeMode)
            return true;
        for (int slot = 0; slot < player.inventory.getSizeInventory(); slot++) {
            if (player.inventory.getStackInSlot(slot) == null)
                continue;
            if (!(staffItem == player.inventory.getStackInSlot(slot).getItem()))
                continue;
            Item torch = ItemBlock.getItemFromBlock(Blocks.torch);
            if (staffItem.removeItemFromInternalStorage(player.inventory.getStackInSlot(slot), torch, 1))
                return true;
        }
        return false;
    }

    public boolean tryToPlaceTorchAround(ItemStack ist, int xO, int yO, int zO, EntityPlayer player, World world) {
        Block var12 = Blocks.torch;

        int x = xO;
        int y = yO;
        int z = zO;

        double playerEyeHeight = player.posY + player.getEyeHeight();

        for (float xOff = -0.2F; xOff <= 0.2F; xOff += 0.4F) {
            for (float yOff = -0.2F; yOff <= 0.2F; yOff += 0.4F) {
                for (float zOff = -0.2F; zOff <= 0.2F; zOff += 0.4F) {

                    Vec3 playerVec = Vec3.createVectorHelper(player.posX + xOff, playerEyeHeight + yOff, player.posZ + zOff);
                    Vec3 rayTraceVector = Vec3.createVectorHelper((float) x + 0.5D + xOff, (float) y + 0.5D + yOff, (float) z + 0.5D + zOff);

                    MovingObjectPosition mop = world.func_147447_a(playerVec, rayTraceVector, false, false, true);

                    if (mop != null && mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                        Block block = world.getBlock(mop.blockX, mop.blockY, mop.blockZ);
                        if (block.getCollisionBoundingBoxFromPool(world, mop.blockX, mop.blockY, mop.blockZ) != null) {
                            int meta = world.getBlockMetadata(mop.blockX, mop.blockY, mop.blockZ);
                            if (block.canCollideCheck(meta, false))
                                return false;
                        }
                    }
                }
            }
        }


        float xOff = (float) player.posX;
        float zOff = (float) player.posZ;
        float yOff = (float) player.posY;

        if (Blocks.torch.canPlaceBlockAt(world, x, y, z)) {
            int rotation = ((MathHelper.floor_double((double) (player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) + 2) % 4;
            int trySide = 0;
            switch (rotation) {
                case (0):
                    trySide = 5;
                    break;
                case (1):
                    trySide = 3;
                    break;
                case (2):
                    trySide = 4;
                    break;
                case (3):
                    trySide = 2;
                    break;
            }

            List<Integer> trySides = new ArrayList<Integer>();
            trySides.add(trySide);
            trySides.add(0);
            int[] tryOtherSides = {2, 3, 4, 5};
            for (int tryOtherSide : tryOtherSides) {
                if (trySides.contains(tryOtherSide)) continue;
                trySides.add(tryOtherSide);
            }
            for (int side : trySides) {
                if (!world.canPlaceEntityOnSide(Blocks.torch, x, y, z, false, side, player, ist))
                    continue;
                if (!(InventoryHelper.consumeItem(Blocks.torch, player, 0, 1) || findAndDrainGoldenStaff(player)))
                    continue;
                if (placeBlockAt(ist, player, world, x, y, z, side, xOff, yOff, zOff, attemptSide(world, x, y, z, side))) {
                    Blocks.torch.onBlockAdded(world, x, y, z);
                    double gauss = 0.5D + world.rand.nextFloat() / 2;
                    world.spawnParticle("mobSpell", x + 0.5D, y + 0.5D, z + 0.5D, gauss, gauss, 0.0F);
                    world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, var12.stepSound.getStepResourcePath(), (var12.stepSound.getVolume() + 1.0F) / 2.0F, var12.stepSound.getPitch() * 0.8F);
                    return true;
                }
            }
        }
        return false;
    }

    private int attemptSide(World world, int x, int y, int z, int side) {
        return Blocks.torch.onBlockPlaced(world, x, y, z, side, x, y, z, 0);
    }

    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        if (!world.setBlock(x, y, z, Blocks.torch, metadata, 3))
            return false;

        if (ContentHelper.areBlocksEqual(world.getBlock(x, y, z), Blocks.torch)) {
            Blocks.torch.onNeighborBlockChange(world, x, y, z, world.getBlock(x, y, z));
            Blocks.torch.onBlockPlacedBy(world, x, y, z, player, stack);
        }

        return true;
    }
}
