package thelm.packagedastral.recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hellfirepvp.astralsorcery.common.crafting.IAltarUpgradeRecipe;
import hellfirepvp.astralsorcery.common.crafting.INighttimeRecipe;
import hellfirepvp.astralsorcery.common.crafting.altar.AbstractAltarRecipe;
import hellfirepvp.astralsorcery.common.crafting.altar.AltarRecipeRegistry;
import hellfirepvp.astralsorcery.common.tile.TileAltar;
import hellfirepvp.astralsorcery.common.tile.base.TileReceiverBaseInventory;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import thelm.packagedauto.api.IPackagePattern;
import thelm.packagedauto.api.IRecipeType;
import thelm.packagedauto.api.MiscUtil;
import thelm.packagedauto.util.PatternHelper;

public class RecipeInfoDiscovery implements IRecipeInfoAltar {

	AbstractAltarRecipe recipe;
	List<ItemStack> input = new ArrayList<>();
	List<ItemStack> matrix = NonNullList.withSize(9, ItemStack.EMPTY);
	ItemStack output;
	List<IPackagePattern> patterns = new ArrayList<>();

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		input.clear();
		output = ItemStack.EMPTY;
		patterns.clear();
		recipe = AltarRecipeRegistry.getRecipeSlow(new ResourceLocation(nbt.getString("Recipe")));
		MiscUtil.loadAllItems(nbt.getTagList("Matrix", 10), matrix);
		output = new ItemStack(nbt.getCompoundTag("Output"));
		if(recipe != null) {
			input.addAll(MiscUtil.condenseStacks(matrix));
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
		nbt.setTag("Matrix", matrixTag);
		nbt.setTag("Output", output.writeToNBT(new NBTTagCompound()));
		return nbt;
	}

	@Override
	public IRecipeType getRecipeType() {
		return RecipeTypeDiscovery.INSTANCE;
	}

	@Override
	public int getLevel() {
		return 0;
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
		return Collections.emptyList();
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
		this.input.clear();
		patterns.clear();
		if(world != null) {
			TileAltar fakeAltar = new TileAltar(TileAltar.AltarLevel.DISCOVERY);
			TileReceiverBaseInventory.ItemHandlerTile handler = fakeAltar.getInventoryHandler();
			int[] slotArray = RecipeTypeDiscovery.SLOTS.toIntArray();
			for(int i = 0; i < 9; ++i) {
				ItemStack toSet = input.get(slotArray[i]);
				toSet.setCount(1);
				matrix.set(i, toSet.copy());
				try {
					handler.setStackInSlot(i, toSet.copy());
				}
				catch(NullPointerException e) {}
			}
			fakeAltar.setWorld(world);
			long prevTime = world.getWorldTime();
			world.setWorldTime(18000);
			for(int i = 0; i >= 0; --i) {
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
			world.setWorldTime(prevTime);
		}
		matrix.clear();
	}

	@Override
	public Int2ObjectMap<ItemStack> getEncoderStacks() {
		Int2ObjectMap<ItemStack> map = new Int2ObjectOpenHashMap<>();
		int[] slotArray = RecipeTypeDiscovery.SLOTS.toIntArray();
		for(int i = 0; i < 9; ++i) {
			map.put(slotArray[i], matrix.get(i));
		}
		return map;
	}
}
