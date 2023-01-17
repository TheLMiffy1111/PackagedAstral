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
import thelm.packagedastral.container.ContainerDiscoveryCrafter;
import thelm.packagedauto.client.gui.GuiContainerTileBase;

public class GuiDiscoveryCrafter extends GuiContainerTileBase<ContainerDiscoveryCrafter> {

	public static final ResourceLocation BLACK = new ResourceLocation("astralsorcery:textures/misc/black.png");
	public static final ResourceLocation BACKGROUND = new ResourceLocation("packagedastral:textures/gui/discovery_crafter.png");

	public GuiDiscoveryCrafter(ContainerDiscoveryCrafter container) {
		super(container);
		xSize = 176;
		ySize = 181;
	}

	@Override
	protected ResourceLocation getBackgroundTexture() {
		return BACKGROUND;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		int scaledStarlight = container.tile.getScaledStarlight(152);
		GlStateManager.color(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(BLACK);
		drawRect(guiLeft+11, guiTop+74, 152, 10, 0, 0, 1, 1);
		SpriteSheetResource spriteStarlight = SpriteLibrary.spriteStarlight;
		spriteStarlight.getResource().bindTexture();
		Tuple<Double, Double> uvOffset = spriteStarlight.getUVOffset(container.tile.getWorld().getTotalWorldTime());
		drawRect(guiLeft+11, guiTop+74, scaledStarlight, 10, uvOffset.key, uvOffset.value, spriteStarlight.getUWidth()*scaledStarlight/152, spriteStarlight.getVLength());
		super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
		drawTexturedModalRect(guiLeft+102, guiTop+35, 176, 0, container.tile.getScaledProgress(22), 16);
		int scaledEnergy = container.tile.getScaledEnergy(40);
		drawTexturedModalRect(guiLeft+10, guiTop+10+40-scaledEnergy, 176, 16+40-scaledEnergy, 12, scaledEnergy);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		String s = container.inventory.getDisplayName().getUnformattedText();
		fontRenderer.drawString(s, Math.max(25, xSize/2 - fontRenderer.getStringWidth(s)/2), 6, 0x404040);
		fontRenderer.drawString(container.playerInventory.getDisplayName().getUnformattedText(), container.getPlayerInvX(), container.getPlayerInvY()-11, 0x404040);
		if(mouseX-guiLeft >= 10 && mouseY-guiTop >= 10 && mouseX-guiLeft <= 21 && mouseY-guiTop <= 49) {
			drawHoveringText(container.tile.getEnergyStorage().getEnergyStored()+" / "+container.tile.getEnergyStorage().getMaxEnergyStored()+" FE", mouseX-guiLeft, mouseY-guiTop);
		}
	}

	protected void drawRect(int x, int y, int width, int height, double textureX, double textureY, double textureWidth, double textureHeight) {
		Tessellator tes = Tessellator.getInstance();
		BufferBuilder vb = tes.getBuffer();
		vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		vb.pos(x, y+height, zLevel).tex(textureX, textureY+textureHeight).endVertex();
		vb.pos(x+width, y+height, zLevel).tex(textureX+textureWidth, textureY+textureHeight).endVertex();
		vb.pos(x+width, y, zLevel).tex(textureX+textureWidth, textureY).endVertex();
		vb.pos(x, y, zLevel).tex(textureX, textureY).endVertex();
		tes.draw();
	}
}
