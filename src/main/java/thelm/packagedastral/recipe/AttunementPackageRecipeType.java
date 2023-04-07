package thelm.packagedastral.recipe;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableList;

import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntRBTreeSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import thelm.packagedauto.api.IGuiIngredientWrapper;
import thelm.packagedauto.api.IPackageRecipeInfo;
import thelm.packagedauto.api.IPackageRecipeType;
import thelm.packagedauto.api.IRecipeLayoutWrapper;

public class AttunementPackageRecipeType implements IPackageRecipeType {

	public static final AttunementPackageRecipeType INSTANCE = new AttunementPackageRecipeType();
	public static final ResourceLocation NAME = new ResourceLocation("packagedastral:attunement");
	public static final IntSet SLOTS;
	public static final IntSet SLOTS_ALTAR;
	public static final List<ResourceLocation> CATEGORIES = ImmutableList.of(
			new ResourceLocation("astralsorcery:altar_attunement"),
			new ResourceLocation("astralsorcery:altar_discovery"));
	public static final Vector3i COLOR = new Vector3i(139, 139, 139);
	public static final Vector3i COLOR_DISABLED = new Vector3i(64, 64, 64);

	static {
		SLOTS_ALTAR = new IntRBTreeSet();
		for(int i = 2; i < 7; ++i) {
			for(int j = 2; j < 7; ++j) {
				SLOTS_ALTAR.add(9*i+j);
			}
		}
		SLOTS = new IntRBTreeSet();
		for(int i = 3; i < 6; ++i) {
			for(int j = 3; j < 6; ++j) {
				SLOTS.add(9*i+j);
			}
		}
		SLOTS.add(20);
		SLOTS.add(24);
		SLOTS.add(56);
		SLOTS.add(60);
	}

	protected AttunementPackageRecipeType() {}

	@Override
	public ResourceLocation getName() {
		return NAME;
	}

	@Override
	public IFormattableTextComponent getDisplayName() {
		return new TranslationTextComponent("recipe.packagedastral.attunement");
	}

	@Override
	public IFormattableTextComponent getShortDisplayName() {
		return new TranslationTextComponent("recipe.packagedastral.attunement.short");
	}

	@Override
	public IPackageRecipeInfo getNewRecipeInfo() {
		return new AttunementPackageRecipeInfo();
	}

	@Override
	public IntSet getEnabledSlots() {
		return SLOTS;
	}

	@Override
	public boolean canSetOutput() {
		return false;
	}

	@Override
	public boolean hasMachine() {
		return true;
	}

	@Override
	public List<ResourceLocation> getJEICategories() {
		return CATEGORIES;
	}

	@Override
	public Int2ObjectMap<ItemStack> getRecipeTransferMap(IRecipeLayoutWrapper recipeLayoutWrapper) {
		Int2ObjectMap<ItemStack> map = new Int2ObjectOpenHashMap<>();
		Map<Integer, IGuiIngredientWrapper<ItemStack>> ingredients = recipeLayoutWrapper.getItemStackIngredients();
		int index = 0;
		int[] slotArray = SLOTS_ALTAR.toIntArray();
		for(Entry<Integer, IGuiIngredientWrapper<ItemStack>> entry : ingredients.entrySet()) {
			IGuiIngredientWrapper<ItemStack> ingredient = entry.getValue();
			if(ingredient.isInput()) {
				ItemStack displayed = entry.getValue().getDisplayedIngredient();
				if(displayed != null && !displayed.isEmpty()) {
					map.put(slotArray[index], displayed);
				}
				++index;
			}
			if(index >= 25) {
				break;
			}
		}
		return map;
	}

	@Override
	public Object getRepresentation() {
		return new ItemStack(BlocksAS.ALTAR_ATTUNEMENT);
	}

	@Override
	public Vector3i getSlotColor(int slot) {
		if(!SLOTS.contains(slot) && slot != 85) {
			return COLOR_DISABLED;
		}
		return COLOR;
	}
}
