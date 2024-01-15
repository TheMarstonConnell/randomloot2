package dev.marston.randomloot.loot.modifiers;

import java.util.HashMap;
import java.util.Set;

import dev.marston.randomloot.RandomLoot;
import dev.marston.randomloot.loot.modifiers.stats.Busted;
import dev.marston.randomloot.loot.modifiers.users.DirtPlace;
import dev.marston.randomloot.loot.modifiers.users.FireBall;
import dev.marston.randomloot.loot.modifiers.users.FirePlace;
import dev.marston.randomloot.loot.modifiers.users.TorchPlace;
import dev.marston.randomloot.loot.modifiers.breakers.Attracting;
import dev.marston.randomloot.loot.modifiers.breakers.Explode;
import dev.marston.randomloot.loot.modifiers.breakers.Learning;
import dev.marston.randomloot.loot.modifiers.breakers.Melting;
import dev.marston.randomloot.loot.modifiers.breakers.Veiny;
import dev.marston.randomloot.loot.modifiers.holders.Effect;
import dev.marston.randomloot.loot.modifiers.holders.Hasty;
import dev.marston.randomloot.loot.modifiers.holders.Healing;
import dev.marston.randomloot.loot.modifiers.holders.Rainy;
import dev.marston.randomloot.loot.modifiers.hurter.Bezerk;
import dev.marston.randomloot.loot.modifiers.hurter.Charging;
import dev.marston.randomloot.loot.modifiers.hurter.Combo;
import dev.marston.randomloot.loot.modifiers.hurter.Critical;
import dev.marston.randomloot.loot.modifiers.hurter.Draining;
import dev.marston.randomloot.loot.modifiers.hurter.Fire;
import dev.marston.randomloot.loot.modifiers.hurter.HurtEffect;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffects;

public class ModifierRegistry {

	public static HashMap<String, Modifier> Modifiers = new HashMap<String, Modifier>();
	public static HashMap<String, Boolean> ModifierEnabled = new HashMap<String, Boolean>();

	public static Modifier EXPLODE = register(new Explode());
	public static Modifier LEARNING = register(new Learning());
	public static Modifier ATTRACTING = register(new Attracting());
	public static Modifier VEINY = register(new Veiny());
	public static Modifier MELTING = register(new Melting());

	public static Modifier TORCH_PLACE = register(new TorchPlace());
	public static Modifier DIRT_PLACE = register(new DirtPlace());
	public static Modifier FIRE_PLACE = register(new FirePlace());
	public static Modifier FIRE_BALL = register(new FireBall());

	public static Modifier FLAMING = register(new Fire());
	public static Modifier CRITICAL = register(new Critical());
	public static Modifier CHARGING = register(new Charging());
	public static Modifier COMBO = register(new Combo());
	public static Modifier DRAINING = register(new Draining());
	public static Modifier POISONOUS = register(new HurtEffect("Poisonous", "poison", 5, MobEffects.POISON));
	public static Modifier WITHERING = register(new HurtEffect("Withering", "wither", 3, MobEffects.WITHER));
	public static Modifier BLINDING = register(new HurtEffect("Blinding", "blinding", 4, MobEffects.BLINDNESS));
	public static Modifier BEZERK = register(new Bezerk());

	public static Modifier HASTY = register(new Hasty());
	public static Modifier FILLING = register(new Effect("Filling", "filling", 2, MobEffects.SATURATION));
	public static Modifier ABSORBTION = register(new Effect("Appley", "absorption", 10, MobEffects.ABSORPTION));
	public static Modifier REGENERATING = register(new Effect("Healing", "regeneration", 3, MobEffects.REGENERATION));
	public static Modifier RESISTANT = register(new Effect("Resistant", "resistance", 1, MobEffects.DAMAGE_RESISTANCE));
	public static Modifier FIRE_RESISTANT = register(
			new Effect("Heat Resistant", "fire_resistance", 1, MobEffects.FIRE_RESISTANCE));
	public static Modifier RAINY = register(new Rainy());
	public static Modifier LIVING = register(new Healing());

	public static Modifier BUSTED = register(new Busted());

	public static Modifier UNBREAKING = register(new Unbreaking());

	public static final Set<Modifier> BREAKERS = Set.of(EXPLODE, LEARNING, ATTRACTING, VEINY, MELTING);
	public static final Set<Modifier> USERS = Set.of(TORCH_PLACE, DIRT_PLACE, FIRE_PLACE, FIRE_BALL);
	public static final Set<Modifier> HURTERS = Set.of(CRITICAL, CHARGING, FLAMING, COMBO, DRAINING, POISONOUS,
			WITHERING, BLINDING, BEZERK);
	public static final Set<Modifier> HOLDERS = Set.of(HASTY, ABSORBTION, FILLING, RAINY,
			LIVING);

	public static final Set<Modifier> STATS = Set.of(BUSTED);

	public static final Set<Modifier> MISC = Set.of(UNBREAKING);

	public static Modifier register(Modifier modifier) {

		String tagName = modifier.tagName();

		if (Modifiers.containsKey(tagName)) {
			RandomLoot.LOGGER.error("Cannot register modifier twice!");
			System.exit(1);
		}

		Modifiers.put(tagName, modifier);
		ModifierEnabled.put(tagName, true);

		return modifier;
	}

	public static Modifier loadModifier(String name, CompoundTag tag) {
		Modifier m = Modifiers.get(name);
		if (m == null) {
			return null;
		}

		return m.fromNBT(tag);
	}

}
