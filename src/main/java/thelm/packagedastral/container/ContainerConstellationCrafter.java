package thelm.packagedastral.container;

import net.minecraft.entity.player.InventoryPlayer;
import thelm.packagedastral.slot.SlotConstellationCrafterRemoveOnly;
import thelm.packagedastral.tile.TileConstellationCrafter;
import thelm.packagedauto.container.ContainerTileBase;
import thelm.packagedauto.slot.SlotBase;
import thelm.packagedauto.slot.SlotRemoveOnly;

public class ContainerConstellationCrafter extends ContainerTileBase<TileConstellationCrafter> {

	public ContainerConstellationCrafter(InventoryPlayer playerInventory, TileConstellationCrafter tile) {
		super(playerInventory, tile);
		addSlotToContainer(new SlotBase(inventory, 22, 8, 71));
		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 3; ++j) {
				addSlotToContainer(new SlotConstellationCrafterRemoveOnly(tile, i*3+j, 62+j*18, 35+i*18));
			}
		}
		addSlotToContainer(new SlotConstellationCrafterRemoveOnly(tile, 9, 44, 17));
		addSlotToContainer(new SlotConstellationCrafterRemoveOnly(tile, 10, 116, 17));
		addSlotToContainer(new SlotConstellationCrafterRemoveOnly(tile, 11, 44, 89));
		addSlotToContainer(new SlotConstellationCrafterRemoveOnly(tile, 12, 116, 89));
		addSlotToContainer(new SlotConstellationCrafterRemoveOnly(tile, 13, 62, 17));
		addSlotToContainer(new SlotConstellationCrafterRemoveOnly(tile, 14, 98, 17));
		addSlotToContainer(new SlotConstellationCrafterRemoveOnly(tile, 15, 44, 35));
		addSlotToContainer(new SlotConstellationCrafterRemoveOnly(tile, 16, 116, 35));
		addSlotToContainer(new SlotConstellationCrafterRemoveOnly(tile, 17, 44, 71));
		addSlotToContainer(new SlotConstellationCrafterRemoveOnly(tile, 18, 116, 71));
		addSlotToContainer(new SlotConstellationCrafterRemoveOnly(tile, 19, 62, 89));
		addSlotToContainer(new SlotConstellationCrafterRemoveOnly(tile, 20, 98, 89));
		addSlotToContainer(new SlotRemoveOnly(inventory, 21, 170, 53));
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
