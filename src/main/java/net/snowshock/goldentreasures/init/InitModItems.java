package net.snowshock.goldentreasures.init;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.BlockDispenser;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import net.snowshock.goldentreasures.items.*;
import net.snowshock.goldentreasures.references.ReferencesModInfo;
import net.snowshock.goldentreasures.references.ReferencesModItems;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static net.snowshock.goldentreasures.references.ReferencesConfigInfo.*;

@GameRegistry.ObjectHolder(ReferencesModInfo.MOD_ID)
public class InitModItems {

    private static final Logger LOGGER = LogManager.getLogger(ReferencesModInfo.MOD_ID);

    public static final ItemFood golden_food = null;
    public static final ItemGoldenTreasures golden_bomb = null;
    public static final ItemGoldenTreasures golden_coin = null;
    public static final ItemGoldenTreasures golden_staff = null;
    public static final ItemGoldenTreasures golden_miner = null;
    public static final ItemGoldenTreasures golden_chalice = null;
    public static final ItemGoldenTreasures golden_lantern = null;
    public static final ItemGoldenTreasures golden_feather = null;
    public static final ItemGoldenTreasures golden_crafting_component = null;

    public static void preInit() {
        LOGGER.debug("Initializing Items....");

        final ItemGoldenBomb bomb = new ItemGoldenBomb();
        registerItemIfEnabled(GoldenBomb.ITEM_ENABLED, bomb, ReferencesModItems.GOLDEN_BOMB);
        if (GoldenBomb.ITEM_ENABLED && GoldenBomb.DISPENSER_ENABLED)
            BlockDispenser.dispenseBehaviorRegistry.putObject(bomb, bomb.getDispenserBehaviour());
        registerItemIfEnabled(GoldenCoin.ITEM_ENABLED, new ItemGoldenCoin(), ReferencesModItems.GOLDEN_COIN);
        registerItemIfEnabled(GoldenFood.ITEM_ENABLED, new ItemGoldenFood(), ReferencesModItems.GOLDEN_FOOD);
        registerItemIfEnabled(GoldenStaff.ITEM_ENABLED, new ItemGoldenStaff(), ReferencesModItems.GOLDEN_STAFF);
        registerItemIfEnabled(GoldenMiner.ITEM_ENABLED, new ItemGoldenMiner(), ReferencesModItems.GOLDEN_MINER);
        registerItemIfEnabled(GoldenChalice.ITEM_ENABLED, new ItemGoldenChalice(), ReferencesModItems.GOLDEN_CHALICE);
        registerItemIfEnabled(GoldenLantern.ITEM_ENABLED, new ItemGoldenLantern(), ReferencesModItems.GOLDEN_LANTERN);
        registerItemIfEnabled(GoldenFeather.ITEM_ENABLED, new ItemGoldenFeather(), ReferencesModItems.GOLDEN_FEATHER);
        GameRegistry.registerItem(new ItemGoldenCraftingComponent(), ReferencesModItems.GOLDEN_CRAFTING_COMPONENT);

        LOGGER.log(Level.INFO, "Mod Items Initialized");
    }

    private static void registerItemIfEnabled(boolean itemEnabled, Item item, String name) {
        if (itemEnabled) {
            GameRegistry.registerItem(item, name);
        }
    }

    public static void initDungeonLoot() {
        addIngredientsToDungeonLoot();
        addItemToDungeonLoot(golden_food, GoldenFood.CHEST_SPAWN_CHANCE);
        addItemToDungeonLoot(golden_bomb, GoldenBomb.CHEST_SPAWN_CHANCE);
        addItemToDungeonLoot(golden_coin, GoldenCoin.CHEST_SPAWN_CHANCE);
        addItemToDungeonLoot(golden_staff, GoldenStaff.CHEST_SPAWN_CHANCE);
        addItemToDungeonLoot(golden_miner, GoldenMiner.CHEST_SPAWN_CHANCE);
        addItemToDungeonLoot(golden_lantern, GoldenLantern.CHEST_SPAWN_CHANCE);
        addItemToDungeonLoot(golden_feather, GoldenFeather.CHEST_SPAWN_CHANCE);
        addItemToDungeonLoot(golden_chalice, GoldenChalice.CHEST_SPAWN_CHANCE);
    }

    private static void addItemToDungeonLoot(Item item, int chestSpawnChance) {
        final WeightedRandomChestContent chestContent = new WeightedRandomChestContent(
                new ItemStack(item, 1), 1, 1, chestSpawnChance);
        addItemToLootLists(chestContent);
    }

    private static void addIngredientsToDungeonLoot() {
        List<ItemStack> ingredients = new ArrayList<>();
        golden_crafting_component.getSubItems(golden_crafting_component, null, ingredients);

        for (ItemStack ingredient : ingredients) {
            addItemToLootLists(new WeightedRandomChestContent(ingredient, 2, 5, 10));
        }
     }

    private static void addItemToLootLists(WeightedRandomChestContent item) {
        for (String type : GeneralConfigs.DUNGEON_SPAWN_TYPES) {
            final ChestGenHooks typeInfo = ChestGenHooks.getInfo(type);
            if (typeInfo != null)
                typeInfo.addItem(item);
        }
    }
}
