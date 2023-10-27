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
import hellfirepvp.astralsorcery.common.starlight.WorldNetworkHandler;
import hellfirepvp.astralsorcery.common.starlight.network.StarlightUpdateHandler;
import hellfirepvp.astralsorcery.common.starlight.transmission.IPrismTransmissionNode;
import hellfirepvp.astralsorcery.common.tile.TileAltar;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.util.SkyCollectionHelper;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
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
import net.minecraftforge.items.ItemHandlerHelper;
import thelm.packagedastral.client.gui.GuiDiscoveryCrafter;
import thelm.packagedastral.container.ContainerDiscoveryCrafter;
import thelm.packagedastral.integration.appeng.networking.HostHelperTileDiscoveryCrafter;
import thelm.packagedastral.inventory.InventoryDiscoveryCrafter;
import thelm.packagedastral.recipe.IRecipeInfoAltar;
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
public class TileDiscoveryCrafter extends TileBase implements ITickable, IPackageCraftingMachine, IAltarCrafter, IGridHost, IActionHost {

	public static final Random RANDOM = new Random();

	public static boolean enabled = true;

	public static int energyCapacity = 5000;
	public static int energyReq = 500;
	public static int energyUsage = 100;
	public static int starlightCapacity = 1000;
	public static boolean craftingEffects = true;
	public static boolean requiresNight = true;
	public static boolean drawMEEnergy = true;

	public boolean firstTick = true;
	public boolean doesSeeSky = false;
	public float posDistribution = -1F;
	public TileAltar fakeAltar = new TileAltar(TileAltar.AltarLevel.DISCOVERY);
	public AbstractAltarRecipe effectRecipe = null;
	public Object clientCraftSound = null;
	public int starlight = 0;
	public boolean isWorking = false;
	public int progress = 0;
	public int progressReq = 0;
	public int remainingProgress = 0;
	public int starlightReq = 0;
	public IRecipeInfoAltar currentRecipe;

	public TileDiscoveryCrafter() {
		setInventory(new InventoryDiscoveryCrafter(this));
		setEnergyStorage(new EnergyStorage(this, energyCapacity));
		if(Loader.isModLoaded("appliedenergistics2")) {
			hostHelper = new HostHelperTileDiscoveryCrafter(this);
		}
	}

	@Override
	public String getUnLocalizedDisplayName() {
		return "tile.packagedastral.discovery_crafter.name";
	}

	@Override
	protected String getLocalizedName() {
		return I18n.translateToLocal("tile.packagedastral.discovery_crafter.name");
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
		if(firstTick) {
			firstTick = false;
			if(!world.isRemote) {
				WorldNetworkHandler handler = WorldNetworkHandler.getNetworkHandler(world);
				handler.addTransmissionTile(this);
				IPrismTransmissionNode node = handler.getTransmissionNode(pos);
				if(node != null && node.needsUpdate()) {
					StarlightUpdateHandler.getInstance().addNode(world, node);
				}
				TileMarkedRelay.updateNearbyAltarPos(world, pos);
			}
		}
		if(world.getTotalWorldTime() % 16 == 0) {
			doesSeeSky = MiscUtils.canSeeSky(world, pos.up(), true, doesSeeSky);
		}
		if(!world.isRemote) {
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
			starlightPassive();
			chargeEnergy();
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
		}
		else {
			clientTick();
		}
	}

