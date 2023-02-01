package thelm.packagedastral.container;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.items.SlotItemHandler;
import thelm.packagedastral.slot.ConstellationCrafterRemoveOnlySlot;
import thelm.packagedastral.tile.ConstellationCrafterTile;
import thelm.packagedauto.container.BaseContainer;
import thelm.packagedauto.container.factory.PositionalTileContainerFactory;
import thelm.packagedauto.slot.RemoveOnlySlot;

public class ConstellationCrafterContainer extends BaseContainer<ConstellationCrafterTile> {

	public static final ContainerType<ConstellationCrafterContainer> TYPE_INSTANCE = (ContainerType<ConstellationCrafterContainer>)IForgeContainerType.
			create(new PositionalTileContainerFactory<>(ConstellationCrafterContainer::new)).
			setRegistryName("packagedastral:constellation_crafter");

	public ConstellationCrafterContainer(int windowId, PlayerInventory playerInventory, ConstellationCrafterTile tile) {
		super(TYPE_INSTANCE, windowId, playerInventory, tile);
		addSlot(new SlotItemHandler(itemHandler, 22, 8, 71));
		int index = 0;
		for(int i = 0; i < 5; ++i) {
			for(int j = 0; j < 5; ++j) {
				int k = i*5+j;
				if(k != 2 && k != 10 && k != 14 && k != 22) {
					addSlot(new ConstellationCrafterRemoveOnlySlot(tile, index++, 44+j*18, 17+i*18));
				}
			}
		}
		addSlot(new RemoveOnlySlot(itemHandler, 21, 170, 53));
		setupPlayerInventory();
	}

	@Override
	public int getPlayerInvX() {
		return 19;
	}

	@Override
	public int getPlayerInvY() {
		return 135;
	}
}
