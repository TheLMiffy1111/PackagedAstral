package thelm.packagedastral.tile;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXMotionController;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.common.constellation.world.DayTimeHelper;
import hellfirepvp.astralsorcery.common.structure.types.StructureType;
import hellfirepvp.astralsorcery.common.tile.altar.AltarCollectionCategory;
import hellfirepvp.astralsorcery.common.tile.base.TileRequiresMultiblock;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.util.log.LogCategory;
import hellfirepvp.observerlib.api.ChangeSubscriber;
import hellfirepvp.observerlib.common.change.ChangeObserverStructure;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import thelm.packagedastral.block.MarkedRelayBlock;
import thelm.packagedastral.integration.appeng.tile.AEMarkedRelayTile;
import thelm.packagedastral.inventory.MarkedRelayItemHandler;
import thelm.packagedastral.structure.MarkedRelayPatternStructure;
import thelm.packagedauto.tile.BaseTile;
import thelm.packagedauto.util.MiscHelper;

public class MarkedRelayTile extends BaseTile implements ITickableTileEntity, TileRequiresMultiblock {

	public static final TileEntityType<MarkedRelayTile> TYPE_INSTANCE = (TileEntityType<MarkedRelayTile>)TileEntityType.Builder.
			of(MiscHelper.INSTANCE.conditionalSupplier(()->ModList.get().isLoaded("appliedenergistics2"),
					()->AEMarkedRelayTile::new, ()->MarkedRelayTile::new), MarkedRelayBlock.INSTANCE).
			build(null).setRegistryName("packagedastral:marked_relay");

	public static final Random RANDOM = new Random();

	public boolean firstTick = true;
	public boolean doesSeeSky = false;
	public ChangeSubscriber<ChangeObserverStructure> structureMatch = null;
	public boolean structureValid = false;
	public BlockPos altarPos = null;
	public double nearestRelayDist = Double.POSITIVE_INFINITY;
	public float proximityMultiplier = 1;

	public MarkedRelayTile() {
		super(TYPE_INSTANCE);
		this.setItemHandler(new MarkedRelayItemHandler(this));
	}

