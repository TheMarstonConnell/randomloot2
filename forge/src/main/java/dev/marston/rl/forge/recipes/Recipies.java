package dev.marston.rl.forge.recipes;

import dev.marston.randomloot.RandomLoot;
import dev.marston.rl.forge.RandomLootModForge;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.IForgeRegistry;

public final class Recipies {

	private Recipies() {

	}

	public static void init(IForgeRegistry<RecipeSerializer<?>> recipeSerializers) {
		RandomLoot.LOGGER.info("registering recipes!");
		register(recipeSerializers, new ResourceLocation(RandomLoot.MODID, "texture_change_recipe"),
				TextureChangeRecipe.getMySerializer());
		register(recipeSerializers, new ResourceLocation(RandomLoot.MODID, "trait_change"),
				TraitAdditionRecipe.getMySerializer());

	}

	private static void register(IForgeRegistry<RecipeSerializer<?>> registry, ResourceLocation id,
			RecipeSerializer<?> serializer) {
		registry.register(id, serializer);
	}
}
