package thelm.packagedastral.block;

import hellfirepvp.observerlib.api.block.BlockStructureObserver;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockReader;
import thelm.packagedastral.PackagedAstral;
import thelm.packagedastral.tile.ConstellationCrafterTile;
import thelm.packagedauto.block.BaseBlock;

public class ConstellationCrafterBlock extends BaseBlock implements BlockStructureObserver {

	public static final ConstellationCrafterBlock INSTANCE = new ConstellationCrafterBlock();
	public static final Item ITEM_INSTANCE = new BlockItem(INSTANCE, new Item.Properties().tab(PackagedAstral.ITEM_GROUP)).setRegistryName("packagedastral:constellation_crafter");

	public ConstellationCrafterBlock() {
		super(AbstractBlock.Properties.of(Material.STONE).strength(15F, 25F).noOcclusion().sound(SoundType.STONE));
		setRegistryName("packagedastral:constellation_crafter");
	}

	@Override
	public ConstellationCrafterTile createTileEntity(BlockState state, IBlockReader world) {
		return ConstellationCrafterTile.TYPE_INSTANCE.create();
	}
}
