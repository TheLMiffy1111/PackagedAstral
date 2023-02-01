package thelm.packagedastral.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockReader;
import thelm.packagedastral.PackagedAstral;
import thelm.packagedastral.tile.TraitCrafterTile;
import thelm.packagedauto.block.BaseBlock;

public class TraitCrafterBlock extends BaseBlock {

	public static final TraitCrafterBlock INSTANCE = new TraitCrafterBlock();
	public static final Item ITEM_INSTANCE = new BlockItem(INSTANCE, new Item.Properties().group(PackagedAstral.ITEM_GROUP)).setRegistryName("packagedastral:trait_crafter");

	public TraitCrafterBlock() {
		super(AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(15F, 25F).notSolid().sound(SoundType.STONE));
		setRegistryName("packagedastral:trait_crafter");
	}

	@Override
	public TraitCrafterTile createTileEntity(BlockState state, IBlockReader world) {
		return TraitCrafterTile.TYPE_INSTANCE.create();
	}
}
