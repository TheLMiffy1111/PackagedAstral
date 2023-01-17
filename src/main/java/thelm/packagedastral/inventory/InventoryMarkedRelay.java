package thelm.packagedastral.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import thelm.packagedastral.tile.TileMarkedRelay;
import thelm.packagedauto.inventory.InventoryTileBase;

public class InventoryMarkedRelay extends InventoryTileBase {

	public final TileMarkedRelay tile;

	public InventoryMarkedRelay(TileMarkedRelay tile) {
		super(tile, 1);
		this.tile = tile;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack ret = super.decrStackSize(index, count);
		syncTile(false);
		return ret;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack ret = super.removeStackFromSlot(index);
		syncTile(false);
		return ret;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		super.setInventorySlotContents(index, stack);
		syncTile(false);
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return false;
	}
}
