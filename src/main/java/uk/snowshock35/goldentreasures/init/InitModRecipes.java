package uk.snowshock35.goldentreasures.init;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
import org.apache.logging.log4j.Level;
import uk.snowshock35.goldentreasures.references.ReferencesConfigInfo;

import static uk.snowshock35.goldentreasures.GoldenTreasures.LOGGER;

public class InitModRecipes
{
    public static void postInit()
    {
        if(ReferencesConfigInfo.GoldenTorch.ITEM_ENABLED) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(InitModBlocks.blockGoldenTorch), "nnn", "nin", "nnn", 'n', "nuggetGold", 'i', "ingotIron"));
            LOGGER.log(Level.INFO, "Created Recipe For: " + InitModBlocks.blockGoldenTorch.getLocalizedName().replace("{{!colors.yellow}}", ""));
        } else {
            LOGGER.log(Level.INFO, "No Items Enabled, Recipes not created!");
        }

        if(ReferencesConfigInfo.GoldenLilypad.ITEM_ENABLED) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(InitModBlocks.blockGoldenLilypad), "nnn", "nin", "nnn", 'n', "nuggetGold", 'i', "ingotIron"));
            LOGGER.log(Level.INFO, "Created Recipe For: " + InitModBlocks.blockGoldenLilypad.getLocalizedName());
        } else {
            LOGGER.log(Level.INFO, "No Items Enabled, Recipes not created!");
        }
    }
}
