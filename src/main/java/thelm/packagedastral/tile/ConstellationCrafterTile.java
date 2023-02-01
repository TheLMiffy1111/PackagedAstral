package thelm.packagedastral.tile;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import hellfirepvp.astralsorcery.client.util.sound.PositionedLoopSound;
import hellfirepvp.astralsorcery.common.block.tile.altar.AltarType;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.constellation.SkyHandler;
import hellfirepvp.astralsorcery.common.constellation.world.DayTimeHelper;
import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipe;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.ActiveSimpleAltarRecipe;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.ActiveSimpleAltarRecipe.CraftingState;
import hellfirepvp.astralsorcery.common.entity.EntityFlare;
import hellfirepvp.astralsorcery.common.lib.SoundsAS;
import hellfirepvp.astralsorcery.common.starlight.WorldNetworkHandler;
import hellfirepvp.astralsorcery.common.starlight.network.StarlightUpdateHandler;
import hellfirepvp.astralsorcery.common.starlight.transmission.IPrismTransmissionNode;
import hellfirepvp.astralsorcery.common.structure.types.StructureType;
import hellfirepvp.astralsorcery.common.tile.altar.AltarCollectionCategory;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;
import hellfirepvp.astralsorcery.common.tile.base.TileRequiresMultiblock;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.util.log.LogCategory;
import hellfirepvp.astralsorcery.common.util.sound.SoundHelper;
import hellfirepvp.astralsorcery.common.util.world.SkyCollectionHelper;
import hellfirepvp.observerlib.api.ChangeSubscriber;
import hellfirepvp.observerlib.common.change.ChangeObserverStructure;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import thelm.packagedastral.block.ConstellationCrafterBlock;
import thelm.packagedastral.container.ConstellationCrafterContainer;
import thelm.packagedastral.integration.appeng.tile.AEConstellationCrafterTile;
import thelm.packagedastral.inventory.ConstellationCrafterItemHandler;
import thelm.packagedastral.network.packet.FinishCraftEffectPacket;
import thelm.packagedastral.recipe.IAltarPackageRecipeInfo;
import thelm.packagedastral.starlight.IStarlightReceiverLinkableTile;
import thelm.packagedastral.structure.ConstellationCrafterPatternStructure;
import thelm.packagedauto.api.IPackageCraftingMachine;
import thelm.packagedauto.api.IPackageRecipeInfo;
import thelm.packagedauto.energy.EnergyStorage;
import thelm.packagedauto.tile.BaseTile;
import thelm.packagedauto.tile.UnpackagerTile;
import thelm.packagedauto.util.MiscHelper;

public class ConstellationCrafterTile extends BaseTile implements ITickableTileEntity, IPackageCraftingMachine, IStarlightReceiverLinkableTile, TileRequiresMultiblock, IHasFakeAltar {

	public static final TileEntityType<ConstellationCrafterTile> TYPE_INSTANCE = (TileEntityType<ConstellationCrafterTile>)TileEntityType.Builder.
			create(MiscHelper.INSTANCE.conditionalSupplier(()->ModList.get().isLoaded("appliedenergistics2"),
					()->AEConstellationCrafterTile::new, ()->ConstellationCrafterTile::new), ConstellationCrafterBlock.INSTANCE).
			build(null).setRegistryName("packagedastral:constellation_crafter");

	public static final Random RANDOM = new Random();

	public static int energyCapacity = 5000;
	public static int energyReq = 500;
	public static int energyUsage = 100;
	public static int starlightCapacity = 4000;
	public static boolean requiresStructure = true;
	public static boolean drawMEEnergy = true;

	public boolean isNetworkInformed = false;
	public boolean doesSeeSky = false;
	public ChangeSubscriber<ChangeObserverStructure> structureMatch = null;
	public boolean structureValid = false;
	public float posDistribution = -1F;
	public TileAltar fakeAltar = new TileAltar().updateType(AltarType.CONSTELLATION, true);
	public SimpleAltarRecipe effectRecipe = null;
	public Object clientCraftSound = null;
	public int starlight = 0;
	public Object2FloatMap<AltarCollectionCategory> tickStarlightCollectionMap = new Object2FloatOpenHashMap<>();
	public boolean isWorking = false;
	public int progress = 0;
	public int progressReq = 0;
	public int remainingProgress = 0;
	public int starlightReq = 0;
	public IAltarPackageRecipeInfo currentRecipe;

	public ConstellationCrafterTile() {
		super(TYPE_INSTANCE);
		setItemHandler(new ConstellationCrafterItemHandler(this));
		setEnergyStorage(new EnergyStorage(this, energyCapacity));
	}

	@Override
	protected ITextComponent getDefaultName() {
		return new TranslationTextComponent("block.packagedastral.constellation_crafter");
	}

