package thelm.packagedastral.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import thelm.packagedastral.PackagedAstral;
import thelm.packagedastral.tile.AttunementCrafterTile;
import thelm.packagedastral.tile.ConstellationCrafterTile;
import thelm.packagedauto.block.BaseBlock;

public class ConstellationCrafterBlock extends BaseBlock {

	public static final ConstellationCrafterBlock INSTANCE = new ConstellationCrafterBlock();
	public static final Item ITEM_INSTANCE = new BlockItem(INSTANCE, new Item.Properties().group(PackagedAstral.ITEM_GROUP)).setRegistryName("packagedastral:constellation_crafter");

	public ConstellationCrafterBlock() {
		super(AbstractBlock.Properties.create(Material.ROCK).hardnessAndResistance(15F, 25F).notSolid().sound(SoundType.STONE));
		setRegistryName("packagedastral:constellation_crafter");
	}

	@Override
	public ConstellationCrafterTile createTileEntity(BlockState state, IBlockReader world) {
		return ConstellationCrafterTile.TYPE_INSTANCE.create();
	}
}
