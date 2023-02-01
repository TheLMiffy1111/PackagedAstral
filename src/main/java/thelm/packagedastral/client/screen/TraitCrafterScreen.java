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
import thelm.packagedastral.container.TraitCrafterContainer;
import thelm.packagedauto.client.screen.BaseScreen;

public class TraitCrafterScreen extends BaseScreen<TraitCrafterContainer> {

	public static final ResourceLocation BLACK = new ResourceLocation("astralsorcery:textures/misc/black.png");
	public static final ResourceLocation BACKGROUND = new ResourceLocation("packagedastral:textures/gui/trait_crafter.png");

	public TraitCrafterScreen(TraitCrafterContainer container, PlayerInventory playerInventory, ITextComponent title) {
		super(container, playerInventory, title);
		xSize = 198;
		ySize = 217;
	}

	@Override
	protected ResourceLocation getBackgroundTexture() {
		return BACKGROUND;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		int scaledStarlight;
		if(container.tile.structureValid) {
			scaledStarlight = container.tile.getScaledStarlight(174);
			RenderSystem.color4f(1, 1, 1, 1);
		}
		else {
			scaledStarlight = 174;
			RenderSystem.color4f(1, 0, 0, 1);
		}
		minecraft.getTextureManager().bindTexture(BLACK);
		blit(matrixStack, guiLeft+11, guiTop+110, 174, 10, 0F, 0F, 1F, 1F);
		SpriteSheetResource spriteStarlight = SpritesAS.SPR_STARLIGHT_STORE;
		spriteStarlight.getResource().bindTexture();
		Tuple<Float, Float> uvOffset = spriteStarlight.getUVOffset(container.tile.getWorld().getGameTime());
		blit(matrixStack, guiLeft+11, guiTop+110, scaledStarlight, 10, uvOffset.getA(), uvOffset.getB(), spriteStarlight.getUWidth()*scaledStarlight/174, spriteStarlight.getVLength());
		super.drawGuiContainerBackgroundLayer(matrixStack, partialTicks, mouseX, mouseY);
		blit(matrixStack, guiLeft+138, guiTop+53, 198, 0, container.tile.getScaledProgress(22), 16);
		int scaledEnergy = container.tile.getScaledEnergy(40);
		blit(matrixStack, guiLeft+10, guiTop+28+40-scaledEnergy, 198, 16+40-scaledEnergy, 12, scaledEnergy);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
		String s = container.tile.getDisplayName().getString();
		font.drawString(matrixStack, s, xSize/2 - font.getStringWidth(s)/2, 6, 0x404040);
		font.drawString(matrixStack, container.playerInventory.getDisplayName().getString(), container.getPlayerInvX(), container.getPlayerInvY()-11, 0x404040);
		if(mouseX-guiLeft >= 10 && mouseY-guiTop >= 28 && mouseX-guiLeft <= 21 && mouseY-guiTop <= 67) {
			renderTooltip(matrixStack, new StringTextComponent(container.tile.getEnergyStorage().getEnergyStored()+" / "+container.tile.getEnergyStorage().getMaxEnergyStored()+" FE"), mouseX-guiLeft, mouseY-guiTop);
		}
	}

	protected void blit(MatrixStack matrixStack, int x, int y, int width, int height, float textureX, float textureY, float textureWidth, float textureHeight) {
		Matrix4f matrix = matrixStack.getLast().getMatrix();
		int blitOffset = getBlitOffset();
		BufferBuilder vb = Tessellator.getInstance().getBuffer();
		vb.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		vb.pos(matrix, x, y+height, blitOffset).tex(textureX, textureY+textureHeight).endVertex();
		vb.pos(matrix, x+width, y+height, blitOffset).tex(textureX+textureWidth, textureY+textureHeight).endVertex();
		vb.pos(matrix, x+width, y, blitOffset).tex(textureX+textureWidth, textureY).endVertex();
		vb.pos(matrix, x, y, blitOffset).tex(textureX, textureY).endVertex();
		vb.finishDrawing();
		RenderSystem.enableAlphaTest();
		WorldVertexBufferUploader.draw(vb);
	}
}
