package thelm.packagedastral.client.event;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import thelm.packagedastral.client.renderer.RendererMarkedRelay;
import thelm.packagedastral.client.renderer.RendererTraitCrafter;
import thelm.packagedastral.event.CommonEventHandler;
import thelm.packagedastral.tile.TileMarkedRelay;
import thelm.packagedastral.tile.TileTraitCrafter;
import thelm.packagedauto.client.IModelRegister;

public class ClientEventHandler extends CommonEventHandler {

	private static List<IModelRegister> modelRegisterList = new ArrayList<>();

	@Override
	public void registerBlock(Block block) {
		super.registerBlock(block);
		if(block instanceof IModelRegister) {
			modelRegisterList.add((IModelRegister)block);
		}
	}

	@Override
	public void registerItem(Item item) {
		super.registerItem(item);
		if(item instanceof IModelRegister) {
			modelRegisterList.add((IModelRegister)item);
		}
	}

	@Override
	public void onPreInit(FMLPreInitializationEvent event) {
		super.onPreInit(event);
		OBJLoader.INSTANCE.addDomain("packagedastral");
		registerModels();
	}

	@Override
	protected void registerTileEntities() {
		super.registerTileEntities();
		if(TileTraitCrafter.enabled) {
			ClientRegistry.bindTileEntitySpecialRenderer(TileTraitCrafter.class, new RendererTraitCrafter());
			ClientRegistry.bindTileEntitySpecialRenderer(TileMarkedRelay.class, new RendererMarkedRelay());
		}
	}

	protected void registerModels() {
		for(IModelRegister model : modelRegisterList) {
			model.registerModels();
		}
	}
}