	@Override
	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent("block.packagedastral.marked_relay");
	}

	@Override
	public void tick() {
		if(firstTick) {
			firstTick = false;
			if(!level.isClientSide) {
				updateProximity();
			}
		}
		if(level.getGameTime() % 16 == 0) {
			doesSeeSky = MiscUtils.canSeeSky(level, worldPosition.above(), true, doesSeeSky);
		}
		if(!level.isClientSide) {
			matchStructure();
			if(altarPos != null && level.isLoaded(altarPos)) {
				TileEntity tile = level.getBlockEntity(altarPos);
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

	@OnlyIn(Dist.CLIENT)
	protected void clientTick() {
		if(structureValid) {
			if(RANDOM.nextBoolean()) {
				Vector3 pos = new Vector3(worldPosition).add(0.5, 0, 0.5);
				Vector3 offset = new Vector3(0, 0, 0);
				MiscUtils.applyRandomOffset(offset, RANDOM, 1.25F);
				pos.add(offset.getX(), 0, offset.getZ());
				EntityVisualFX vfx = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).
						spawn(pos).
						alpha(VFXAlphaFunction.FADE_OUT).
						setScaleMultiplier(0.15F+RANDOM.nextFloat()*0.1F).
						setGravityStrength(-0.001F) .
						setMaxAge(30+RANDOM.nextInt(20));
				if(RANDOM.nextBoolean()) {
					vfx.color(VFXColorFunction.WHITE);
				}
			}
			if(altarPos != null && doesSeeSky) {
				Vector3 pos = new Vector3(worldPosition).add(0.5, 0.35, 0.5);
				Vector3 target = new Vector3(altarPos).add(0.5, 0.5, 0.5);
				int maxAge = 30;
				maxAge *= Math.max(pos.distance(target)/3, 1);
				EntityVisualFX vfx = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).
						spawn(pos).
						alpha(VFXAlphaFunction.proximity(target::clone, 2F).andThen(VFXAlphaFunction.FADE_OUT)).
						motion(VFXMotionController.target(target::clone, 0.08F)).
						setMotion(Vector3.random().normalize().multiply(0.1F+RANDOM.nextFloat()*0.05F)).
						setScaleMultiplier(0.15F+RANDOM.nextFloat()*0.05F) .
						setMaxAge(maxAge);
				if(RANDOM.nextBoolean()) {
					vfx.color(VFXColorFunction.WHITE);
				}
			}
		}
	}

	@Override
	public StructureType getRequiredStructureType() {
		return MarkedRelayPatternStructure.TYPE_INSTANCE;
	}

	protected void provideStarlight(IAltarCrafter altar) {
		if(doesSeeSky) {
			float heightAmount = MathHelper.clamp((float)Math.pow(worldPosition.getY()/7F, 1.5F)/60, 0, 1);
			heightAmount = 0.7F+heightAmount*0.3F;
			heightAmount *= DayTimeHelper.getCurrentDaytimeDistribution(level);
			heightAmount *= proximityMultiplier;
			if(heightAmount > 1E-4F) {
				altar.collectStarlight(heightAmount*45, AltarCollectionCategory.RELAY);
			}
		}
	}

	protected void matchStructure() {
		if(structureMatch == null) {
			structureMatch = getRequiredStructureType().observe(level, worldPosition);
		}
		boolean matches = getItemHandler().getStackInSlot(0).isEmpty() && structureMatch.isValid(level);
		if(matches != structureValid) {
			LogCategory.STRUCTURE_MATCH.info(()->"Structure match updated: "+getClass().getName()+" at "+worldPosition+" ("+structureValid+" -> "+matches+")");
			structureValid = matches;
			updateAltarPos();
			if(structureValid) {
				updateProximity();
			}
			else {
				updateNearbyProximity(level, worldPosition);
			}
			syncTile(false);
			setChanged();
		}
	}

	protected void updateProximity() {
		if(level.isClientSide) {
			return;
		}
		setNearestRelayDist(Double.POSITIVE_INFINITY);
		getNearbyRelays(level, worldPosition, 8).forEach(relay->{
			double dist = worldPosition.distSqr(relay.getBlockPos());
			if(dist < relay.nearestRelayDist) {
				relay.setNearestRelayDist(dist);
			}
			if(dist < nearestRelayDist) {
				setNearestRelayDist(dist);
			}
		});
	}

	protected void updateAltarPos() {
		if(level.isClientSide) {
			return;
		}
		altarPos = null;
		if(structureValid) {
			List<BlockPos> altars = getNearbyAltars(level, worldPosition);
			BlockPos nearestAltar = null;
			double nearestDist = Double.POSITIVE_INFINITY;
			for(BlockPos altar : altars) {
				double dist = altar.distSqr(worldPosition);
				if(nearestAltar == null || dist < nearestDist) {
					nearestAltar = altar;
					nearestDist = dist;
				}
			}
			altarPos = nearestAltar;
		}
		syncTile(false);
		setChanged();
	}

	protected void setNearestRelayDist(double dist) {
		nearestRelayDist = dist;
		proximityMultiplier = MathHelper.clamp((float)Math.sqrt(dist)/8, 0, 1);
		setChanged();
	}

	public static void updateNearbyProximity(World world, BlockPos pos) {
		getNearbyRelays(world, pos, 8).forEach(MarkedRelayTile::updateProximity);
	}

	public static void updateNearbyAltarPos(World world, BlockPos pos) {
		getNearbyRelays(world, pos, 16).forEach(MarkedRelayTile::updateAltarPos);
	}

	public static List<MarkedRelayTile> getNearbyRelays(World world, BlockPos pos, int range) {
		return BlockPos.betweenClosedStream(pos.offset(-range, -range, -range), pos.offset(range, range, range)).map(checkPos->{
			if(!checkPos.equals(pos) && checkPos.closerThan(pos, range) && world.isLoaded(checkPos)) {
				TileEntity tile = world.getBlockEntity(checkPos);
				if(tile instanceof MarkedRelayTile) {
					MarkedRelayTile relay = (MarkedRelayTile)tile;
					if(relay.structureValid) {
						return relay;
					}
				}
			}
			return null;
		}).filter(Objects::nonNull).collect(Collectors.toList());
	}

	public static List<BlockPos> getNearbyAltars(World world, BlockPos pos) {
		return BlockPos.betweenClosedStream(pos.offset(-16, -16, -16), pos.offset(16, 16, 16)).map(checkPos->{
			if(!checkPos.equals(pos) && checkPos.closerThan(pos, 16) && world.isLoaded(checkPos) && world.getBlockEntity(checkPos) instanceof IAltarCrafter) {
				return checkPos.immutable();
			}
			return null;
		}).filter(Objects::nonNull).collect(Collectors.toList());
	}

	public void ejectItem() {
		ItemStack stack = itemHandler.getStackInSlot(0);
		itemHandler.setStackInSlot(0, ItemStack.EMPTY);
		if(!stack.isEmpty()) {
			double dx = level.random.nextFloat()/2+0.25;
			double dy = level.random.nextFloat()/2+0.25;
			double dz = level.random.nextFloat()/2+0.25;
			ItemEntity itemEntity = new ItemEntity(level, worldPosition.getX()+dx, worldPosition.getY()+dy, worldPosition.getZ()+dz, stack);
			itemEntity.setDefaultPickUpDelay();
			level.addFreshEntity(itemEntity);
		}
	}

	@Override
	public int getComparatorSignal() {
		return itemHandler.getStackInSlot(0).isEmpty() ? 0 : 15;
	}

	@Override
	public void load(BlockState blockState, CompoundNBT nbt) {
		super.load(blockState, nbt);
		setNearestRelayDist(nbt.getDouble("NearestRelayDist"));
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		super.save(nbt);
		nbt.putDouble("NearestRelayDist", nearestRelayDist);
		return nbt;
	}

	@Override
	public void readSync(CompoundNBT nbt) {
		super.readSync(nbt);
		itemHandler.read(nbt);
		structureValid = nbt.getBoolean("MultiblockValid");
		altarPos = null;
		if(nbt.contains("AltarPos")) {
			int[] posArray = nbt.getIntArray("AltarPos");
			altarPos = new BlockPos(posArray[0], posArray[1], posArray[2]);
		}
	}

	@Override
	public CompoundNBT writeSync(CompoundNBT nbt) {
		super.writeSync(nbt);
		itemHandler.write(nbt);
		nbt.putBoolean("MultiblockValid", structureValid);
		if(altarPos != null) {
			nbt.putIntArray("AltarPos", new int[] {altarPos.getX(), altarPos.getY(), altarPos.getZ()});
		}
		return nbt;
	}

	@Override
	public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player) {
		return null;
	}
}
