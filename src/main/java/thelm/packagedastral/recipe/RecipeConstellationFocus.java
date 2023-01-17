package thelm.packagedastral.recipe;

import java.util.Random;

import hellfirepvp.astralsorcery.client.effect.EffectHandler;
import hellfirepvp.astralsorcery.client.effect.EffectHelper;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.crafting.INighttimeRecipe;
import hellfirepvp.astralsorcery.common.crafting.ISpecialCraftingEffects;
import hellfirepvp.astralsorcery.common.crafting.ItemHandle;
import hellfirepvp.astralsorcery.common.crafting.altar.AbstractAltarRecipe;
import hellfirepvp.astralsorcery.common.crafting.altar.ActiveCraftingTask;
import hellfirepvp.astralsorcery.common.crafting.altar.recipes.TraitRecipe;
import hellfirepvp.astralsorcery.common.crafting.helper.ShapedRecipeSlot;
import hellfirepvp.astralsorcery.common.item.ItemCraftingComponent;
import hellfirepvp.astralsorcery.common.item.crystal.base.ItemTunedCrystalBase;
import hellfirepvp.astralsorcery.common.item.useables.ItemUsableDust;
import hellfirepvp.astralsorcery.common.lib.Constellations;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import hellfirepvp.astralsorcery.common.tile.TileAltar;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thelm.packagedastral.item.ItemConstellationFocus;

public class RecipeConstellationFocus extends TraitRecipe implements INighttimeRecipe, ISpecialCraftingEffects {

	public static final RecipeConstellationFocus INSTANCE = new RecipeConstellationFocus();

	private static Vector3[] offsetPillarsT2 = new Vector3[] {
			new Vector3(3, 2, 3), new Vector3(-3, 2, 3), new Vector3(3, 2, -3), new Vector3(-3, 2, -3)
	};
	private static Vector3[] offsetPillarsT3 = new Vector3[] {
			new Vector3(4, 3, 4), new Vector3(-4, 3, 4), new Vector3(4, 3, -4), new Vector3(-4, 3, -4)
	};

	protected RecipeConstellationFocus() {
		super(shapedRecipe("packagedastral/constellation_focus", ItemConstellationFocus.INSTANCE).
				addPart(ItemsAS.shiftingStar, ShapedRecipeSlot.CENTER).
				addPart("paneGlassColorless", ShapedRecipeSlot.UPPER_LEFT, ShapedRecipeSlot.UPPER_RIGHT, ShapedRecipeSlot.LOWER_LEFT, ShapedRecipeSlot.LOWER_RIGHT).
				addPart(ItemCraftingComponent.MetaType.GLASS_LENS.asStack(), ShapedRecipeSlot.UPPER_CENTER, ShapedRecipeSlot.LEFT, ShapedRecipeSlot.RIGHT, ShapedRecipeSlot.LOWER_CENTER).
				unregisteredAccessibleShapedRecipe());
		setCstItem(getTunedCrystal(Constellations.discidia), ConstellationAtlarSlot.UP_UP_RIGHT);
		setInnerTraitItem(getTunedCrystal(Constellations.armara), TraitRecipeSlot.RIGHT_CENTER);
		setInnerTraitItem(getTunedCrystal(Constellations.vicio), TraitRecipeSlot.LOWER_CENTER);
		setInnerTraitItem(getTunedCrystal(Constellations.aevitas), TraitRecipeSlot.LEFT_CENTER);
		setCstItem(getTunedCrystal(Constellations.evorsio), ConstellationAtlarSlot.UP_UP_LEFT);
		setInnerTraitItem(getTunedCrystal(Constellations.octans), TraitRecipeSlot.UPPER_CENTER);
		setCstItem(getTunedCrystal(Constellations.bootes), ConstellationAtlarSlot.UP_RIGHT_RIGHT);
		setCstItem(getTunedCrystal(Constellations.fornax), ConstellationAtlarSlot.DOWN_RIGHT_RIGHT);
		setCstItem(getTunedCrystal(Constellations.lucerna), ConstellationAtlarSlot.DOWN_DOWN_RIGHT);
		setCstItem(getTunedCrystal(Constellations.horologium), ConstellationAtlarSlot.DOWN_DOWN_LEFT);
		setCstItem(getTunedCrystal(Constellations.mineralis), ConstellationAtlarSlot.DOWN_LEFT_LEFT);
		setCstItem(getTunedCrystal(Constellations.pelotrio), ConstellationAtlarSlot.UP_LEFT_LEFT);
		addOuterTraitItem(ItemUsableDust.DustType.ILLUMINATION.asStack());
		addOuterTraitItem("dustAstralStarmetal");
		addOuterTraitItem(ItemUsableDust.DustType.ILLUMINATION.asStack());
		addOuterTraitItem(ItemUsableDust.DustType.NOCTURNAL.asStack());
		addOuterTraitItem(ItemUsableDust.DustType.ILLUMINATION.asStack());
		addOuterTraitItem("dustAstralStarmetal");
		addOuterTraitItem(ItemUsableDust.DustType.ILLUMINATION.asStack());
		addOuterTraitItem(ItemUsableDust.DustType.NOCTURNAL.asStack());;
		setRequiredConstellation(Constellations.lucerna);
	}

	public ItemHandle getTunedCrystal(IWeakConstellation constellation) {
		ItemStack rock = new ItemStack(ItemsAS.tunedRockCrystal);
		ItemStack celestial = new ItemStack(ItemsAS.tunedCelestialCrystal);
		NBTHelper.getPersistentData(rock).setTag("crystalProperties", new NBTTagCompound());
		ItemTunedCrystalBase.applyMainConstellation(rock, constellation);
		NBTHelper.getPersistentData(celestial).setTag("crystalProperties", new NBTTagCompound());
		ItemTunedCrystalBase.applyMainConstellation(celestial, constellation);
		return new ItemHandle(rock, celestial);
	}

	@Override
	public AbstractAltarRecipe copyNewEffectInstance() {
		return new RecipeConstellationFocus();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void onCraftClientTick(TileAltar altar, ActiveCraftingTask.CraftingState state, long tick, Random rand) {
		super.onCraftClientTick(altar, state, tick, rand);
		if(state == ActiveCraftingTask.CraftingState.ACTIVE) {
			Vector3 r = new Vector3(altar).add(rand.nextFloat()*7-3, 0.1, rand.nextFloat()*7-3);
			EffectHelper.genericFlareParticle(r).scale(0.7F).gravity(0.02);
			if(tick % 50 == 0) {
				Vector3 vec = new Vector3(altar).add(0.5, 0.5, 0.5);
				switch(altar.getAltarLevel()) {
				case ATTUNEMENT:
					for(Vector3 offset : offsetPillarsT2) {
						EffectHandler.getInstance().lightbeam(offset.clone().add(vec), vec, 1.2);
					}
					break;
				case CONSTELLATION_CRAFT:
					for(Vector3 offset : offsetPillarsT3) {
						EffectHandler.getInstance().lightbeam(offset.clone().add(vec), vec, 1.2);
					}
					break;
				default:
					break;
				}
			}
			if(rand.nextInt(10) == 0) {
				Vector3 from = new Vector3(altar).add(0.5, -0.6, 0.5);
				MiscUtils.applyRandomOffset(from, rand, 1.8F);
				from.addY(rand.nextFloat()*2-1);
				EffectHandler.getInstance().lightbeam(from.clone().addY(5+rand.nextInt(3)), from, 1).setMaxAge(64);
			}
		}
	}
}
