package thelm.packagedastral.integration.patchouli.component;

import java.awt.Color;
import java.util.List;
import java.util.function.UnaryOperator;

import com.google.common.collect.Lists;
import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import hellfirepvp.astralsorcery.client.util.RenderingConstellationUtils;
import hellfirepvp.astralsorcery.common.block.tile.altar.AltarType;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.crafting.helper.WrappedIngredient;
import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import thelm.packagedauto.util.MiscHelper;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;

public class AltarRecipeGridComponent implements ICustomComponent {

	public static final ResourceLocation DISCOVERY_GRID = new ResourceLocation("packagedastral:textures/gui/patchouli/discovery_grid.png");
	public static final ResourceLocation ATTUNEMENT_GRID = new ResourceLocation("packagedastral:textures/gui/patchouli/attunement_grid.png");
	public static final ResourceLocation CONSTELLATION_GRID = new ResourceLocation("packagedastral:textures/gui/patchouli/constellation_grid.png");
	public static final ResourceLocation TRAIT_GRID = new ResourceLocation("packagedastral:textures/gui/patchouli/trait_grid.png");

	@SerializedName("recipe")
	public IVariable recipeRaw;
	transient SimpleAltarRecipe recipe;
	transient int x;
	transient int y;

	@Override
	public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
		ResourceLocation recipeId = new ResourceLocation(lookup.apply(recipeRaw).asString());
		IRecipe<?> recipe = MiscHelper.INSTANCE.getRecipeManager().byKey(recipeId).orElse(null);
		if(recipe instanceof SimpleAltarRecipe) {
			this.recipe = (SimpleAltarRecipe)recipe;
		}
	}

	@Override
	public void build(int componentX, int componentY, int pageNum) {
		x = componentX < 0 ? 9 : componentX;
		y = componentY < 0 ? 12 : componentY;
	}

	@Override
	public void render(MatrixStack matrixStack, IComponentRenderContext context, float partialTicks, int mouseX, int mouseY) {
		if(recipe != null) {
			Minecraft mc = Minecraft.getInstance();
			RenderSystem.enableBlend();
			switch(recipe.getAltarType()) {
			case DISCOVERY:
				mc.textureManager.bind(DISCOVERY_GRID);
				break;
			case ATTUNEMENT:
				mc.textureManager.bind(ATTUNEMENT_GRID);
				break;
			case CONSTELLATION:
				mc.textureManager.bind(CONSTELLATION_GRID);
				break;
			case RADIANCE:
				mc.textureManager.bind(TRAIT_GRID);
				break;
			}
			AbstractGui.blit(matrixStack, x-1, y-1, 1, 1, 100, 119, 128, 128);
			if(recipe.getAltarType() == AltarType.RADIANCE) {
				IConstellation constellation = recipe.getFocusConstellation();
				if(constellation != null) {
					RenderingConstellationUtils.renderConstellationIntoGUI(Color.BLACK, constellation, matrixStack, x, y, 0, 98, 98, 1.2F, ()->0.9F, true, false);
				}
				List<Ingredient> ingredients = Lists.transform(recipe.getRelayInputs(), WrappedIngredient::getIngredient);
				float degreePerInput = 360F/ingredients.size();
				int ticksElapsed = context.getTicksInBook();
				float currentDegree = (Screen.hasShiftDown() ? ticksElapsed : ticksElapsed+partialTicks) - 90;
				for(Ingredient ingredient : ingredients) {
					double radians = Math.toRadians(currentDegree);
					double xPos = x+3+2*19 + Math.cos(radians)*48;
					double yPos = y+3+2*19 + Math.sin(radians)*48;
					matrixStack.pushPose();
					matrixStack.translate(xPos-MathHelper.floor(xPos), yPos-MathHelper.floor(yPos), 0);
					context.renderIngredient(matrixStack, MathHelper.floor(xPos), MathHelper.floor(yPos), mouseX, mouseY, ingredient);
					matrixStack.popPose();
					currentDegree += degreePerInput;
				}
			}
			for(int yy = 0; yy < 5; ++yy) {
				for(int xx = 0; xx < 5; ++xx) {
					Ingredient ingredient = recipe.getInputs().getIngredient(yy*5+xx);
					context.renderIngredient(matrixStack, x+3+xx*19, y+3+yy*19, mouseX, mouseY, ingredient);
				}
			}
		}
	}
}
