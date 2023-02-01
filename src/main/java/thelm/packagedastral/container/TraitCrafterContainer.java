package thelm.packagedastral.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.items.SlotItemHandler;
import thelm.packagedastral.slot.TraitCrafterRemoveOnlySlot;
import thelm.packagedastral.tile.TraitCrafterTile;
import thelm.packagedauto.container.BaseContainer;
import thelm.packagedauto.container.factory.PositionalTileContainerFactory;
import thelm.packagedauto.slot.RemoveOnlySlot;

public class TraitCrafterContainer extends BaseContainer<TraitCrafterTile> {

	public static final ContainerType<TraitCrafterContainer> TYPE_INSTANCE = (ContainerType<TraitCrafterContainer>)IForgeContainerType.
			create(new PositionalTileContainerFactory<>(TraitCrafterContainer::new)).
			setRegistryName("packagedastral:trait_crafter");

	public TraitCrafterContainer(int windowId, PlayerInventory playerInventory, TraitCrafterTile tile) {
		super(TYPE_INSTANCE, windowId, playerInventory, tile);
		addSlot(new SlotItemHandler(itemHandler, 26, 8, 71));
		for(int i = 0; i < 5; ++i) {
			for(int j = 0; j < 5; ++j) {
				addSlot(new TraitCrafterRemoveOnlySlot(tile, i*5+j, 44+j*18, 17+i*18));
			}
		}
		addSlot(new RemoveOnlySlot(itemHandler, 25, 170, 53));
		setupPlayerInventory();
	}

	@Override
	public int getPlayerInvX() {
		return 19;
	}

	@Override
	public int getPlayerInvY() {
		return 135;
	}
}
