package thelm.packagedastral.structure;

import hellfirepvp.astralsorcery.common.block.BlockMarble;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.structure.array.PatternBlockArray;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import thelm.packagedastral.block.BlockTraitCrafter;

public class StructureTraitCrafter extends PatternBlockArray {

	public static final StructureTraitCrafter INSTANCE = new StructureTraitCrafter();

	protected StructureTraitCrafter() {
		super(new ResourceLocation("packagedastral:pattern_trait_crafter"));
		addAll(StructureConstellationCrafter.INSTANCE);
		load();
	}

	private void load() {
		IBlockState bricks = BlocksAS.blockMarble.getDefaultState().withProperty(BlockMarble.MARBLE_TYPE, BlockMarble.MarbleBlockType.BRICKS);
		addBlock(4, 3, 3, bricks);
		addBlock(4, 3, -3, bricks);
		addBlock(-4, 3, 3, bricks);
		addBlock(-4, 3, -3, bricks);
		addBlock(3, 3, 4, bricks);
		addBlock(-3, 3, 4, bricks);
		addBlock(3, 3, -4, bricks);
		addBlock(-3, 3,-4, bricks);
		addBlock(3, 4, 3, bricks);
		addBlock(3, 4, 2, bricks);
		addBlock(3, 4, 1, bricks);
		addBlock(3, 4, -1, bricks);
		addBlock(3, 4, -2, bricks);
		addBlock(3, 4, -3, bricks);
		addBlock(2, 4, -3, bricks);
		addBlock(1, 4, -3, bricks);
		addBlock(-1, 4, -3, bricks);
		addBlock(-2, 4, -3, bricks);
		addBlock(-3, 4, -3, bricks);
		addBlock(-3, 4, -2, bricks);
		addBlock(-3, 4, -1, bricks);
		addBlock(-3, 4, 1, bricks);
		addBlock(-3, 4, 2, bricks);
		addBlock(-3, 4, 3, bricks);
		addBlock(-2, 4, 3, bricks);
		addBlock(-1, 4, 3, bricks);
		addBlock(1, 4, 3, bricks);
		addBlock(2, 4, 3, bricks);
		addBlock(0, 0, 0, BlockTraitCrafter.INSTANCE.getDefaultState());
	}
}
