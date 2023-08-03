package thelm.packagedastral.event;

import hellfirepvp.astralsorcery.common.structure.types.StructureType;
import hellfirepvp.observerlib.api.structure.MatchableStructure;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import thelm.packagedastral.block.AttunementCrafterBlock;
import thelm.packagedastral.block.ConstellationCrafterBlock;
import thelm.packagedastral.block.DiscoveryCrafterBlock;
import thelm.packagedastral.block.MarkedRelayBlock;
import thelm.packagedastral.block.TraitCrafterBlock;
import thelm.packagedastral.config.PackagedAstralConfig;
import thelm.packagedastral.container.AttunementCrafterContainer;
import thelm.packagedastral.container.ConstellationCrafterContainer;
import thelm.packagedastral.container.DiscoveryCrafterContainer;
import thelm.packagedastral.container.TraitCrafterContainer;
import thelm.packagedastral.ingredient.AttunedCrystalIngredient;
import thelm.packagedastral.item.ConstellationFocusItem;
import thelm.packagedastral.network.PacketHandler;
import thelm.packagedastral.recipe.AttunementPackageRecipeType;
import thelm.packagedastral.recipe.ConstellationPackageRecipeType;
import thelm.packagedastral.recipe.DiscoveryPackageRecipeType;
import thelm.packagedastral.recipe.TraitPackageRecipeType;
import thelm.packagedastral.structure.AttunementCrafterPatternStructure;
import thelm.packagedastral.structure.ConstellationCrafterPatternStructure;
import thelm.packagedastral.structure.MarkedRelayPatternStructure;
import thelm.packagedastral.structure.TraitCrafterPatternStructure;
import thelm.packagedastral.tile.AttunementCrafterTile;
import thelm.packagedastral.tile.ConstellationCrafterTile;
import thelm.packagedastral.tile.DiscoveryCrafterTile;
import thelm.packagedastral.tile.MarkedRelayTile;
import thelm.packagedastral.tile.TraitCrafterTile;
import thelm.packagedauto.util.ApiImpl;

public class CommonEventHandler {

	public static final CommonEventHandler INSTANCE = new CommonEventHandler();

	public static CommonEventHandler getInstance() {
		return INSTANCE;
	}

	public void onConstruct() {
		FMLJavaModLoadingContext.get().getModEventBus().register(this);
		PackagedAstralConfig.registerConfig();
		CraftingHelper.register(AttunedCrystalIngredient.ID, AttunedCrystalIngredient.SERIALIZER);
	}

	@SubscribeEvent
	public void onBlockRegister(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();
		registry.register(DiscoveryCrafterBlock.INSTANCE);
		registry.register(AttunementCrafterBlock.INSTANCE);
		registry.register(ConstellationCrafterBlock.INSTANCE);
		registry.register(TraitCrafterBlock.INSTANCE);
		registry.register(MarkedRelayBlock.INSTANCE);
	}

	@SubscribeEvent
	public void onItemRegister(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		registry.register(MarkedRelayBlock.ITEM_INSTANCE);
		registry.register(DiscoveryCrafterBlock.ITEM_INSTANCE);
		registry.register(AttunementCrafterBlock.ITEM_INSTANCE);
		registry.register(ConstellationCrafterBlock.ITEM_INSTANCE);
		registry.register(TraitCrafterBlock.ITEM_INSTANCE);
		registry.register(ConstellationFocusItem.INSTANCE);
	}

	@SubscribeEvent
	public void onTileRegister(RegistryEvent.Register<TileEntityType<?>> event) {
		IForgeRegistry<TileEntityType<?>> registry = event.getRegistry();
		registry.register(MarkedRelayTile.TYPE_INSTANCE);
		registry.register(DiscoveryCrafterTile.TYPE_INSTANCE);
		registry.register(AttunementCrafterTile.TYPE_INSTANCE);
		registry.register(ConstellationCrafterTile.TYPE_INSTANCE);
		registry.register(TraitCrafterTile.TYPE_INSTANCE);
	}

	@SubscribeEvent
	public void onContainerRegister(RegistryEvent.Register<ContainerType<?>> event) {
		IForgeRegistry<ContainerType<?>> registry = event.getRegistry();
		registry.register(DiscoveryCrafterContainer.TYPE_INSTANCE);
		registry.register(AttunementCrafterContainer.TYPE_INSTANCE);
		registry.register(ConstellationCrafterContainer.TYPE_INSTANCE);
		registry.register(TraitCrafterContainer.TYPE_INSTANCE);
	}

	@SubscribeEvent
	public void onStructureTypeRegister(RegistryEvent.Register<StructureType> event) {
		IForgeRegistry<StructureType> registry = event.getRegistry();
		registry.register(MarkedRelayPatternStructure.TYPE_INSTANCE);
		registry.register(AttunementCrafterPatternStructure.TYPE_INSTANCE);
		registry.register(ConstellationCrafterPatternStructure.TYPE_INSTANCE);
		registry.register(TraitCrafterPatternStructure.TYPE_INSTANCE);
	}

	@SubscribeEvent
	public void onMatchableStructureRegister(RegistryEvent.Register<MatchableStructure> event) {
		IForgeRegistry<MatchableStructure> registry = event.getRegistry();
		registry.register(MarkedRelayPatternStructure.INSTANCE);
		registry.register(AttunementCrafterPatternStructure.INSTANCE);
		registry.register(ConstellationCrafterPatternStructure.INSTANCE);
		registry.register(TraitCrafterPatternStructure.INSTANCE);
	}

	@SubscribeEvent
	public void onCommonSetup(FMLCommonSetupEvent event) {
		ApiImpl.INSTANCE.registerRecipeType(DiscoveryPackageRecipeType.INSTANCE);
		ApiImpl.INSTANCE.registerRecipeType(AttunementPackageRecipeType.INSTANCE);
		ApiImpl.INSTANCE.registerRecipeType(ConstellationPackageRecipeType.INSTANCE);
		ApiImpl.INSTANCE.registerRecipeType(TraitPackageRecipeType.INSTANCE);
		PacketHandler.registerPackets();
	}

	@SubscribeEvent
	public void onModConfig(ModConfig.ModConfigEvent event) {
		switch(event.getConfig().getType()) {
		case SERVER:
			PackagedAstralConfig.reloadServerConfig();
			break;
		default:
			break;
		}
	}
}
