package dev.marston.rl.forge;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import dev.marston.randomloot.Config;
import dev.marston.randomloot.RandomLoot;
import dev.marston.randomloot.loot.modifiers.Modifier;
import dev.marston.randomloot.loot.modifiers.ModifierRegistry;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = RandomLoot.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgeConfig {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

	private static ForgeConfigSpec.DoubleValue CASE_CHANCE;

	private static ForgeConfigSpec.DoubleValue MOD_CHANCE;

	private static ForgeConfigSpec.DoubleValue GOODNESS;

	static final ForgeConfigSpec SPEC = build();

	public static ForgeConfigSpec build() {
		init();
		return BUILDER.build();
	}

	private static Map<String, ForgeConfigSpec.BooleanValue> MODIFIERS_ENABLED;

	public static void init() {

		BUILDER.push("Loot Chances");
		CASE_CHANCE = BUILDER.comment("chance to find a case in a chest.").defineInRange("caseChance", 0.25, 0.0, 1.0);
		MOD_CHANCE = BUILDER.comment("chance to find a modifier template in a chest.").defineInRange("modChance", 0.15,
				0.0, 1.0);
		BUILDER.pop();

		BUILDER.push("Modifiers Enabled");
		MODIFIERS_ENABLED = new HashMap<String, ForgeConfigSpec.BooleanValue>();

		for (Entry<String, Modifier> entry : ModifierRegistry.Modifiers.entrySet()) {
			String key = entry.getKey();
			Modifier mod = entry.getValue();

			MODIFIERS_ENABLED.put(key,
					BUILDER.comment("should the " + mod.name() + " trait be enabled").define(key + "_enabled", true));
		}
		BUILDER.pop();

		BUILDER.push("Misc");
		GOODNESS = BUILDER.comment("rate of tool improvement per player").defineInRange("goodness_rate", 1.0, 0.01,
				10.0);
		BUILDER.pop();
	}


	@SubscribeEvent
	static void onLoad(final ModConfigEvent event) {
		Config.CaseChance = CASE_CHANCE.get();
		Config.ModChance = MOD_CHANCE.get();
		Config.Goodness = GOODNESS.get();

		Config.ModsEnabled = new HashMap<String, Boolean>();
		for (Entry<String, BooleanValue> entry : MODIFIERS_ENABLED.entrySet()) {
			String key = entry.getKey();
			BooleanValue val = entry.getValue();

			Config.ModsEnabled.put(key, val.get());
		}

	}
}
