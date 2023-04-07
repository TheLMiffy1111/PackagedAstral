package thelm.packagedastral.client.event;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import thelm.packagedastral.block.MarkedRelayBlock;
import thelm.packagedastral.client.renderer.MarkedRelayRenderer;
import thelm.packagedastral.client.renderer.TraitCrafterRenderer;
import thelm.packagedastral.client.screen.AttunementCrafterScreen;
import thelm.packagedastral.client.screen.ConstellationCrafterScreen;
import thelm.packagedastral.client.screen.DiscoveryCrafterScreen;
import thelm.packagedastral.client.screen.TraitCrafterScreen;
import thelm.packagedastral.container.AttunementCrafterContainer;
import thelm.packagedastral.container.ConstellationCrafterContainer;
import thelm.packagedastral.container.DiscoveryCrafterContainer;
import thelm.packagedastral.container.TraitCrafterContainer;
import thelm.packagedastral.tile.MarkedRelayTile;
import thelm.packagedastral.tile.TraitCrafterTile;

public class ClientEventHandler {

	public static final ClientEventHandler INSTANCE = new ClientEventHandler();

	public static ClientEventHandler getInstance() {
		return INSTANCE;
	}

	public void onConstruct() {
		FMLJavaModLoadingContext.get().getModEventBus().register(this);
	}

	@SubscribeEvent
	public void onClientSetup(FMLClientSetupEvent event) {
		RenderTypeLookup.setRenderLayer(MarkedRelayBlock.INSTANCE, RenderType.translucent());

		ScreenManager.register(DiscoveryCrafterContainer.TYPE_INSTANCE, DiscoveryCrafterScreen::new);
		ScreenManager.register(AttunementCrafterContainer.TYPE_INSTANCE, AttunementCrafterScreen::new);
		ScreenManager.register(ConstellationCrafterContainer.TYPE_INSTANCE, ConstellationCrafterScreen::new);
		ScreenManager.register(TraitCrafterContainer.TYPE_INSTANCE, TraitCrafterScreen::new);

		ClientRegistry.bindTileEntityRenderer(TraitCrafterTile.TYPE_INSTANCE, TraitCrafterRenderer::new);
		ClientRegistry.bindTileEntityRenderer(MarkedRelayTile.TYPE_INSTANCE, MarkedRelayRenderer::new);
	}
}
