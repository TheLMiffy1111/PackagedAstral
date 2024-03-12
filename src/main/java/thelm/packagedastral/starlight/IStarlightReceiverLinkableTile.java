package thelm.packagedastral.starlight;

import java.util.LinkedList;
import java.util.List;

import hellfirepvp.astralsorcery.common.auxiliary.link.ILinkableTile;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.starlight.IStarlightReceiver;
import hellfirepvp.astralsorcery.common.starlight.transmission.ITransmissionReceiver;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IStarlightReceiverLinkableTile extends IStarlightReceiver, ILinkableTile {

	void receiveStarlight(IWeakConstellation type, double amount);

	@Override
	default World getLinkWorld() {
		return ((TileEntity)this).getWorld();
	}

	@Override
	default BlockPos getLinkPos() {
		return ((TileEntity)this).getPos();
	}

	@Override
	default World getTrWorld() {
		return ((TileEntity)this).getWorld();
	}

	@Override
	default BlockPos getTrPos() {
		return ((TileEntity)this).getPos();
	}

	@Override
	default void onLinkCreate(EntityPlayer player, BlockPos other) {}

	@Override
	default boolean tryLink(EntityPlayer player, BlockPos other) {
		return false;
	}

	@Override
	default boolean tryUnlink(EntityPlayer player, BlockPos other) {
		return false;
	}

	@Override
	default List<BlockPos> getLinkedPositions() {
		return new LinkedList<>();
	}

	@Override
	default ITransmissionReceiver provideEndpoint(BlockPos pos) {
		return new TransmissionReceiverLinkable(pos);
	}
}
