package thelm.packagedastral.config;

import java.io.File;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thelm.packagedastral.tile.TileAttunementCrafter;
import thelm.packagedastral.tile.TileConstellationCrafter;
import thelm.packagedastral.tile.TileDiscoveryCrafter;
import thelm.packagedastral.tile.TileTraitCrafter;

public class PackagedAstralConfig {

	private PackagedAstralConfig() {}

	public static Configuration config;

	public static void init(File file) {
		MinecraftForge.EVENT_BUS.register(PackagedAstralConfig.class);
		config = new Configuration(file);
		config.load();
		init();
	}

	public static void init() {
		String category;
		category = "blocks.discovery_crafter";
		TileDiscoveryCrafter.enabled = config.get(category, "enabled", TileDiscoveryCrafter.enabled, "Should the Luminous Package Crafter be enabled.").setRequiresMcRestart(true).getBoolean();
		TileDiscoveryCrafter.energyCapacity = config.get(category, "energy_capacity", TileDiscoveryCrafter.energyCapacity, "How much FE the Luminous Package Crafter should hold.", 0, Integer.MAX_VALUE).getInt();
		TileDiscoveryCrafter.energyReq = config.get(category, "energy_req", TileDiscoveryCrafter.energyReq, "How much FE the Luminous Package Crafter should use.", 0, Integer.MAX_VALUE).getInt();
		TileDiscoveryCrafter.energyUsage = config.get(category, "energy_usage", TileDiscoveryCrafter.energyUsage, "How much FE/t maximum the Luminous Package Crafter should use.", 0, Integer.MAX_VALUE).getInt();
		TileDiscoveryCrafter.starlightCapacity = config.get(category, "starlight_capacity", TileDiscoveryCrafter.starlightCapacity, "How much starlight the Luminous Package Crafter should hold.", 1000, Integer.MAX_VALUE).getInt();
		TileDiscoveryCrafter.craftingEffects = config.get(category, "crafting_effects", TileDiscoveryCrafter.craftingEffects, "Should the Luminous Package Crafter do crafting effects.").getBoolean();
		TileDiscoveryCrafter.requiresNight = config.get(category, "requires_night", TileDiscoveryCrafter.requiresNight, "Should the Luminous Package Crafter require night to start nighttime recipes.").getBoolean();
		TileDiscoveryCrafter.drawMEEnergy = config.get(category, "draw_me_energy", TileDiscoveryCrafter.drawMEEnergy, "Should the Luminous Package Crafter draw energy from ME systems.").getBoolean();
		category = "blocks.attunement_crafter";
		TileAttunementCrafter.enabled = TileDiscoveryCrafter.enabled && config.get(category, "enabled", TileAttunementCrafter.enabled, "Should the Starlight Package Crafting Altar be enabled (requires previous tiers).").setRequiresMcRestart(true).getBoolean();
		TileAttunementCrafter.energyCapacity = config.get(category, "energy_capacity", TileAttunementCrafter.energyCapacity, "How much FE the Starlight Package Crafting Altar should hold.", 0, Integer.MAX_VALUE).getInt();
		TileAttunementCrafter.energyReq = config.get(category, "energy_req", TileAttunementCrafter.energyReq, "How much FE the Starlight Package Crafting Altar should use.", 0, Integer.MAX_VALUE).getInt();
		TileAttunementCrafter.energyUsage = config.get(category, "energy_usage", TileAttunementCrafter.energyUsage, "How much FE/t maximum the Starlight Package Crafting Altar should use.", 0, Integer.MAX_VALUE).getInt();
		TileAttunementCrafter.starlightCapacity = config.get(category, "starlight_capacity", TileAttunementCrafter.starlightCapacity, "How much starlight the Starlight Package Crafting Altar should hold.", 2000, Integer.MAX_VALUE).getInt();
		TileAttunementCrafter.requiresStructure = config.get(category, "requires_structure", TileAttunementCrafter.requiresStructure, "Should the Starlight Package Crafting Altar require the structure and do crafting effects.").getBoolean();
		TileAttunementCrafter.requiresNight = config.get(category, "requires_night", TileAttunementCrafter.requiresNight, "Should the Starlight Package Crafting Altar require night to start nighttime recipes.").getBoolean();
		TileAttunementCrafter.drawMEEnergy = config.get(category, "draw_me_energy", TileAttunementCrafter.drawMEEnergy, "Should the Starlight Package Crafting Altar draw energy from ME systems.").getBoolean();
		category = "blocks.constellation_crafter";
		TileConstellationCrafter.enabled = TileAttunementCrafter.enabled && config.get(category, "enabled", TileConstellationCrafter.enabled, "Should the Celestial Package Crafting Altar be enabled (requires previous tiers).").setRequiresMcRestart(true).getBoolean();
		TileConstellationCrafter.energyCapacity = config.get(category, "energy_capacity", TileConstellationCrafter.energyCapacity, "How much FE the Celestial Package Crafting Altar should hold.", 0, Integer.MAX_VALUE).getInt();
		TileConstellationCrafter.energyReq = config.get(category, "energy_req", TileConstellationCrafter.energyReq, "How much FE the Celestial Package Crafting Altar should use.", 0, Integer.MAX_VALUE).getInt();
		TileConstellationCrafter.energyUsage = config.get(category, "energy_usage", TileConstellationCrafter.energyUsage, "How much FE/t maximum the Celestial Package Crafting Altar should use.", 0, Integer.MAX_VALUE).getInt();
		TileConstellationCrafter.starlightCapacity = config.get(category, "starlight_capacity", TileConstellationCrafter.starlightCapacity, "How much starlight the Celestial Package Crafting Altar should hold.", 4000, Integer.MAX_VALUE).getInt();
		TileConstellationCrafter.requiresStructure = config.get(category, "requires_structure", TileConstellationCrafter.requiresStructure, "Should the Celestial Package Crafting Altar require the structure and do crafting effects.").getBoolean();
		TileConstellationCrafter.requiresNight = config.get(category, "requires_night", TileConstellationCrafter.requiresNight, "Should the Celestial Package Crafting Altar require night to start nighttime recipes.").getBoolean();
		TileConstellationCrafter.drawMEEnergy = config.get(category, "draw_me_energy", TileConstellationCrafter.drawMEEnergy, "Should the Celestial Package Crafting Altar draw energy from ME systems.").getBoolean();
		category = "blocks.trait_crafter";
		TileTraitCrafter.enabled = TileConstellationCrafter.enabled && config.get(category, "enabled", TileTraitCrafter.enabled, "Should the Iridescent Package Crafting Altar be enabled (requires previous tiers).").setRequiresMcRestart(true).getBoolean();
		TileTraitCrafter.energyCapacity = config.get(category, "energy_capacity", TileTraitCrafter.energyCapacity, "How much FE the Iridescent Package Crafting Altar should hold.", 0, Integer.MAX_VALUE).getInt();
		TileTraitCrafter.energyReq = config.get(category, "energy_req", TileTraitCrafter.energyReq, "How much FE the Iridescent Package Crafting Altar should use.", 0, Integer.MAX_VALUE).getInt();
		TileTraitCrafter.energyUsage = config.get(category, "energy_usage", TileTraitCrafter.energyUsage, "How much FE/t maximum the Iridescent Package Crafting Altar should use.", 0, Integer.MAX_VALUE).getInt();
		TileTraitCrafter.starlightCapacity = config.get(category, "starlight_capacity", TileTraitCrafter.starlightCapacity, "How much starlight the Iridescent Package Crafting Altar should hold.", 8000, Integer.MAX_VALUE).getInt();
		TileTraitCrafter.requiresStructure = config.get(category, "requires_structure", TileTraitCrafter.requiresStructure, "Should the Iridescent Package Crafting Altar require the structure and do crafting effects.").getBoolean();
		TileTraitCrafter.requiresNight = config.get(category, "requires_night", TileTraitCrafter.requiresNight, "Should the Iridescent Package Crafting Altar require night to start nighttime recipes.").getBoolean();
		TileTraitCrafter.drawMEEnergy = config.get(category, "draw_me_energy", TileTraitCrafter.drawMEEnergy, "Should the Iridescent Package Crafting Altar draw energy from ME systems.").getBoolean();
		if(config.hasChanged()) {
			config.save();
		}
	}

	@SubscribeEvent
	public void onConfigChanged(OnConfigChangedEvent event) {
		if(event.getModID().equals("packagedastral")) {
			init();
		}
	}
}
