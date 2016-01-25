package net.snowshock.goldentreasures.items;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.snowshock.goldentreasures.references.ReferencesModItems;
import net.snowshock.goldentreasures.utils.ContentHelper;
import net.snowshock.goldentreasures.utils.InventoryHelper;
import net.snowshock.goldentreasures.utils.LanguageHelper;
import net.snowshock.goldentreasures.utils.NBTHelper;
import org.lwjgl.input.Keyboard;

import java.util.List;

import static net.snowshock.goldentreasures.references.ReferencesConfigInfo.GoldenMiner.*;

public class ItemGoldenMiner extends ItemGoldenTreasuresTogglable {

    public static final List<String> defaultBlocks =
            ImmutableList.of(
                    "minecraft:dirt",
                    "minecraft:grass",
                    "minecraft:gravel",
                    "minecraft:cobblestone",
                    "minecraft:stone",
                    "minecraft:sand",
                    "minecraft:sandstone",
                    "minecraft:snow",
                    "minecraft:soul_sand",
                    "minecraft:netherrack",
                    "minecraft:end_stone");

    public ItemGoldenMiner() {
        super();
        this.setUnlocalizedName(ReferencesModItems.GOLDEN_MINER);
        this.setMaxStackSize(1);
        canRepair = false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.common;
    }

    @Override
    public void addInformation(ItemStack ist, EntityPlayer player, List list, boolean par4) {
        if (!Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && !Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
            return;
        this.formatTooltip(ImmutableMap.of("charge", Integer.toString(getStoredGunpowderLevel(ist))), ist, list);
        if (this.isEnabled(ist))
            LanguageHelper.formatTooltip("tooltip.absorb_active", ImmutableMap.of("item", EnumChatFormatting.GRAY + Items.gunpowder.getItemStackDisplayName(new ItemStack(Items.gunpowder))), ist, list);
        LanguageHelper.formatTooltip("tooltip.absorb", null, ist, list);
    }

    @Override
    public boolean onItemUse(ItemStack ist, EntityPlayer player, World world, int x, int y, int z, int side, float xOff, float yOff, float zOff) {
        if (getStoredGunpowderLevel(ist) > getCost() || player.capabilities.isCreativeMode) {
            doExplosion(world, x, y, z, side, ist);
        }
        return true;
    }

    private int getStoredGunpowderLevel(ItemStack ist) {
        return NBTHelper.getInteger("gunpowder", ist);
    }

    @Override
    public void onUpdate(ItemStack ist, World world, Entity e, int i, boolean isHeld) {
        if (world.isRemote)
            return;
        EntityPlayer player = null;
        if (e instanceof EntityPlayer) {
            player = (EntityPlayer) e;
        }
        if (player == null)
            return;

        if (this.isEnabled(ist)) {
            if (getStoredGunpowderLevel(ist) + gunpowderWorth() < gunpowderLimit()) {
                if (InventoryHelper.consumeItem(new ItemStack(Items.gunpowder), player)) {
                    setStoredGunpowderLevel(ist, getStoredGunpowderLevel(ist) + gunpowderWorth());
                }
            }
        }
    }

    public int getExplosionRadius() {
        return EXPLOSION_RADIUS;
    }

    public boolean centeredExplosion() {
        return CENTERED_EXPLOSION;
    }

    public boolean perfectCube() {
        return PERFECT_CUBE;
    }

    public void doExplosion(World world, int x, int y, int z, int side, ItemStack ist) {
        boolean destroyedSomething = false;
        boolean playOnce = true;
        if (!centeredExplosion()) {
            y = y + (side == 0 ? getExplosionRadius() : side == 1 ? -getExplosionRadius() : 0);
            z = z + (side == 2 ? getExplosionRadius() : side == 3 ? -getExplosionRadius() : 0);
            x = x + (side == 4 ? getExplosionRadius() : side == 5 ? -getExplosionRadius() : 0);
        }
        for (int xD = -getExplosionRadius(); xD <= getExplosionRadius(); xD++) {
            for (int yD = -getExplosionRadius(); yD <= getExplosionRadius(); yD++) {
                for (int zD = -getExplosionRadius(); zD <= getExplosionRadius(); zD++) {
                    if (!perfectCube()) {
                        ChunkCoordinates origin = new ChunkCoordinates(x, y, z);
                        ChunkCoordinates target = new ChunkCoordinates(x + xD, y + yD, z + zD);
                        double distance = origin.getDistanceSquaredToChunkCoordinates(target);
                        if (distance >= getExplosionRadius())
                            continue;
                    }

                    if (isBreakable(ContentHelper.getIdent(world.getBlock(x + xD, y + yD, z + zD)))) {
                        world.setBlock(x + xD, y + yD, z + zD, Blocks.air);
                        if (world.rand.nextInt(2) == 0) {
                            world.spawnParticle("largeexplode", x + xD + (world.rand.nextFloat() - 0.5F), y + yD + (world.rand.nextFloat() - 0.5F), z + zD + (world.rand.nextFloat() - 0.5F), 0.0D, 0.0D, 0.0D);
                        }
                        destroyedSomething = true;
                        if (playOnce) {
                            world.playSoundEffect(x, y, z, "random.explode", 4.0F, (1.0F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F) * 0.7F);
                            playOnce = false;
                        }
                    }
                }
            }
        }
        if (destroyedSomething) {
            int newGunpowderLevel = getStoredGunpowderLevel(ist) - getCost();
            newGunpowderLevel = clampToZero(newGunpowderLevel);
            setStoredGunpowderLevel(ist, newGunpowderLevel);
        }
    }

    private int clampToZero(int newGunpowderLevel) {
        return newGunpowderLevel >=0 ? newGunpowderLevel : 0;
    }

    private void setStoredGunpowderLevel(ItemStack ist, int i) {
        NBTHelper.setInteger("gunpowder", ist, i);
    }

    public boolean isBreakable(String id) {
        return BLOCKS.indexOf(id) != -1;
    }

    private int getCost() {
        return COST;
    }

    private int gunpowderWorth() {
        return GUNPOWDER_WORTH;
    }

    private int gunpowderLimit() {
        return GUNPOWDER_LIMIT;
    }

}
