package thelm.packagedastral.structure;

import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.structure.types.StructureType;
import hellfirepvp.observerlib.api.util.PatternBlockArray;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import thelm.packagedastral.block.TraitCrafterBlock;

public class TraitCrafterPatternStructure extends PatternBlockArray {

	public static final TraitCrafterPatternStructure INSTANCE = new TraitCrafterPatternStructure();
	public static final StructureType TYPE_INSTANCE = new StructureType(INSTANCE.getRegistryName(), ()->INSTANCE);

	protected TraitCrafterPatternStructure() {
		super(new ResourceLocation("packagedastral:trait_crafter_pattern"));
		load();
	}

	private void load() {
		BlockState bricks = BlocksAS.MARBLE_BRICKS.getDefaultState();
		addAll(ConstellationCrafterPatternStructure.INSTANCE);
		addBlock(TraitCrafterBlock.INSTANCE.getDefaultState(), 0, 0, 0);
		addBlock(bricks, 4, 3, 3);
		addBlock(bricks, 4, 3, -3);
		addBlock(bricks, -4, 3, 3);
		addBlock(bricks, -4, 3, -3);
		addBlock(bricks, 3, 3, 4);
		addBlock(bricks, -3, 3, 4);
		addBlock(bricks, 3, 3, -4);
		addBlock(bricks, -3, 3,-4);
		addBlock(bricks, 3, 4, 3);
		addBlock(bricks, 3, 4, 2);
		addBlock(bricks, 3, 4, 1);
		addBlock(bricks, 3, 4, -1);
		addBlock(bricks, 3, 4, -2);
		addBlock(bricks, 3, 4, -3);
		addBlock(bricks, 2, 4, -3);
		addBlock(bricks, 1, 4, -3);
		addBlock(bricks, -1, 4, -3);
		addBlock(bricks, -2, 4, -3);
		addBlock(bricks, -3, 4, -3);
		addBlock(bricks, -3, 4, -2);
		addBlock(bricks, -3, 4, -1);
		addBlock(bricks, -3, 4, 1);
		addBlock(bricks, -3, 4, 2);
		addBlock(bricks, -3, 4, 3);
		addBlock(bricks, -2, 4, 3);
		addBlock(bricks, -1, 4, 3);
		addBlock(bricks, 1, 4, 3);
		addBlock(bricks, 2, 4, 3);
	}
}
