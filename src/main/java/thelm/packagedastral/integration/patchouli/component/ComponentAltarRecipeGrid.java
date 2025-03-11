package thelm.packagedastral.integration.patchouli.component;

import java.awt.Color;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.gson.annotations.SerializedName;

import hellfirepvp.astralsorcery.client.util.RenderConstellation;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.crafting.ItemHandle;
import hellfirepvp.astralsorcery.common.crafting.altar.AbstractAltarRecipe;
import hellfirepvp.astralsorcery.common.crafting.altar.AltarRecipeRegistry;
import hellfirepvp.astralsorcery.common.crafting.altar.recipes.AttunementRecipe;
import hellfirepvp.astralsorcery.common.crafting.altar.recipes.ConstellationRecipe;
import hellfirepvp.astralsorcery.common.crafting.altar.recipes.DiscoveryRecipe;
import hellfirepvp.astralsorcery.common.crafting.altar.recipes.TraitRecipe;
import hellfirepvp.astralsorcery.common.crafting.helper.ShapedRecipeSlot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.VariableHolder;
import vazkii.patchouli.client.book.gui.GuiBook;

public class ComponentAltarRecipeGrid implements ICustomComponent {
	
	public static final ResourceLocation DISCOVERY_GRID = new ResourceLocation("packagedastral:textures/gui/patchouli/discovery_grid.png");
	public static final ResourceLocation ATTUNEMENT_GRID = new ResourceLocation("packagedastral:textures/gui/patchouli/attunement_grid.png");
	public static final ResourceLocation CONSTELLATION_GRID = new ResourceLocation("packagedastral:textures/gui/patchouli/constellation_grid.png");
	public static final ResourceLocation TRAIT_GRID = new ResourceLocation("packagedastral:textures/gui/patchouli/trait_grid.png");
	public static final RenderConstellation.BrightnessFunction BRIGHTNESS_FUNCTION = new RenderConstellation.BrightnessFunction() {
		@Override
		public float getBrightness() {
			return 0.5F;
		}
	};

	@VariableHolder
	@SerializedName("recipe")
	public String recipeRaw;
	transient AbstractAltarRecipe altarRecipe;
	transient int x;
	transient int y;

	@Override
	public void build(int componentX, int componentY, int pageNum) {
		altarRecipe = AltarRecipeRegistry.getRecipeSlow(new ResourceLocation("astralsorcery", "shaped/"+recipeRaw));
		if(altarRecipe == null) {
			altarRecipe = AltarRecipeRegistry.getRecipeSlow(new ResourceLocation("astralsorcery", "shaped/internal/altar/"+recipeRaw));
		}
		x = componentX < 0 ? 9 : componentX;
		y = componentY < 0 ? 12 : componentY;
	}

