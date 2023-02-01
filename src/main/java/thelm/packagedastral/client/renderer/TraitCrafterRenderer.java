package thelm.packagedastral.client.renderer;

import java.awt.Color;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import hellfirepvp.astralsorcery.client.util.ColorizationHelper;
import hellfirepvp.astralsorcery.client.util.RenderingConstellationUtils;
import hellfirepvp.astralsorcery.client.util.RenderingDrawUtils;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.constellation.world.DayTimeHelper;
import hellfirepvp.astralsorcery.common.crafting.helper.WrappedIngredient;
import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipe;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import thelm.packagedastral.tile.TraitCrafterTile;

public class TraitCrafterRenderer extends TileEntityRenderer<TraitCrafterTile> {

	public TraitCrafterRenderer(TileEntityRendererDispatcher rendererDispatcher) {
		super(rendererDispatcher);
	}

	@Override
	public void render(TraitCrafterTile tile, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
		if(TraitCrafterTile.requiresStructure && tile.structureValid) {
			long worldTime = tile.getWorld().getGameTime();
			SimpleAltarRecipe recipe = tile.effectRecipe;
			if(recipe != null) {
				IConstellation c = recipe.getFocusConstellation();
				if(c != null) {
					float dayAlpha = DayTimeHelper.getCurrentDaytimeDistribution(tile.getWorld())*0.6F;
					int max = 3000;
					int t = (int)(worldTime % max);
					float halfAge = max/2;
					float tr = 1F-(Math.abs(halfAge-t)/halfAge);
					tr *= 1.3;
					RenderingConstellationUtils.renderConstellationIntoWorldFlat(c, matrixStack, buffer, new Vector3(0.5, 0.03, 0.5), 5.5+tr, 2, 0.1F+dayAlpha);
				}
			}
			matrixStack.push();
			matrixStack.translate(0.5, 4.5, 0.5);
			long id = tile.getPos().toLong();
			if(recipe != null) {
				List<WrappedIngredient> traitInputs = recipe.getRelayInputs();
				if(!traitInputs.isEmpty()) {
					int amount = 60/traitInputs.size();
					for(int i = 0; i < traitInputs.size(); i++) {
						WrappedIngredient ingredient = traitInputs.get(i);
						ItemStack traitInput = ingredient.getRandomMatchingStack(worldTime);
						Color color = ColorizationHelper.getColor(traitInput).orElse(ColorsAS.CELESTIAL_CRYSTAL);
						RenderingDrawUtils.renderLightRayFan(matrixStack, buffer, color, 0x1231943167156902L | id | (i*0x5151L), 20, 2F, amount);
					}
				}
				else {
					RenderingDrawUtils.renderLightRayFan(matrixStack, buffer, Color.WHITE, id*31L, 15, 1.5F, 35);
					RenderingDrawUtils.renderLightRayFan(matrixStack, buffer, ColorsAS.CELESTIAL_CRYSTAL, id*16L, 10, 1F, 25);
				}
				RenderingDrawUtils.renderLightRayFan(matrixStack, buffer, Color.WHITE, id*31L, 10, 1F, 10);
			}
			else {
				RenderingDrawUtils.renderLightRayFan(matrixStack, buffer, Color.WHITE, id*31L, 15, 1.5F, 35);
				RenderingDrawUtils.renderLightRayFan(matrixStack, buffer, ColorsAS.CELESTIAL_CRYSTAL, id*16L, 10, 1F, 25);
			}
			matrixStack.pop();
		}
	}
}
