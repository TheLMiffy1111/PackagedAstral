package thelm.packagedastral.tile;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import thelm.packagedastral.block.MarkedRelayBlock;
import thelm.packagedastral.inventory.MarkedRelayItemHandler;
import thelm.packagedauto.tile.BaseTile;

public class MarkedRelayTile extends BaseTile {

	public static final TileEntityType<MarkedRelayTile> TYPE_INSTANCE = (TileEntityType<MarkedRelayTile>)TileEntityType.Builder.
			create(MarkedRelayTile::new, MarkedRelayBlock.INSTANCE).
			build(null).setRegistryName("packagedastral:marked_pedestal");

	public MarkedRelayTile() {
		super(TYPE_INSTANCE);
		this.setItemHandler(new MarkedRelayItemHandler(this));
	}

	@Override
	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent("block.packagedastral.marked_relay");
	}

	public void spawnItem() {
		ItemStack stack = itemHandler.getStackInSlot(0);
		itemHandler.setStackInSlot(0, ItemStack.EMPTY);
		if(!world.isRemote && !stack.isEmpty()) {
			double dx = world.rand.nextFloat()/2+0.25;
			double dy = world.rand.nextFloat()/2+0.25;
			double dz = world.rand.nextFloat()/2+0.25;
			ItemEntity itemEntity = new ItemEntity(world, pos.getX()+dx, pos.getY()+dy, pos.getZ()+dz, stack);
			itemEntity.setDefaultPickupDelay();
			world.addEntity(itemEntity);
		}
	}

	@Override
	public void readSync(CompoundNBT nbt) {
		super.readSync(nbt);
		itemHandler.read(nbt);
	}

	@Override
	public CompoundNBT writeSync(CompoundNBT nbt) {
		super.writeSync(nbt);
		itemHandler.write(nbt);
		return nbt;
	}

	@Override
	public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player) {
		return null;
	}
}
