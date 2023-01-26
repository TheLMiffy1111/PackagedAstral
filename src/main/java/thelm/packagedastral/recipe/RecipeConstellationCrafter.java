package thelm.packagedastral.recipe;

import java.util.Random;

import hellfirepvp.astralsorcery.client.effect.EffectHelper;
import hellfirepvp.astralsorcery.common.block.BlockBlackMarble;
import hellfirepvp.astralsorcery.common.block.BlockMarble;
import hellfirepvp.astralsorcery.common.crafting.INighttimeRecipe;
import hellfirepvp.astralsorcery.common.crafting.ISpecialCraftingEffects;
import hellfirepvp.astralsorcery.common.crafting.ItemHandle;
import hellfirepvp.astralsorcery.common.crafting.altar.AbstractAltarRecipe;
import hellfirepvp.astralsorcery.common.crafting.altar.ActiveCraftingTask;
import hellfirepvp.astralsorcery.common.crafting.altar.recipes.ConstellationRecipe;
import hellfirepvp.astralsorcery.common.crafting.helper.ShapedRecipeSlot;
import hellfirepvp.astralsorcery.common.item.ItemCraftingComponent;
import hellfirepvp.astralsorcery.common.tile.TileAltar;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thelm.packagedastral.block.BlockAttunementCrafter;
import thelm.packagedastral.block.BlockConstellationCrafter;

public class RecipeConstellationCrafter extends ConstellationRecipe implements INighttimeRecipe, ISpecialCraftingEffects {

	public static final RecipeConstellationCrafter INSTANCE = new RecipeConstellationCrafter();

	private static Vector3[] offsetPillars = new Vector3[] {
			new Vector3(4, 3, 4), new Vector3(-4, 3, 4), new Vector3(4, 3, -4), new Vector3(-4, 3, -4)
	};

	protected RecipeConstellationCrafter() {
		super(shapedRecipe("packagedastral/constellation_crafter", BlockConstellationCrafter.INSTANCE).
				addPart(BlockAttunementCrafter.INSTANCE, ShapedRecipeSlot.CENTER).
				addPart(ItemHandle.getCrystalVariant(false, false), ShapedRecipeSlot.UPPER_CENTER).
				addPart("ingotAstralStarmetal", ShapedRecipeSlot.LOWER_CENTER).
				addPart(BlockMarble.MarbleBlockType.CHISELED.asStack(), ShapedRecipeSlot.LEFT, ShapedRecipeSlot.RIGHT).
				addPart("gemAquamarine", ShapedRecipeSlot.UPPER_LEFT, ShapedRecipeSlot.UPPER_RIGHT).
				addPart(BlockMarble.MarbleBlockType.PILLAR.asStack(), ShapedRecipeSlot.LOWER_LEFT, ShapedRecipeSlot.LOWER_RIGHT).
				unregisteredAccessibleShapedRecipe());
		setAttItem("dustAstralStarmetal", AttunementAltarSlot.UPPER_LEFT, AttunementAltarSlot.UPPER_RIGHT);
		setAttItem(BlockMarble.MarbleBlockType.CHISELED.asStack(), AttunementAltarSlot.LOWER_LEFT, AttunementAltarSlot.LOWER_RIGHT);
		setCstItem("ingotGold", ConstellationAtlarSlot.UP_UP_LEFT, ConstellationAtlarSlot.UP_UP_RIGHT);
		setCstItem("dustRedstone", ConstellationAtlarSlot.UP_LEFT_LEFT, ConstellationAtlarSlot.UP_RIGHT_RIGHT);
		setCstItem(ItemCraftingComponent.MetaType.RESO_GEM.asStack(), ConstellationAtlarSlot.DOWN_LEFT_LEFT, ConstellationAtlarSlot.DOWN_RIGHT_RIGHT);
		setCstItem(BlockBlackMarble.BlackMarbleBlockType.RAW.asStack(), ConstellationAtlarSlot.DOWN_DOWN_LEFT, ConstellationAtlarSlot.DOWN_DOWN_RIGHT);
	}

	@Override
	public AbstractAltarRecipe copyNewEffectInstance() {
		return new RecipeConstellationCrafter();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void onCraftClientTick(TileAltar altar, ActiveCraftingTask.CraftingState state, long tick, Random rand) {
		super.onCraftClientTick(altar, state, tick, rand);
		if(state == ActiveCraftingTask.CraftingState.ACTIVE) {
			Vector3 vec = new Vector3(altar).add(0.5, 0.5, 0.5);
			for(int i = 0; i < 3; i++) {
				Vector3 dir = offsetPillars[rand.nextInt(offsetPillars.length)].clone();
				dir.multiply(rand.nextFloat()).add(vec);
				EffectHelper.genericFlareParticle(dir).setColor(MiscUtils.calcRandomConstellationColor(rand.nextFloat())).
				scale(0.2F+0.2F*rand.nextFloat()).gravity(0.004);
			}
		}
	}
}
