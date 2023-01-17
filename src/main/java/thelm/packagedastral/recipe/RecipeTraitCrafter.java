package thelm.packagedastral.recipe;

import java.awt.Color;
import java.util.Random;

import hellfirepvp.astralsorcery.client.effect.EffectHandler;
import hellfirepvp.astralsorcery.client.effect.EffectHelper;
import hellfirepvp.astralsorcery.client.effect.EntityComplexFX;
import hellfirepvp.astralsorcery.common.block.BlockBlackMarble;
import hellfirepvp.astralsorcery.common.block.BlockMarble;
import hellfirepvp.astralsorcery.common.crafting.INighttimeRecipe;
import hellfirepvp.astralsorcery.common.crafting.ISpecialCraftingEffects;
import hellfirepvp.astralsorcery.common.crafting.ItemHandle;
import hellfirepvp.astralsorcery.common.crafting.altar.AbstractAltarRecipe;
import hellfirepvp.astralsorcery.common.crafting.altar.ActiveCraftingTask;
import hellfirepvp.astralsorcery.common.crafting.altar.recipes.TraitRecipe;
import hellfirepvp.astralsorcery.common.crafting.altar.recipes.upgrade.TraitUpgradeRecipe;
import hellfirepvp.astralsorcery.common.crafting.helper.ShapedRecipeSlot;
import hellfirepvp.astralsorcery.common.item.ItemCraftingComponent;
import hellfirepvp.astralsorcery.common.item.useables.ItemUsableDust;
import hellfirepvp.astralsorcery.common.lib.Constellations;
import hellfirepvp.astralsorcery.common.tile.TileAltar;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.init.Items;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thelm.packagedastral.block.BlockConstellationCrafter;
import thelm.packagedastral.block.BlockTraitCrafter;
import thelm.packagedastral.item.ItemConstellationFocus;

public class RecipeTraitCrafter extends TraitRecipe implements INighttimeRecipe, ISpecialCraftingEffects {

	public static final AbstractAltarRecipe INSTANCE = new RecipeTraitCrafter();

	private static Vector3[] offsetPillars = new Vector3[] {
			new Vector3(4, 4, 4), new Vector3(-4, 4, 4), new Vector3(4, 4, -4), new Vector3(-4, 4, -4)
	};

	protected RecipeTraitCrafter() {
		super(shapedRecipe("packagedastral/trait_crafter", BlockTraitCrafter.INSTANCE).
				addPart(BlockConstellationCrafter.INSTANCE, ShapedRecipeSlot.CENTER).
				addPart(ItemConstellationFocus.INSTANCE, ShapedRecipeSlot.UPPER_CENTER).
				addPart(ItemHandle.getCrystalVariant(false, true), ShapedRecipeSlot.LOWER_CENTER).
				addPart(Items.ENDER_EYE, ShapedRecipeSlot.LEFT, ShapedRecipeSlot.RIGHT).
				addPart(BlockMarble.MarbleBlockType.RUNED.asStack(), ShapedRecipeSlot.UPPER_LEFT, ShapedRecipeSlot.UPPER_RIGHT, ShapedRecipeSlot.LOWER_LEFT, ShapedRecipeSlot.LOWER_RIGHT).
				unregisteredAccessibleShapedRecipe());
		setAttItem(BlockMarble.MarbleBlockType.RUNED.asStack(), AttunementAltarSlot.UPPER_LEFT, AttunementAltarSlot.UPPER_RIGHT, AttunementAltarSlot.LOWER_LEFT, AttunementAltarSlot.LOWER_RIGHT);
		setCstItem(BlockBlackMarble.BlackMarbleBlockType.CHISELED.asStack(), ConstellationAtlarSlot.UP_UP_LEFT, ConstellationAtlarSlot.UP_UP_RIGHT, ConstellationAtlarSlot.DOWN_DOWN_LEFT, ConstellationAtlarSlot.DOWN_DOWN_RIGHT);
		setCstItem(ItemCraftingComponent.MetaType.RESO_GEM.asStack(), ConstellationAtlarSlot.UP_LEFT_LEFT, ConstellationAtlarSlot.UP_RIGHT_RIGHT, ConstellationAtlarSlot.DOWN_LEFT_LEFT, ConstellationAtlarSlot.DOWN_RIGHT_RIGHT);
		setInnerTraitItem(ItemCraftingComponent.MetaType.GLASS_LENS.asStack(), TraitRecipeSlot.UPPER_CENTER, TraitRecipeSlot.LEFT_CENTER, TraitRecipeSlot.RIGHT_CENTER, TraitRecipeSlot.LOWER_CENTER);
		addOuterTraitItem(ItemUsableDust.DustType.ILLUMINATION.asStack());
		addOuterTraitItem("gemAquamarine");
		addOuterTraitItem("dustAstralStarmetal");
		addOuterTraitItem("dustRedstone");
		addOuterTraitItem(ItemUsableDust.DustType.ILLUMINATION.asStack());
		addOuterTraitItem("enderpearl");
		addOuterTraitItem(ItemUsableDust.DustType.NOCTURNAL.asStack());
		addOuterTraitItem("dustRedstone");
		addOuterTraitItem(ItemUsableDust.DustType.ILLUMINATION.asStack());
		addOuterTraitItem("gemAquamarine");
		addOuterTraitItem("dustAstralStarmetal");
		addOuterTraitItem("dustRedstone");
		addOuterTraitItem(ItemUsableDust.DustType.ILLUMINATION.asStack());
		addOuterTraitItem("enderpearl");
		addOuterTraitItem(ItemUsableDust.DustType.NOCTURNAL.asStack());
		addOuterTraitItem("dustRedstone");
		setRequiredConstellation(Constellations.horologium);
	}

