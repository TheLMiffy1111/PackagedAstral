package thelm.packagedastral.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.IItemHandlerModifiable;
import thelm.packagedastral.tile.DiscoveryCrafterTile;
import thelm.packagedauto.inventory.BaseItemHandler;

public class DiscoveryCrafterItemHandler extends BaseItemHandler<DiscoveryCrafterTile> {

	public DiscoveryCrafterItemHandler(DiscoveryCrafterTile tile) {
		super(tile, 11);
	}

	@Override
	public boolean isItemValid(int index, ItemStack stack) {
		if(index == 10) {
			return stack.getCapability(CapabilityEnergy.ENERGY, null).isPresent();
		}
		return false;
	}

	@Override
	public IItemHandlerModifiable getWrapperForDirection(Direction side) {
		return wrapperMap.computeIfAbsent(side, s->new DiscoveryCrafterItemHandlerWrapper(this, s));
	}

	@Override
	public int get(int id) {
		switch(id) {
		case 0: return tile.starlight;
		case 1: return tile.progress;
		case 2: return tile.progressReq;
		case 3: return tile.remainingProgress;
		case 4: return tile.isWorking ? 1 : 0;
		default: return 0;
		}
	}

	@Override
	public void set(int id, int value) {
		switch(id) {
		case 0:
			tile.starlight = value;
			break;
		case 1:
			tile.progress = value;
			break;
		case 2:
			tile.progressReq = value;
			break;
		case 3:
			tile.remainingProgress = value;
			break;
		case 4:
			tile.isWorking = value != 0;
			break;
		}
	}

	@Override
	public int size() {
		return 5;
	}
}
