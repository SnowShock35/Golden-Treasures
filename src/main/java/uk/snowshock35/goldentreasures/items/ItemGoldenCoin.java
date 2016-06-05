package uk.snowshock35.goldentreasures.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import uk.snowshock35.goldentreasures.references.ReferencesModItems;
import uk.snowshock35.goldentreasures.utils.NBTHelper;
import uk.snowshock35.goldentreasures.references.ReferencesConfigInfo;

import java.util.List;

public class ItemGoldenCoin extends ItemGoldenTreasuresTogglable {
    @SideOnly(Side.CLIENT)
    private IIcon iconOverlay;

    public ItemGoldenCoin() {
        super();
        this.setUnlocalizedName(ReferencesModItems.GOLDEN_COIN);
        this.setMaxStackSize(1);
        this.setMaxDamage(0);
        canRepair = false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.epic;
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
        if (enabled && renderPass == 1)
            return iconOverlay;
        else
            return super.getIcon(ist, renderPass);
    }

    @Override
    public void onUpdate(ItemStack ist, World world, Entity entity, int i, boolean isHeld) {
        if (world.isRemote)
            return;
        if (!disabledAudio())
            if (NBTHelper.getShort("soundTimer", ist) > 0) {
                if (NBTHelper.getShort("soundTimer", ist) % 2 == 0) {
                    world.playSoundAtEntity(entity, "random.orb", 0.1F, 0.5F * ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.8F));
                }
                NBTHelper.setShort("soundTimer", ist, NBTHelper.getShort("soundTimer", ist) - 1);
            }
        if (!isEnabled(ist))
            return;
        EntityPlayer player = null;
        if (entity instanceof EntityPlayer) {
            player = (EntityPlayer) entity;
        }
        if (player == null)
            return;
        scanForEntitiesInRange(world, player, getStandardPullDistance());
    }

    private void scanForEntitiesInRange(World world, EntityPlayer player, double d) {
        List iList = world.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(player.posX - d, player.posY - d, player.posZ - d, player.posX + d, player.posY + d, player.posZ + d));
        for (Object anIList : iList) {
            EntityItem item = (EntityItem) anIList;
            if (!checkForRoom(item.getEntityItem(), player)) {
                continue;
            }
            if (item.delayBeforeCanPickup > 0) {
                item.delayBeforeCanPickup = 0;
            }
            if (player.getDistanceToEntity(item) < 1.5D) {
                continue;
            }
            teleportEntityToPlayer(item, player);
            break;
        }
        List iList2 = world.getEntitiesWithinAABB(EntityXPOrb.class, AxisAlignedBB.getBoundingBox(player.posX - d, player.posY - d, player.posZ - d, player.posX + d, player.posY + d, player.posZ + d));
        for (Object anIList2 : iList2) {
            EntityXPOrb item = (EntityXPOrb) anIList2;
            if (player.xpCooldown > 0) {
                player.xpCooldown = 0;
            }
            if (player.getDistanceToEntity(item) < 1.5D) {
                continue;
            }
            teleportEntityToPlayer(item, player);
            break;
        }
    }

    private void teleportEntityToPlayer(Entity item, EntityPlayer player) {
        player.worldObj.spawnParticle("mobSpell", item.posX + 0.5D + player.worldObj.rand.nextGaussian() / 8, item.posY + 0.2D, item.posZ + 0.5D + player.worldObj.rand.nextGaussian() / 8, 0.9D, 0.9D, 0.0D);
        player.getLookVec();
        double x = player.posX + player.getLookVec().xCoord * 0.2D;
        double y = player.posY - player.height / 2F;
        double z = player.posZ + player.getLookVec().zCoord * 0.2D;
        item.setPosition(x, y, z);
        if (!disabledAudio()) {
            player.worldObj.playSoundAtEntity(player, "random.orb", 0.1F, 0.5F * ((player.worldObj.rand.nextFloat() - player.worldObj.rand.nextFloat()) * 0.7F + 1.8F));
        }
    }

    private boolean checkForRoom(ItemStack item, EntityPlayer player) {
        int remaining = item.stackSize;
        for (ItemStack ist : player.inventory.mainInventory) {
            if (ist == null) {
                continue;
            }
            if (ist.getItem() == item.getItem() && ist.getItemDamage() == item.getItemDamage()) {
                if (ist.stackSize + remaining <= ist.getMaxStackSize())
                    return true;
                else {
                    int count = ist.stackSize;
                    while (count < ist.getMaxStackSize()) {
                        count++;
                        remaining--;
                        if (remaining == 0)
                            return true;
                    }
                }
            }
        }
        for (int slot = 0; slot < player.inventory.mainInventory.length; slot++) {
            if (player.inventory.mainInventory[slot] == null)
                return true;
        }
        return false;
    }

    @Override
    public void onUsingTick(ItemStack ist, EntityPlayer player, int count) {
        scanForEntitiesInRange(player.worldObj, player, getLongRangePullDistance());
    }

    public double getLongRangePullDistance() {
        return (double) ReferencesConfigInfo.GoldenCoin.LONG_PULL_DISTANCE;
    }

    public double getStandardPullDistance() {
        return (double) ReferencesConfigInfo.GoldenCoin.STANDARD_PULL_DISTANCE;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 64;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.block;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack ist, World world, EntityPlayer player) {
        if (player.isSneaking()) {
            if (!disabledAudio()) {
                NBTHelper.setShort("soundTimer", ist, 6);
            }
            toggleEnabled(ist);
        } else {
            player.setItemInUse(ist, this.getMaxItemUseDuration(ist));
        }
        return ist;
    }

    private boolean disabledAudio() {
        return ReferencesConfigInfo.GoldenCoin.AUDIO_DISABLED;
    }

}
