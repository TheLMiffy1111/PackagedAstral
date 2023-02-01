package thelm.packagedastral.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.items.SlotItemHandler;
import thelm.packagedastral.slot.DiscoveryCrafterRemoveOnlySlot;
import thelm.packagedastral.tile.DiscoveryCrafterTile;
import thelm.packagedauto.container.BaseContainer;
import thelm.packagedauto.container.factory.PositionalTileContainerFactory;
import thelm.packagedauto.slot.RemoveOnlySlot;

public class DiscoveryCrafterContainer extends BaseContainer<DiscoveryCrafterTile> {

	public static final ContainerType<DiscoveryCrafterContainer> TYPE_INSTANCE = (ContainerType<DiscoveryCrafterContainer>)IForgeContainerType.
			create(new PositionalTileContainerFactory<>(DiscoveryCrafterContainer::new)).
			setRegistryName("packagedastral:discovery_crafter");

	public DiscoveryCrafterContainer(int windowId, PlayerInventory playerInventory, DiscoveryCrafterTile tile) {
		super(TYPE_INSTANCE, windowId, playerInventory, tile);
		addSlot(new SlotItemHandler(itemHandler, 10, 8, 53));
		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 3; ++j) {
				addSlot(new DiscoveryCrafterRemoveOnlySlot(tile, i*3+j, 44+j*18, 17+i*18));
			}
		}
		addSlot(new RemoveOnlySlot(itemHandler, 9, 134, 35));
		setupPlayerInventory();
	}

	@Override
	public int getPlayerInvX() {
		return 8;
	}

	@Override
	public int getPlayerInvY() {
		return 99;
	}
}
