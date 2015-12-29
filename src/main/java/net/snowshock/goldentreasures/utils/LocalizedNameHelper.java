package net.snowshock.goldentreasures.utils;

public class LocalizedNameHelper {
    public static String getUnwrappedUnlocalizedName(String unlocalizedName) {
        return unlocalizedName.substring(unlocalizedName.indexOf(".") + 1);
    }
}
