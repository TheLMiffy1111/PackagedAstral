package thelm.packagedastral.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.items.SlotItemHandler;
import thelm.packagedastral.slot.AttunementCrafterRemoveOnlySlot;
import thelm.packagedastral.tile.AttunementCrafterTile;
import thelm.packagedauto.container.BaseContainer;
import thelm.packagedauto.container.factory.PositionalTileContainerFactory;
import thelm.packagedauto.slot.RemoveOnlySlot;

public class AttunementCrafterContainer extends BaseContainer<AttunementCrafterTile> {

	public static final ContainerType<AttunementCrafterContainer> TYPE_INSTANCE = (ContainerType<AttunementCrafterContainer>)IForgeContainerType.
			create(new PositionalTileContainerFactory<>(AttunementCrafterContainer::new)).
			setRegistryName("packagedastral:attunement_crafter");

	public AttunementCrafterContainer(int windowId, PlayerInventory playerInventory, AttunementCrafterTile tile) {
		super(TYPE_INSTANCE, windowId, playerInventory, tile);
		addSlot(new SlotItemHandler(itemHandler, 14, 8, 71));
		addSlot(new AttunementCrafterRemoveOnlySlot(tile, 0, 44, 17));
		addSlot(new AttunementCrafterRemoveOnlySlot(tile, 1, 116, 17));
		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 3; ++j) {
				addSlot(new AttunementCrafterRemoveOnlySlot(tile, 2+i*3+j, 62+j*18, 35+i*18));
			}
		}
		addSlot(new AttunementCrafterRemoveOnlySlot(tile, 11, 44, 89));
		addSlot(new AttunementCrafterRemoveOnlySlot(tile, 12, 116, 89));
		addSlot(new RemoveOnlySlot(itemHandler, 13, 170, 53));
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
