package dev.marston.rl.forge;

import java.util.Map;

import dev.marston.randomloot.Globals;
import dev.marston.randomloot.RandomLoot;
import dev.marston.rl.forge.fmods.ModRegistry;
import dev.marston.rl.forge.recipes.Recipies;

import dev.marston.rl.forge.items.LootCase;
import dev.marston.rl.forge.items.LootRegistry;
import dev.marston.randomloot.loot.LootUtils;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

@Mod(RandomLoot.MODID)
public class RandomLootModForge {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, RandomLoot.MODID);

	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister
			.create(Registries.CREATIVE_MODE_TAB, RandomLoot.MODID);

	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister
			.create(Registries.RECIPE_SERIALIZER, RandomLoot.MODID);

	static RandomLootModForge INSTANCE;

	public RandomLootModForge() {

		if (INSTANCE != null) {
			throw new IllegalStateException();
		}
		INSTANCE = this;

		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

		bus.addListener((RegisterEvent event) -> {
			if (!event.getRegistryKey().equals(Registries.BLOCK)) {
				return;
			}
			Recipies.init(ForgeRegistries.RECIPE_SERIALIZERS);
		});

		bus.addListener(this::commonSetup);
		bus.addListener(this::addCreative);

		MinecraftForge.EVENT_BUS.register(this);

		ModLootModifiers.register(bus);

		ModRegistry.init();

		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ForgeConfig.SPEC);

		GenWiki.genWiki();

	}

	private void commonSetup(final FMLCommonSetupEvent event) {
		RandomLoot.LOGGER.info("RandomLoot Common Setup");

	}

	private void addCreative(BuildCreativeModeTabContentsEvent event) {
		if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES)
			event.accept(LootRegistry.CaseItem);
	}

	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event) {
		RandomLoot.LOGGER.info("Starting server with RandomLoot installed!");

		Globals.Seed = event.getServer().getWorldData().worldGenOptions().seed();
	}

	@Mod.EventBusSubscriber(modid = RandomLoot.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class ClientModEvents {
		@SubscribeEvent
		public static void onClientSetup(FMLClientSetupEvent event) {
			RandomLoot.LOGGER.info("Client is starting with RandomLoot installed!");

			event.enqueueWork(() -> {
				ItemProperties.register(LootRegistry.ToolItem, new ResourceLocation(RandomLoot.MODID, "cosmetic"),
						(stack, level, living, id) -> {
							return LootUtils.getTexture(stack);
						});
			});
		}
	}

	@EventBusSubscriber(modid = RandomLoot.MODID, bus = EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {

		@SubscribeEvent
		public static void registerItems(RegisterEvent event) {

			event.register(ForgeRegistries.Keys.ITEMS, helper -> {
				for (Map.Entry<String, Item> entry : LootRegistry.Items.entrySet()) {
					String key = entry.getKey();
					Item val = entry.getValue();

					helper.register(new ResourceLocation(RandomLoot.MODID, key), val);
				}

			});
			LootCase.initDispenser();

		}

	}
}
