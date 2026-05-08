package ModName.ItemRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import ModName.ModName;
import ModName.Models.TrophyModel;
import cpw.mods.fml.common.registry.GameRegistry;

public class TrophyItemRenderer implements IItemRenderer {

    private EntityItem entityItem;
    private final TrophyModel model = new TrophyModel();
    private final ResourceLocation texture = new ResourceLocation(ModName.MODID, "textures/blocks/trophy.png");

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        renderBase(type);
        renderItem(type, item);
    }

    private void renderBase(ItemRenderType type) {
        GL11.glPushMatrix();

        switch (type) {
            case INVENTORY, ENTITY:
                GL11.glTranslated(0.0, 1.0, 0.0);
                break;
            case EQUIPPED, EQUIPPED_FIRST_PERSON:
                GL11.glTranslated(0.5, 2.0, 0.5);
                break;
            default:
                break;
        }

        GL11.glRotatef(180f, 0.0f, 0.0f, 1.0f);

        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        this.model.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);

        GL11.glPopMatrix();
    }

    private void renderItem(ItemRenderType type, ItemStack item) {
        if (!item.hasTagCompound() || !item.getTagCompound()
            .hasKey("trophyitem")) {
            return;
        }

        String[] parts = item.getTagCompound()
            .getString("trophyitem")
            .split(":");

        String modid = parts[0];
        String name = parts[1];
        int meta = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;

        Item renderItem = GameRegistry.findItem(modid, name);
        ItemStack renderStack = new ItemStack(renderItem, 1, meta);

        if (this.entityItem == null) {
            this.entityItem = new EntityItem(Minecraft.getMinecraft().theWorld, 0, 0, 0);
        }
        this.entityItem.setEntityItemStack(renderStack);

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);

        float offsetY = (float) Math
            .sin((Minecraft.getMinecraft().theWorld.getTotalWorldTime() + Minecraft.getSystemTime()) * 0.00075f) * 0.1f;
        switch (type) {
            case INVENTORY, ENTITY:
                GL11.glTranslated(0.0, 0.3 + offsetY, 0.0);
                break;
            case EQUIPPED, EQUIPPED_FIRST_PERSON:
                GL11.glTranslated(0.5, 1.15 + offsetY, 0.5);
                break;
            default:
                break;
        }

        float rotation = ((Minecraft.getMinecraft().theWorld.getTotalWorldTime() + Minecraft.getSystemTime()) * 0.035f)
            % 360.0F;
        GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);

        RenderItem.renderInFrame = true;
        RenderManager.instance.renderEntityWithPosYaw(this.entityItem, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
        RenderItem.renderInFrame = false;

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }
}
