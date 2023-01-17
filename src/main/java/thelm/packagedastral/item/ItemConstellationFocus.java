package thelm.packagedastral.item;

import hellfirepvp.astralsorcery.common.entities.EntityItemHighlighted;
import hellfirepvp.astralsorcery.common.item.base.ItemHighlighted;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thelm.packagedastral.PackagedAstral;
import thelm.packagedauto.client.IModelRegister;

public class ItemConstellationFocus extends Item implements ItemHighlighted, IModelRegister {

	public static final ItemConstellationFocus INSTANCE = new ItemConstellationFocus();
	public static final ModelResourceLocation MODEL_LOCATION = new ModelResourceLocation("packagedastral:constellation_focus#inventory");

	protected ItemConstellationFocus() {
		setRegistryName("packagedastral:constellation_focus");
		setTranslationKey("packagedastral.constellation_focus");
		setCreativeTab(PackagedAstral.CREATIVE_TAB);
	}

	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		return true;
	}

	@Override
	public Entity createEntity(World world, Entity location, ItemStack stack) {
		EntityItemHighlighted entity = new EntityItemHighlighted(world, location.posX, location.posY, location.posZ, stack);
		entity.setDefaultPickupDelay();
		entity.motionX = location.motionX;
		entity.motionY = location.motionY;
		entity.motionZ = location.motionZ;
		if(location instanceof EntityItem) {
			entity.setThrower(((EntityItem)location).getThrower());
			entity.setOwner(((EntityItem)location).getOwner());
		}
		return entity;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0, MODEL_LOCATION);
	}
}
