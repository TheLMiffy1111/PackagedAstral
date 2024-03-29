package thelm.packagedastral.block;

import hellfirepvp.astralsorcery.common.structure.BlockStructureObserver;
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
import thelm.packagedastral.tile.TileAttunementCrafter;
import thelm.packagedauto.block.BlockBase;
import thelm.packagedauto.tile.TileBase;

public class BlockAttunementCrafter extends BlockBase implements BlockStructureObserver {

	public static final BlockAttunementCrafter INSTANCE = new BlockAttunementCrafter();
	public static final Item ITEM_INSTANCE = new ItemBlock(INSTANCE).setRegistryName("packagedastral:attunement_crafter");
	public static final ModelResourceLocation MODEL_LOCATION = new ModelResourceLocation("packagedastral:attunement_crafter#normal");

	public BlockAttunementCrafter() {
		super(Material.ROCK, MapColor.GRAY);
		setHardness(15F);
		setResistance(25F);
		setSoundType(SoundType.STONE);
		setTranslationKey("packagedastral.attunement_crafter");
		setRegistryName("packagedastral:attunement_crafter");
		setCreativeTab(PackagedAstral.CREATIVE_TAB);
	}

	@Override
	public TileBase createNewTileEntity(World world, int meta) {
		return new TileAttunementCrafter();
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity tileentity = world.getTileEntity(pos);
		if(tileentity instanceof TileAttunementCrafter) {
			((TileAttunementCrafter)tileentity).onBreak();
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
