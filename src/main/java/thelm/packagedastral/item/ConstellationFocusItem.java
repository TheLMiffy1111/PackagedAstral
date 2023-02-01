package thelm.packagedastral.item;

import java.awt.Color;

import hellfirepvp.astralsorcery.common.entity.item.EntityItemHighlighted;
import hellfirepvp.astralsorcery.common.lib.EntityTypesAS;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import thelm.packagedastral.PackagedAstral;

public class ConstellationFocusItem extends Item {

	public static final ConstellationFocusItem INSTANCE = new ConstellationFocusItem();

	protected ConstellationFocusItem() {
		super(new Item.Properties().group(PackagedAstral.ITEM_GROUP));
		setRegistryName("packagedastral:constellation_focus");
	}

	@Override
	public boolean hasCustomEntity(ItemStack stack) {
		return true;
	}

	@Override
	public Entity createEntity(World world, Entity location, ItemStack stack) {
		EntityItemHighlighted entity = new EntityItemHighlighted(EntityTypesAS.ITEM_HIGHLIGHT, world, location.getPosX(), location.getPosY(), location.getPosZ(), stack);
		entity.read(location.writeWithoutTypeId(new CompoundNBT()));
		entity.applyColor(Color.WHITE);
		if(location instanceof ItemEntity) {
			entity.setReplacedEntity((ItemEntity)location);
		}
		return entity;
	}
}
