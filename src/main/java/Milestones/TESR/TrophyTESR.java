package Milestones.TESR;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import Milestones.Milestones;
import Milestones.Models.TrophyModel;
import Milestones.TileEntity.TrophyTileEntity;
import cpw.mods.fml.common.registry.GameRegistry;

public class TrophyTESR extends TileEntitySpecialRenderer {

    private EntityItem entityItem;
    private final TrophyModel model = new TrophyModel();
    private final ResourceLocation texture = new ResourceLocation(Milestones.MODID, "textures/blocks/trophy.png");

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks) {
        if (!(te instanceof TrophyTileEntity)) return;

        renderBase(te, x, y, z, partialTicks);
        renderItem(te, x, y, z, partialTicks);
    }

    private void renderBase(TileEntity te, double x, double y, double z, float partialTicks) {
        GL11.glPushMatrix();

        GL11.glTranslated(x + 0.5, y + 1.5, z + 0.5);

        GL11.glRotatef(180f, 0.0f, 0.0f, 1.0f);

        this.bindTexture(texture);
        this.model.render(null, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f);

        GL11.glPopMatrix();
    }

    private void renderItem(TileEntity te, double x, double y, double z, float partialTicks) {
        TrophyTileEntity trophyTE = (TrophyTileEntity) te;

        if (trophyTE.item.isEmpty()) {
            return;
        }

        String[] parts = trophyTE.item.split(":");

        String modid = parts[0];
        String name = parts[1];
        int meta = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;

        Item item = GameRegistry.findItem(modid, name);
        ItemStack stack = new ItemStack(item, 1, meta);

        if (this.entityItem == null) {
            this.entityItem = new EntityItem(te.getWorldObj(), 0, 0, 0);
        }
        this.entityItem.setEntityItemStack(stack);

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);

        float offsetY = (float) Math.sin(
            (te.getWorldObj()
                .getTotalWorldTime() + partialTicks) * 0.05f)
            * 0.1f;
        GL11.glTranslated(x + 0.5, y + 0.7 + offsetY, z + 0.5);

        float rotation = ((te.getWorldObj()
            .getTotalWorldTime() + partialTicks) * 2.0f) % 360.0F;
        GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);

        RenderItem.renderInFrame = true;
        RenderManager.instance.renderEntityWithPosYaw(this.entityItem, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
        RenderItem.renderInFrame = false;

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
    }
}
