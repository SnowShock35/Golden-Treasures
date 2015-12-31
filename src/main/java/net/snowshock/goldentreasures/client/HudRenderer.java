package net.snowshock.goldentreasures.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.snowshock.goldentreasures.items.ItemGoldenMiner;
import net.snowshock.goldentreasures.items.ItemGoldenStaff;
import net.snowshock.goldentreasures.references.Colors;
import net.snowshock.goldentreasures.utils.ContentHelper;
import net.snowshock.goldentreasures.utils.NBTHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import static net.snowshock.goldentreasures.references.ReferencesConfigInfo.GoldenMiner;
import static net.snowshock.goldentreasures.references.ReferencesConfigInfo.GoldenStaff;

public class HudRenderer {
    private static RenderItem itemRenderer = new RenderItem();
//    private static int time;

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
//        handleTickIncrement(event);
        handleStaffHUDCheck();
        handleMinerHUDCheck();
    }

//    public void handleTickIncrement(TickEvent.RenderTickEvent event) {
//        // this is currently used for nothing but the blinking magazine in the handgun HUD renderer
//        if (event.phase != TickEvent.Phase.END)
//            return;
//        //4096 is just an arbitrary stopping point, I didn't need it to go that high, honestly. left in case we need something weird.
//        if (getTime() > 4096) {
//            time = 0;
//        } else {
//            time++;
//        }
//    }

//    public static int getTime() {
//        return time;
//    }


    public void handleMinerHUDCheck(){
        Minecraft mc = Minecraft.getMinecraft();
        if (!Minecraft.isGuiEnabled() || !mc.inGameHasFocus)
            return;
        EntityPlayer player = mc.thePlayer;

        if (player == null ||
                player.getCurrentEquippedItem() == null ||
                !(player.getCurrentEquippedItem().getItem() instanceof ItemGoldenMiner))
            return;

        ItemStack goldenMinerStack = player.getCurrentEquippedItem();
        ItemStack gunpowderStack = new ItemStack(Items.gunpowder, NBTHelper.getInteger("gunpowder", goldenMinerStack), 0);
        renderStandardTwoItemHUD(mc, player, goldenMinerStack, gunpowderStack, GoldenMiner.HUD_POSITION, 0, 0);
    }

    public void handleStaffHUDCheck() {
        // handles rendering the hud for the golden staff so we don't have to use chat messages, because annoying.
        Minecraft mc = Minecraft.getMinecraft();
        if (!Minecraft.isGuiEnabled() || !mc.inGameHasFocus)
            return;
        EntityPlayer player = mc.thePlayer;

        if (player == null ||
                player.getCurrentEquippedItem() == null ||
                !(player.getCurrentEquippedItem().getItem() instanceof ItemGoldenStaff))
            return;
        ItemStack goldenStaffStack = player.getCurrentEquippedItem();
        ItemGoldenStaff goldenStaffItem = (ItemGoldenStaff) goldenStaffStack.getItem();
        String placementItemName = goldenStaffItem.getTorchPlacementMode(goldenStaffStack);
        //for use with font renderer, hopefully.
        int amountOfItem = goldenStaffItem.getTorchCount(goldenStaffStack);
        Item placementItem = null;
        if (placementItemName != null)
            placementItem = ContentHelper.getItem(placementItemName);

        ItemStack placementStack = null;
        if (placementItem != null) {
            placementStack = new ItemStack(placementItem, amountOfItem, 0);
        }
        renderStandardTwoItemHUD(mc, player, goldenStaffStack, placementStack, GoldenStaff.HUD_POSITION, 0, 0);
    }

    private static void renderStandardTwoItemHUD(Minecraft minecraft, EntityPlayer player, ItemStack hudStack, ItemStack secondaryStack, int hudPosition, int colorOverride, int stackSizeOverride) {
        int stackSize = 0;
        if (stackSizeOverride > 0)
            stackSize = stackSizeOverride;
        int color = Colors.get(Colors.PURE);
        if (colorOverride > 0)
            color = colorOverride;
        float overlayScale = 2.5F;
        float overlayOpacity = 0.75F;

        GL11.glPushMatrix();
        ScaledResolution sr = new ScaledResolution(minecraft, minecraft.displayWidth, minecraft.displayHeight);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, sr.getScaledWidth_double(), sr.getScaledHeight_double(), 0.0D, 1000.0D, 3000.0D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);

        GL11.glPushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glEnable(GL11.GL_LIGHTING);

        int hudOverlayX = 0;
        int hudOverlayY = 0;

        switch (hudPosition) {
            case 0: {
                hudOverlayX = 0;
                hudOverlayY = 0;
                break;
            }
            case 1: {
                hudOverlayX = (int) (sr.getScaledWidth() - 16 * overlayScale);
                hudOverlayY = 0;
                break;
            }
            case 2: {
                hudOverlayX = 0;
                hudOverlayY = (int) (sr.getScaledHeight() - 16 * overlayScale);
                break;
            }
            case 3: {
                hudOverlayX = (int) (sr.getScaledWidth() - 16 * overlayScale);
                hudOverlayY = (int) (sr.getScaledHeight() - 16 * overlayScale);
                break;
            }
            default: {
                break;
            }
        }

        renderItemIntoGUI(minecraft.fontRenderer, hudStack, hudOverlayX, hudOverlayY, overlayOpacity, overlayScale);

        boolean skipStackRender = false;

        if (secondaryStack != null) {
            if (stackSize == 0)
                stackSize = secondaryStack.stackSize;
            itemRenderer.renderItemAndEffectIntoGUI(minecraft.fontRenderer, minecraft.getTextureManager(), secondaryStack, hudOverlayX, hudOverlayY + 24);
        }

        minecraft.fontRenderer.drawStringWithShadow(Integer.toString(stackSize),hudOverlayX + 15, hudOverlayY + 30, color);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

    public static void renderItemIntoGUI(FontRenderer fontRenderer, ItemStack itemStack, int x, int y, float opacity, float scale) {
        if (itemStack == null)
            return;
        GL11.glDisable(GL11.GL_LIGHTING);
        if (!(itemStack.getItem() instanceof ItemBlock)) {
            FMLClientHandler.instance().getClient().renderEngine.bindTexture(TextureMap.locationItemsTexture);
        } else {
            FMLClientHandler.instance().getClient().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        }
        for (int passes = 0; passes < itemStack.getItem().getRenderPasses(itemStack.getItemDamage()); passes++) {
            int overlayColour = itemStack.getItem().getColorFromItemStack(itemStack, passes);
            IIcon icon = itemStack.getItem().getIcon(itemStack, passes);
            float red = (overlayColour >> 16 & 255) / 255.0F;
            float green = (overlayColour >> 8 & 255) / 255.0F;
            float blue = (overlayColour & 255) / 255.0F;
            GL11.glColor4f(red, green, blue, opacity);
            drawTexturedQuad(x, y, icon, 16 * scale, 16 * scale, -90);
            GL11.glEnable(GL11.GL_LIGHTING);
        }
    }

    public static void drawTexturedQuad(int x, int y, IIcon icon, float width, float height, double zLevel) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + height, zLevel, icon.getMinU(), icon.getMaxV());
        tessellator.addVertexWithUV(x + width, y + height, zLevel, icon.getMaxU(), icon.getMaxV());
        tessellator.addVertexWithUV(x + width, y, zLevel, icon.getMaxU(), icon.getMinV());
        tessellator.addVertexWithUV(x, y, zLevel, icon.getMinU(), icon.getMinV());
        tessellator.draw();
    }
}
