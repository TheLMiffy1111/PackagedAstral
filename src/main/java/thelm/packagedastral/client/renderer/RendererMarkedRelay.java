package thelm.packagedastral.client.renderer;

import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import thelm.packagedastral.tile.TileMarkedRelay;

public class RendererMarkedRelay extends TileEntitySpecialRenderer<TileMarkedRelay> {

	@Override
	public void render(TileMarkedRelay te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		ItemStack stack = te.getInventory().getStackInSlot(0);
		if(!stack.isEmpty()) {
			RenderingUtils.renderItemAsEntity(stack, x, y-0.5, z, partialTicks, (int)te.getWorld().getTotalWorldTime());
		}
	}
}
