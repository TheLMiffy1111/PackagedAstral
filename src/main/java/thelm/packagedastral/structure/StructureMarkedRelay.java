package thelm.packagedastral.structure;

import hellfirepvp.astralsorcery.common.block.BlockMarble;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.structure.array.PatternBlockArray;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import thelm.packagedastral.block.BlockMarkedRelay;

public class StructureMarkedRelay extends PatternBlockArray {

	public static final StructureMarkedRelay INSTANCE = new StructureMarkedRelay();

	protected StructureMarkedRelay() {
		super(new ResourceLocation("packagedastral:pattern_marked_relay"));
		load();
	}

	private void load() {
		IBlockState marble = BlocksAS.blockMarble.getDefaultState();
		IBlockState chiseled = marble.withProperty(BlockMarble.MARBLE_TYPE, BlockMarble.MarbleBlockType.CHISELED);
		IBlockState arch = marble.withProperty(BlockMarble.MARBLE_TYPE, BlockMarble.MarbleBlockType.ARCH);
		IBlockState black = BlocksAS.blockBlackMarble.getDefaultState();
		addBlock(0, 0, 0, BlockMarkedRelay.INSTANCE.getDefaultState());
		addBlock(-1, -1, -1, chiseled);
		addBlock(1, -1, -1, chiseled);
		addBlock(1, -1, 1, chiseled);
		addBlock(-1, -1, 1, chiseled);
		addBlock(-1, -1, 0, arch);
		addBlock(1, -1, 0, arch);
		addBlock(0, -1, 1, arch);
		addBlock(0, -1, -1, arch);
		addBlock(0, -1, 0, black);
	}
}
