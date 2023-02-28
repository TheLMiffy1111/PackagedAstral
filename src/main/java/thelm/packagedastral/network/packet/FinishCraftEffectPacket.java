package thelm.packagedastral.network.packet;

import java.util.function.Supplier;

import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipe;
import hellfirepvp.astralsorcery.common.lib.SoundsAS;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;
import hellfirepvp.astralsorcery.common.util.sound.SoundHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.PacketDistributor.TargetPoint;
import thelm.packagedastral.network.PacketHandler;
import thelm.packagedastral.tile.IHasFakeAltar;
import thelm.packagedauto.util.MiscHelper;

public class FinishCraftEffectPacket {

	private BlockPos pos;
	private SimpleAltarRecipe recipe;
	private boolean doEffect;

	public FinishCraftEffectPacket(BlockPos pos, SimpleAltarRecipe recipe, boolean doEffect) {
		this.pos = pos;
		this.recipe = recipe;
		this.doEffect = doEffect;
	}

	public FinishCraftEffectPacket(BlockPos pos, ResourceLocation recipe, boolean doEffect) {
		this.pos = pos;
		this.recipe = MiscHelper.INSTANCE.getRecipeManager().byKey(recipe).map(r->(SimpleAltarRecipe)r).get();
		this.doEffect = doEffect;
	}

	public static void encode(FinishCraftEffectPacket pkt, PacketBuffer buf) {
		buf.writeBlockPos(pkt.pos);
		buf.writeResourceLocation(pkt.recipe.getId());
		buf.writeBoolean(pkt.doEffect);
	}

	public static FinishCraftEffectPacket decode(PacketBuffer buf) {
		return new FinishCraftEffectPacket(buf.readBlockPos(), buf.readResourceLocation(), buf.readBoolean());
	}

	public static void handle(FinishCraftEffectPacket pkt, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(()->{
			ClientWorld world = Minecraft.getInstance().level;
			if(world.isLoaded(pkt.pos)) {
				TileEntity te = world.getBlockEntity(pkt.pos);
				if(te instanceof IHasFakeAltar) {
					TileAltar fakeAltar = ((IHasFakeAltar)te).getFakeAltar();
					pkt.recipe.getCraftingEffects().forEach(effect->{
						try {
							effect.onCraftingFinish(fakeAltar, false);
						}
						catch(Exception e) {}
					});
				}
				SoundHelper.playSoundClientWorld(SoundsAS.ALTAR_CRAFT_FINISH, pkt.pos, 0.6F, 1F);
			}
		});
		ctx.get().setPacketHandled(true);
	}

	public static void finishCraft(BlockPos pos, SimpleAltarRecipe recipe,  boolean doEffect, RegistryKey<World> dimension, double range) {
		PacketHandler.INSTANCE.send(PacketDistributor.NEAR.with(()->new TargetPoint(pos.getX()+0.5D, pos.getY()+0.5D, pos.getZ()+0.5D, range, dimension)), new FinishCraftEffectPacket(pos, recipe, doEffect));
	}
}
