package thelm.packagedastral.integration.patchouli;

import java.util.Map;
import java.util.stream.Stream;

import hellfirepvp.astralsorcery.common.structure.array.BlockArray;
import hellfirepvp.astralsorcery.common.structure.array.BlockArray.BlockInformation;
import hellfirepvp.astralsorcery.common.structure.array.PatternBlockArray;
import it.unimi.dsi.fastutil.objects.Object2CharLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2CharMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import thelm.packagedastral.structure.StructureAttunementCrafter;
import thelm.packagedastral.structure.StructureConstellationCrafter;
import thelm.packagedastral.structure.StructureMarkedRelay;
import thelm.packagedastral.structure.StructureTraitCrafter;
import thelm.packagedastral.tile.TileAttunementCrafter;
import thelm.packagedastral.tile.TileConstellationCrafter;
import thelm.packagedastral.tile.TileDiscoveryCrafter;
import thelm.packagedastral.tile.TileTraitCrafter;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.PatchouliAPI;

public class PackagedAstralPatchouliHandler {

	public static void init() {
		PatchouliAPI.instance.setConfigFlag("packagedastral:discovery", TileDiscoveryCrafter.enabled);
		PatchouliAPI.instance.setConfigFlag("packagedastral:attunement", TileAttunementCrafter.enabled);
		PatchouliAPI.instance.setConfigFlag("packagedastral:constellation", TileConstellationCrafter.enabled);
		PatchouliAPI.instance.setConfigFlag("packagedastral:trait", TileTraitCrafter.enabled);
		registerSimpleMultiblock(StructureAttunementCrafter.INSTANCE);
		registerSimpleMultiblock(StructureConstellationCrafter.INSTANCE);
		registerSimpleMultiblock(StructureTraitCrafter.INSTANCE);
		registerSimpleMultiblock(StructureMarkedRelay.INSTANCE);
	}

	public static void registerSimpleMultiblock(PatternBlockArray structure) {
		PatchouliAPI.instance.registerMultiblock(structure.getRegistryName(), convertSimpleMultiblock(structure));
	}

	public static IMultiblock convertSimpleMultiblock(BlockArray structure) {
		Vec3i min = structure.getMin();
		Vec3i max = structure.getMax();
		Vec3i size = structure.getSize();
		Map<BlockPos, BlockInformation> patternMap = structure.getPattern();
		Object2CharMap<Object> targetMap = new Object2CharLinkedOpenHashMap<>();
		char nextChar = 'a';
		String[][] pattern = new String[size.getY()][size.getX()];
		BlockPos.MutableBlockPos currentPos = new BlockPos.MutableBlockPos();
		for(int y = max.getY(); y >= min.getY(); --y) {
			for(int x = min.getX(); x <= max.getX(); ++x) {
				StringBuilder sb = new StringBuilder();
				for(int z = min.getZ(); z <= max.getZ(); ++z) {
					currentPos.setPos(x, y, z);
					if(patternMap.containsKey(currentPos)) {
						IBlockState state = patternMap.get(currentPos).state;
						if(!targetMap.containsKey(state)) {
							targetMap.put(state, x == 0 && y == 0 && z == 0 ? '0' : nextChar++);
						}
						sb.append(targetMap.getChar(state));
					}
					else if(x == 0 && y == 0 && z == 0) {
						targetMap.put(PatchouliAPI.instance.anyMatcher(), '0');
						sb.append('0');
					}
					else {
						sb.append('_');
					}
				}
				pattern[max.getY()-y][x-min.getX()] = sb.toString();
			}
		}
		Object[] targets = targetMap.object2CharEntrySet().stream().flatMap(entry->Stream.of(entry.getCharValue(), entry.getKey())).toArray();
		return PatchouliAPI.instance.makeMultiblock(pattern, targets);
	}
}
