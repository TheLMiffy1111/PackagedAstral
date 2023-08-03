package thelm.packagedastral.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;

import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import thelm.packagedastral.tile.MarkedRelayTile;
import thelm.packagedauto.client.RenderTimer;

public class MarkedRelayRenderer extends TileEntityRenderer<MarkedRelayTile> {

	public MarkedRelayRenderer(TileEntityRendererDispatcher rendererDispatcher) {
		super(rendererDispatcher);
	}

	@Override
	public void render(MarkedRelayTile tile, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
		matrixStack.pushPose();
		matrixStack.translate(0.5, 0.1, 0.5);
		ItemStack stack = tile.getItemHandler().getStackInSlot(0);
		if(!stack.isEmpty()) {
			RenderingUtils.renderItemAsEntity(stack, matrixStack, buffer, 0, 0, 0, combinedLight, partialTicks, RenderTimer.INSTANCE.getTicks());
		}
		else if(tile.structureValid) {
			RenderingUtils.renderItemAsEntity(new ItemStack(ItemsAS.GLASS_LENS), matrixStack, buffer, 0, 0, 0, combinedLight, partialTicks, RenderTimer.INSTANCE.getTicks());
		}
		matrixStack.popPose();
	}
}
