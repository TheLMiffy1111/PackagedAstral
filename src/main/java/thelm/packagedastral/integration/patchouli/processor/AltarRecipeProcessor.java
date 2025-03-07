package thelm.packagedastral.integration.patchouli.processor;

import java.util.Collections;

import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import thelm.packagedauto.util.MiscHelper;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class AltarRecipeProcessor implements IComponentProcessor {

	SimpleAltarRecipe recipe;

	@Override
	public void setup(IVariableProvider variables) {
		ResourceLocation recipeId = new ResourceLocation(variables.get("recipe").asString());
		IRecipe<?> recipe = MiscHelper.INSTANCE.getRecipeManager().byKey(recipeId).orElse(null);
		if(recipe instanceof SimpleAltarRecipe) {
			this.recipe = (SimpleAltarRecipe)recipe;
		}
	}

	@Override
	public IVariable process(String key) {
		if(recipe != null) {
			if(key.equals("output")) {
				return IVariable.from(recipe.getOutputForRender(Collections.emptyList()));
			}
		}
		return null;
	}
}
