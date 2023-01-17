package thelm.packagedastral.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import thelm.packagedastral.config.PackagedAstralConfig;

public class GuiPackagedAstralConfig extends GuiConfig {

	public GuiPackagedAstralConfig(GuiScreen parent) {
		super(parent, getConfigElements(), "packagedastral", false, false, getAbridgedConfigPath(PackagedAstralConfig.config.toString()));
	}

	private static List<IConfigElement> getConfigElements() {
		ArrayList<IConfigElement> list = new ArrayList<>();
		for(String category : PackagedAstralConfig.config.getCategoryNames()) {
			list.add(new ConfigElement(PackagedAstralConfig.config.getCategory(category)));
		}
		return list;
	}
}
