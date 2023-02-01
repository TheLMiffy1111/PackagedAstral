package thelm.packagedastral.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import thelm.packagedastral.tile.DiscoveryCrafterTile;

//Code from CoFHCore
public class DiscoveryCrafterRemoveOnlySlot extends SlotItemHandler {

	public final DiscoveryCrafterTile tile;

	public DiscoveryCrafterRemoveOnlySlot(DiscoveryCrafterTile tile, int index, int x, int y) {
		super(tile.getItemHandler(), index, x, y);
		this.tile = tile;
	}

	@Override
	public boolean canTakeStack(PlayerEntity player) {
		return !tile.isWorking;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return false;
	}
}
