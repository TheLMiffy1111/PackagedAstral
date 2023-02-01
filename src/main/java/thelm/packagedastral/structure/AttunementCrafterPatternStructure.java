package thelm.packagedastral.structure;

import hellfirepvp.astralsorcery.common.block.marble.BlockMarblePillar;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.structure.types.StructureType;
import hellfirepvp.observerlib.api.block.MatchableState;
import hellfirepvp.observerlib.api.block.SimpleMatchableBlock;
import hellfirepvp.observerlib.api.util.PatternBlockArray;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import thelm.packagedastral.block.AttunementCrafterBlock;

public class AttunementCrafterPatternStructure extends PatternBlockArray {

	public static final AttunementCrafterPatternStructure INSTANCE = new AttunementCrafterPatternStructure();
	public static final StructureType TYPE_INSTANCE = new StructureType(INSTANCE.getRegistryName(), ()->INSTANCE);

	protected AttunementCrafterPatternStructure() {
		super(new ResourceLocation("packagedastral:attunement_crafter_pattern"));
		load();
	}

	private void load() {
		BlockState chiseled = BlocksAS.MARBLE_CHISELED.getDefaultState();
		BlockState bricks = BlocksAS.MARBLE_BRICKS.getDefaultState();
		BlockState arch = BlocksAS.MARBLE_ARCH.getDefaultState();
		BlockState sootyRaw = BlocksAS.BLACK_MARBLE_RAW.getDefaultState();
		addBlock(AttunementCrafterBlock.INSTANCE.getDefaultState(), 0, 0, 0);
		for(int i = -3; i <= 3; ++i) {
			for(int j = -3; j <= 3; ++j) {
				if(i != 0 || j != 0) {
					addBlock(sootyRaw, i, -1, j);
				}
			}
		}
		for(int i = -3; i <= 3; i++) {
			addBlock(arch, 4, -1, i);
			addBlock(arch, -4, -1, i);
			addBlock(arch, i, -1, 4);
			addBlock(arch, i, -1, -4);
			addBlock(bricks, 3, -1, i);
			addBlock(bricks, -3, -1, i);
			addBlock(bricks, i, -1, 3);
			addBlock(bricks, i, -1, -3);
		}
		addBlock(chiseled, 3, -1, 3);
		addBlock(chiseled, 3, -1, -3);
		addBlock(chiseled, -3, -1, 3);
		addBlock(chiseled, -3, -1, -3);
		addBlock(bricks, 2, -1, 0);
		addBlock(bricks, -2, -1, 0);
		addBlock(bricks, 0, -1, 2);
		addBlock(bricks, 0, -1, -2);
		addBlock(getPillarState(BlockMarblePillar.PillarType.BOTTOM), 3, 0, 3);
		addBlock(getPillarState(BlockMarblePillar.PillarType.BOTTOM), 3, 0, -3);
		addBlock(getPillarState(BlockMarblePillar.PillarType.BOTTOM), -3, 0, 3);
		addBlock(getPillarState(BlockMarblePillar.PillarType.BOTTOM), -3, 0, -3);
		addBlock(getPillarState(BlockMarblePillar.PillarType.TOP), 3, 1, 3);
		addBlock(getPillarState(BlockMarblePillar.PillarType.TOP), 3, 1, -3);
		addBlock(getPillarState(BlockMarblePillar.PillarType.TOP), -3, 1, 3);
		addBlock(getPillarState(BlockMarblePillar.PillarType.TOP), -3, 1, -3);
		addBlock(chiseled, 3, 2, 3);
		addBlock(chiseled, 3, 2, -3);
		addBlock(chiseled, -3, 2, 3);
		addBlock(chiseled, -3, 2, -3);
	}

	private MatchableState getPillarState(BlockMarblePillar.PillarType type) {
		return new SimpleMatchableBlock(BlocksAS.MARBLE_PILLAR) {
			@Override
			public BlockState getDescriptiveState(long tick) {
				return BlocksAS.MARBLE_PILLAR.getDefaultState().with(BlockMarblePillar.PILLAR_TYPE, type);
			}
		};
	}
}