	@SideOnly(Side.CLIENT)
	protected void clientTick() {
		if(isWorking) {
			if(craftingEffects && effectRecipe != null) {
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
			if(recipe.getLevel() == 0 && (!requiresNight || !recipe.requiresNight() || ConstellationSkyHandler.getInstance().isNight(world))) {
				ItemStack slotStack = inventory.getStackInSlot(9);
				ItemStack outputStack = recipe.getOutput();
				if(slotStack.isEmpty() || slotStack.getItem() == outputStack.getItem() && slotStack.getItemDamage() == outputStack.getItemDamage() && ItemStack.areItemStackShareTagsEqual(slotStack, outputStack) && slotStack.getCount()+outputStack.getCount() <= outputStack.getMaxStackSize()) {
					currentRecipe = recipe;
					effectRecipe = recipe.getRecipe();
					isWorking = true;
					progressReq = recipe.getTimeRequired() / (int)Math.round(2*Math.pow(2, Math.max(0, 0-recipe.getLevelRequired())));
					remainingProgress = energyReq;
					starlightReq = recipe.getStarlightRequired();
					for(int i = 0; i < 9; ++i) {
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
		return isWorking || !inventory.stacks.subList(0, 9).stream().allMatch(ItemStack::isEmpty);
	}

	protected void tickProcess() {
		if(starlight >= currentRecipe.getStarlightRequired()) {
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
		if(inventory.getStackInSlot(9).isEmpty()) {
			inventory.setInventorySlotContents(9, currentRecipe.getOutput());
		}
		else {
			inventory.getStackInSlot(9).grow(currentRecipe.getOutput().getCount());
		}
		for(int i = 0; i < 9; ++i) {
			inventory.setInventorySlotContents(i, MiscUtil.getContainerItem(inventory.getStackInSlot(i)));
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
		int endIndex = isWorking ? 9 : 0;
		for(EnumFacing facing : EnumFacing.VALUES) {
			TileEntity tile = world.getTileEntity(pos.offset(facing));
			if(tile != null && !(tile instanceof TileUnpackager) && tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite())) {
				IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite());
				for(int i = 9; i >= endIndex; --i) {
					ItemStack stack = inventory.getStackInSlot(i);
					if(stack.isEmpty()) {
						continue;
					}
					ItemStack stackRem = ItemHandlerHelper.insertItem(itemHandler, stack, false);
					inventory.setInventorySlotContents(i, stackRem);
				}
			}
		}
	}

	protected void chargeEnergy() {
		ItemStack energyStack = inventory.getStackInSlot(10);
		if(energyStack.hasCapability(CapabilityEnergy.ENERGY, null)) {
			int energyRequest = Math.min(energyStorage.getMaxReceive(), energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored());
			energyStorage.receiveEnergy(energyStack.getCapability(CapabilityEnergy.ENERGY, null).extractEnergy(energyRequest, false), false);
			if(energyStack.getCount() <= 0) {
				inventory.setInventorySlotContents(10, ItemStack.EMPTY);
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

	public void onBreak() {
		if(!world.isRemote) {
			WorldNetworkHandler handler = WorldNetworkHandler.getNetworkHandler(world);
			IPrismTransmissionNode node = handler.getTransmissionNode(pos);
			if(node != null) {
				StarlightUpdateHandler.getInstance().removeNode(world, node);
			}
			handler.removeTransmission(this);
		}
	}

	@Override
	public int getComparatorSignal() {
		if(isWorking) {
			return 1;
		}
		if(!inventory.stacks.subList(0, 10).stream().allMatch(ItemStack::isEmpty)) {
			return 15;
		}
		return 0;
	}

	public HostHelperTileDiscoveryCrafter hostHelper;

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
		starlight = nbt.getInteger("Starlight");
		progressReq = nbt.getInteger("ProgressReq");
		progress = nbt.getInteger("Progress");
		remainingProgress = nbt.getInteger("EnergyProgress");
		starlightReq = nbt.getInteger("StarlightReq");
		currentRecipe = null;
		if(nbt.hasKey("Recipe")) {
			NBTTagCompound tag = nbt.getCompoundTag("Recipe");
			IRecipeInfo recipe = MiscUtil.readRecipeFromNBT(tag);
			if(recipe instanceof IRecipeInfoAltar && ((IRecipeInfoAltar)recipe).getLevel() == 0) {
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
		nbt.setInteger("Starlight", starlight);
		nbt.setInteger("ProgressReq", progressReq);
		nbt.setInteger("Progress", progress);
		nbt.setInteger("EnergyProgress", remainingProgress);
		nbt.setInteger("StarlightReq", starlightReq);
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
		isWorking = nbt.getBoolean("Working");
		effectRecipe = null;
		if(nbt.hasKey("EffectRecipe")) {
			effectRecipe = AltarRecipeRegistry.getRecipe(nbt.getInteger("EffectRecipe"));
		}
	}

	@Override
	public NBTTagCompound writeSyncNBT(NBTTagCompound nbt) {
		super.writeSyncNBT(nbt);
		nbt.setBoolean("Working", isWorking);
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

	public int getScaledStarlightReq(int scale) {
		if(starlightCapacity <= 0 || starlight >= starlightReq) {
			return 0;
		}
		return scale * starlightReq / starlightCapacity - getScaledStarlight(scale);
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
		return new GuiDiscoveryCrafter(new ContainerDiscoveryCrafter(player.inventory, this));
	}

	@Override
	public Container getServerGuiElement(EntityPlayer player, Object... args) {
		return new ContainerDiscoveryCrafter(player.inventory, this);
	}
}