	@Override
	public void setWorldAndPos(World world, BlockPos pos) {
		super.setWorldAndPos(world, pos);
		fakeAltar.setWorldAndPos(world, pos);
	}

	@Override
	public void setPos(BlockPos pos) {
		super.setPos(pos);
		fakeAltar.setPos(pos);
	}

	@Override
	public void tick() {
		if(!world.isRemote) {
			if(!isNetworkInformed && WorldNetworkHandler.getNetworkHandler(world).getTransmissionNode(pos) == null) {
				WorldNetworkHandler handler = WorldNetworkHandler.getNetworkHandler(world);
				handler.addTransmissionTile(this);
				IPrismTransmissionNode node = handler.getTransmissionNode(pos);
				if(node != null && node.needsUpdate()) {
					StarlightUpdateHandler.getInstance().addNode(world, node);
				}
				isNetworkInformed = true;
			}
			if(isWorking) {
				tickProcess();
				if(remainingProgress <= 0) {
					energyStorage.receiveEnergy(Math.abs(remainingProgress), false);
					finishProcess();
					ejectItems();
				}
			}
			if(world.getGameTime() % 16 == 0) {
				doesSeeSky = MiscUtils.canSeeSky(world, pos.up(), true, doesSeeSky);
			}
			gatherStarlight();
			chargeEnergy();
			matchStructure();
			if(world.getGameTime() % 8 == 0) {
				ejectItems();
			}
			energyStorage.updateIfChanged();
		}
		else {
			clientTick();
		}
	}

	@OnlyIn(Dist.CLIENT)
	protected void clientTick() {
		if(isWorking) {
			if(requiresStructure && effectRecipe != null) {
				effectRecipe.getCraftingEffects().forEach(effect->{
					try {
						effect.onTick(fakeAltar, CraftingState.ACTIVE);
					}
					catch(Exception e) {}
				});
			}
			if(Minecraft.getInstance().gameSettings.getSoundLevel(SoundCategory.BLOCKS) > 0) {
				if(clientCraftSound == null || ((PositionedLoopSound)clientCraftSound).hasStoppedPlaying()) {
					clientCraftSound = SoundHelper.playSoundLoopFadeInClient(
							SoundsAS.ALTAR_CRAFT_LOOP_T3, new Vector3(this).add(0.5, 0.5, 0.5), 0.6F, 1F, false,
							s->isRemoved() || SoundHelper.getSoundVolume(SoundCategory.BLOCKS) <= 0 || !isWorking).
							setFadeInTicks(40).setFadeOutTicks(20);
				}
			}
			else {
				clientCraftSound = null;
			}
		}
	}

	@Override
	public void remove() {
		if(world != null && !world.isRemote) {
			WorldNetworkHandler handler = WorldNetworkHandler.getNetworkHandler(world);
			IPrismTransmissionNode node = handler.getTransmissionNode(pos);
			if(node != null) {
				StarlightUpdateHandler.getInstance().removeNode(world, node);
			}
			handler.removeTransmission(this);
			isNetworkInformed = false;
		}
	}

