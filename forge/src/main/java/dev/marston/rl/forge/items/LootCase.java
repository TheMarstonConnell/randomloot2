package dev.marston.rl.forge.items;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import dev.marston.randomloot.loot.LootUtils;
import dev.marston.randomloot.loot.modifiers.Modifier;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.StatType;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public class LootCase extends Item {

	public static void initDispenser() {
		DispenserBlock.registerBehavior(LootRegistry.CaseItem, new DefaultDispenseItemBehavior() {
			public ItemStack execute(BlockSource source, ItemStack stack) {
				Direction direction = source.state().getValue(DispenserBlock.FACING);
				Position position = DispenserBlock.getDispensePosition(source);

				ItemStack tool = LootUtils.genTool(null, source.level(), (LootItem) LootRegistry.ToolItem, LootRegistry.CaseItem); // generate tool and give it to the player

				spawnTool(source.level(), tool, 6, direction, position);

				return ItemStack.EMPTY;
			}
		});
	}

	public static void spawnTool(Level level, ItemStack stack, int speed, Direction direction, Position pos) {
		double d0 = pos.x();
		double d1 = pos.y();
		double d2 = pos.z();
		if (direction.getAxis() == Direction.Axis.Y) {
			d1 -= 0.125D;
		} else {
			d1 -= 0.15625D;
		}

		ItemEntity itementity = new ItemEntity(level, d0, d1, d2, stack);
		double d3 = level.random.nextDouble() * 0.1D + 0.2D;
		itementity.setDeltaMovement(
				level.random.triangle((double) direction.getStepX() * d3, 0.0172275D * (double) speed),
				level.random.triangle(0.2D, 0.0172275D * (double) speed),
				level.random.triangle((double) direction.getStepZ() * d3, 0.0172275D * (double) speed));
		level.addFreshEntity(itementity);
	}

	public LootCase() {
		super(new Properties().stacksTo(1));
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return true;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack lootCase = player.getItemInHand(hand);

		if (player instanceof ServerPlayer) {
			ServerPlayer sPlayer = (ServerPlayer) player;
			StatType<Item> itemUsed = Stats.ITEM_USED;

			sPlayer.getStats().increment(sPlayer, itemUsed.get(LootRegistry.CaseItem), 1);
		}

		Modifier.TrackEntityParticle(level, player, ParticleTypes.CLOUD);

		if (!level.isClientSide) {
			LootUtils.generateTool((ServerPlayer) player, level,(LootItem) LootRegistry.ToolItem, LootRegistry.CaseItem); // generate tool and give it to the player
		}

		player.awardStat(Stats.ITEM_USED.get(this));
		if (!player.getAbilities().instabuild) {
			lootCase.shrink(1);
		}

		return InteractionResultHolder.sidedSuccess(lootCase, level.isClientSide());
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tipList, TooltipFlag flag) {

		MutableComponent comp = Modifier.makeComp("Right-click for loot!", ChatFormatting.GRAY);

		tipList.add(comp);

	}

}
