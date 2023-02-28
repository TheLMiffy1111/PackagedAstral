package thelm.packagedastral.slot;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import thelm.packagedastral.tile.ConstellationCrafterTile;

//Code from CoFHCore
public class ConstellationCrafterRemoveOnlySlot extends SlotItemHandler {

	public final ConstellationCrafterTile tile;

	public ConstellationCrafterRemoveOnlySlot(ConstellationCrafterTile tile, int index, int x, int y) {
		super(tile.getItemHandler(), index, x, y);
		this.tile = tile;
	}

	@Override
	public boolean mayPickup(PlayerEntity player) {
		return !tile.isWorking;
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return false;
	}
}