package thelm.packagedastral.structure;

import hellfirepvp.astralsorcery.common.block.marble.BlockMarblePillar;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.structure.types.StructureType;
import hellfirepvp.observerlib.api.block.MatchableState;
import hellfirepvp.observerlib.api.block.SimpleMatchableBlock;
import hellfirepvp.observerlib.api.util.PatternBlockArray;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;

public class ConstellationCrafterPatternStructure extends PatternBlockArray {

	public static final ConstellationCrafterPatternStructure INSTANCE = new ConstellationCrafterPatternStructure();
	public static final StructureType TYPE_INSTANCE = new StructureType(INSTANCE.getRegistryName(), ()->INSTANCE);

	protected ConstellationCrafterPatternStructure() {
		super(new ResourceLocation("packagedastral:constellation_crafter_pattern"));
		load();
	}

	private void load() {
		BlockState raw = BlocksAS.MARBLE_RAW.defaultBlockState();
		BlockState chiseled = BlocksAS.MARBLE_CHISELED.defaultBlockState();
		BlockState runed = BlocksAS.MARBLE_RUNED.defaultBlockState();
		BlockState bricks = BlocksAS.MARBLE_BRICKS.defaultBlockState();
		BlockState sootyRaw = BlocksAS.BLACK_MARBLE_RAW.defaultBlockState();
		addBlock(BlocksAS.ALTAR_CONSTELLATION.defaultBlockState(), 0, 0, 0);
		for(int i = -2; i <= 2; ++i) {
			for(int j = -2; j <= 2; ++j) {
				if(i != 0 || j != 0) {
					addBlock(sootyRaw, i, -1, j);
				}
			}
		}
		for(int i = -3; i <= 3; ++i) {
			addBlock(bricks, 4, -1, i);
			addBlock(bricks, -4, -1, i);
			addBlock(bricks, i, -1, 4);
			addBlock(bricks, i, -1, -4);
		}
		addBlock(raw, -4, -1, -4);
		addBlock(raw, -4, -1, -3);
		addBlock(raw, -3, -1, -4);
		addBlock(raw, 4, -1, -4);
		addBlock(raw, 4, -1, -3);
		addBlock(raw, 3, -1, -4);
		addBlock(raw, -4, -1, 4);
		addBlock(raw, -4, -1, 3);
		addBlock(raw, -3, -1, 4);
		addBlock(raw, 4, -1, 4);
		addBlock(raw, 4, -1, 3);
		addBlock(raw, 3, -1, 4);
		addBlock(bricks, -5, -1, -5);
		addBlock(bricks, -5, -1, -4);
		addBlock(bricks, -5, -1, -3);
		addBlock(bricks, -4, -1, -5);
		addBlock(bricks, -3, -1, -5);
		addBlock(bricks, 5, -1, -5);
		addBlock(bricks, 5, -1, -4);
		addBlock(bricks, 5, -1, -3);
		addBlock(bricks, 4, -1, -5);
		addBlock(bricks, 3, -1, -5);
		addBlock(bricks, -5, -1, 5);
		addBlock(bricks, -5, -1, 4);
		addBlock(bricks, -5, -1, 3);
		addBlock(bricks, -4, -1, 5);
		addBlock(bricks, -3, -1, 5);
		addBlock(bricks, 5, -1, 5);
		addBlock(bricks, 5, -1, 4);
		addBlock(bricks, 5, -1, 3);
		addBlock(bricks, 4, -1, 5);
		addBlock(bricks, 3, -1, 5);
		addBlock(runed, -4, 0, -4);
		addBlock(runed, -4, 0, 4);
		addBlock(runed, 4, 0, -4);
		addBlock(runed, 4, 0, 4);
		addBlock(getPillarState(BlockMarblePillar.PillarType.BOTTOM), 4, 1, 4);
		addBlock(getPillarState(BlockMarblePillar.PillarType.BOTTOM), 4, 1, -4);
		addBlock(getPillarState(BlockMarblePillar.PillarType.BOTTOM), -4, 1, 4);
		addBlock(getPillarState(BlockMarblePillar.PillarType.BOTTOM), -4, 1, -4);
		addBlock(getPillarState(BlockMarblePillar.PillarType.TOP), 4, 2, 4);
		addBlock(getPillarState(BlockMarblePillar.PillarType.TOP), 4, 2, -4);
		addBlock(getPillarState(BlockMarblePillar.PillarType.TOP), -4, 2, 4);
		addBlock(getPillarState(BlockMarblePillar.PillarType.TOP), -4, 2, -4);
		addBlock(chiseled, -4, 3, -4);
		addBlock(chiseled, -4, 3, 4);
		addBlock(chiseled, 4, 3, -4);
		addBlock(chiseled, 4, 3, 4);
	}

	private MatchableState getPillarState(BlockMarblePillar.PillarType type) {
		return new SimpleMatchableBlock(BlocksAS.MARBLE_PILLAR) {
			@Override
			public BlockState getDescriptiveState(long tick) {
				return BlocksAS.MARBLE_PILLAR.defaultBlockState().setValue(BlockMarblePillar.PILLAR_TYPE, type);
			}
		};
	}
}
