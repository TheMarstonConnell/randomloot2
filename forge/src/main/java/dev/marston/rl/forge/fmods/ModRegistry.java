package dev.marston.rl.forge.fmods;

import dev.marston.randomloot.RandomLoot;
import dev.marston.randomloot.loot.modifiers.Modifier;
import dev.marston.randomloot.loot.modifiers.ModifierRegistry;

import java.util.Set;

public class ModRegistry {

    public static Modifier ORE_FINDER = register(new OreFinder());
    public static Modifier SPAWNER_FINDER = register(new TreasureFinder());

    public static void init() {
        ModifierRegistry.HOLDERS.addAll(Set.of(ORE_FINDER, SPAWNER_FINDER));
    }

    public static Modifier register(Modifier modifier) {

        String tagName = modifier.tagName();

        if (ModifierRegistry.Modifiers.containsKey(tagName)) {
            RandomLoot.LOGGER.error("Cannot register modifier twice!");
            System.exit(1);
        }

        ModifierRegistry.Modifiers.put(tagName, modifier);
        ModifierRegistry.ModifierEnabled.put(tagName, true);

        return modifier;
    }
}
