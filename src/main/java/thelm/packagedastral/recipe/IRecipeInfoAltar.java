package thelm.packagedastral.recipe;

import java.util.Collections;
import java.util.List;

import hellfirepvp.astralsorcery.common.crafting.altar.AbstractAltarRecipe;
import net.minecraft.item.ItemStack;
import thelm.packagedauto.api.IRecipeInfo;

public interface IRecipeInfoAltar extends IRecipeInfo {

	int getLevel();

	ItemStack getOutput();

	AbstractAltarRecipe getRecipe();

	List<ItemStack> getMatrix();

	List<ItemStack> getRelayInputs();

	boolean requiresNight();

	int getLevelRequired();

	int getTimeRequired();

	int getStarlightRequired();

	@Override
	default List<ItemStack> getOutputs() {
		ItemStack output = getOutput();
		return output.isEmpty() ? Collections.emptyList() : Collections.singletonList(output);
	}
}
