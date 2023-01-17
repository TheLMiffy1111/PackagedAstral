package thelm.packagedastral.proxy;

import hellfirepvp.astralsorcery.common.block.BlockInfusedWood;
import hellfirepvp.astralsorcery.common.block.BlockMarble;
import hellfirepvp.astralsorcery.common.crafting.altar.AltarRecipeRegistry;
import hellfirepvp.astralsorcery.common.crafting.helper.ShapedRecipe;
import hellfirepvp.astralsorcery.common.crafting.helper.ShapedRecipeSlot;
import hellfirepvp.astralsorcery.common.item.useables.ItemUsableDust;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import hellfirepvp.astralsorcery.common.structure.StructureMatcherRegistry;
import hellfirepvp.astralsorcery.common.structure.StructureRegistry;
import hellfirepvp.astralsorcery.common.structure.array.PatternBlockArray;
import hellfirepvp.astralsorcery.common.structure.match.StructureMatcherPatternArray;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import thelm.packagedastral.block.BlockAttunementCrafter;
import thelm.packagedastral.block.BlockConstellationCrafter;
import thelm.packagedastral.block.BlockDiscoveryCrafter;
import thelm.packagedastral.block.BlockMarkedRelay;
import thelm.packagedastral.block.BlockTraitCrafter;
import thelm.packagedastral.config.PackagedAstralConfig;
import thelm.packagedastral.item.ItemConstellationFocus;
import thelm.packagedastral.recipe.RecipeAttunementCrafter;
import thelm.packagedastral.recipe.RecipeConstellationCrafter;
import thelm.packagedastral.recipe.RecipeConstellationFocus;
import thelm.packagedastral.recipe.RecipeTraitCrafter;
import thelm.packagedastral.recipe.RecipeTypeAttunement;
import thelm.packagedastral.recipe.RecipeTypeConstellation;
import thelm.packagedastral.recipe.RecipeTypeDiscovery;
import thelm.packagedastral.recipe.RecipeTypeTrait;
import thelm.packagedastral.structure.StructureAttunementCrafter;
import thelm.packagedastral.structure.StructureConstellationCrafter;
import thelm.packagedastral.structure.StructureTraitCrafter;
import thelm.packagedastral.tile.TileAttunementCrafter;
import thelm.packagedastral.tile.TileConstellationCrafter;
import thelm.packagedastral.tile.TileDiscoveryCrafter;
import thelm.packagedastral.tile.TileMarkedRelay;
import thelm.packagedastral.tile.TileTraitCrafter;
import thelm.packagedauto.api.RecipeTypeRegistry;
import thelm.packagedauto.item.ItemMisc;

public class CommonProxy {

	public void registerBlock(Block block) {
		ForgeRegistries.BLOCKS.register(block);
	}

	public void registerItem(Item item) {
		ForgeRegistries.ITEMS.register(item);
	}

	public void registerStructure(PatternBlockArray structure) {
		StructureRegistry.INSTANCE.register(structure);
		StructureMatcherRegistry.INSTANCE.register(()->new StructureMatcherPatternArray(structure.getRegistryName()));
	}

	public void register(FMLPreInitializationEvent event) {
		registerConfig(event);
		registerBlocks();
		registerItems();
		registerModels();
		registerTileEntities();
		registerRecipeTypes();
	}

	public void register(FMLInitializationEvent event) {
		registerStructures();
		registerRecipes();
	}

	protected void registerConfig(FMLPreInitializationEvent event) {
		PackagedAstralConfig.init(event.getSuggestedConfigurationFile());
	}

	protected void registerBlocks() {
		if(TileDiscoveryCrafter.enabled) {
			registerBlock(BlockDiscoveryCrafter.INSTANCE);
		}
		if(TileAttunementCrafter.enabled) {
			registerBlock(BlockAttunementCrafter.INSTANCE);
		}
		if(TileConstellationCrafter.enabled) {
			registerBlock(BlockConstellationCrafter.INSTANCE);
		}
		if(TileTraitCrafter.enabled) {
			registerBlock(BlockTraitCrafter.INSTANCE);
			registerBlock(BlockMarkedRelay.INSTANCE);
		}
	}

	protected void registerItems() {
		if(TileDiscoveryCrafter.enabled) {
			registerItem(BlockDiscoveryCrafter.ITEM_INSTANCE);
		}
		if(TileAttunementCrafter.enabled) {
			registerItem(BlockAttunementCrafter.ITEM_INSTANCE);
		}
		if(TileConstellationCrafter.enabled) {
			registerItem(BlockConstellationCrafter.ITEM_INSTANCE);
		}
		if(TileTraitCrafter.enabled) {
			registerItem(BlockTraitCrafter.ITEM_INSTANCE);
			registerItem(BlockMarkedRelay.ITEM_INSTANCE);
			registerItem(ItemConstellationFocus.INSTANCE);
		}
	}

