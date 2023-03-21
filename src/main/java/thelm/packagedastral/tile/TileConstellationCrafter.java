package thelm.packagedastral.tile;

import java.util.List;
import java.util.Random;

import appeng.api.networking.IGridHost;
import appeng.api.networking.IGridNode;
import appeng.api.networking.security.IActionHost;
import appeng.api.util.AECableType;
import appeng.api.util.AEPartLocation;
import hellfirepvp.astralsorcery.client.util.PositionedLoopSound;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.constellation.distribution.ConstellationSkyHandler;
import hellfirepvp.astralsorcery.common.crafting.altar.AbstractAltarRecipe;
import hellfirepvp.astralsorcery.common.crafting.altar.ActiveCraftingTask;
import hellfirepvp.astralsorcery.common.crafting.altar.AltarRecipeRegistry;
import hellfirepvp.astralsorcery.common.entities.EntityFlare;
import hellfirepvp.astralsorcery.common.lib.Sounds;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.network.packet.server.PktParticleEvent;
import hellfirepvp.astralsorcery.common.starlight.WorldNetworkHandler;
import hellfirepvp.astralsorcery.common.starlight.network.StarlightUpdateHandler;
import hellfirepvp.astralsorcery.common.starlight.transmission.IPrismTransmissionNode;
import hellfirepvp.astralsorcery.common.structure.array.PatternBlockArray;
import hellfirepvp.astralsorcery.common.structure.change.ChangeSubscriber;
import hellfirepvp.astralsorcery.common.structure.match.StructureMatcherPatternArray;
import hellfirepvp.astralsorcery.common.tile.TileAltar;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.util.PatternMatchHelper;
import hellfirepvp.astralsorcery.common.util.SkyCollectionHelper;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.util.log.LogCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import thelm.packagedastral.client.gui.GuiConstellationCrafter;
import thelm.packagedastral.container.ContainerConstellationCrafter;
import thelm.packagedastral.integration.appeng.networking.HostHelperTileConstellationCrafter;
import thelm.packagedastral.inventory.InventoryConstellationCrafter;
import thelm.packagedastral.recipe.IRecipeInfoAltar;
import thelm.packagedastral.starlight.IStarlightReceiverLinkableTile;
import thelm.packagedastral.structure.StructureConstellationCrafter;
import thelm.packagedauto.api.IPackageCraftingMachine;
import thelm.packagedauto.api.IRecipeInfo;
import thelm.packagedauto.api.MiscUtil;
import thelm.packagedauto.energy.EnergyStorage;
import thelm.packagedauto.tile.TileBase;
import thelm.packagedauto.tile.TileUnpackager;

@Optional.InterfaceList({
	@Optional.Interface(iface="appeng.api.networking.IGridHost", modid="appliedenergistics2"),
	@Optional.Interface(iface="appeng.api.networking.security.IActionHost", modid="appliedenergistics2"),
})
public class TileConstellationCrafter extends TileBase implements ITickable, IPackageCraftingMachine, IStarlightReceiverLinkableTile, IGridHost, IActionHost {

	public static final Random RANDOM = new Random();

	public static boolean enabled = true;

	public static int energyCapacity = 5000;
	public static int energyReq = 500;
	public static int energyUsage = 100;
	public static int starlightCapacity = 4000;
	public static boolean requiresStructure = true;
	public static boolean requiresNight = true;
	public static boolean drawMEEnergy = true;

	public boolean isNetworkInformed = false;
	public boolean doesSeeSky = false;
	public ChangeSubscriber<StructureMatcherPatternArray> structureMatch = null;
	public boolean structureValid = false;
	public float posDistribution = -1F;
	public TileAltar fakeAltar = new TileAltar(TileAltar.AltarLevel.CONSTELLATION_CRAFT);
	public AbstractAltarRecipe effectRecipe = null;
	public Object clientCraftSound = null;
	public int starlight = 0;
	public boolean isWorking = false;
	public int progress = 0;
	public int progressReq = 0;
	public int remainingProgress = 0;
	public int starlightReq = 0;
	public IRecipeInfoAltar currentRecipe;

	public TileConstellationCrafter() {
		setInventory(new InventoryConstellationCrafter(this));
		setEnergyStorage(new EnergyStorage(this, energyCapacity));
		if(Loader.isModLoaded("appliedenergistics2")) {
			hostHelper = new HostHelperTileConstellationCrafter(this);
		}
	}

