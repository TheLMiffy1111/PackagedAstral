package thelm.packagedastral.structure;

import hellfirepvp.astralsorcery.common.block.BlockMarble;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.structure.array.PatternBlockArray;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import thelm.packagedastral.block.BlockConstellationCrafter;

public class StructureConstellationCrafter extends PatternBlockArray {

	public static final StructureConstellationCrafter INSTANCE = new StructureConstellationCrafter();

	protected StructureConstellationCrafter() {
		super(new ResourceLocation("packagedastral:pattern_constellation_crafter"));
		load();
	}

	private void load() {
		IBlockState marble = BlocksAS.blockMarble.getDefaultState();
		IBlockState chiseled = marble.withProperty(BlockMarble.MARBLE_TYPE, BlockMarble.MarbleBlockType.CHISELED);
		IBlockState bricks = marble.withProperty(BlockMarble.MARBLE_TYPE, BlockMarble.MarbleBlockType.BRICKS);
		IBlockState runed = marble.withProperty(BlockMarble.MARBLE_TYPE, BlockMarble.MarbleBlockType.RUNED);
		IBlockState pillar = marble.withProperty(BlockMarble.MARBLE_TYPE, BlockMarble.MarbleBlockType.PILLAR);
		IBlockState black = BlocksAS.blockBlackMarble.getDefaultState();
		for(int i = -3; i <= 3; ++i) {
			for(int j = -3; j <= 3; ++j) {
				if(i != 0 || j != 0) {
					addBlock(i, -1, j, black);
				}
			}
		}
		for(int i = -3; i <= 3; ++i) {
			addBlock(4, -1, i, bricks);
			addBlock(-4, -1, i, bricks);
			addBlock(i, -1, 4, bricks);
			addBlock(i, -1, -4, bricks);
		}
		addBlock(-4, -1, -4, marble);
		addBlock(-4, -1, -3, marble);
		addBlock(-3, -1, -4, marble);
		addBlock(4, -1, -4, marble);
		addBlock(4, -1, -3, marble);
		addBlock(3, -1, -4, marble);
		addBlock(-4, -1, 4, marble);
		addBlock(-4, -1, 3, marble);
		addBlock(-3, -1, 4, marble);
		addBlock(4, -1, 4, marble);
		addBlock(4, -1, 3, marble);
		addBlock(3, -1, 4, marble);
		addBlock(-5, -1, -5, bricks);
		addBlock(-5, -1, -4, bricks);
		addBlock(-5, -1, -3, bricks);
		addBlock(-4, -1, -5, bricks);
		addBlock(-3, -1, -5, bricks);
		addBlock(5, -1, -5, bricks);
		addBlock(5, -1, -4, bricks);
		addBlock(5, -1, -3, bricks);
		addBlock(4, -1, -5, bricks);
		addBlock(3, -1, -5, bricks);
		addBlock(-5, -1, 5, bricks);
		addBlock(-5, -1, 4, bricks);
		addBlock(-5, -1, 3, bricks);
		addBlock(-4, -1, 5, bricks);
		addBlock(-3, -1, 5, bricks);
		addBlock(5, -1, 5, bricks);
		addBlock(5, -1, 4, bricks);
		addBlock(5, -1, 3, bricks);
		addBlock(4, -1, 5, bricks);
		addBlock(3, -1, 5, bricks);
		addBlock(-4, 0, -4, runed);
		addBlock(-4, 0, 4, runed);
		addBlock(4, 0, -4, runed);
		addBlock(4, 0, 4, runed);
		addBlock(-4, 1, -4, pillar);
		addBlock(-4, 1, 4, pillar);
		addBlock(4, 1, -4, pillar);
		addBlock(4, 1, 4, pillar);
		addBlock(-4, 2, -4, pillar);
		addBlock(-4, 2, 4, pillar);
		addBlock(4, 2, -4, pillar);
		addBlock(4, 2, 4, pillar);
		addBlock(-4, 3, -4, chiseled);
		addBlock(-4, 3, 4, chiseled);
		addBlock(4, 3, -4, chiseled);
		addBlock(4, 3, 4, chiseled);
		addBlock(0, 0, 0, BlockConstellationCrafter.INSTANCE.getDefaultState());
	}
}
