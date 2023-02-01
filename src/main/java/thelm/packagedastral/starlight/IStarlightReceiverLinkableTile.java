package thelm.packagedastral.starlight;

import java.util.LinkedList;
import java.util.List;

import hellfirepvp.astralsorcery.common.auxiliary.link.LinkableTileEntity;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.starlight.IStarlightReceiver;
import hellfirepvp.astralsorcery.common.starlight.transmission.ITransmissionReceiver;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IStarlightReceiverLinkableTile extends IStarlightReceiver<ITransmissionReceiver>, LinkableTileEntity {

	void receiveStarlight(IWeakConstellation type, double amount);

	@Override
	default World getTrWorld() {
		return ((TileEntity)this).getWorld();
	}

	@Override
	default BlockPos getTrPos() {
		return ((TileEntity)this).getPos();
	}

	@Override
	default void onBlockLinkCreate(PlayerEntity player, BlockPos other) {

	}

	@Override
	default void onEntityLinkCreate(PlayerEntity player, LivingEntity linked) {
		
	}

	@Override
	default boolean tryLinkBlock(PlayerEntity player, BlockPos other) {
		return false;
	}

	@Override
	default boolean tryLinkEntity(PlayerEntity player, LivingEntity other) {
		return false;
	}

	@Override
	default boolean tryUnlink(PlayerEntity player, BlockPos other) {
		return false;
	}

	@Override
	default List<BlockPos> getLinkedPositions() {
		return new LinkedList<>();
	}

	@Override
	default ITransmissionReceiver provideEndpoint(BlockPos pos) {
		return new TransmissionReceiverLinkableTile(pos);
	}
}
