package thelm.packagedastral.recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.crafting.IAltarUpgradeRecipe;
import hellfirepvp.astralsorcery.common.crafting.INighttimeRecipe;
import hellfirepvp.astralsorcery.common.crafting.ItemHandle;
import hellfirepvp.astralsorcery.common.crafting.altar.AbstractAltarRecipe;
import hellfirepvp.astralsorcery.common.crafting.altar.AltarRecipeRegistry;
import hellfirepvp.astralsorcery.common.crafting.altar.recipes.TraitRecipe;
import hellfirepvp.astralsorcery.common.tile.TileAltar;
import hellfirepvp.astralsorcery.common.tile.base.TileReceiverBaseInventory;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.RecipeMatcher;
import thelm.packagedauto.api.IPackagePattern;
import thelm.packagedauto.api.IRecipeType;
import thelm.packagedauto.api.MiscUtil;
import thelm.packagedauto.util.PatternHelper;

public class RecipeInfoTrait implements IRecipeInfoAltar {

	AbstractAltarRecipe recipe;
	List<ItemStack> input = new ArrayList<>();
	List<ItemStack> matrix = NonNullList.withSize(25, ItemStack.EMPTY);
	List<ItemStack> inputRelay = new ArrayList<>();
	ItemStack output;
	List<IPackagePattern> patterns = new ArrayList<>();

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		input.clear();
		output = ItemStack.EMPTY;
		patterns.clear();
		recipe = AltarRecipeRegistry.getRecipeSlow(new ResourceLocation(nbt.getString("Recipe")));
		MiscUtil.loadAllItems(nbt.getTagList("Matrix", 10), matrix);
		MiscUtil.loadAllItems(nbt.getTagList("InputRelay", 10), inputRelay);
		output = new ItemStack(nbt.getCompoundTag("Output"));
		if(recipe != null) {
			List<ItemStack> toCondense = new ArrayList<>(matrix);
			toCondense.addAll(inputRelay);
			input.addAll(MiscUtil.condenseStacks(toCondense));
			for(int i = 0; i*9 < input.size(); ++i) {
				patterns.add(new PatternHelper(this, i));
			}
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		if(recipe != null) {
			nbt.setString("Recipe", recipe.getNativeRecipe().getRegistryName().toString());
		}
		NBTTagList matrixTag = MiscUtil.saveAllItems(new NBTTagList(), matrix);
		NBTTagList inputRelayTag = MiscUtil.saveAllItems(new NBTTagList(), inputRelay);
		nbt.setTag("Matrix", matrixTag);
		nbt.setTag("InputRelay", inputRelayTag);
		nbt.setTag("Output", output.writeToNBT(new NBTTagCompound()));
		return nbt;
	}

	@Override
	public IRecipeType getRecipeType() {
		return RecipeTypeTrait.INSTANCE;
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
	public AbstractAltarRecipe getRecipe() {
		return recipe;
	}

	@Override
	public List<ItemStack> getMatrix() {
		return matrix;
	}

	@Override
	public List<ItemStack> getRelayInputs() {
		return inputRelay;
	}

	@Override
	public boolean requiresNight() {
		return recipe instanceof INighttimeRecipe;
	}

	@Override
	public int getLevelRequired() {
		return recipe.getNeededLevel().ordinal();
	}

	@Override
	public int getTimeRequired() {
		return recipe.craftingTickTime();
	}

	@Override
	public int getStarlightRequired() {
		return recipe.getPassiveStarlightRequired();
	}

	@Override
	public void generateFromStacks(List<ItemStack> input, List<ItemStack> output, World world) {
		recipe = null;
		inputRelay.clear();
		this.input.clear();
		patterns.clear();
		if(world != null) {
			TileAltar fakeAltar = new TileAltar(TileAltar.AltarLevel.TRAIT_CRAFT);
			TileReceiverBaseInventory.ItemHandlerTile handler = fakeAltar.getInventoryHandler();
			int[] slotArray = RecipeTypeTrait.SLOTS.toIntArray();
			for(int i = 0; i < 25; ++i) {
				ItemStack toSet = input.get(slotArray[i]);
				toSet.setCount(1);
				matrix.set(i, toSet.copy());
				try {
					handler.setStackInSlot(i, toSet.copy());
				}
				catch(NullPointerException e) {}
			}
			for(int i = 25; i < 73; ++i) {
				ItemStack toSet = input.get(slotArray[i]);
				if(!toSet.isEmpty()) {
					toSet.setCount(1);
					inputRelay.add(toSet.copy());
				}
			}
			fakeAltar.setWorld(world);
			long prevTime = world.getWorldTime();
			world.setWorldTime(18000);
			for(AbstractAltarRecipe recipe : AltarRecipeRegistry.getRecipesForLevel(TileAltar.AltarLevel.TRAIT_CRAFT)) {
				if(recipe instanceof TraitRecipe && !(recipe instanceof IAltarUpgradeRecipe)) {
					TraitRecipe trait = (TraitRecipe)recipe;
					IConstellation prevConst = trait.getRequiredConstellation();
					trait.setRequiredConstellation(null);
					if(recipe.matches(fakeAltar, handler, true)) {
						List<Ingredient> matchers = Lists.transform(trait.getTraitItemHandles(), ItemHandle::getRecipeIngredient);
						if(RecipeMatcher.findMatches(inputRelay, matchers) != null) {
							try {
								this.output = recipe.getOutput(fakeAltar.copyGetCurrentCraftingGrid(), fakeAltar).copy();
							}
							catch(NullPointerException e) {
								trait.setRequiredConstellation(prevConst);
								continue;
							}
							this.recipe = recipe;
							List<ItemStack> toCondense = new ArrayList<>(matrix);
							toCondense.addAll(inputRelay);
							this.input.addAll(MiscUtil.condenseStacks(toCondense));
							for(int j = 0; j*9 < this.input.size(); ++j) {
								patterns.add(new PatternHelper(this, j));
							}
							world.setWorldTime(prevTime);
							trait.setRequiredConstellation(prevConst);
							return;
						}
					}
					trait.setRequiredConstellation(prevConst);
				}
			}
			if(inputRelay.isEmpty()) {
				for(int i = 2; i >= 0; --i) {
					TileAltar.AltarLevel level = TileAltar.AltarLevel.values()[i];
					for(AbstractAltarRecipe recipe : AltarRecipeRegistry.getRecipesForLevel(level)) {
						if(!(recipe instanceof IAltarUpgradeRecipe) && recipe.matches(fakeAltar, handler, true)) {
							try {
								this.output = recipe.getOutput(fakeAltar.copyGetCurrentCraftingGrid(), fakeAltar).copy();
							}
							catch(NullPointerException e) {
								continue;
							}
							this.recipe = recipe;
							this.input.addAll(MiscUtil.condenseStacks(matrix));
							for(int j = 0; j*9 < this.input.size(); ++j) {
								patterns.add(new PatternHelper(this, j));
							}
							world.setWorldTime(prevTime);
							return;
						}
					}
				}
			}
			world.setWorldTime(prevTime);
		}
		matrix.clear();
	}

	@Override
	public Int2ObjectMap<ItemStack> getEncoderStacks() {
		Int2ObjectMap<ItemStack> map = new Int2ObjectOpenHashMap<>();
		int[] slotArray = RecipeTypeTrait.SLOTS.toIntArray();
		for(int i = 0; i < 25; ++i) {
			map.put(slotArray[i], matrix.get(i));
		}
		for(int i = 0; i < inputRelay.size(); ++i) {
			map.put(slotArray[i+25], inputRelay.get(i));
		}
		return map;
	}
}
