package thelm.packagedastral.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import thelm.packagedastral.PackagedAstral;
import thelm.packagedastral.tile.TraitCrafterTile;
import thelm.packagedauto.block.BaseBlock;

public class TraitCrafterBlock extends BaseBlock {

	public static final TraitCrafterBlock INSTANCE = new TraitCrafterBlock();
	public static final Item ITEM_INSTANCE = new BlockItem(INSTANCE, new Item.Properties().tab(PackagedAstral.ITEM_GROUP)).setRegistryName("packagedastral:trait_crafter");

	public TraitCrafterBlock() {
		super(AbstractBlock.Properties.of(Material.STONE).strength(15F, 25F).noOcclusion().sound(SoundType.STONE));
		setRegistryName("packagedastral:trait_crafter");
	}

	@Override
	public TraitCrafterTile createTileEntity(BlockState state, IBlockReader world) {
		return TraitCrafterTile.TYPE_INSTANCE.create();
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult rayTraceResult) {
		if(playerIn.isShiftKeyDown()) {
			TileEntity tileentity = worldIn.getBlockEntity(pos);
			if(tileentity instanceof TraitCrafterTile) {
				TraitCrafterTile crafter = (TraitCrafterTile)tileentity;
				if(!crafter.isWorking) {
					if(!worldIn.isClientSide) {
						ITextComponent message = crafter.getMessage();
						if(message != null) {
							playerIn.sendMessage(message, Util.NIL_UUID);
						}
					}
					return ActionResultType.SUCCESS;
				}
			}
		}
		return super.use(state, worldIn, pos, playerIn, hand, rayTraceResult);
	}

	@Override
	public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
		if(state.getBlock() == newState.getBlock()) {
			return;
		}
		TileEntity tileentity = worldIn.getBlockEntity(pos);
		if(tileentity instanceof TraitCrafterTile) {
			TraitCrafterTile crafter = (TraitCrafterTile)tileentity;
			if(crafter.isWorking) {
				crafter.endProcess();
			}
		}
		super.onRemove(state, worldIn, pos, newState, isMoving);
	}
}
