package thelm.packagedastral.recipe;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.ImmutableList;

import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntLinkedOpenHashSet;
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

public class TraitPackageRecipeType implements IPackageRecipeType {

	public static final TraitPackageRecipeType INSTANCE = new TraitPackageRecipeType();
	public static final ResourceLocation NAME = new ResourceLocation("packagedastral:trait");
	public static final IntSet SLOTS;
	public static final IntSet SLOTS_ALTAR;
	public static final List<ResourceLocation> CATEGORIES = ImmutableList.of(
			new ResourceLocation("astralsorcery:altar_trait"),
			new ResourceLocation("astralsorcery:altar_constellation"),
			new ResourceLocation("astralsorcery:altar_attunement"),
			new ResourceLocation("astralsorcery:altar_discovery"));
	public static final Vector3i COLOR = new Vector3i(139, 139, 139);
	public static final Vector3i COLOR_RELAY = new Vector3i(179, 179, 139);
	public static final Vector3i COLOR_DISABLED = new Vector3i(64, 64, 64);

	static {
		SLOTS_ALTAR = new IntRBTreeSet();
		for(int i = 2; i < 7; ++i) {
			for(int j = 2; j < 7; ++j) {
				SLOTS_ALTAR.add(9*i+j);
			}
		}
		SLOTS = new IntLinkedOpenHashSet(SLOTS_ALTAR);
		for(int i = 0; i < 2; ++i) {
			for(int j = 1; j < 8; ++j) {
				SLOTS.add(9*i+j);
			}
		}
		for(int i = 2; i < 7; ++i) {
			SLOTS.add(9*i);
			SLOTS.add(9*i+1);
			SLOTS.add(9*i+7);
			SLOTS.add(9*i+8);
		}
		for(int i = 7; i < 9; ++i) {
			for(int j = 1; j < 8; ++j) {
				SLOTS.add(9*i+j);
			}
		}
	}

	protected TraitPackageRecipeType() {}

	@Override
	public ResourceLocation getName() {
		return NAME;
	}

	@Override
	public IFormattableTextComponent getDisplayName() {
		return new TranslationTextComponent("recipe.packagedastral.trait");
	}

	@Override
	public IFormattableTextComponent getShortDisplayName() {
		return new TranslationTextComponent("recipe.packagedastral.trait.short");
	}

	@Override
	public IPackageRecipeInfo getNewRecipeInfo() {
		return new TraitPackageRecipeInfo();
	}

	@Override
	public IntSet getEnabledSlots() {
		return SLOTS;
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
		int[] slotArray = SLOTS.toIntArray();
		for(Entry<Integer, IGuiIngredientWrapper<ItemStack>> entry : ingredients.entrySet()) {
			IGuiIngredientWrapper<ItemStack> ingredient = entry.getValue();
			if(ingredient.isInput()) {
				ItemStack displayed = entry.getValue().getDisplayedIngredient();
				if(displayed != null && !displayed.isEmpty()) {
					map.put(slotArray[index], displayed);
				}
				++index;
			}
			if(index >= 73) {
				break;
			}
		}
		return map;
	}

	@Override
	public Object getRepresentation() {
		return new ItemStack(BlocksAS.ALTAR_RADIANCE);
	}

	@Override
	public Vector3i getSlotColor(int slot) {
		if(!SLOTS.contains(slot) && slot != 85) {
			return COLOR_DISABLED;
		}
		if(!SLOTS_ALTAR.contains(slot) && slot != 85) {
			return COLOR_RELAY;
		}
		return COLOR;
	}
}
