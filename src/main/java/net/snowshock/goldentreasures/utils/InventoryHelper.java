package net.snowshock.goldentreasures.utils;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;

public class InventoryHelper {
    public InventoryHelper() {
    }

    public static void removeItem(ItemStack stack, IInventory inventory, int quantity) {
        removeItem(stack, inventory, quantity, -1);
    }

    public static void removeItem(ItemStack stack, EntityPlayer player, int quantity) {
        removeItem(stack, player.inventory, quantity, player.inventory.mainInventory.length);
    }

    public static void removeItem(ItemStack stack, IInventory inventory, int quantity, int limit) {
        for (int slot = 0; slot < Math.min(inventory.getSizeInventory(), limit > 0 ? limit : inventory.getSizeInventory()); ++slot) {
            ItemStack ist = inventory.getStackInSlot(slot);
            if (ist != null && inventory.getStackInSlot(slot).isItemEqual(stack)) {
                while (quantity > 0 && inventory.getStackInSlot(slot) != null) {
                    inventory.setInventorySlotContents(slot, inventory.decrStackSize(slot, 1));
                    --quantity;
                }
            }
        }

        inventory.markDirty();
    }

    public static ItemStack getTargetItem(ItemStack self, IInventory inventory) {
        return getTargetItem(self, inventory, true);
    }

    public static ItemStack getTargetItem(ItemStack self, IInventory inventory, boolean disposeOfItem) {
        ItemStack targetItem = null;
        int itemQuantity = 0;

        for (int slot = 0; slot < inventory.getSizeInventory(); ++slot) {
            ItemStack stack = inventory.getStackInSlot(slot);
            if (stack != null && !self.isItemEqual(stack) && stack.getMaxStackSize() != 1 && stack.getTagCompound() == null && getItemQuantity(stack, inventory) > itemQuantity) {
                itemQuantity = getItemQuantity(stack, inventory);
                targetItem = stack.copy();
                if (disposeOfItem) {
                    inventory.decrStackSize(slot, 1);
                }
            }
        }

        inventory.markDirty();
        return targetItem;
    }

    public static int getItemQuantity(ItemStack stack, IInventory inventory) {
        return getItemQuantity(stack, inventory, 0);
    }

    public static int getItemQuantity(ItemStack stack, EntityPlayer player) {
        return getItemQuantity(stack, player.inventory, player.inventory.mainInventory.length);
    }

    public static int getItemQuantity(ItemStack stack, IInventory inventory, int limit) {
        int itemQuantity = 0;

        for (int slot = 0; slot < Math.min(inventory.getSizeInventory(), limit > 0 ? limit : inventory.getSizeInventory()); ++slot) {
            ItemStack newStack = inventory.getStackInSlot(slot);
            if (newStack != null && stack.isItemEqual(newStack)) {
                itemQuantity += newStack.stackSize;
            }
        }

        return itemQuantity;
    }

    public static boolean consumeItem(Object item, EntityPlayer player) {
        return consumeItem((Object[]) (new Object[]{item}), player, 0, 1);
    }

    public static boolean consumeItem(Object item, EntityPlayer player, int minCount) {
        return consumeItem((Object[]) (new Object[]{item}), player, minCount, 1);
    }

    public static boolean consumeItem(Object item, EntityPlayer player, int minCount, int amountDecreased) {
        return consumeItem(new Object[]{item}, player, minCount, amountDecreased);
    }

    public static boolean consumeItem(Object[] itemList, EntityPlayer player, int minCount, int amountDecreased) {
        if (player.capabilities.isCreativeMode) {
            return true;
        } else if (itemList.length == 0 || !(itemList[0] instanceof ItemStack) && !(itemList[0] instanceof Item) && !(itemList[0] instanceof Block)) {
            return false;
        } else {
            ArrayList suggestedSlots = new ArrayList();
            int itemCount = 0;

            int count;
            int stackSize;
            for (count = 0; count < player.inventory.mainInventory.length; ++count) {
                if (player.inventory.mainInventory[count] != null) {
                    ItemStack i$ = player.inventory.mainInventory[count];
                    Object[] slot = itemList;
                    stackSize = itemList.length;

                    for (int i$1 = 0; i$1 < stackSize; ++i$1) {
                        Object stack = slot[i$1];
                        if (stack instanceof ItemStack && i$.isItemEqual((ItemStack) stack) || stack instanceof Block && ContentHelper.areItemsEqual(Item.getItemFromBlock((Block) stack), i$.getItem()) || stack instanceof Item && ContentHelper.areItemsEqual((Item) stack, i$.getItem())) {
                            itemCount += player.inventory.mainInventory[count].stackSize;
                            suggestedSlots.add(Integer.valueOf(count));
                        }
                    }
                }
            }

            count = amountDecreased;
            if (suggestedSlots.size() > 0 && itemCount >= minCount + amountDecreased) {
                for (Iterator var12 = suggestedSlots.iterator(); var12.hasNext(); count -= stackSize) {
                    int var13 = ((Integer) var12.next()).intValue();
                    stackSize = player.inventory.getStackInSlot(var13).stackSize;
                    if (stackSize >= count) {
                        player.inventory.decrStackSize(var13, count);
                        return true;
                    }

                    player.inventory.decrStackSize(var13, stackSize);
                }
            }

            return false;
        }
    }
}
