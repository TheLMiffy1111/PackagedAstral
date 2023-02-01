package thelm.packagedastral.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.world.IBlockReader;
import thelm.packagedastral.PackagedAstral;
import thelm.packagedastral.tile.AttunementCrafterTile;
import thelm.packagedauto.block.BaseBlock;

public class AttunementCrafterBlock extends BaseBlock {

	public static final AttunementCrafterBlock INSTANCE = new AttunementCrafterBlock();
	public static final Item ITEM_INSTANCE = new BlockItem(INSTANCE, new Item.Properties().group(PackagedAstral.ITEM_GROUP)).setRegistryName("packagedastral:attunement_crafter");

	public AttunementCrafterBlock() {
		super(AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(15F, 25F).notSolid().sound(SoundType.STONE));
		setRegistryName("packagedastral:attunement_crafter");
	}

	@Override
	public AttunementCrafterTile createTileEntity(BlockState state, IBlockReader world) {
		return AttunementCrafterTile.TYPE_INSTANCE.create();
	}
}
