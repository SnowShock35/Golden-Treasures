package net.snowshock.goldentreasures.init;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemFood;
import net.snowshock.goldentreasures.items.*;
import net.snowshock.goldentreasures.references.ReferencesModInfo;
import net.snowshock.goldentreasures.references.ReferencesModItems;

@GameRegistry.ObjectHolder(ReferencesModInfo.MOD_ID)
public class InitModItems {
    public static final ItemGoldenTreasures golden_bomb = null;
    public static final ItemGoldenTreasures golden_coin = null;
    public static final ItemGoldenTreasures golden_miner = null;
    public static final ItemGoldenTreasures golden_chalice = null;
    public static final ItemGoldenTreasures golden_lantern = null;
    public static final ItemGoldenTreasures golden_staff = null;
    public static final ItemFood golden_food = null;

    public static void init() {
        GameRegistry.registerItem(new ItemGoldenBomb(), ReferencesModItems.GOLDEN_BOMB);
        GameRegistry.registerItem(new ItemGoldenCoin(), ReferencesModItems.GOLDEN_COIN);
        GameRegistry.registerItem(new ItemGoldenMiner(), ReferencesModItems.GOLDEN_MINER);
        GameRegistry.registerItem(new ItemGoldenChalice(), ReferencesModItems.GOLDEN_CHALICE);
        GameRegistry.registerItem(new ItemGoldenLantern(), ReferencesModItems.GOLDEN_LANTERN);
        GameRegistry.registerItem(new ItemGoldenFood(), ReferencesModItems.GOLDEN_FOOD);
        GameRegistry.registerItem(new ItemGoldenStaff(), ReferencesModItems.GOLDEN_STAFF);
    }
}

// TODO: Reimpliment these
//    public static final ItemGoldenTreasures golden_lilypad = null;
//    GameRegistry.registerItem(golden_lilypad, ReferencesModItems.GOLDEN_LILYPAD);
