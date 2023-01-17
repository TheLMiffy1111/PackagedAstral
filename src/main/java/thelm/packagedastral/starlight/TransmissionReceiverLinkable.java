package thelm.packagedastral.starlight;

import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.starlight.transmission.IPrismTransmissionNode;
import hellfirepvp.astralsorcery.common.starlight.transmission.base.SimpleTransmissionReceiver;
import hellfirepvp.astralsorcery.common.starlight.transmission.registry.TransmissionClassRegistry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TransmissionReceiverLinkable extends SimpleTransmissionReceiver {

	public TransmissionReceiverLinkable(BlockPos pos) {
		super(pos);
	}

	@Override
	public void onStarlightReceive(World world, boolean isChunkLoaded, IWeakConstellation type, double amount) {
		if(isChunkLoaded) {
			TileEntity tile = world.getTileEntity(getLocationPos());
			if(tile instanceof IStarlightReceiverLinkableTile) {
				((IStarlightReceiverLinkableTile)tile).receiveStarlight(type, amount);
			}
		}
	}

	@Override
	public TransmissionClassRegistry.TransmissionProvider getProvider() {
		return new Provider();
	}

	public static class Provider implements TransmissionClassRegistry.TransmissionProvider {

		@Override
		public IPrismTransmissionNode provideEmptyNode() {
			return new TransmissionReceiverLinkable(null);
		}

		@Override
		public String getIdentifier() {
			return "packagedastral:transmission_receiver_linkable";
		}
	}
}