	@Override
	public boolean acceptPackage(IPackageRecipeInfo recipeInfo, List<ItemStack> stacks, Direction direction) {
		if(!isBusy() && recipeInfo instanceof IAltarPackageRecipeInfo) {
			IAltarPackageRecipeInfo recipe = (IAltarPackageRecipeInfo)recipeInfo;
			if(recipe.getLevel() == 2 && structureValid && starlight >= recipe.getStarlightRequired()) {
				ItemStack slotStack = itemHandler.getStackInSlot(21);
				ItemStack outputStack = recipe.getOutput();
				if(slotStack.isEmpty() || slotStack.getItem() == outputStack.getItem() && ItemStack.areItemStackTagsEqual(slotStack, outputStack) && slotStack.getCount()+outputStack.getCount() <= outputStack.getMaxStackSize()) {
					currentRecipe = recipe;
					effectRecipe = recipe.getRecipe();
					isWorking = true;
					progressReq = recipe.getTimeRequired() / (int)Math.round(2*Math.pow(2, Math.max(0, 2-recipe.getLevelRequired())));
					remainingProgress = energyReq;
					starlightReq = recipe.getStarlightRequired();
					for(int i = 0; i < 21; ++i) {
						itemHandler.setStackInSlot(i, recipe.getMatrix().get(i).copy());
					}
					syncTile(false);
					markDirty();
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isBusy() {
		return isWorking || !itemHandler.getStacks().subList(0, 21).stream().allMatch(ItemStack::isEmpty);
	}

	protected void tickProcess() {
		if(world.getGameTime() % 8 == 0 && !structureValid) {
			endProcess();
		}
		else if(starlight >= starlightReq) {
			progress++;
			if(progress >= progressReq) {
				progress = progressReq;
				int energy = energyStorage.extractEnergy(energyUsage, false);
				remainingProgress -= energy;
			}
		}
	}

	protected void finishProcess() {
		if(currentRecipe == null) {
			endProcess();
			return;
		}
		if(itemHandler.getStackInSlot(21).isEmpty()) {
			itemHandler.setStackInSlot(21, currentRecipe.getOutput());
		}
		else {
			itemHandler.getStackInSlot(21).grow(currentRecipe.getOutput().getCount());
		}
		List<ItemStack> remainingItems = currentRecipe.getRemainingItems();
		for(int i = 0; i < 21; ++i) {
			itemHandler.setStackInSlot(i, remainingItems.get(i));
		}
		FinishCraftEffectPacket.finishCraft(pos, effectRecipe, requiresStructure, world.getDimensionKey(), 32);
		EntityFlare.spawnAmbientFlare(world, pos.add(-3+RANDOM.nextInt(7), 1+RANDOM.nextInt(3), -3+RANDOM.nextInt(7)));
		EntityFlare.spawnAmbientFlare(world, pos.add(-3+RANDOM.nextInt(7), 1+RANDOM.nextInt(3), -3+RANDOM.nextInt(7)));
		endProcess();
	}

	public void endProcess() {
		progressReq = 0;
		progress = 0;
		remainingProgress = 0;
		starlightReq = 0;
		isWorking = false;
		effectRecipe = null;
		currentRecipe = null;
		syncTile(false);
		markDirty();
	}

	protected void ejectItems() {
		int endIndex = isWorking ? 21 : 0;
		for(Direction direction : Direction.values()) {
			TileEntity tile = world.getTileEntity(pos.offset(direction));
			if(tile != null && !(tile instanceof UnpackagerTile) && tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction.getOpposite()).isPresent()) {
				IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction.getOpposite()).resolve().get();
				boolean flag = true;
				for(int i = 21; i >= endIndex; --i) {
					ItemStack stack = this.itemHandler.getStackInSlot(i);
					if(stack.isEmpty()) {
						continue;
					}
					for(int slot = 0; slot < itemHandler.getSlots(); ++slot) {
						ItemStack stackRem = itemHandler.insertItem(slot, stack, false);
						if(stackRem.getCount() < stack.getCount()) {
							stack = stackRem;
							flag = false;
						}
						if(stack.isEmpty()) {
							break;
						}
					}
					this.itemHandler.setStackInSlot(i, stack);
					if(flag) {
						break;
					}
				}
			}
		}
	}

	protected void chargeEnergy() {
		int prevStored = energyStorage.getEnergyStored();
		ItemStack energyStack = itemHandler.getStackInSlot(22);
		if(energyStack.getCapability(CapabilityEnergy.ENERGY, null).isPresent()) {
			int energyRequest = Math.min(energyStorage.getMaxReceive(), energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored());
			energyStorage.receiveEnergy(energyStack.getCapability(CapabilityEnergy.ENERGY, null).resolve().get().extractEnergy(energyRequest, false), false);
			if(energyStack.getCount() <= 0) {
				itemHandler.setStackInSlot(22, ItemStack.EMPTY);
			}
		}
	}

	@Override
	public void receiveStarlight(IWeakConstellation type, double amount) {
		collectStarlight((float)amount*60, AltarCollectionCategory.FOCUSED_NETWORK);
	}

	protected void gatherStarlight() {
		tickStarlightCollectionMap.clear();
		starlight *= 0.9F;
		if(doesSeeSky && SkyHandler.getContext(world) != null) {
			float heightAmount = MathHelper.clamp((float)Math.pow(pos.getY()/7F, 1.5F)/65F, 0F, 1F);
			heightAmount *= DayTimeHelper.getCurrentDaytimeDistribution(getWorld());
			collectStarlight(heightAmount*3*60, AltarCollectionCategory.HEIGHT);
			if(posDistribution == -1) {
				if(world instanceof ISeedReader) {
					posDistribution = SkyCollectionHelper.getSkyNoiseDistribution((ISeedReader)world, pos);
				}
				else {
					posDistribution = 0.3F;
				}
			}
			float fieldAmount = MathHelper.sqrt(posDistribution);
			fieldAmount *= DayTimeHelper.getCurrentDaytimeDistribution(getWorld());
			collectStarlight(fieldAmount*3*65, AltarCollectionCategory.FOSIC_FIELD);
		}
	}

	public float getRemainingCollectionCapacity(AltarCollectionCategory category) {
		return tickStarlightCollectionMap.computeIfAbsent(category, this::getCollectionCap);
	}

	public float getCollectionCap(AltarCollectionCategory category) {
		return starlightCapacity/6F/3;
	}

	public void collectStarlight(float percent, AltarCollectionCategory category) {
		int collectable = MathHelper.floor(Math.min(percent, getRemainingCollectionCapacity(category)));
		starlight = MathHelper.clamp(starlight+collectable, 0, starlightCapacity);
		tickStarlightCollectionMap.computeIfPresent(category, (cat, remaining)->Math.max(remaining-collectable, 0));
		markDirty();
	}

	@Override
	public StructureType getRequiredStructureType() {
		return requiresStructure ? ConstellationCrafterPatternStructure.TYPE_INSTANCE : null;
	}

	protected void matchStructure() {
		if(requiresStructure && structureMatch == null) {
			structureMatch = getRequiredStructureType().observe(world, pos);
		}
		boolean matches = !requiresStructure || structureMatch.isValid(world);
		if(matches != structureValid) {
			LogCategory.STRUCTURE_MATCH.info(()->"Structure match updated: "+getClass().getName()+" at "+pos+" ("+structureValid+" -> "+matches+")");
			structureValid = matches;
			syncTile(false);
			markDirty();
		}
	}

	@Override
	public TileAltar getFakeAltar() {
		return fakeAltar;
	}

	@Override
	public void read(BlockState blockState, CompoundNBT nbt) {
		super.read(blockState, nbt);
		currentRecipe = null;
		if(nbt.contains("Recipe")) {
			CompoundNBT tag = nbt.getCompound("Recipe");
			IPackageRecipeInfo recipe = MiscHelper.INSTANCE.readRecipe(tag);
			if(recipe instanceof IAltarPackageRecipeInfo && ((IAltarPackageRecipeInfo)recipe).getLevel() == 2) {
				currentRecipe = (IAltarPackageRecipeInfo)recipe;
			}
		}
	}

	@Override
	public CompoundNBT write(CompoundNBT nbt) {
		super.write(nbt);
		if(currentRecipe != null) {
			CompoundNBT tag = MiscHelper.INSTANCE.writeRecipe(new CompoundNBT(), currentRecipe);
			nbt.put("Recipe", tag);
		}
		return nbt;
	}

	@Override
	public void readSync(CompoundNBT nbt) {
		super.readSync(nbt);
		starlight = nbt.getInt("Starlight");
		structureValid = nbt.getBoolean("MultiblockValid");
		isWorking = nbt.getBoolean("Working");
		progressReq = nbt.getInt("ProgressReq");
		progress = nbt.getInt("Progress");
		remainingProgress = nbt.getInt("EnergyProgress");
		starlightReq = nbt.getInt("StarlightReq");
		effectRecipe = null;
		if(nbt.contains("EffectRecipe")) {
			IRecipe recipe = MiscHelper.INSTANCE.getRecipeManager().getRecipe(new ResourceLocation(nbt.getString("EffectRecipe"))).orElse(null);
			if(recipe instanceof SimpleAltarRecipe) {
				effectRecipe = (SimpleAltarRecipe)recipe;
			}
		}
		ActiveSimpleAltarRecipe fakeActiveRecipe = effectRecipe == null ? null : new ActiveSimpleAltarRecipe(effectRecipe, 1, UUID.randomUUID());
		try {
			Field activeRecipeField = TileAltar.class.getDeclaredField("activeRecipe");
			activeRecipeField.setAccessible(true);
			activeRecipeField.set(fakeAltar, fakeActiveRecipe);
		}
		catch(Exception e) {};
	}

	@Override
	public CompoundNBT writeSync(CompoundNBT nbt) {
		super.writeSync(nbt);
		nbt.putInt("Starlight", starlight);
		nbt.putBoolean("MultiblockValid", structureValid);
		nbt.putBoolean("Working", isWorking);
		nbt.putInt("ProgressReq", progressReq);
		nbt.putInt("Progress", progress);
		nbt.putInt("EnergyProgress", remainingProgress);
		nbt.putInt("StarlightReq", starlightReq);
		if(effectRecipe != null) {
			nbt.putString("EffectRecipe", effectRecipe.getId().toString());
		}
		return nbt;
	}

	public int getScaledEnergy(int scale) {
		if(energyStorage.getMaxEnergyStored() <= 0) {
			return 0;
		}
		return scale * energyStorage.getEnergyStored() / energyStorage.getMaxEnergyStored();
	}

	public int getScaledStarlight(int scale) {
		if(starlightCapacity <= 0) {
			return 0;
		}
		return scale * starlight / starlightCapacity;
	}

	public int getScaledProgress(int scale) {
		if(progress <= 0 || progressReq <= 0) {
			return 0;
		}
		return scale * progress / progressReq;
	}

	@Override
	public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player) {
		syncTile(false);
		return new ConstellationCrafterContainer(windowId, playerInventory, this);
	}
}
