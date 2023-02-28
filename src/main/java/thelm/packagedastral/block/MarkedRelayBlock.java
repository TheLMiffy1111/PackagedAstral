package thelm.packagedastral.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import thelm.packagedastral.PackagedAstral;
import thelm.packagedastral.tile.MarkedRelayTile;
import thelm.packagedauto.block.BaseBlock;

public class MarkedRelayBlock extends BaseBlock {

	public static final MarkedRelayBlock INSTANCE = new MarkedRelayBlock();
	public static final Item ITEM_INSTANCE = new BlockItem(INSTANCE, new Item.Properties().tab(PackagedAstral.ITEM_GROUP)).setRegistryName("packagedastral:marked_relay");
	public static final VoxelShape SHAPE = box(2, 0, 2, 14, 2, 14);

	public MarkedRelayBlock() {
		super(AbstractBlock.Properties.of(Material.GLASS, MaterialColor.QUARTZ).strength(15F, 5F).lightLevel(state->4).noOcclusion().sound(SoundType.GLASS));
		setRegistryName("packagedastral:marked_relay");
	}

	@Override
	public MarkedRelayTile createTileEntity(BlockState state, IBlockReader world) {
		return MarkedRelayTile.TYPE_INSTANCE.create();
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}

	@Override
    public boolean isPathfindable(BlockState state, IBlockReader world, BlockPos pos, PathType type) {
        return false;
    }

	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
		return ActionResultType.PASS;
	}
}
