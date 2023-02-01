package thelm.packagedastral.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import thelm.packagedastral.tile.TraitCrafterTile;

//Code from CoFHCore
public class TraitCrafterRemoveOnlySlot extends SlotItemHandler {

	public final TraitCrafterTile tile;

	public TraitCrafterRemoveOnlySlot(TraitCrafterTile tile, int index, int x, int y) {
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
