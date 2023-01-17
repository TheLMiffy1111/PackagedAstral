package thelm.packagedastral.container;

import net.minecraft.entity.player.InventoryPlayer;
import thelm.packagedastral.slot.SlotTraitCrafterRemoveOnly;
import thelm.packagedastral.tile.TileTraitCrafter;
import thelm.packagedauto.container.ContainerTileBase;
import thelm.packagedauto.slot.SlotBase;
import thelm.packagedauto.slot.SlotRemoveOnly;

public class ContainerTraitCrafter extends ContainerTileBase<TileTraitCrafter> {

	public ContainerTraitCrafter(InventoryPlayer playerInventory, TileTraitCrafter tile) {
		super(playerInventory, tile);
		addSlotToContainer(new SlotBase(inventory, 26, 8, 71));
		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 3; ++j) {
				addSlotToContainer(new SlotTraitCrafterRemoveOnly(tile, i*3+j, 62+j*18, 35+i*18));
			}
		}
		addSlotToContainer(new SlotTraitCrafterRemoveOnly(tile, 9, 44, 17));
		addSlotToContainer(new SlotTraitCrafterRemoveOnly(tile, 10, 116, 17));
		addSlotToContainer(new SlotTraitCrafterRemoveOnly(tile, 11, 44, 89));
		addSlotToContainer(new SlotTraitCrafterRemoveOnly(tile, 12, 116, 89));
		addSlotToContainer(new SlotTraitCrafterRemoveOnly(tile, 13, 62, 17));
		addSlotToContainer(new SlotTraitCrafterRemoveOnly(tile, 14, 98, 17));
		addSlotToContainer(new SlotTraitCrafterRemoveOnly(tile, 15, 44, 35));
		addSlotToContainer(new SlotTraitCrafterRemoveOnly(tile, 16, 116, 35));
		addSlotToContainer(new SlotTraitCrafterRemoveOnly(tile, 17, 44, 71));
		addSlotToContainer(new SlotTraitCrafterRemoveOnly(tile, 18, 116, 71));
		addSlotToContainer(new SlotTraitCrafterRemoveOnly(tile, 19, 62, 89));
		addSlotToContainer(new SlotTraitCrafterRemoveOnly(tile, 20, 98, 89));
		addSlotToContainer(new SlotTraitCrafterRemoveOnly(tile, 21, 80, 17));
		addSlotToContainer(new SlotTraitCrafterRemoveOnly(tile, 22, 44, 53));
		addSlotToContainer(new SlotTraitCrafterRemoveOnly(tile, 23, 116, 53));
		addSlotToContainer(new SlotTraitCrafterRemoveOnly(tile, 24, 80, 89));
		addSlotToContainer(new SlotRemoveOnly(inventory, 25, 170, 53));
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
