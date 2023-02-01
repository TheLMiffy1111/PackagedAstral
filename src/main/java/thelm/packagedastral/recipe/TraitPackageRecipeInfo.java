package thelm.packagedastral.recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import hellfirepvp.astralsorcery.common.block.tile.altar.AltarType;
import hellfirepvp.astralsorcery.common.crafting.helper.WrappedIngredient;
import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipe;
import hellfirepvp.astralsorcery.common.lib.RecipeTypesAS;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.items.ItemStackHandler;
import thelm.packagedauto.api.IPackagePattern;
import thelm.packagedauto.api.IPackageRecipeType;
import thelm.packagedauto.util.MiscHelper;
import thelm.packagedauto.util.PackagePattern;

public class TraitPackageRecipeInfo implements IAltarPackageRecipeInfo {

	SimpleAltarRecipe recipe;
	List<ItemStack> input = new ArrayList<>();
	List<ItemStack> matrix = NonNullList.withSize(25, ItemStack.EMPTY);
	List<ItemStack> inputRelay = new ArrayList<>();
	ItemStack output;
	List<IPackagePattern> patterns = new ArrayList<>();

	@Override
	public void read(CompoundNBT nbt) {
		input.clear();
		patterns.clear();
		IRecipe recipe = MiscHelper.INSTANCE.getRecipeManager().getRecipe(new ResourceLocation(nbt.getString("Recipe"))).orElse(null);
		MiscHelper.INSTANCE.loadAllItems(nbt.getList("Matrix", 10), matrix);
		MiscHelper.INSTANCE.loadAllItems(nbt.getList("InputRelay", 10), inputRelay);
		output = ItemStack.read(nbt.getCompound("Output"));
		if(recipe instanceof SimpleAltarRecipe) {
			this.recipe = (SimpleAltarRecipe)recipe;
			List<ItemStack> toCondense = new ArrayList<>(matrix);
			toCondense.addAll(inputRelay);
			input.addAll(MiscHelper.INSTANCE.condenseStacks(toCondense));
			for(int i = 0; i*9 < input.size(); ++i) {
				patterns.add(new PackagePattern(this, i));
			}
		}
	}

	@Override
	public CompoundNBT write(CompoundNBT nbt) {
		if(recipe != null) {
			nbt.putString("Recipe", recipe.getId().toString());
		}
		ListNBT matrixTag = MiscHelper.INSTANCE.saveAllItems(new ListNBT(), matrix);
		ListNBT inputRelayTag = MiscHelper.INSTANCE.saveAllItems(new ListNBT(), inputRelay);
		nbt.put("Matrix", matrixTag);
		nbt.put("InputRelay", inputRelayTag);
		nbt.put("Output", output.write(new CompoundNBT()));
		return nbt;
	}

	@Override
	public IPackageRecipeType getRecipeType() {
		return TraitPackageRecipeType.INSTANCE;
	}

	@Override
	public int getLevel() {
		return 3;
	}

	@Override
	public boolean isValid() {
		return recipe != null;
	}

	@Override
	public List<IPackagePattern> getPatterns() {
		return Collections.unmodifiableList(patterns);
	}

	@Override
	public List<ItemStack> getInputs() {
		return Collections.unmodifiableList(input);
	}

	@Override
	public ItemStack getOutput() {
		return output.copy();
	}

	@Override
	public SimpleAltarRecipe getRecipe() {
		return recipe;
	}

	@Override
	public List<ItemStack> getMatrix() {
		return Collections.unmodifiableList(matrix);
	}

	@Override
	public List<ItemStack> getRelayInputs() {
		return Collections.unmodifiableList(inputRelay);
	}

	@Override
	public List<ItemStack> getRemainingItems() {
		return MiscHelper.INSTANCE.getRemainingItems(matrix);
	}

	@Override
	public int getLevelRequired() {
		return recipe.getAltarType().ordinal();
	}

	@Override
	public int getTimeRequired() {
		return recipe.getDuration();
	}

	@Override
	public int getStarlightRequired() {
		return recipe.getStarlightRequirement();
	}

	@Override
	public void generateFromStacks(List<ItemStack> input, List<ItemStack> output, World world) {
		recipe = null;
		inputRelay.clear();
		this.input.clear();
		patterns.clear();
		if(world != null) {
			TileAltar fakeAltar = new TileAltar().updateType(AltarType.RADIANCE, true);
			ItemStackHandler handler = new ItemStackHandler(25);
			int[] slotArray = TraitPackageRecipeType.SLOTS.toIntArray();
			for(int i = 0; i < 25; ++i) {
				ItemStack toSet = input.get(slotArray[i]);
				toSet.setCount(1);
				matrix.set(i, toSet.copy());
				handler.setStackInSlot(i, toSet.copy());
			}
			for(int i = 25; i < 73; ++i) {
				ItemStack toSet = input.get(slotArray[i]);
				if(!toSet.isEmpty()) {
					toSet.setCount(1);
					inputRelay.add(toSet.copy());
				}
			}
			for(SimpleAltarRecipe recipe : MiscHelper.INSTANCE.getRecipeManager().getRecipesForType(RecipeTypesAS.TYPE_ALTAR.getType())) {
				if(recipe.getInputs().containsInputs(handler, true)) {
					List<Ingredient> matchers = Lists.transform(recipe.getRelayInputs(), WrappedIngredient::getIngredient);
					if(inputRelay.isEmpty() && matchers.isEmpty() || RecipeMatcher.findMatches(inputRelay, matchers) != null) {
						try {
							List<ItemStack> outputs = recipe.getOutputs(fakeAltar);
							if(outputs.isEmpty()) {
								continue;
							}
							this.output = outputs.get(0);
						}
						catch(NullPointerException e) {
							continue;
						}
						this.recipe = recipe;
						this.input.addAll(MiscHelper.INSTANCE.condenseStacks(matrix));
						for(int j = 0; j*9 < this.input.size(); ++j) {
							patterns.add(new PackagePattern(this, j));
						}
						return;
					}
				}
			}
		}
		matrix.clear();
	}

	@Override
	public Int2ObjectMap<ItemStack> getEncoderStacks() {
		Int2ObjectMap<ItemStack> map = new Int2ObjectOpenHashMap<>();
		int[] slotArray = TraitPackageRecipeType.SLOTS.toIntArray();
		for(int i = 0; i < 25; ++i) {
			map.put(slotArray[i], matrix.get(i));
		}
		for(int i = 0; i < inputRelay.size(); ++i) {
			map.put(slotArray[i+25], inputRelay.get(i));
		}
		return map;
	}
}
