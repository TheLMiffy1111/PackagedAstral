package thelm.packagedastral.tile;

import java.awt.Color;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

import com.google.common.collect.Streams;

import hellfirepvp.astralsorcery.client.effect.EffectHelper;
import hellfirepvp.astralsorcery.client.effect.fx.EntityFXFacingParticle;
import hellfirepvp.astralsorcery.common.constellation.distribution.ConstellationSkyHandler;
import hellfirepvp.astralsorcery.common.constellation.distribution.WorldSkyHandler;
import hellfirepvp.astralsorcery.common.structure.array.PatternBlockArray;
import hellfirepvp.astralsorcery.common.structure.change.ChangeSubscriber;
import hellfirepvp.astralsorcery.common.structure.match.StructureMatcherPatternArray;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.util.PatternMatchHelper;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.util.log.LogCategory;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thelm.packagedastral.inventory.InventoryMarkedRelay;
import thelm.packagedastral.structure.StructureMarkedRelay;
import thelm.packagedauto.tile.TileBase;

public class TileMarkedRelay extends TileBase implements ITickable {

	public static final Random RANDOM = new Random();
	
	public boolean doesSeeSky = false;
	public ChangeSubscriber<StructureMatcherPatternArray> structureMatch = null;
	public boolean structureValid = false;
	public BlockPos altarPos = null;

	public TileMarkedRelay() {
		setInventory(new InventoryMarkedRelay(this));
	}

	@Override
	protected String getLocalizedName() {
		return I18n.translateToLocal("tile.packagedastral.marked_relay.name");
	}

	@Override
	public void update() {
		if(world.getTotalWorldTime() % 16 == 0) {
			doesSeeSky = MiscUtils.canSeeSky(world, pos.up(), true, doesSeeSky);
		}
		if(!world.isRemote) {
			matchStructure();
			if(altarPos != null && world.isBlockLoaded(altarPos)) {
				TileEntity tile = world.getTileEntity(altarPos);
				if(tile instanceof IAltarCrafter) {
					provideStarlight((IAltarCrafter)tile);
				}
				else {
					updateAltarPos();
				}
			}
		}
		else {
			clientTick();
		}
	}

	@SideOnly(Side.CLIENT)
	public void clientTick() {
		if(structureValid) {
			if(RANDOM.nextInt(3) == 0) {
				Vector3 at = new Vector3(pos);
				at.add(RANDOM.nextFloat()*2.6-0.8, 0, RANDOM.nextFloat()*2.6-0.8);
				EntityFXFacingParticle p = EffectHelper.genericFlareParticle(at);
				p.setAlphaMultiplier(0.7F);
				p.setMaxAge((int)(30+RANDOM.nextFloat()*50));
				p.gravity(0.01).scale(0.3F+RANDOM.nextFloat()*0.1F);
				if(RANDOM.nextBoolean()) {
					p.setColor(Color.WHITE);
				}
			}
			if(altarPos != null && doesSeeSky && RANDOM.nextInt(4) == 0) {
				Vector3 at = new Vector3(pos);
				Vector3 dir = new Vector3(altarPos).subtract(at).normalize().multiply(0.05);
				at.add(RANDOM.nextFloat()*0.4+0.3, RANDOM.nextFloat()*0.3+0.1, RANDOM.nextFloat()*0.4+0.3);
				EntityFXFacingParticle p = EffectHelper.genericFlareParticle(at);
				p.setAlphaMultiplier(0.7F);
				p.motion(dir.getX(), dir.getY(), dir.getZ());
				p.setMaxAge((int)(15+RANDOM.nextFloat()*30));
				p.gravity(0.015).scale(0.2F+RANDOM.nextFloat()*0.04F);
				if(RANDOM.nextBoolean()) {
					p.setColor(Color.WHITE);
				}
			}
		}
	}

