package thelm.packagedastral.network;

import java.util.Optional;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import thelm.packagedastral.network.packet.FinishCraftEffectPacket;

public class PacketHandler {

	public static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
			new ResourceLocation("packagedastral", PROTOCOL_VERSION),
			()->PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

	public static void registerPackets() {
		int id = 0;
		INSTANCE.registerMessage(id++, FinishCraftEffectPacket.class,
				FinishCraftEffectPacket::encode, FinishCraftEffectPacket::decode,
				FinishCraftEffectPacket::handle, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
	}
}