	@Override
	public String getUnLocalizedDisplayName() {
		return "tile.packagedastral.constellation_crafter.name";
	}

	@Override
	protected String getLocalizedName() {
		return I18n.translateToLocal("tile.packagedastral.constellation_crafter.name");
	}

	@Override
	public void setWorld(World world) {
		super.setWorld(world);
		fakeAltar.setWorld(world);
	}

	@Override
	public void setPos(BlockPos pos) {
		super.setPos(pos);
		fakeAltar.setPos(pos);
	}

	@Override
	public void update() {
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
					finishProcess();
					if(hostHelper != null && hostHelper.isActive()) {
						hostHelper.ejectItem();
					}
					else {
						ejectItems();
					}
				}
			}
			if(world.getTotalWorldTime() % 16 == 0) {
				doesSeeSky = MiscUtils.canSeeSky(world, pos.up(), true, doesSeeSky);
			}
			starlightPassive();
			chargeEnergy();
			matchStructure();
			if(world.getTotalWorldTime() % 8 == 0) {
				if(hostHelper != null && hostHelper.isActive()) {
					hostHelper.ejectItem();
					if(drawMEEnergy) {
						hostHelper.chargeEnergy();
					}
				}
				else {
					ejectItems();
				}
			}
			energyStorage.updateIfChanged();
		}
		else {
			clientTick();
		}
	}

	@SideOnly(Side.CLIENT)
	protected void clientTick() {
		if(isWorking) {
			if(requiresStructure && effectRecipe != null) {
				try {
					effectRecipe.onCraftClientTick(fakeAltar, ActiveCraftingTask.CraftingState.ACTIVE, world.getTotalWorldTime(), RANDOM);
				}
				catch(Exception e) {}
			}
			if(Minecraft.getMinecraft().gameSettings.getSoundLevel(SoundCategory.BLOCKS) > 0) {
				if(clientCraftSound == null || ((PositionedLoopSound)clientCraftSound).hasStoppedPlaying()) {
					PositionedLoopSound posSound = new PositionedLoopSound(Sounds.attunement, SoundCategory.BLOCKS, 0.25F, 1F, new Vector3(pos));
					posSound.setRefreshFunction(()->isInvalid() || Minecraft.getMinecraft().gameSettings.getSoundLevel(SoundCategory.MASTER) <= 0 || !isWorking);
					Minecraft.getMinecraft().getSoundHandler().playSound(posSound);
					clientCraftSound = posSound;
				}
			}
			else {
				clientCraftSound = null;
			}
		}
	}

	@Override
	public boolean acceptPackage(IRecipeInfo recipeInfo, List<ItemStack> stacks, EnumFacing facing) {
		if(!isBusy() && recipeInfo instanceof IRecipeInfoAltar) {
			IRecipeInfoAltar recipe = (IRecipeInfoAltar)recipeInfo;
			if(recipe.getLevel() == 2 && structureValid && starlight >= recipe.getStarlightRequired() && (!requiresNight || !recipe.requiresNight() || ConstellationSkyHandler.getInstance().isNight(world))) {
				ItemStack slotStack = inventory.getStackInSlot(21);
				ItemStack outputStack = recipe.getOutput();
				if(slotStack.isEmpty() || slotStack.getItem() == outputStack.getItem() && slotStack.getItemDamage() == outputStack.getItemDamage() && ItemStack.areItemStackShareTagsEqual(slotStack, outputStack) && slotStack.getCount()+outputStack.getCount() <= outputStack.getMaxStackSize()) {
					currentRecipe = recipe;
					effectRecipe = recipe.getRecipe();
					isWorking = true;
					progressReq = recipe.getTimeRequired() / (int)Math.round(2*Math.pow(2, Math.max(0, 2-recipe.getLevelRequired())));
					remainingProgress = energyReq;
					starlightReq = recipe.getStarlightRequired();
					for(int i = 0; i < 21; ++i) {
						inventory.setInventorySlotContents(i, recipe.getMatrix().get(i).copy());
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
		return isWorking || !inventory.stacks.subList(0, 21).stream().allMatch(ItemStack::isEmpty);
	}

	protected void tickProcess() {
		if(world.getTotalWorldTime() % 8 == 0 && !structureValid) {
			endProcess();
		}
		else if(starlight >= currentRecipe.getStarlightRequired()) {
			progress++;
			if(progress >= progressReq) {
				progress = progressReq;
				int energy = energyStorage.extractEnergy(Math.min(energyUsage, remainingProgress), false);
				remainingProgress -= energy;
			}
		}
	}

	protected void finishProcess() {
		if(currentRecipe == null) {
			endProcess();
			return;
		}
		starlight -= starlightReq;
		if(inventory.getStackInSlot(21).isEmpty()) {
			inventory.setInventorySlotContents(21, currentRecipe.getOutput());
		}
		else {
			inventory.getStackInSlot(21).grow(currentRecipe.getOutput().getCount());
		}
		for(int i = 0; i < 21; ++i) {
			inventory.setInventorySlotContents(i, MiscUtil.getContainerItem(inventory.getStackInSlot(i)));
		}
		if(requiresStructure) {
			PktParticleEvent ev = new PktParticleEvent(PktParticleEvent.ParticleEventType.CRAFT_FINISH_BURST, new Vector3(pos).add(0.5, 0.05, 0.5));
			PacketChannel.CHANNEL.sendToAllAround(ev, PacketChannel.pointFromPos(world, pos, 32));
		}
		world.playSound(null, pos, Sounds.craftFinish, SoundCategory.BLOCKS, 1F, 1.7F);
		EntityFlare.spawnAmbient(world, new Vector3(pos).add(-3+RANDOM.nextFloat()*7, 0.6, -3+RANDOM.nextFloat()*7));
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
		for(EnumFacing facing : EnumFacing.VALUES) {
			TileEntity tile = world.getTileEntity(pos.offset(facing));
			if(tile != null && !(tile instanceof TileUnpackager) && tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite())) {
				IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());
				boolean flag = true;
				for(int i = 21; i >= endIndex; --i) {
					ItemStack stack = inventory.getStackInSlot(i);
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
					inventory.setInventorySlotContents(i, stack);
					if(flag) {
						break;
					}
				}
			}
		}
	}

	protected void chargeEnergy() {
		int prevStored = energyStorage.getEnergyStored();
		ItemStack energyStack = inventory.getStackInSlot(22);
		if(energyStack.hasCapability(CapabilityEnergy.ENERGY, null)) {
			int energyRequest = Math.min(energyStorage.getMaxReceive(), energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored());
			energyStorage.receiveEnergy(energyStack.getCapability(CapabilityEnergy.ENERGY, null).extractEnergy(energyRequest, false), false);
			if(energyStack.getCount() <= 0) {
				inventory.setInventorySlotContents(22, ItemStack.EMPTY);
			}
		}
	}

	@Override
	public void receiveStarlight(IWeakConstellation type, double amount) {
		if(amount <= 0.001) {
			return;
		}
		starlight = Math.min(starlightCapacity, (int)(starlight+amount*200));
		markDirty();
	}

	protected void starlightPassive() {
		boolean update = starlight > 0;
		starlight = (int)(starlight*0.95);
		if(doesSeeSky && ConstellationSkyHandler.getInstance().getWorldHandler(world) != null) {
			int yLevel = pos.getY();
			if(yLevel > 40) {
				float dstr, collect = 160F;
				if(yLevel > 120) {
					dstr = 1+(yLevel-120)/272F;
				}
				else {
					dstr = (yLevel-20)/100F;
				}
				if(posDistribution == -1F) {
					posDistribution = SkyCollectionHelper.getSkyNoiseDistribution(world, pos);
				}
				collect *= dstr;
				collect = (float)(collect*(0.6+0.4*posDistribution));
				collect = (float)(collect*(0.2+0.8*ConstellationSkyHandler.getInstance().getCurrentDaytimeDistribution(world)));
				starlight = Math.min(starlightCapacity, (int)(starlight+collect));
				update = true;
			}
		}
		if(update) {
			markDirty();
		}
	}

	protected void matchStructure() {
		PatternBlockArray structure = StructureConstellationCrafter.INSTANCE;
		if(requiresStructure && structureMatch == null) {
			structureMatch = PatternMatchHelper.getOrCreateMatcher(world, pos, structure);
		}
		boolean matches = !requiresStructure || structureMatch.matches(world);
		if(matches != structureValid) {
			LogCategory.STRUCTURE_MATCH.info(()->"Structure match updated: "+getClass().getName()+" at "+pos+" ("+structureValid+" -> "+matches+")");
			structureValid = matches;
			syncTile(false);
			markDirty();
		}
	}

	public void onBreak() {
		if(!world.isRemote) {
			WorldNetworkHandler handler = WorldNetworkHandler.getNetworkHandler(world);
			IPrismTransmissionNode node = handler.getTransmissionNode(pos);
			if(node != null) {
				StarlightUpdateHandler.getInstance().removeNode(world, node);
			}
			handler.removeTransmission(this);
			isNetworkInformed = false;
		}
	}

	public HostHelperTileConstellationCrafter hostHelper;

	@Override
	public void invalidate() {
		super.invalidate();
		if(hostHelper != null) {
			hostHelper.invalidate();
		}
	}

	@Override
	public void onChunkUnload() {
		super.onChunkUnload();
		if(hostHelper != null) {
			hostHelper.invalidate();
		}
	}

	@Optional.Method(modid="appliedenergistics2")
	@Override
	public IGridNode getGridNode(AEPartLocation dir) {
		return getActionableNode();
	}

	@Optional.Method(modid="appliedenergistics2")
	@Override
	public AECableType getCableConnectionType(AEPartLocation dir) {
		return AECableType.SMART;
	}

	@Optional.Method(modid="appliedenergistics2")
	@Override
	public void securityBreak() {
		world.destroyBlock(pos, true);
	}

	@Optional.Method(modid="appliedenergistics2")
	@Override
	public IGridNode getActionableNode() {
		return hostHelper.getNode();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		currentRecipe = null;
		if(nbt.hasKey("Recipe")) {
			NBTTagCompound tag = nbt.getCompoundTag("Recipe");
			IRecipeInfo recipe = MiscUtil.readRecipeFromNBT(tag);
			if(recipe instanceof IRecipeInfoAltar && ((IRecipeInfoAltar)recipe).getLevel() == 2) {
				currentRecipe = (IRecipeInfoAltar)recipe;
			}
		}
		if(hostHelper != null) {
			hostHelper.readFromNBT(nbt);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		if(currentRecipe != null) {
			NBTTagCompound tag = MiscUtil.writeRecipeToNBT(new NBTTagCompound(), currentRecipe);
			nbt.setTag("Recipe", tag);
		}
		if(hostHelper != null) {
			hostHelper.writeToNBT(nbt);
		}
		return nbt;
	}

	@Override
	public void readSyncNBT(NBTTagCompound nbt) {
		super.readSyncNBT(nbt);
		starlight = nbt.getInteger("Starlight");
		structureValid = nbt.getBoolean("MultiblockValid");
		isWorking = nbt.getBoolean("Working");
		progressReq = nbt.getInteger("ProgressReq");
		progress = nbt.getInteger("Progress");
		remainingProgress = nbt.getInteger("EnergyProgress");
		starlightReq = nbt.getInteger("StarlightReq");
		effectRecipe = null;
		if(nbt.hasKey("EffectRecipe")) {
			effectRecipe = AltarRecipeRegistry.getRecipe(nbt.getInteger("EffectRecipe"));
		}
	}

	@Override
	public NBTTagCompound writeSyncNBT(NBTTagCompound nbt) {
		super.writeSyncNBT(nbt);
		nbt.setInteger("Starlight", starlight);
		nbt.setBoolean("MultiblockValid", structureValid);
		nbt.setBoolean("Working", isWorking);
		nbt.setInteger("ProgressReq", progressReq);
		nbt.setInteger("Progress", progress);
		nbt.setInteger("EnergyProgress", remainingProgress);
		nbt.setInteger("StarlightReq", starlightReq);
		if(effectRecipe != null) {
			nbt.setInteger("EffectRecipe", effectRecipe.getUniqueRecipeId());
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

	@SideOnly(Side.CLIENT)
	@Override
	public GuiContainer getClientGuiElement(EntityPlayer player, Object... args) {
		return new GuiConstellationCrafter(new ContainerConstellationCrafter(player.inventory, this));
	}

	@Override
	public Container getServerGuiElement(EntityPlayer player, Object... args) {
		return new ContainerConstellationCrafter(player.inventory, this);
	}
}
