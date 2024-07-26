package thelm.packagedastral.recipe;

import java.util.Collections;
import java.util.List;

import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipe;
import net.minecraft.item.ItemStack;
import thelm.packagedauto.api.IPackageRecipeInfo;

public interface IAltarPackageRecipeInfo extends IPackageRecipeInfo {

	int getLevel();

	ItemStack getOutput();

	SimpleAltarRecipe getRecipe();

	List<ItemStack> getMatrix();

	List<ItemStack> getRelayInputs();

	List<ItemStack> getRemainingItems();

	int getLevelRequired();

	int getTimeRequired();

	int getStarlightRequired();

	@Override
	default List<ItemStack> getOutputs() {
		ItemStack output = getOutput();
		return output.isEmpty() ? Collections.emptyList() : Collections.singletonList(output);
	}
}
