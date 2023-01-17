package thelm.packagedastral.structure;

import hellfirepvp.astralsorcery.common.block.BlockMarble;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.structure.array.PatternBlockArray;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import thelm.packagedastral.block.BlockAttunementCrafter;

public class StructureAttunementCrafter extends PatternBlockArray {

	public static final StructureAttunementCrafter INSTANCE = new StructureAttunementCrafter();

	protected StructureAttunementCrafter() {
		super(new ResourceLocation("packagedastral:pattern_attunement_crafter"));
		load();
	}

	private void load() {
		IBlockState marble = BlocksAS.blockMarble.getDefaultState();
		IBlockState chiseled = marble.withProperty(BlockMarble.MARBLE_TYPE, BlockMarble.MarbleBlockType.CHISELED);
		IBlockState bricks = marble.withProperty(BlockMarble.MARBLE_TYPE, BlockMarble.MarbleBlockType.BRICKS);
		IBlockState arch = marble.withProperty(BlockMarble.MARBLE_TYPE, BlockMarble.MarbleBlockType.ARCH);
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
			addBlock(4, -1, i, arch);
			addBlock(-4, -1, i, arch);
			addBlock(i, -1, 4, arch);
			addBlock(i, -1, -4, arch);
			addBlock(3, -1, i, bricks);
			addBlock(-3, -1, i, bricks);
			addBlock(i, -1, 3, bricks);
			addBlock(i, -1, -3, bricks);
		}
		addBlock(3, -1, 3, chiseled);
		addBlock(3, -1, -3, chiseled);
		addBlock(-3, -1, 3, chiseled);
		addBlock(-3, -1, -3, chiseled);
		addBlock(2, -1, 0, bricks);
		addBlock(-2, -1, 0, bricks);
		addBlock(0, -1, 2, bricks);
		addBlock(0, -1, -2, bricks);
		for(int i = 0; i < 2; ++i) {
			addBlock(3, i, 3, pillar);
			addBlock(3, i, -3, pillar);
			addBlock(-3, i, 3, pillar);
			addBlock(-3, i, -3, pillar);
		}
		addBlock(3, 2, 3, chiseled);
		addBlock(3, 2, -3, chiseled);
		addBlock(-3, 2, 3, chiseled);
		addBlock(-3, 2, -3, chiseled);
		addBlock(0, 0, 0, BlockAttunementCrafter.INSTANCE.getDefaultState());
	}
}
