package thelm.packagedastral.client.screen;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import hellfirepvp.astralsorcery.client.lib.SpritesAS;
import hellfirepvp.astralsorcery.client.resource.SpriteSheetResource;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import thelm.packagedastral.container.ConstellationCrafterContainer;
import thelm.packagedauto.client.RenderTimer;
import thelm.packagedauto.client.screen.BaseScreen;

public class ConstellationCrafterScreen extends BaseScreen<ConstellationCrafterContainer> {

	public static final ResourceLocation BLACK = new ResourceLocation("astralsorcery:textures/misc/black.png");
	public static final ResourceLocation BACKGROUND = new ResourceLocation("packagedastral:textures/gui/constellation_crafter.png");

	public ConstellationCrafterScreen(ConstellationCrafterContainer container, PlayerInventory playerInventory, ITextComponent title) {
		super(container, playerInventory, title);
		imageWidth = 198;
		imageHeight = 217;
	}

	@Override
	protected ResourceLocation getBackgroundTexture() {
		return BACKGROUND;
	}

	@Override
	protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		int scaledStarlight;
		if(menu.tile.structureValid) {
			scaledStarlight = menu.tile.getScaledStarlight(174);
			RenderSystem.color4f(1, 1, 1, 1);
		}
		else {
			scaledStarlight = 174;
			RenderSystem.color4f(1, 0, 0, 1);
		}
		minecraft.getTextureManager().bind(BLACK);
		blit(matrixStack, leftPos+11, topPos+110, 174, 10, 0F, 0F, 1F, 1F);
		SpriteSheetResource spriteStarlight = SpritesAS.SPR_STARLIGHT_STORE;
		spriteStarlight.getResource().bindTexture();
		Tuple<Float, Float> uvOffset = spriteStarlight.getUVOffset(RenderTimer.INSTANCE.getTicks());
		blit(matrixStack, leftPos+11, topPos+110, scaledStarlight, 10, uvOffset.getA(), uvOffset.getB(), spriteStarlight.getUWidth()*scaledStarlight/174, spriteStarlight.getVLength());
		int scaledStarlightReq = menu.tile.getScaledStarlightReq(174);
		if(scaledStarlightReq > 0) {
			RenderSystem.color4f(0.2F, 0.5F, 1.0F, 0.4F);
			blit(matrixStack, leftPos+11+scaledStarlight, topPos+110, scaledStarlightReq, 10, uvOffset.getA()+spriteStarlight.getUWidth()*scaledStarlight/174, uvOffset.getB(), spriteStarlight.getUWidth()*scaledStarlightReq/174, spriteStarlight.getVLength());
		}
		super.renderBg(matrixStack, partialTicks, mouseX, mouseY);
		blit(matrixStack, leftPos+138, topPos+53, 198, 0, menu.tile.getScaledProgress(22), 16);
		int scaledEnergy = menu.tile.getScaledEnergy(40);
		blit(matrixStack, leftPos+10, topPos+28+40-scaledEnergy, 198, 16+40-scaledEnergy, 12, scaledEnergy);
	}

	@Override
	protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {
		String s = menu.tile.getDisplayName().getString();
		font.draw(matrixStack, s, imageWidth/2 - font.width(s)/2, 6, 0x404040);
		font.draw(matrixStack, menu.playerInventory.getDisplayName().getString(), menu.getPlayerInvX(), menu.getPlayerInvY()-11, 0x404040);
		if(mouseX-leftPos >= 10 && mouseY-topPos >= 28 && mouseX-leftPos <= 21 && mouseY-topPos <= 67) {
			renderTooltip(matrixStack, new StringTextComponent(menu.tile.getEnergyStorage().getEnergyStored()+" / "+menu.tile.getEnergyStorage().getMaxEnergyStored()+" FE"), mouseX-leftPos, mouseY-topPos);
		}
	}

	protected void blit(MatrixStack matrixStack, int x, int y, int width, int height, float textureX, float textureY, float textureWidth, float textureHeight) {
		Matrix4f matrix = matrixStack.last().pose();
		int blitOffset = getBlitOffset();
		BufferBuilder vb = Tessellator.getInstance().getBuilder();
		vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		vb.vertex(matrix, x, y+height, blitOffset).uv(textureX, textureY+textureHeight).endVertex();
		vb.vertex(matrix, x+width, y+height, blitOffset).uv(textureX+textureWidth, textureY+textureHeight).endVertex();
		vb.vertex(matrix, x+width, y, blitOffset).uv(textureX+textureWidth, textureY).endVertex();
		vb.vertex(matrix, x, y, blitOffset).uv(textureX, textureY).endVertex();
		vb.end();
		RenderSystem.enableAlphaTest();
		WorldVertexBufferUploader.end(vb);
	}
}
