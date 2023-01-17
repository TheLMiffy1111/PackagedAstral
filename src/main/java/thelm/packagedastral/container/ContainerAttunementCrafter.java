package thelm.packagedastral.container;

import net.minecraft.entity.player.InventoryPlayer;
import thelm.packagedastral.slot.SlotAttunementCrafterRemoveOnly;
import thelm.packagedastral.tile.TileAttunementCrafter;
import thelm.packagedauto.container.ContainerTileBase;
import thelm.packagedauto.slot.SlotBase;
import thelm.packagedauto.slot.SlotRemoveOnly;

public class ContainerAttunementCrafter extends ContainerTileBase<TileAttunementCrafter> {

	public ContainerAttunementCrafter(InventoryPlayer playerInventory, TileAttunementCrafter tile) {
		super(playerInventory, tile);
		addSlotToContainer(new SlotBase(inventory, 14, 8, 71));
		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 3; ++j) {
				addSlotToContainer(new SlotAttunementCrafterRemoveOnly(tile, i*3+j, 62+j*18, 35+i*18));
			}
		}
		addSlotToContainer(new SlotAttunementCrafterRemoveOnly(tile, 9, 44, 17));
		addSlotToContainer(new SlotAttunementCrafterRemoveOnly(tile, 10, 116, 17));
		addSlotToContainer(new SlotAttunementCrafterRemoveOnly(tile, 11, 44, 89));
		addSlotToContainer(new SlotAttunementCrafterRemoveOnly(tile, 12, 116, 89));
		addSlotToContainer(new SlotRemoveOnly(inventory, 13, 170, 53));
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
