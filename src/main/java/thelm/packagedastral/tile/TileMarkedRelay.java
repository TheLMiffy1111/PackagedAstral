package thelm.packagedastral.tile;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.translation.I18n;
import thelm.packagedastral.inventory.InventoryMarkedRelay;
import thelm.packagedauto.tile.TileBase;

public class TileMarkedRelay extends TileBase {

	public TileMarkedRelay() {
		setInventory(new InventoryMarkedRelay(this));
	}

	@Override
	protected String getLocalizedName() {
		return I18n.translateToLocal("tile.packagedastral.marked_relay.name");
	}

	public void spawnItem() {
		ItemStack stack = inventory.getStackInSlot(0);
		inventory.setInventorySlotContents(0, ItemStack.EMPTY);
		if(!world.isRemote && !stack.isEmpty()) {
			double dx = world.rand.nextFloat()/2+0.25;
			double dy = world.rand.nextFloat()/2+0.25;
			double dz = world.rand.nextFloat()/2+0.25;
			EntityItem entityitem = new EntityItem(world, pos.getX()+dx, pos.getY()+dy, pos.getZ()+dz, stack);
			entityitem.setDefaultPickupDelay();
			world.spawnEntity(entityitem);
		}
	}

	@Override
	public void readSyncNBT(NBTTagCompound nbt) {
		super.readSyncNBT(nbt);
		inventory.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeSyncNBT(NBTTagCompound nbt) {
		super.writeSyncNBT(nbt);
		inventory.writeToNBT(nbt);
		return nbt;
	}

	@Override
	public GuiContainer getClientGuiElement(EntityPlayer player, Object... args) {
		return null;
	}

	@Override
	public Container getServerGuiElement(EntityPlayer player, Object... args) {
		return null;
	}
}
