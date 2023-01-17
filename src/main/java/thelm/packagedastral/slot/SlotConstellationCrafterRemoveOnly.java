package thelm.packagedastral.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import thelm.packagedastral.tile.TileConstellationCrafter;
import thelm.packagedauto.slot.SlotBase;

//Code from CoFHCore
public class SlotConstellationCrafterRemoveOnly extends SlotBase {

	public final TileConstellationCrafter tile;

	public SlotConstellationCrafterRemoveOnly(TileConstellationCrafter tile, int index, int x, int y) {
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