	protected void provideStarlight(IAltarCrafter altar) {
		if(doesSeeSky) {
			WorldSkyHandler handle = ConstellationSkyHandler.getInstance().getWorldHandler(world);
			int yLevel = pos.getY();
			if(handle != null && yLevel > 40) {
				float dstr;
				double coll = 0.3;
				if(yLevel > 120) {
					dstr = 1;
				}
				else {
					dstr = (yLevel-40)/80F;
				}
				coll *= dstr;
				coll *= 0.2+0.8*ConstellationSkyHandler.getInstance().getCurrentDaytimeDistribution(world);
				altar.receiveStarlight(null, coll);
			}
		}
	}

	protected void matchStructure() {
		PatternBlockArray structure = StructureMarkedRelay.INSTANCE;
		if(structureMatch == null) {
			structureMatch = PatternMatchHelper.getOrCreateMatcher(world, pos, structure);
		}
		boolean matches = getInventory().getStackInSlot(0).isEmpty() && structureMatch.matches(world);
		if(matches != structureValid) {
			LogCategory.STRUCTURE_MATCH.info(()->"Structure match updated: "+getClass().getName()+" at "+pos+" ("+structureValid+" -> "+matches+")");
			structureValid = matches;
			updateAltarPos();
			syncTile(false);
			markDirty();
		}
	}

	protected void updateAltarPos() {
		if(world.isRemote) {
			return;
		}
		altarPos = null;
		if(structureValid) {
			List<BlockPos> altars = getNearbyAltars(world, pos);
			BlockPos nearestAltar = null;
			double nearestDist = Double.POSITIVE_INFINITY;
			for(BlockPos altar : altars) {
				double dist = altar.distanceSq(pos);
				if(nearestAltar == null || dist < nearestDist) {
					nearestAltar = altar;
					nearestDist = dist;
				}
			}
			altarPos = nearestAltar;
		}
		syncTile(false);
		markDirty();
	}

	public static void updateNearbyAltarPos(World world, BlockPos pos) {
		getNearbyRelays(world, pos).forEach(TileMarkedRelay::updateAltarPos);
	}

	public static List<TileMarkedRelay> getNearbyRelays(World world, BlockPos pos) {
		return Streams.stream(BlockPos.getAllInBoxMutable(pos.add(-16, -16, -16), pos.add(16, 16, 16))).map(checkPos->{
			if(!checkPos.equals(pos) && world.isBlockLoaded(checkPos)) {
				TileEntity tile = world.getTileEntity(checkPos);
				if(tile instanceof TileMarkedRelay) {
					return (TileMarkedRelay)tile;
				}
			}
			return null;
		}).filter(Objects::nonNull).collect(Collectors.toList());
	}

	public static List<BlockPos> getNearbyAltars(World world, BlockPos pos) {
		return Streams.stream(BlockPos.getAllInBoxMutable(pos.add(-16, -16, -16), pos.add(16, 16, 16))).map(checkPos->{
			if(!checkPos.equals(pos) && world.isBlockLoaded(checkPos) && world.getTileEntity(checkPos) instanceof IAltarCrafter) {
				return checkPos.toImmutable();
			}
			return null;
		}).filter(Objects::nonNull).collect(Collectors.toList());
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
	public int getComparatorSignal() {
		return inventory.getStackInSlot(0).isEmpty() ? 0 : 15;
	}

	@Override
	public void readSyncNBT(NBTTagCompound nbt) {
		super.readSyncNBT(nbt);
		inventory.readFromNBT(nbt);
		structureValid = nbt.getBoolean("MultiblockValid");
		altarPos = null;
		if(nbt.hasKey("AltarPos")) {
			int[] posArray = nbt.getIntArray("AltarPos");
			altarPos = new BlockPos(posArray[0], posArray[1], posArray[2]);
		}
	}

	@Override
	public NBTTagCompound writeSyncNBT(NBTTagCompound nbt) {
		super.writeSyncNBT(nbt);
		inventory.writeToNBT(nbt);
		nbt.setBoolean("MultiblockValid", structureValid);
		if(altarPos != null) {
			nbt.setIntArray("AltarPos", new int[] {altarPos.getX(), altarPos.getY(), altarPos.getZ()});
		}
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
