package net.snowshock.goldentreasures.init;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemFood;
import net.snowshock.goldentreasures.items.*;
import net.snowshock.goldentreasures.references.ReferencesModInfo;
import net.snowshock.goldentreasures.references.ReferencesModItems;

@GameRegistry.ObjectHolder(ReferencesModInfo.MOD_ID)
public class InitModItems {
    public static final ItemGoldenTreasures golden_bomb = new ItemGoldenBomb();
    public static final ItemGoldenTreasures golden_chalice = new ItemGoldenChalice();
    public static final ItemGoldenTreasures golden_coin = new ItemGoldenCoin();
    public static final ItemGoldenTreasures golden_lantern = new ItemGoldenLantern();
    public static final ItemGoldenTreasures golden_staff = new ItemGoldenStaff();
    public static final ItemGoldenTreasures golden_miner = new ItemGoldenMiner();
    public static final ItemFood itemGoldenFood = null;



    public static void init() {
        GameRegistry.registerItem(golden_bomb, ReferencesModItems.GOLDEN_BOMB);
        GameRegistry.registerItem(golden_chalice, ReferencesModItems.GOLDEN_CHALICE);
        GameRegistry.registerItem(golden_coin, ReferencesModItems.GOLDEN_COIN);
        GameRegistry.registerItem(golden_lantern, ReferencesModItems.GOLDEN_LANTERN);
        GameRegistry.registerItem(golden_miner, ReferencesModItems.GOLDEN_MINER);
        GameRegistry.registerItem(golden_staff, ReferencesModItems.GOLDEN_STAFF);
        GameRegistry.registerItem(new ItemGoldenFood(), ReferencesModItems.GOLDEN_FOOD);
    }
}

//        TODO: Reimpliment these
//        public static final ItemGoldenTreasures golden_lilypad = new ItemGoldenLilypad();
//        GameRegistry.registerItem(golden_lilypad, ReferencesModItems.GOLDEN_LILYPAD);