	protected void registerModels() {}

	protected void registerTileEntities() {
		if(TileDiscoveryCrafter.enabled) {
			GameRegistry.registerTileEntity(TileDiscoveryCrafter.class, new ResourceLocation("packagedastral:discovery_crafter"));
		}
		if(TileAttunementCrafter.enabled) {
			GameRegistry.registerTileEntity(TileAttunementCrafter.class, new ResourceLocation("packagedastral:attunement_crafter"));
		}
		if(TileConstellationCrafter.enabled) {
			GameRegistry.registerTileEntity(TileConstellationCrafter.class, new ResourceLocation("packagedastral:constellation_crafter"));
		}
		if(TileTraitCrafter.enabled) {
			GameRegistry.registerTileEntity(TileTraitCrafter.class, new ResourceLocation("packagedastral:trait_crafter"));
			GameRegistry.registerTileEntity(TileMarkedRelay.class, new ResourceLocation("packagedastral:marked_relay"));
		}
	}

	protected void registerRecipeTypes() {
		if(TileDiscoveryCrafter.enabled) {
			RecipeTypeRegistry.registerRecipeType(RecipeTypeDiscovery.INSTANCE);
		}
		if(TileAttunementCrafter.enabled) {
			RecipeTypeRegistry.registerRecipeType(RecipeTypeAttunement.INSTANCE);
		}
		if(TileConstellationCrafter.enabled) {
			RecipeTypeRegistry.registerRecipeType(RecipeTypeConstellation.INSTANCE);
		}
		if(TileTraitCrafter.enabled) {
			RecipeTypeRegistry.registerRecipeType(RecipeTypeTrait.INSTANCE);
		}
	}

	protected void registerStructures() {
		if(TileAttunementCrafter.enabled) {
			registerStructure(StructureAttunementCrafter.INSTANCE);
		}
		if(TileConstellationCrafter.enabled) {
			registerStructure(StructureConstellationCrafter.INSTANCE);
		}
		if(TileTraitCrafter.enabled) {
			registerStructure(StructureTraitCrafter.INSTANCE);
		}
	}

	protected void registerRecipes() {
		if(TileDiscoveryCrafter.enabled) {
			Item component = Loader.isModLoaded("appliedenergistics2") ? ItemMisc.ME_PACKAGE_COMPONENT : ItemMisc.PACKAGE_COMPONENT;
			AltarRecipeRegistry.registerDiscoveryRecipe(
					ShapedRecipe.Builder.newShapedRecipe("packagedastral/discovery_crafter", BlockDiscoveryCrafter.INSTANCE).
					addPart(new ItemStack(BlocksAS.blockAltar, 1, 0), ShapedRecipeSlot.CENTER).
					addPart(ItemsAS.wand, ShapedRecipeSlot.UPPER_CENTER).
					addPart(component, ShapedRecipeSlot.LOWER_CENTER).
					addPart(BlockInfusedWood.WoodType.RAW.asStack(), ShapedRecipeSlot.LEFT, ShapedRecipeSlot.RIGHT).
					addPart(BlockMarble.MarbleBlockType.CHISELED.asStack(), ShapedRecipeSlot.UPPER_LEFT, ShapedRecipeSlot.UPPER_RIGHT, ShapedRecipeSlot.LOWER_LEFT, ShapedRecipeSlot.LOWER_RIGHT).
					unregisteredAccessibleShapedRecipe());
		}
		if(TileAttunementCrafter.enabled) {
			AltarRecipeRegistry.registerAltarRecipe(RecipeAttunementCrafter.INSTANCE);
		}
		if(TileConstellationCrafter.enabled) {
			AltarRecipeRegistry.registerAltarRecipe(RecipeConstellationCrafter.INSTANCE);
		}
		if(TileTraitCrafter.enabled) {
			AltarRecipeRegistry.registerAltarRecipe(RecipeTraitCrafter.INSTANCE);
			AltarRecipeRegistry.registerDiscoveryRecipe(
					ShapedRecipe.Builder.newShapedRecipe("packagedastral/marked_relay", BlockMarkedRelay.ITEM_INSTANCE).
					addPart(BlocksAS.attunementRelay, ShapedRecipeSlot.CENTER).
					addPart(ItemUsableDust.DustType.ILLUMINATION.asStack(), ShapedRecipeSlot.UPPER_CENTER, ShapedRecipeSlot.LEFT, ShapedRecipeSlot.RIGHT, ShapedRecipeSlot.LOWER_CENTER).
					unregisteredAccessibleShapedRecipe());
			AltarRecipeRegistry.registerAltarRecipe(RecipeConstellationFocus.INSTANCE);
		}
	}
}
