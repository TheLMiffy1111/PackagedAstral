package thelm.packagedastral.container;

import net.minecraft.entity.player.InventoryPlayer;
import thelm.packagedastral.slot.SlotDiscoveryCrafterRemoveOnly;
import thelm.packagedastral.tile.TileDiscoveryCrafter;
import thelm.packagedauto.container.ContainerTileBase;
import thelm.packagedauto.slot.SlotBase;
import thelm.packagedauto.slot.SlotRemoveOnly;

public class ContainerDiscoveryCrafter extends ContainerTileBase<TileDiscoveryCrafter> {

	public ContainerDiscoveryCrafter(InventoryPlayer playerInventory, TileDiscoveryCrafter tile) {
		super(playerInventory, tile);
		addSlotToContainer(new SlotBase(inventory, 10, 8, 53));
		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 3; ++j) {
				addSlotToContainer(new SlotDiscoveryCrafterRemoveOnly(tile, i*3+j, 44+j*18, 17+i*18));
			}
		}
		addSlotToContainer(new SlotRemoveOnly(inventory, 9, 134, 35));
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
