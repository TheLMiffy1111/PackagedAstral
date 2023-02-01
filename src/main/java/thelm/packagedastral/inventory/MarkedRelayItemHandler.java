package thelm.packagedastral.inventory;

import net.minecraft.item.ItemStack;
import thelm.packagedastral.tile.MarkedRelayTile;
import thelm.packagedauto.inventory.BaseItemHandler;

public class MarkedRelayItemHandler extends BaseItemHandler<MarkedRelayTile> {

	public MarkedRelayItemHandler(MarkedRelayTile tile) {
		super(tile, 1);
	}

	@Override
	protected void onContentsChanged(int slot) {
		syncTile(false);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		return stack;
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		return ItemStack.EMPTY;
	}
}
