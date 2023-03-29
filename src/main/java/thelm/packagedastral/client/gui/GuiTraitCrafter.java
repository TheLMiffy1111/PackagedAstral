package thelm.packagedastral.client.gui;

import org.lwjgl.opengl.GL11;

import hellfirepvp.astralsorcery.client.util.SpriteLibrary;
import hellfirepvp.astralsorcery.client.util.resource.SpriteSheetResource;
import hellfirepvp.astralsorcery.common.util.data.Tuple;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import thelm.packagedastral.container.ContainerTraitCrafter;
import thelm.packagedauto.client.RenderTimer;
import thelm.packagedauto.client.gui.GuiContainerTileBase;

public class GuiTraitCrafter extends GuiContainerTileBase<ContainerTraitCrafter> {

	public static final ResourceLocation BLACK = new ResourceLocation("astralsorcery:textures/misc/black.png");
	public static final ResourceLocation BACKGROUND = new ResourceLocation("packagedastral:textures/gui/trait_crafter.png");

	public GuiTraitCrafter(ContainerTraitCrafter container) {
		super(container);
		xSize = 198;
		ySize = 217;
	}

	@Override
	protected ResourceLocation getBackgroundTexture() {
		return BACKGROUND;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		int scaledStarlight;
		if(container.tile.structureValid) {
			scaledStarlight = container.tile.getScaledStarlight(174);
			GlStateManager.color(1, 1, 1, 1);
		}
		else {
			scaledStarlight = 174;
			GlStateManager.color(1, 0, 0, 1);
		}
		mc.getTextureManager().bindTexture(BLACK);
		drawRect(guiLeft+11, guiTop+110, 174, 10, 0, 0, 1, 1);
		SpriteSheetResource spriteStarlight = SpriteLibrary.spriteStarlight;
		spriteStarlight.getResource().bindTexture();
		Tuple<Double, Double> uvOffset = spriteStarlight.getUVOffset(RenderTimer.INSTANCE.getTicks());
		drawRect(guiLeft+11, guiTop+110, scaledStarlight, 10, uvOffset.key, uvOffset.value, spriteStarlight.getUWidth()*scaledStarlight/174, spriteStarlight.getVLength());
		int scaledStarlightReq = container.tile.getScaledStarlightReq(174);
		if(scaledStarlightReq > 0) {
			GlStateManager.color(0.2F, 0.5F, 1.0F, 0.4F);
			drawRect(guiLeft+11+scaledStarlight, guiTop+110, scaledStarlightReq, 10, uvOffset.key+spriteStarlight.getUWidth()*scaledStarlight/174, uvOffset.value, spriteStarlight.getUWidth()*scaledStarlightReq/174, spriteStarlight.getVLength());
		}
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		drawTexturedModalRect(guiLeft+138, guiTop+53, 198, 0, container.tile.getScaledProgress(22), 16);
		int scaledEnergy = container.tile.getScaledEnergy(40);
		drawTexturedModalRect(guiLeft+10, guiTop+28+40-scaledEnergy, 198, 16+40-scaledEnergy, 12, scaledEnergy);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		String s = container.inventory.getDisplayName().getUnformattedText();
		fontRenderer.drawString(s, xSize/2 - fontRenderer.getStringWidth(s)/2, 6, 0x404040);
		fontRenderer.drawString(container.playerInventory.getDisplayName().getUnformattedText(), container.getPlayerInvX(), container.getPlayerInvY()-11, 0x404040);
		if(mouseX-guiLeft >= 10 && mouseY-guiTop >= 28 && mouseX-guiLeft <= 21 && mouseY-guiTop <= 67) {
			drawHoveringText(container.tile.getEnergyStorage().getEnergyStored()+" / "+container.tile.getEnergyStorage().getMaxEnergyStored()+" FE", mouseX-guiLeft, mouseY-guiTop);
		}
	}

	protected void drawRect(int x, int y, int width, int height, double textureX, double textureY, double textureWidth, double textureHeight) {
		Tessellator tes = Tessellator.getInstance();
		BufferBuilder vb = tes.getBuffer();
		vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		vb.pos(x, y+height, zLevel).tex(textureX, textureY+textureHeight).endVertex();
		vb.pos(x+width, y+height, zLevel).tex(textureX +textureWidth, textureY+textureHeight).endVertex();
		vb.pos(x+width, y, zLevel).tex(textureX+textureWidth, textureY).endVertex();
		vb.pos(x, y, zLevel).tex(textureX, textureY).endVertex();
		tes.draw();
	}
}
