package net.snowshock.goldentreasures.utils;


import com.google.common.collect.ImmutableMap;
import com.google.common.collect.UnmodifiableIterator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

@SideOnly(Side.CLIENT)
public class LanguageHelper {
    private static Map<String, String> preprocesssed = new HashMap();
    public static Map<String, String> globals = new HashMap();

    public LanguageHelper() {
    }

    public static String getLocalization(String key) {
        String localization = getLocalization(key, true);
        if(preprocesssed.containsKey(key)) {
            return preprocesssed.get(key);
        } else {
            if(localization.contains("{{!")) {
                while(localization.contains("{{!")) {
                    int startingIndex = localization.indexOf("{{!");
                    int endingIndex = localization.substring(startingIndex).indexOf("}}") + startingIndex;
                    String fragment = localization.substring(startingIndex + 3, endingIndex);

                    try {
                        String e = globals.get(fragment.toLowerCase());
                        localization = localization.substring(0, startingIndex) + e + localization.substring(endingIndex + 2);
                    } catch (Exception var6) {
                        localization = localization.substring(0, startingIndex) + localization.substring(endingIndex + 2);
                    }
                }

                preprocesssed.put(key, localization);
            }

            return localization;
        }
    }

    private static String getLocalization(String key, boolean fallback) {
        String localization = StatCollector.translateToLocal(key);
        if(localization.equals(key) && fallback) {
            localization = StatCollector.translateToFallback(key);
        }

        return localization;
    }

    public static void formatTooltip(String langName, ImmutableMap<String, String> toFormat, ItemStack stack, List list) {
        String langTooltip = getLocalization(langName);
        if(langTooltip != null && !langTooltip.equals(langName)) {
            Entry len$;
            if(toFormat != null) {
                for(UnmodifiableIterator arr$ = toFormat.entrySet().iterator(); arr$.hasNext(); langTooltip = langTooltip.replace("{{" + (String)len$.getKey() + "}}", (CharSequence)len$.getValue())) {
                    len$ = (Entry)arr$.next();
                }
            }

            String[] var9 = langTooltip.split(";");
            int var10 = var9.length;

            for(int i$ = 0; i$ < var10; ++i$) {
                String descriptionLine = var9[i$];
                if(descriptionLine != null && descriptionLine.length() > 0) {
                    list.add(descriptionLine);
                }
            }

        }
    }
}
