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
		case 1: return tile.starlightReq;
		case 2: return tile.progress;
		case 3: return tile.progressReq;
		case 4: return tile.remainingProgress;
		case 5: return tile.isWorking ? 1 : 0;
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
			tile.starlightReq = value;
			break;
		case 2:
			tile.progress = value;
			break;
		case 3:
			tile.progressReq = value;
			break;
		case 4:
			tile.remainingProgress = value;
			break;
		case 5:
			tile.isWorking = value != 0;
			break;
		}
	}

	@Override
	public int getCount() {
		return 6;
	}
}
