package thelm.packagedastral.integration.patchouli;

import java.util.Map;
import java.util.stream.Stream;

import hellfirepvp.observerlib.api.block.MatchableState;
import hellfirepvp.observerlib.api.util.BlockArray;
import hellfirepvp.observerlib.api.util.PatternBlockArray;
import it.unimi.dsi.fastutil.objects.Object2CharLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2CharMap;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import thelm.packagedastral.structure.AttunementCrafterPatternStructure;
import thelm.packagedastral.structure.ConstellationCrafterPatternStructure;
import thelm.packagedastral.structure.MarkedRelayPatternStructure;
import thelm.packagedastral.structure.TraitCrafterPatternStructure;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.PatchouliAPI;

public class PackagedAstralPatchouliHandler {

	public static void init() {
		registerSimpleMultiblock(AttunementCrafterPatternStructure.INSTANCE);
		registerSimpleMultiblock(ConstellationCrafterPatternStructure.INSTANCE);
		registerSimpleMultiblock(TraitCrafterPatternStructure.INSTANCE);
		registerSimpleMultiblock(MarkedRelayPatternStructure.INSTANCE);
	}

	public static void registerSimpleMultiblock(PatternBlockArray structure) {
		PatchouliAPI.get().registerMultiblock(structure.getRegistryName(), convertSimpleMultiblock(structure));
	}

	public static IMultiblock convertSimpleMultiblock(BlockArray structure) {
		Vector3i min = structure.getMinimumOffset();
		Vector3i max = structure.getMaximumOffset();
		Map<BlockPos, MatchableState> patternMap = structure.getContents();
		Object2CharMap<Object> targetMap = new Object2CharLinkedOpenHashMap<>();
		char nextChar = 'a';
		String[][] pattern = new String[max.getY()-min.getY()+1][max.getX()-min.getX()+1];
		BlockPos.Mutable currentPos = new BlockPos.Mutable();
		for(int y = max.getY(); y >= min.getY(); --y) {
			for(int x = min.getX(); x <= max.getX(); ++x) {
				StringBuilder sb = new StringBuilder();
				for(int z = min.getZ(); z <= max.getZ(); ++z) {
					currentPos.set(x, y, z);
					if(patternMap.containsKey(currentPos)) {
						BlockState state = patternMap.get(currentPos).getDescriptiveState(0);
						if(!targetMap.containsKey(state)) {
							targetMap.put(state, x == 0 && y == 0 && z == 0 ? '0' : nextChar++);
						}
						sb.append(targetMap.getChar(state));
					}
					else if(x == 0 && y == 0 && z == 0) {
						targetMap.put(PatchouliAPI.get().anyMatcher(), '0');
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
		return PatchouliAPI.get().makeMultiblock(pattern, targets);
	}
}
