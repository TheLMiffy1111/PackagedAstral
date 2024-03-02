package thelm.packagedastral.recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hellfirepvp.astralsorcery.common.block.tile.altar.AltarType;
import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipe;
import hellfirepvp.astralsorcery.common.lib.RecipeTypesAS;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntRBTreeSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import thelm.packagedauto.api.IPackagePattern;
import thelm.packagedauto.api.IPackageRecipeType;
import thelm.packagedauto.util.MiscHelper;
import thelm.packagedauto.util.PackagePattern;

public class AttunementPackageRecipeInfo implements IAltarPackageRecipeInfo {

	public static final IntSet SLOTS_MATRIX;

	static {
		SLOTS_MATRIX = new IntRBTreeSet();
		for(int i = 1; i < 4; ++i) {
			for(int j = 1; j < 4; ++j) {
				SLOTS_MATRIX.add(5*i+j);
			}
		}
		SLOTS_MATRIX.add(0);
		SLOTS_MATRIX.add(4);
		SLOTS_MATRIX.add(20);
		SLOTS_MATRIX.add(24);
	}

	SimpleAltarRecipe recipe;
	List<ItemStack> input = new ArrayList<>();
	List<ItemStack> matrix = NonNullList.withSize(13, ItemStack.EMPTY);
	ItemStack output;
	List<IPackagePattern> patterns = new ArrayList<>();

	@Override
	public void read(CompoundNBT nbt) {
		input.clear();
		patterns.clear();
		IRecipe<?> recipe = MiscHelper.INSTANCE.getRecipeManager().byKey(new ResourceLocation(nbt.getString("Recipe"))).orElse(null);
		MiscHelper.INSTANCE.loadAllItems(nbt.getList("Matrix", 10), matrix);
		output = ItemStack.of(nbt.getCompound("Output"));
		if(recipe instanceof SimpleAltarRecipe) {
			this.recipe = (SimpleAltarRecipe)recipe;
			input.addAll(MiscHelper.INSTANCE.condenseStacks(matrix));
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
		nbt.put("Matrix", matrixTag);
		nbt.put("Output", output.save(new CompoundNBT()));
		return nbt;
	}

	@Override
	public IPackageRecipeType getRecipeType() {
		return AttunementPackageRecipeType.INSTANCE;
	}

	@Override
	public int getLevel() {
		return 1;
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
		return Collections.emptyList();
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
		this.input.clear();
		patterns.clear();
		if(world != null) {
			TileAltar fakeAltar = new TileAltar().updateType(AltarType.ATTUNEMENT, true);
			ItemStackHandler handler = new ItemStackHandler(25);
			int[] slotArray = AttunementPackageRecipeType.SLOTS.toIntArray();
			int[] slotArray1 = SLOTS_MATRIX.toIntArray();
			for(int i = 0; i < 13; ++i) {
				ItemStack toSet = input.get(slotArray[i]);
				toSet.setCount(1);
				matrix.set(i, toSet.copy());
				handler.setStackInSlot(slotArray1[i], toSet.copy());
			}
			for(SimpleAltarRecipe recipe : MiscHelper.INSTANCE.getRecipeManager().getAllRecipesFor(RecipeTypesAS.TYPE_ALTAR.getType())) {
				if(recipe.getAltarType().ordinal() <= 1 && recipe.getFocusConstellation() == null &&
						recipe.getRelayInputs().isEmpty() && recipe.getInputs().containsInputs(handler, true)) {
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
		matrix.clear();
	}

	@Override
	public Int2ObjectMap<ItemStack> getEncoderStacks() {
		Int2ObjectMap<ItemStack> map = new Int2ObjectOpenHashMap<>();
		int[] slotArray = AttunementPackageRecipeType.SLOTS.toIntArray();
		for(int i = 0; i < 13; ++i) {
			map.put(slotArray[i], matrix.get(i));
		}
		return map;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof AttunementPackageRecipeInfo) {
			AttunementPackageRecipeInfo other = (AttunementPackageRecipeInfo)obj;
			return MiscHelper.INSTANCE.recipeEquals(this, recipe, other, other.recipe);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return MiscHelper.INSTANCE.recipeHashCode(this, recipe);
	}
}
