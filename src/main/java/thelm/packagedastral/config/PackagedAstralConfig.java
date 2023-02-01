package thelm.packagedastral.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import thelm.packagedastral.tile.AttunementCrafterTile;
import thelm.packagedastral.tile.ConstellationCrafterTile;
import thelm.packagedastral.tile.DiscoveryCrafterTile;
import thelm.packagedastral.tile.TraitCrafterTile;

public class PackagedAstralConfig {

	private PackagedAstralConfig() {}

	private static ForgeConfigSpec serverSpec;

	public static ForgeConfigSpec.IntValue discoveryCrafterEnergyCapacity;
	public static ForgeConfigSpec.IntValue discoveryCrafterEnergyReq;
	public static ForgeConfigSpec.IntValue discoveryCrafterEnergyUsage;
	public static ForgeConfigSpec.BooleanValue discoveryCrafterCraftingEffects;
	public static ForgeConfigSpec.BooleanValue discoveryCrafterDrawMEEnergy;

	public static ForgeConfigSpec.IntValue attunementCrafterEnergyCapacity;
	public static ForgeConfigSpec.IntValue attunementCrafterEnergyReq;
	public static ForgeConfigSpec.IntValue attunementCrafterEnergyUsage;
	public static ForgeConfigSpec.BooleanValue attunementCrafterRequiresStructure;
	public static ForgeConfigSpec.BooleanValue attunementCrafterDrawMEEnergy;

	public static ForgeConfigSpec.IntValue constellationCrafterEnergyCapacity;
	public static ForgeConfigSpec.IntValue constellationCrafterEnergyReq;
	public static ForgeConfigSpec.IntValue constellationCrafterEnergyUsage;
	public static ForgeConfigSpec.BooleanValue constellationCrafterRequiresStructure;
	public static ForgeConfigSpec.BooleanValue constellationCrafterDrawMEEnergy;

	public static ForgeConfigSpec.IntValue traitCrafterEnergyCapacity;
	public static ForgeConfigSpec.IntValue traitCrafterEnergyReq;
	public static ForgeConfigSpec.IntValue traitCrafterEnergyUsage;
	public static ForgeConfigSpec.BooleanValue traitCrafterRequiresStructure;
	public static ForgeConfigSpec.BooleanValue traitCrafterDrawMEEnergy;