	@Override
	public AbstractAltarRecipe copyNewEffectInstance() {
		return new TraitUpgradeRecipe();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void onCraftClientTick(TileAltar altar, ActiveCraftingTask.CraftingState state, long tick, Random rand) {
		super.onCraftClientTick(altar, state, tick, rand);
		if(state == ActiveCraftingTask.CraftingState.ACTIVE) {
			Vector3 vec = new Vector3(altar).add(0.5, 0.5, 0.5);
			for(int i = 0; i < 4; i++) {
				Vector3 dir = offsetPillars[rand.nextInt(offsetPillars.length)].clone();
				dir.multiply(rand.nextFloat()).setY(dir.getY()*rand.nextFloat()).add(vec.clone());
				EffectHelper.genericFlareParticle(dir).setColor(MiscUtils.calcRandomConstellationColor(rand.nextFloat())).
				scale(0.4F+0.2F*rand.nextFloat()).gravity(0.004);
				EffectHelper.genericFlareParticle(dir).setColor(Color.WHITE).
				scale(0.1F+0.2F*rand.nextFloat()).gravity(0.004);
			}
			Vector3 f = new Vector3(altar).add(-3+rand.nextFloat()*7, 0.02, -3+rand.nextFloat()*7);
			EffectHelper.genericFlareParticle(f).
			gravity(0.004).enableAlphaFade(EntityComplexFX.AlphaFunction.FADE_OUT).
			scale(rand.nextFloat()*0.2F+0.15F).setColor(Color.WHITE);
			int j;
			for(j = 0; j < 1; j++) {
				Vector3 r = Vector3.random().setY(0).normalize().multiply(1.3+rand.nextFloat()*0.5).add(vec.clone().addY(2.2));
				EffectHelper.genericFlareParticle(r).
				gravity(0.004).enableAlphaFade(EntityComplexFX.AlphaFunction.FADE_OUT).
				scale(rand.nextFloat()*0.2F+0.1F).setColor(Color.WHITE);
			}
			for(j = 0; j < 2; j++) {
				Vector3 r = Vector3.random().setY(0).normalize().multiply(2+rand.nextFloat()*0.5).add(vec.clone().addY(1.3));
				EffectHelper.genericFlareParticle(r).
				gravity(0.004).enableAlphaFade(EntityComplexFX.AlphaFunction.FADE_OUT).
				scale(rand.nextFloat()*0.2F+0.1F).setColor(Color.WHITE);
			}
			for(j = 0; j < 10; j++) {
				Vector3 dir = offsetPillars[rand.nextInt(offsetPillars.length)].clone();
				dir.setY(rand.nextFloat()*dir.getY()).add(-0.3+1.6*rand.nextFloat(), 0, -0.3+1.6*rand.nextFloat());
				Vector3 r = vec.clone().add(dir);
				EffectHelper.genericFlareParticle(r).
				gravity(0.01+0.02*rand.nextFloat()).enableAlphaFade(EntityComplexFX.AlphaFunction.FADE_OUT).
				scale(rand.nextFloat()*0.5F+0.3F).setColor(Color.WHITE);
			}
			if(rand.nextInt(10) == 0) {
				Vector3 from = new Vector3(altar).add(-3+rand.nextFloat()*7, 0.02, -3+rand.nextFloat()*7);
				MiscUtils.applyRandomOffset(from, rand, 0.4F);
				EffectHandler.getInstance().lightbeam(from.clone().addY(4+rand.nextInt(2)), from, 1D).
				setColorOverlay(new Color(0x5369FF)).setMaxAge(64);
			}
		}
	}
}
