package thelm.packagedastral.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import thelm.packagedastral.tile.TileTraitCrafter;
import thelm.packagedauto.slot.SlotBase;

//Code from CoFHCore
public class SlotTraitCrafterRemoveOnly extends SlotBase {

	public final TileTraitCrafter tile;

	public SlotTraitCrafterRemoveOnly(TileTraitCrafter tile, int index, int x, int y) {
		super(tile.getInventory(), index, x, y);
		this.tile = tile;
	}

	@Override
	public boolean canTakeStack(EntityPlayer playerIn) {
		return !tile.isWorking;
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		return false;
	}
}
