package uk.snowshock35.goldentreasures.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import uk.snowshock35.goldentreasures.entity.EntityGoldenBomb;
import uk.snowshock35.goldentreasures.references.ReferencesModItems;

public class ItemGoldenBomb extends ItemGoldenTreasures {
    public ItemGoldenBomb() {
        super();
        this.setUnlocalizedName(ReferencesModItems.GOLDEN_BOMB);
        this.setMaxStackSize(64);
        this.setMaxDamage(0);
        canRepair = false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack stack) {
        return EnumRarity.rare;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack, int pass) {
        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        if (par2World.isRemote)
            return par1ItemStack;
        if (!par3EntityPlayer.capabilities.isCreativeMode) {
            --par1ItemStack.stackSize;
        }

        par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        par2World.spawnEntityInWorld(new EntityGoldenBomb(par2World, par3EntityPlayer));

        return par1ItemStack;
    }

    private final IBehaviorDispenseItem dispenserBehaviour = new DispenserBehaviour();
    public IBehaviorDispenseItem getDispenserBehaviour()
    {
        return dispenserBehaviour;
    }

    protected class DispenserBehaviour extends BehaviorProjectileDispense {

        @Override
        protected IProjectile getProjectileEntity(World world, IPosition position) {
            final EntityGoldenBomb entityGoldenBomb = new EntityGoldenBomb(world);
            entityGoldenBomb.setPosition(position.getX(), position.getY(), position.getZ());
            return entityGoldenBomb;
        }
    }
}
