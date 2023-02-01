package thelm.packagedastral.ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import hellfirepvp.astralsorcery.common.constellation.ConstellationRegistry;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.item.crystal.ItemAttunedCrystalBase;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.common.crafting.StackList;

public class AttunedCrystalIngredient extends Ingredient {

	public static final ResourceLocation ID = new ResourceLocation("packagedastral:attuned_crystal");
	public static final IIngredientSerializer<AttunedCrystalIngredient> SERIALIZER = new Serializer();

	private final boolean hasToBeCelestial;
	private final boolean canBeCelestial;
	private final IWeakConstellation constellation;

	public AttunedCrystalIngredient(boolean hasToBeCelestial, boolean canBeCelestial, IWeakConstellation constellation) {
		super(getItems(hasToBeCelestial, canBeCelestial, constellation));
		this.hasToBeCelestial = hasToBeCelestial;
		this.canBeCelestial = canBeCelestial;
		this.constellation = constellation;
	}

	private static Stream<IItemList> getItems(boolean hasToBeCelestial, boolean canBeCelestial, IWeakConstellation constellation) {
		if(hasToBeCelestial) {
			canBeCelestial = true;
		}
		List<ItemStack> stacks = new ArrayList<>();
		if(!hasToBeCelestial) {
			ItemStack stack = new ItemStack(ItemsAS.ATTUNED_ROCK_CRYSTAL);
			ItemsAS.ATTUNED_ROCK_CRYSTAL.setAttunedConstellation(stack, constellation);
			stacks.add(stack);
		}
		if(canBeCelestial) {
			ItemStack stack = new ItemStack(ItemsAS.ATTUNED_CELESTIAL_CRYSTAL);
			ItemsAS.ATTUNED_CELESTIAL_CRYSTAL.setAttunedConstellation(stack, constellation);
			stacks.add(stack);
		}
		return Stream.of(new StackList(stacks));
	}

	@Override
	public boolean test(ItemStack stack) {
		if(super.test(stack)) {
			ItemAttunedCrystalBase item = (ItemAttunedCrystalBase)stack.getItem();
			return item.getAttunedConstellation(stack) == constellation;
		}
		return false;
	}

	@Override
	public boolean isSimple() {
		return false;
	}

	@Override
	public JsonElement serialize() {
		JsonObject json = new JsonObject();
		json.addProperty("type", ID.toString());
		json.addProperty("hasToBeCelestial", hasToBeCelestial);
		json.addProperty("canBeCelestial", canBeCelestial);
		json.addProperty("constellation", constellation.getRegistryName().toString());
		return json;
	}

	@Override
	public IIngredientSerializer<? extends Ingredient> getSerializer() {
		return SERIALIZER;
	}

	private static class Serializer implements IIngredientSerializer<AttunedCrystalIngredient> {

		@Override
		public AttunedCrystalIngredient parse(JsonObject json) {
			boolean hasToBeCelestial = JSONUtils.getBoolean(json, "hasToBeCelestial", false);
			boolean canBeCelestial = JSONUtils.getBoolean(json, "canBeCelestial", true);
			ResourceLocation constellationName = new ResourceLocation(JSONUtils.getString(json, "constellation"));
			IConstellation constellation = ConstellationRegistry.getConstellation(constellationName);
			if(constellation instanceof IWeakConstellation) {
				return new AttunedCrystalIngredient(hasToBeCelestial, canBeCelestial, (IWeakConstellation)constellation);
			}
			throw new JsonSyntaxException("Unknown or non-attunable constellation '"+constellationName+"'");
		}

		@Override
		public AttunedCrystalIngredient parse(PacketBuffer buffer) {
			boolean hasToBeCelestial = buffer.readBoolean();
			boolean canBeCelestial = buffer.readBoolean();
			IConstellation constellation = buffer.readRegistryId();
			if(constellation instanceof IWeakConstellation) {
				return new AttunedCrystalIngredient(hasToBeCelestial, canBeCelestial, (IWeakConstellation)constellation);
			}
			throw new IllegalArgumentException("Read non-attunable constellation '"+constellation.getRegistryName()+"'");
		}

		@Override
		public void write(PacketBuffer buffer, AttunedCrystalIngredient ingredient) {
			buffer.writeBoolean(ingredient.hasToBeCelestial);
			buffer.writeBoolean(ingredient.canBeCelestial);
			buffer.writeRegistryId(ingredient.constellation);
		}
	}
}
