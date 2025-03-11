package thelm.packagedastral.integration.patchouli.processor;

import hellfirepvp.astralsorcery.common.crafting.altar.AbstractAltarRecipe;
import hellfirepvp.astralsorcery.common.crafting.altar.AltarRecipeRegistry;
import net.minecraft.util.ResourceLocation;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.api.PatchouliAPI;

public class ProcessorAltarRecipe implements IComponentProcessor {

	AbstractAltarRecipe altarRecipe;

	@Override
	public void setup(IVariableProvider<String> variables) {
		String recipeRaw = variables.get("recipe");
		altarRecipe = AltarRecipeRegistry.getRecipeSlow(new ResourceLocation("astralsorcery", "shaped/"+recipeRaw));
		if(altarRecipe == null) {
			altarRecipe = AltarRecipeRegistry.getRecipeSlow(new ResourceLocation("astralsorcery", "shaped/internal/altar/"+recipeRaw));
		}
	}

	@Override
	public String process(String key) {
		if(altarRecipe != null) {
			if(key.equals("output")) {
				return PatchouliAPI.instance.serializeItemStack(altarRecipe.getOutputForRender());
			}
		}
		return null;
	}
}