	@Override
	public void render(IComponentRenderContext context, float partialTicks, int mouseX, int mouseY) {
		if(altarRecipe != null) {
			Minecraft mc = Minecraft.getMinecraft();
			GlStateManager.enableBlend();
			switch(altarRecipe.getNeededLevel()) {
			case DISCOVERY:
				mc.renderEngine.bindTexture(DISCOVERY_GRID);
				break;
			case ATTUNEMENT:
				mc.renderEngine.bindTexture(ATTUNEMENT_GRID);
				break;
			case CONSTELLATION_CRAFT:
				mc.renderEngine.bindTexture(CONSTELLATION_GRID);
				break;
			case TRAIT_CRAFT:
			case BRILLIANCE:
				mc.renderEngine.bindTexture(TRAIT_GRID);
				break;
			}
			Gui.drawModalRectWithCustomSizedTexture(x-1, y-1, 1, 1, 100, 119, 128, 128);
			if(altarRecipe instanceof TraitRecipe) {
				TraitRecipe recipe = (TraitRecipe)altarRecipe;
				IConstellation constellation = recipe.getRequiredConstellation();
				if(constellation != null) {
//					GlStateManager.disableAlpha();
					RenderConstellation.renderConstellationIntoGUI(Color.BLACK, constellation, x, y, 0, 98, 98, 2, BRIGHTNESS_FUNCTION, true, false);
//					GlStateManager.enableAlpha();
				}
				List<Ingredient> ingredients = Lists.transform(recipe.getTraitItemHandles(), ItemHandle::getRecipeIngredient);
				float degreePerInput = 360F/ingredients.size();
				int ticksElapsed = ((GuiBook)context.getGui()).ticksInBook;
				float currentDegree = (GuiScreen.isShiftKeyDown() ? ticksElapsed : ticksElapsed+partialTicks) - 90;
				for(Ingredient ingredient : ingredients) {
					double radians = Math.toRadians(currentDegree);
					double xPos = x+3+2*19 + Math.cos(radians)*48;
					double yPos = y+3+2*19 + Math.sin(radians)*48;
					GlStateManager.pushMatrix();
					GlStateManager.translate(xPos-MathHelper.floor(xPos), yPos-MathHelper.floor(yPos), 0);
					context.renderIngredient(MathHelper.floor(xPos), MathHelper.floor(yPos), mouseX, mouseY, ingredient);
					GlStateManager.popMatrix();
					currentDegree += degreePerInput;
				}
				ItemHandle handle;
				handle = recipe.getInnerTraitItemHandle(TraitRecipe.TraitRecipeSlot.UPPER_CENTER);
				if(handle != null) {
					context.renderIngredient(x+3+2*19, y+3+0*19, mouseX, mouseY, handle.getRecipeIngredient());
				}
				handle = recipe.getInnerTraitItemHandle(TraitRecipe.TraitRecipeSlot.LEFT_CENTER);
				if(handle != null) {
					context.renderIngredient(x+3+0*19, y+3+2*19, mouseX, mouseY, handle.getRecipeIngredient());
				}
				handle = recipe.getInnerTraitItemHandle(TraitRecipe.TraitRecipeSlot.RIGHT_CENTER);
				if(handle != null) {
					context.renderIngredient(x+3+4*19, y+3+2*19, mouseX, mouseY, handle.getRecipeIngredient());
				}
				handle = recipe.getInnerTraitItemHandle(TraitRecipe.TraitRecipeSlot.LOWER_CENTER);
				if(handle != null) {
					context.renderIngredient(x+3+2*19, y+3+4*19, mouseX, mouseY, handle.getRecipeIngredient());
				}
			}
			if(altarRecipe instanceof ConstellationRecipe) {
				ConstellationRecipe recipe = (ConstellationRecipe)altarRecipe;
				ItemHandle handle;
				handle = recipe.getCstItemHandle(ConstellationRecipe.ConstellationAtlarSlot.UP_UP_LEFT);
				if(handle != null) {
					context.renderIngredient(x+3+1*19, y+3+0*19, mouseX, mouseY, handle.getRecipeIngredient());
				}
				handle = recipe.getCstItemHandle(ConstellationRecipe.ConstellationAtlarSlot.UP_UP_RIGHT);
				if(handle != null) {
					context.renderIngredient(x+3+3*19, y+3+0*19, mouseX, mouseY, handle.getRecipeIngredient());
				}
				handle = recipe.getCstItemHandle(ConstellationRecipe.ConstellationAtlarSlot.UP_LEFT_LEFT);
				if(handle != null) {
					context.renderIngredient(x+3+0*19, y+3+1*19, mouseX, mouseY, handle.getRecipeIngredient());
				}
				handle = recipe.getCstItemHandle(ConstellationRecipe.ConstellationAtlarSlot.UP_RIGHT_RIGHT);
				if(handle != null) {
					context.renderIngredient(x+3+4*19, y+3+1*19, mouseX, mouseY, handle.getRecipeIngredient());
				}
				handle = recipe.getCstItemHandle(ConstellationRecipe.ConstellationAtlarSlot.DOWN_LEFT_LEFT);
				if(handle != null) {
					context.renderIngredient(x+3+0*19, y+3+3*19, mouseX, mouseY, handle.getRecipeIngredient());
				}
				handle = recipe.getCstItemHandle(ConstellationRecipe.ConstellationAtlarSlot.DOWN_RIGHT_RIGHT);
				if(handle != null) {
					context.renderIngredient(x+3+4*19, y+3+3*19, mouseX, mouseY, handle.getRecipeIngredient());
				}
				handle = recipe.getCstItemHandle(ConstellationRecipe.ConstellationAtlarSlot.DOWN_DOWN_LEFT);
				if(handle != null) {
					context.renderIngredient(x+3+1*19, y+3+4*19, mouseX, mouseY, handle.getRecipeIngredient());
				}
				handle = recipe.getCstItemHandle(ConstellationRecipe.ConstellationAtlarSlot.DOWN_DOWN_RIGHT);
				if(handle != null) {
					context.renderIngredient(x+3+3*19, y+3+4*19, mouseX, mouseY, handle.getRecipeIngredient());
				}
			}
			if(altarRecipe instanceof AttunementRecipe) {
				AttunementRecipe recipe = (AttunementRecipe)altarRecipe;
				ItemHandle handle;
				handle = recipe.getAttItemHandle(AttunementRecipe.AttunementAltarSlot.UPPER_LEFT);
				if(handle != null) {
					context.renderIngredient(x+3+0*19, y+3+0*19, mouseX, mouseY, handle.getRecipeIngredient());
				}
				handle = recipe.getAttItemHandle(AttunementRecipe.AttunementAltarSlot.UPPER_RIGHT);
				if(handle != null) {
					context.renderIngredient(x+3+4*19, y+3+0*19, mouseX, mouseY, handle.getRecipeIngredient());
				}
				handle = recipe.getAttItemHandle(AttunementRecipe.AttunementAltarSlot.LOWER_LEFT);
				if(handle != null) {
					context.renderIngredient(x+3+0*19, y+3+4*19, mouseX, mouseY, handle.getRecipeIngredient());
				}
				handle = recipe.getAttItemHandle(AttunementRecipe.AttunementAltarSlot.LOWER_RIGHT);
				if(handle != null) {
					context.renderIngredient(x+3+4*19, y+3+4*19, mouseX, mouseY, handle.getRecipeIngredient());
				}
			}
			if(altarRecipe instanceof DiscoveryRecipe) {
				DiscoveryRecipe recipe = (DiscoveryRecipe)altarRecipe;
				ItemHandle handle;
				for(ShapedRecipeSlot srs : ShapedRecipeSlot.values()) {
					handle = recipe.getNativeRecipe().getExpectedStackHandle(srs);
					if(handle != null) {
						int i = srs.ordinal();
						context.renderIngredient(x+3+(1+i%3)*19, y+3+(1+i/3)*19, mouseX, mouseY, handle.getRecipeIngredient());
					}
				}
			}
		}
	}
}