	public static void registerConfig() {
		buildConfig();
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, serverSpec);
	}

	private static void buildConfig() {
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

		builder.push("discovery_crafter");
		builder.comment("How much FE the Luminous Package Crafter should hold.");
		discoveryCrafterEnergyCapacity = builder.defineInRange("energy_capacity", 5000, 0, Integer.MAX_VALUE);
		builder.comment("How much total FE the Luminous Package Crafter should use per operation.");
		discoveryCrafterEnergyReq = builder.defineInRange("energy_req", 500, 0, Integer.MAX_VALUE);
		builder.comment("How much FE/t maximum the Luminous Package Crafter can use.");
		discoveryCrafterEnergyUsage = builder.defineInRange("energy_usage", 100, 0, Integer.MAX_VALUE);
		builder.comment("Should the Luminous Package Crafter do crafting effects.");
		discoveryCrafterCraftingEffects = builder.define("crafting_effects", true);
		builder.comment("Should the Luminous Package Crafter draw energy from ME systems.");
		discoveryCrafterDrawMEEnergy = builder.define("draw_me_energy", true);
		builder.pop();

		builder.push("attunement_crafter");
		builder.comment("How much FE the Starlight Package Crafting Altar should hold.");
		attunementCrafterEnergyCapacity = builder.defineInRange("energy_capacity", 5000, 0, Integer.MAX_VALUE);
		builder.comment("How much total FE the Starlight Package Crafting Altar should use per operation.");
		attunementCrafterEnergyReq = builder.defineInRange("energy_req", 500, 0, Integer.MAX_VALUE);
		builder.comment("How much FE/t maximum the Starlight Package Crafting Altar can use.");
		attunementCrafterEnergyUsage = builder.defineInRange("energy_usage", 100, 0, Integer.MAX_VALUE);
		builder.comment("Should the Starlight Package Crafting Altar require the structure and do crafting effects.");
		attunementCrafterRequiresStructure = builder.define("requires_structure", true);
		builder.comment("Should the Starlight Package Crafting Altar draw energy from ME systems.");
		attunementCrafterDrawMEEnergy = builder.define("draw_me_energy", true);
		builder.pop();

		builder.push("constellation_crafter");
		builder.comment("How much FE the Celestial Package Crafting Altar should hold.");
		constellationCrafterEnergyCapacity = builder.defineInRange("energy_capacity", 5000, 0, Integer.MAX_VALUE);
		builder.comment("How much total FE the Celestial Package Crafting Altar should use per operation.");
		constellationCrafterEnergyReq = builder.defineInRange("energy_req", 500, 0, Integer.MAX_VALUE);
		builder.comment("How much FE/t maximum the Celestial Package Crafting Altar can use.");
		constellationCrafterEnergyUsage = builder.defineInRange("energy_usage", 100, 0, Integer.MAX_VALUE);
		builder.comment("Should the Celestial Package Crafting Altar require the structure and do crafting effects.");
		constellationCrafterRequiresStructure = builder.define("requires_structure", true);
		builder.comment("Should the Celestial Package Crafting Altar draw energy from ME systems.");
		constellationCrafterDrawMEEnergy = builder.define("draw_me_energy", true);
		builder.pop();

		builder.push("trait_crafter");
		builder.comment("How much FE the Iridescent Package Crafting Altar should hold.");
		traitCrafterEnergyCapacity = builder.defineInRange("energy_capacity", 5000, 0, Integer.MAX_VALUE);
		builder.comment("How much total FE the Iridescent Package Crafting Altar should use per operation.");
		traitCrafterEnergyReq = builder.defineInRange("energy_req", 500, 0, Integer.MAX_VALUE);
		builder.comment("How much FE/t maximum the Iridescent Package Crafting Altar can use.");
		traitCrafterEnergyUsage = builder.defineInRange("energy_usage", 100, 0, Integer.MAX_VALUE);
		builder.comment("Should the Iridescent Package Crafting Altar require the structure and do crafting effects.");
		traitCrafterRequiresStructure = builder.define("requires_structure", true);
		builder.comment("Should the Iridescent Package Crafting Altar draw energy from ME systems.");
		traitCrafterDrawMEEnergy = builder.define("draw_me_energy", true);
		builder.pop();

		serverSpec = builder.build();
	}

	public static void reloadServerConfig() {
		DiscoveryCrafterTile.energyCapacity = discoveryCrafterEnergyCapacity.get();
		DiscoveryCrafterTile.energyReq = discoveryCrafterEnergyReq.get();
		DiscoveryCrafterTile.energyUsage = discoveryCrafterEnergyUsage.get();
		DiscoveryCrafterTile.craftingEffects = discoveryCrafterCraftingEffects.get();
		DiscoveryCrafterTile.drawMEEnergy = discoveryCrafterDrawMEEnergy.get();

		AttunementCrafterTile.energyCapacity = attunementCrafterEnergyCapacity.get();
		AttunementCrafterTile.energyReq = attunementCrafterEnergyReq.get();
		AttunementCrafterTile.energyUsage = attunementCrafterEnergyUsage.get();
		AttunementCrafterTile.requiresStructure = attunementCrafterRequiresStructure.get();
		AttunementCrafterTile.drawMEEnergy = attunementCrafterDrawMEEnergy.get();

		ConstellationCrafterTile.energyCapacity = constellationCrafterEnergyCapacity.get();
		ConstellationCrafterTile.energyReq = constellationCrafterEnergyReq.get();
		ConstellationCrafterTile.energyUsage = constellationCrafterEnergyUsage.get();
		ConstellationCrafterTile.requiresStructure = constellationCrafterRequiresStructure.get();
		ConstellationCrafterTile.drawMEEnergy = constellationCrafterDrawMEEnergy.get();

		TraitCrafterTile.energyCapacity = traitCrafterEnergyCapacity.get();
		TraitCrafterTile.energyReq = traitCrafterEnergyReq.get();
		TraitCrafterTile.energyUsage = traitCrafterEnergyUsage.get();
		TraitCrafterTile.requiresStructure = traitCrafterRequiresStructure.get();
		TraitCrafterTile.drawMEEnergy = traitCrafterDrawMEEnergy.get();
	}
}
