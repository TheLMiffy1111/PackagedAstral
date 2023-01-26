package thelm.packagedastral.client.renderer;

import java.awt.Color;
import java.util.Collection;

import hellfirepvp.astralsorcery.client.render.tile.TESRCollectorCrystal;
import hellfirepvp.astralsorcery.client.util.ItemColorizationHelper;
import hellfirepvp.astralsorcery.client.util.RenderConstellation;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import hellfirepvp.astralsorcery.common.block.network.BlockCollectorCrystalBase;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.constellation.distribution.ConstellationSkyHandler;
import hellfirepvp.astralsorcery.common.crafting.ItemHandle;
import hellfirepvp.astralsorcery.common.crafting.altar.AbstractAltarRecipe;
import hellfirepvp.astralsorcery.common.crafting.altar.recipes.TraitRecipe;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import thelm.packagedastral.tile.TileTraitCrafter;

public class RendererTraitCrafter extends TileEntitySpecialRenderer<TileTraitCrafter> {

	@Override
	public void render(TileTraitCrafter te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if(TileTraitCrafter.requiresStructure && te.structureValid) {
			long worldTime = te.getWorld().getTotalWorldTime();
			AbstractAltarRecipe recipe = te.effectRecipe;
			if(recipe instanceof TraitRecipe) {
				IConstellation c = ((TraitRecipe)recipe).getRequiredConstellation();
				if(c != null) {
					GlStateManager.pushMatrix();
					float alphaDaytime = ConstellationSkyHandler.getInstance().getCurrentDaytimeDistribution(te.getWorld())*0.8F;
					int max = 5000;
					int t = (int)(worldTime % max);
					float halfAge = max/2;
					float tr = 1-Math.abs(halfAge-t)/halfAge;
					tr *= 2;
					RenderingUtils.removeStandartTranslationFromTESRMatrix(partialTicks);
					float br = 0.9F*alphaDaytime;
					RenderConstellation.renderConstellationIntoWorldFlat(c, c.getConstellationColor(), new Vector3(te).add(0.5, 0.03, 0.5), 5+tr, 2, 0.1F+br);
					GlStateManager.popMatrix();
				}
			}
			GlStateManager.pushMatrix();
			GlStateManager.translate(x+0.5, y+4, z+0.5);
			if(recipe instanceof TraitRecipe) {
				Collection<ItemHandle> requiredHandles = ((TraitRecipe)recipe).getTraitItemHandles();
				if(!requiredHandles.isEmpty()) {
					int amt = 60/requiredHandles.size();
					for(ItemHandle outer : requiredHandles) {
						NonNullList<ItemStack> stacksApplicable = outer.getApplicableItemsForRender();
						ItemStack element = stacksApplicable.get((int)((worldTime/60) % stacksApplicable.size()));
						Color col = ItemColorizationHelper.getDominantColorFromItemStack(element);
						if(col == null) {
							col = BlockCollectorCrystalBase.CollectorCrystalType.CELESTIAL_CRYSTAL.displayColor;
						}
						RenderingUtils.renderLightRayEffects(0, 0.5, 0, col, 0x12315L | outer.hashCode(), worldTime, 20, 2F, amt, amt/2);
					}
				}
				RenderingUtils.renderLightRayEffects(0, 0.5, 0, Color.WHITE, 0, worldTime, 15, 2F, 40, 25);
			}
			else {
				RenderingUtils.renderLightRayEffects(0, 0.5, 0, Color.WHITE, 305223265L, worldTime, 20, 2F, 50, 25);
				RenderingUtils.renderLightRayEffects(0, 0.5, 0, Color.BLUE, 0, worldTime, 10, 1F, 40, 25);
			}
			GlStateManager.translate(0, 0.15, 0);
			GlStateManager.scale(0.7, 0.7, 0.7);
			TESRCollectorCrystal.renderCrystal(null, true, true);
			GlStateManager.popMatrix();
		}
	}
}
