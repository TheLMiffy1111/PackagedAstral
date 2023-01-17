package thelm.packagedastral.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thelm.packagedastral.PackagedAstral;
import thelm.packagedastral.tile.TileConstellationCrafter;
import thelm.packagedauto.block.BlockBase;
import thelm.packagedauto.tile.TileBase;

public class BlockConstellationCrafter extends BlockBase {

	public static final BlockConstellationCrafter INSTANCE = new BlockConstellationCrafter();
	public static final Item ITEM_INSTANCE = new ItemBlock(INSTANCE).setRegistryName("packagedastral:constellation_crafter");
	public static final ModelResourceLocation MODEL_LOCATION = new ModelResourceLocation("packagedastral:constellation_crafter#normal");

	public BlockConstellationCrafter() {
		super(Material.ROCK, MapColor.GRAY);
		setHardness(15F);
		setResistance(25F);
		setSoundType(SoundType.STONE);
		setTranslationKey("packagedastral.constellation_crafter");
		setRegistryName("packagedastral:constellation_crafter");
		setCreativeTab(PackagedAstral.CREATIVE_TAB);
	}

	@Override
	public TileBase createNewTileEntity(World world, int meta) {
		return new TileConstellationCrafter();
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity tileentity = world.getTileEntity(pos);
		if(tileentity instanceof TileConstellationCrafter) {
			((TileConstellationCrafter)tileentity).onBreak();
		}
		super.breakBlock(world, pos, state);
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(ITEM_INSTANCE, 0, MODEL_LOCATION);
	}
}
