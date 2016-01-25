package net.snowshock.goldentreasures.init;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorProjectileDispense;
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

import static net.snowshock.goldentreasures.references.ReferencesConfigInfo.GeneralConfigs.NUM_INGREDIENTS;
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
    public static final ItemGoldenTreasures golden_ingredient = null;

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
        GameRegistry.registerItem(new ItemGoldenIngredient(), ReferencesModItems.GOLDEN_INGREDIENT);

        LOGGER.log(Level.INFO, "Mod Items Initialized");
    }

    private static void registerItemIfEnabled(boolean itemEnabled, Item item, String name) {
        if (itemEnabled) {
            GameRegistry.registerItem(item, name);
        }
    }

    public static void initDungeonLoot() {
        addIngredientsToDungeonLoot();
    }

    private static void addIngredientsToDungeonLoot() {
        for (int meta = 0; meta < NUM_INGREDIENTS; meta++) {
            final WeightedRandomChestContent item = new WeightedRandomChestContent(new ItemStack(golden_ingredient, 1, meta), 3, 5, 10);
            addItemToLootLists(item);
        }
    }

    private static void addItemToLootLists(WeightedRandomChestContent item) {
        ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(item);
        ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST).addItem(item);
        ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(item);
        ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY).addItem(item);
        ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(item);
        ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(item);
        ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(item);
        ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(item);
        ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(item);
    }
}
