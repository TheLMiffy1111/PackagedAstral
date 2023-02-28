package thelm.packagedastral;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import thelm.packagedastral.block.TraitCrafterBlock;
import thelm.packagedastral.client.event.ClientEventHandler;
import thelm.packagedastral.event.CommonEventHandler;

@Mod(PackagedAstral.MOD_ID)
public class PackagedAstral {

	public static final String MOD_ID = "packagedastral";
	public static final ItemGroup ITEM_GROUP = new ItemGroup("packagedastral") {
		@OnlyIn(Dist.CLIENT)
		@Override
		public ItemStack makeIcon() {
			return new ItemStack(TraitCrafterBlock.INSTANCE);
		}
	};
	public static PackagedAstral core;

	public PackagedAstral() {
		core = this;
		CommonEventHandler.getInstance().onConstruct();
		DistExecutor.runWhenOn(Dist.CLIENT, ()->()->{
			ClientEventHandler.getInstance().onConstruct();
		});
	}
}
