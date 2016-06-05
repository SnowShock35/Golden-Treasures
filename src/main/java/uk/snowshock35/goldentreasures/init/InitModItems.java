package uk.snowshock35.goldentreasures.init;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.BlockDispenser;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import uk.snowshock35.goldentreasures.items.*;
import uk.snowshock35.goldentreasures.references.ReferencesModInfo;
import uk.snowshock35.goldentreasures.references.ReferencesModItems;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.snowshock35.goldentreasures.references.ReferencesConfigInfo;

import java.util.ArrayList;
import java.util.List;

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
        registerItemIfEnabled(ReferencesConfigInfo.GoldenBomb.ITEM_ENABLED, bomb, ReferencesModItems.GOLDEN_BOMB);
        if (ReferencesConfigInfo.GoldenBomb.ITEM_ENABLED && ReferencesConfigInfo.GoldenBomb.DISPENSER_ENABLED)
            BlockDispenser.dispenseBehaviorRegistry.putObject(bomb, bomb.getDispenserBehaviour());
        registerItemIfEnabled(ReferencesConfigInfo.GoldenCoin.ITEM_ENABLED, new ItemGoldenCoin(), ReferencesModItems.GOLDEN_COIN);
        registerItemIfEnabled(ReferencesConfigInfo.GoldenFood.ITEM_ENABLED, new ItemGoldenFood(), ReferencesModItems.GOLDEN_FOOD);
        registerItemIfEnabled(ReferencesConfigInfo.GoldenStaff.ITEM_ENABLED, new ItemGoldenStaff(), ReferencesModItems.GOLDEN_STAFF);
        registerItemIfEnabled(ReferencesConfigInfo.GoldenMiner.ITEM_ENABLED, new ItemGoldenMiner(), ReferencesModItems.GOLDEN_MINER);
        registerItemIfEnabled(ReferencesConfigInfo.GoldenChalice.ITEM_ENABLED, new ItemGoldenChalice(), ReferencesModItems.GOLDEN_CHALICE);
        registerItemIfEnabled(ReferencesConfigInfo.GoldenLantern.ITEM_ENABLED, new ItemGoldenLantern(), ReferencesModItems.GOLDEN_LANTERN);
        registerItemIfEnabled(ReferencesConfigInfo.GoldenFeather.ITEM_ENABLED, new ItemGoldenFeather(), ReferencesModItems.GOLDEN_FEATHER);
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
        addItemToDungeonLoot(golden_food, ReferencesConfigInfo.GoldenFood.CHEST_SPAWN_CHANCE);
        addItemToDungeonLoot(golden_bomb, ReferencesConfigInfo.GoldenBomb.CHEST_SPAWN_CHANCE);
        addItemToDungeonLoot(golden_coin, ReferencesConfigInfo.GoldenCoin.CHEST_SPAWN_CHANCE);
        addItemToDungeonLoot(golden_staff, ReferencesConfigInfo.GoldenStaff.CHEST_SPAWN_CHANCE);
        addItemToDungeonLoot(golden_miner, ReferencesConfigInfo.GoldenMiner.CHEST_SPAWN_CHANCE);
        addItemToDungeonLoot(golden_lantern, ReferencesConfigInfo.GoldenLantern.CHEST_SPAWN_CHANCE);
        addItemToDungeonLoot(golden_feather, ReferencesConfigInfo.GoldenFeather.CHEST_SPAWN_CHANCE);
        addItemToDungeonLoot(golden_chalice, ReferencesConfigInfo.GoldenChalice.CHEST_SPAWN_CHANCE);
    }

    private static void addItemToDungeonLoot(Item item, int chestSpawnChance) {
        if(chestSpawnChance > 0) {
            final WeightedRandomChestContent chestContent = new WeightedRandomChestContent(
                    new ItemStack(item, 1), 1, 1, chestSpawnChance);
            addItemToLootLists(chestContent);
        }
    }

    private static void addIngredientsToDungeonLoot() {
        List<ItemStack> ingredients = new ArrayList<>();
        golden_crafting_component.getSubItems(golden_crafting_component, null, ingredients);

        for (ItemStack ingredient : ingredients) {
            addItemToLootLists(new WeightedRandomChestContent(ingredient, 2, 5, 10));
        }
     }

    private static void addItemToLootLists(WeightedRandomChestContent item) {
        for (String type : ReferencesConfigInfo.GeneralConfigs.DUNGEON_SPAWN_TYPES) {
            final ChestGenHooks typeInfo = ChestGenHooks.getInfo(type);
            if (typeInfo != null)
                typeInfo.addItem(item);
        }
    }
}
