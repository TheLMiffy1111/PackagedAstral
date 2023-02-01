package thelm.packagedastral.starlight;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.starlight.transmission.IPrismTransmissionNode;
import hellfirepvp.astralsorcery.common.starlight.transmission.ITransmissionReceiver;
import hellfirepvp.astralsorcery.common.starlight.transmission.registry.TransmissionProvider;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TransmissionReceiverLinkableTile implements ITransmissionReceiver {

	private BlockPos pos;
	private Set<BlockPos> sourcesToThis = new HashSet<>();

	public TransmissionReceiverLinkableTile(BlockPos pos) {
		this.pos = pos;
	}

	@Override
	public void onStarlightReceive(World world, IWeakConstellation type, double amount) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof IStarlightReceiverLinkableTile) {
			((IStarlightReceiverLinkableTile)tile).receiveStarlight(type, amount);
		}
	}

	@Override
	public BlockPos getLocationPos() {
		return pos;
	}

	@Override
	public void notifySourceLink(World world, BlockPos source) {
		sourcesToThis.add(source);
	}

	@Override
	public void notifySourceUnlink(World world, BlockPos source) {
		sourcesToThis.remove(source);
	}

	@Override
	public boolean notifyBlockChange(World world, BlockPos changed) {
		return false;
	}

	@Override
	public List<BlockPos> getSources() {
		return new LinkedList<>(sourcesToThis);
	}

	@Override
	public TransmissionProvider getProvider() {
		return new Provider();
	}

	@Override
	public void readFromNBT(CompoundNBT nbt) {
		sourcesToThis.clear();
		pos = NBTHelper.readBlockPosFromNBT(nbt);
		ListNBT list = nbt.getList("Sources", 10);
		for (int i = 0; i < list.size(); i++) {
			sourcesToThis.add(NBTHelper.readBlockPosFromNBT(list.getCompound(i)));
		}
	}

	@Override
	public void writeToNBT(CompoundNBT compound) {
		NBTHelper.writeBlockPosToNBT(pos, compound);
		ListNBT sources = new ListNBT();
		for(BlockPos source : sourcesToThis) {
			CompoundNBT comp = new CompoundNBT();
			NBTHelper.writeBlockPosToNBT(source, comp);
			sources.add(comp);
		}
		compound.put("Sources", sources);
	}

	public static class Provider extends TransmissionProvider {

		@Override
		public IPrismTransmissionNode get() {
			return new TransmissionReceiverLinkableTile(null);
		}

		@Override
		public ResourceLocation getIdentifier() {
			return new ResourceLocation("packagedastral:transmission_receiver_linkable");
		}
	}
}
