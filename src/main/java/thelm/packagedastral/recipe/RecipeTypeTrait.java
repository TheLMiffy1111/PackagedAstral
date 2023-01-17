package thelm.packagedastral.recipe;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;

import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntLinkedOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import mezz.jei.api.gui.IGuiIngredient;
import mezz.jei.api.gui.IRecipeLayout;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thelm.packagedauto.api.IRecipeInfo;
import thelm.packagedauto.api.IRecipeType;

public class RecipeTypeTrait implements IRecipeType {

	public static final RecipeTypeTrait INSTANCE = new RecipeTypeTrait();
	public static final ResourceLocation NAME = new ResourceLocation("packagedastral:trait");
	public static final IntSet SLOTS;
	public static final IntSet SLOTS_CENTER;
	public static final List<String> CATEGORIES = ImmutableList.of(
			"astralsorcery.altar.trait",
			"astralsorcery.altar.constellation",
			"astralsorcery.altar.attunement",
			"astralsorcery.altar.discovery");
	public static final Color COLOR = new Color(139, 139, 139);
	public static final Color COLOR_RELAY = new Color(179, 179, 139);
	public static final Color COLOR_DISABLED = new Color(64, 64, 64);

	static {
		SLOTS = new IntLinkedOpenHashSet(RecipeTypeConstellation.SLOTS);
		SLOTS.add(22);
		SLOTS.add(38);
		SLOTS.add(42);
		SLOTS.add(58);
		SLOTS_CENTER = new IntLinkedOpenHashSet(SLOTS);
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

	protected RecipeTypeTrait() {}

	@Override
	public ResourceLocation getName() {
		return NAME;
	}

	@Override
	public String getLocalizedName() {
		return I18n.translateToLocal("recipe.packagedastral.trait");
	}

	@Override
	public String getLocalizedNameShort() {
		return I18n.translateToLocal("recipe.packagedastral.trait.short");
	}

	@Override
	public IRecipeInfo getNewRecipeInfo() {
		return new RecipeInfoTrait();
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
	public List<String> getJEICategories() {
		return CATEGORIES;
	}

	@Optional.Method(modid="jei")
	@Override
	public Int2ObjectMap<ItemStack> getRecipeTransferMap(IRecipeLayout recipeLayout, String category) {
		Int2ObjectMap<ItemStack> map = new Int2ObjectOpenHashMap<>();
		Map<Integer, ? extends IGuiIngredient<ItemStack>> ingredients = recipeLayout.getItemStacks().getGuiIngredients();
		int index = 0;
		int[] slotArray = SLOTS.toIntArray();
		for(Map.Entry<Integer, ? extends IGuiIngredient<ItemStack>> entry : ingredients.entrySet()) {
			IGuiIngredient<ItemStack> ingredient = entry.getValue();
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

	@SideOnly(Side.CLIENT)
	@Override
	public Object getRepresentation() {
		return new ItemStack(BlocksAS.blockAltar, 1, 3);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Color getSlotColor(int slot) {
		if(!SLOTS.contains(slot) && slot != 85) {
			return COLOR_DISABLED;
		}
		if(!SLOTS_CENTER.contains(slot) && slot != 85) {
			return COLOR_RELAY;
		}
		return COLOR;
	}
}
