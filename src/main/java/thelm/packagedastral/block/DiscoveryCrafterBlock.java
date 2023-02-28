package thelm.packagedastral.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockReader;
import thelm.packagedastral.PackagedAstral;
import thelm.packagedastral.tile.DiscoveryCrafterTile;
import thelm.packagedauto.block.BaseBlock;

public class DiscoveryCrafterBlock extends BaseBlock {

	public static final DiscoveryCrafterBlock INSTANCE = new DiscoveryCrafterBlock();
	public static final Item ITEM_INSTANCE = new BlockItem(INSTANCE, new Item.Properties().tab(PackagedAstral.ITEM_GROUP)).setRegistryName("packagedastral:discovery_crafter");

	public DiscoveryCrafterBlock() {
		super(AbstractBlock.Properties.of(Material.STONE).strength(15F, 25F).noOcclusion().sound(SoundType.STONE));
		setRegistryName("packagedastral:discovery_crafter");
	}

	@Override
	public DiscoveryCrafterTile createTileEntity(BlockState state, IBlockReader world) {
		return DiscoveryCrafterTile.TYPE_INSTANCE.create();
	}
}
