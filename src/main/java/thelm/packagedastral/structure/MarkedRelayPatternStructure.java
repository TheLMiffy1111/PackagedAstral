package thelm.packagedastral.structure;

import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.structure.types.StructureType;
import hellfirepvp.observerlib.api.util.PatternBlockArray;
import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import thelm.packagedastral.block.MarkedRelayBlock;

public class MarkedRelayPatternStructure extends PatternBlockArray {

	public static final MarkedRelayPatternStructure INSTANCE = new MarkedRelayPatternStructure();
	public static final StructureType TYPE_INSTANCE = new StructureType(INSTANCE.getRegistryName(), ()->INSTANCE);

	protected MarkedRelayPatternStructure() {
		super(new ResourceLocation("packagedastral:marked_relay_pattern"));
		load();
	}

	private void load() {
		addBlock(MarkedRelayBlock.INSTANCE.defaultBlockState(), 0, 0, 0);
		BlockState chiseled = BlocksAS.MARBLE_CHISELED.defaultBlockState();
		BlockState arch = BlocksAS.MARBLE_ARCH.defaultBlockState();
		BlockState sootyRaw = BlocksAS.BLACK_MARBLE_RAW.defaultBlockState();
		addBlock(sootyRaw, 0, -1, 0);
		addBlock(chiseled, -1, -1, -1);
		addBlock(chiseled, 1, -1, -1);
		addBlock(chiseled, -1, -1, 1);
		addBlock(chiseled, 1, -1, 1);
		addBlock(arch, -1, -1, 0);
		addBlock(arch, 1, -1, 0);
		addBlock(arch, 0, -1, -1);
		addBlock(arch, 0, -1, 1);
	}
}
