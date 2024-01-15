package dev.marston.rl.forge;

import dev.marston.randomloot.Config;
import org.jetbrains.annotations.NotNull;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import dev.marston.rl.forge.items.LootRegistry;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

public class CaseModifier extends LootModifier {
	private final Item item;

	public static final Supplier<Codec<CaseModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(
			inst -> codecStart(inst).and(ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(m -> m.item))
					.apply(inst, CaseModifier::new)));

	protected CaseModifier(LootItemCondition[] conditionsIn, Item item) {
		super(conditionsIn);
		this.item = item;

	}

	@Override
	public Codec<? extends IGlobalLootModifier> codec() {
		return CODEC.get();
	}

	@Override
	protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot,
			LootContext context) {

		String path = context.getQueriedLootTableId().getPath();

		if (!path.contains("chests")) {
			return generatedLoot;
		}

		double chance;
		if (item == LootRegistry.CaseItem) {
			chance = Config.CaseChance;
		} else if (item == LootRegistry.ModAdd) {
			chance = Config.ModChance;
		} else {
			return generatedLoot;
		}

		if (context.getRandom().nextDouble() < chance) {
			generatedLoot.add(new ItemStack(item));
		}

		return generatedLoot;
	}
}