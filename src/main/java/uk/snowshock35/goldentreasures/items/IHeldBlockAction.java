package uk.snowshock35.goldentreasures.items;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Interface for blocks which will do an action when the {@link ItemBlock} for this item is held.
 */
public interface IHeldBlockAction {
    void doHeldItemUpdate(ItemStack ist, World world, Entity entity, int i);
}
