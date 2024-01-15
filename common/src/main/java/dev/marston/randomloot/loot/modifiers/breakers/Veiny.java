package dev.marston.randomloot.loot.modifiers.breakers;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jetbrains.annotations.Nullable;

import dev.marston.randomloot.loot.IRandomTool;
import dev.marston.randomloot.loot.LootUtils.ToolType;
import dev.marston.randomloot.loot.LootUtils;
import dev.marston.randomloot.loot.modifiers.BlockBreakModifier;
import dev.marston.randomloot.loot.modifiers.Modifier;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class Veiny implements BlockBreakModifier {

	private String name;
	private float power;
	private final static String POWER = "power";

	public Veiny(String name, float power) {
		this.name = name;
		this.power = power;
	}

	public Veiny() {
		this.name = "Veiny";
		this.power = 5.0f;
	}

	public Modifier clone() {
		return new Veiny();
	}

	private void removeBlock(ItemStack itemstack, BlockPos pos, ServerPlayer player, Level level, BlockState state) {

//		if (!state.canHarvestBlock(level, pos, player)) { // TODO: figure out whats up with this
//			return;
//		}

		state.getBlock().playerDestroy(level, player, pos, state, null, itemstack);
		level.removeBlock(pos, false);
	}

	public void checkAndBreak(ItemStack itemstack, BlockPos pos, Player player, Level level, int index, Block blockType,
			Set<BlockPos> tobreak) {

		if (index > power) {
			return;
		}

		BlockState startingState = level.getBlockState(pos);

		if (!startingState.is(blockType)) {
			return;
		}

		if (index > 0) {
			tobreak.add(pos);
		}

		int dex = index + 1;

		checkAndBreak(itemstack, pos.above(), player, level, dex, blockType, tobreak);
		checkAndBreak(itemstack, pos.below(), player, level, dex, blockType, tobreak);
		checkAndBreak(itemstack, pos.east(), player, level, dex, blockType, tobreak);
		checkAndBreak(itemstack, pos.west(), player, level, dex, blockType, tobreak);
		checkAndBreak(itemstack, pos.north(), player, level, dex, blockType, tobreak);
		checkAndBreak(itemstack, pos.south(), player, level, dex, blockType, tobreak);

	}

	@Override
	public boolean startBreak(ItemStack itemstack, BlockPos pos, LivingEntity p) {

		if (!(p instanceof ServerPlayer)) {
			return false;
		}

		ServerPlayer player = (ServerPlayer) p;

		if (!player.isCrouching()) {
			return false;
		}

		Level l = player.level();

		if (l.isClientSide) {
			return false;
		}

		BlockState state = l.getBlockState(pos);

		IRandomTool li = (IRandomTool) itemstack.getItem();

		if (!li.isCorrectToolForDrops(itemstack, state)) {
			return false;
		}

		Block b = state.getBlock();

		Set<BlockPos> toBreak = new HashSet<BlockPos>();

		checkAndBreak(itemstack, pos, player, l, 0, b, toBreak);

		for (Iterator<BlockPos> iterator = toBreak.iterator(); iterator.hasNext();) {
			BlockPos blockPos = iterator.next();

			removeBlock(itemstack, blockPos, player, l, l.getBlockState(blockPos));

			itemstack.hurtAndBreak(1, player, (entity) -> {

				entity.broadcastBreakEvent(EquipmentSlot.MAINHAND);
			});

		}
		return false;
	}

	@Override
	public CompoundTag toNBT() {

		CompoundTag tag = new CompoundTag();

		tag.putFloat(POWER, power);

		tag.putString(NAME, name);

		return tag;
	}

	@Override
	public Modifier fromNBT(CompoundTag tag) {
		return new Veiny(tag.getString(NAME), tag.getFloat(POWER));
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public String tagName() {
		return "veiny";
	}

	@Override
	public String color() {
		return ChatFormatting.DARK_GREEN.name();
	}

	@Override
	public String description() {
		return "Breaking any block while crouching will cause all blocks of the same type adjacent to it to break up to "
				+ ((int) power) + " in each direction.";
	}

	@Override
	public void writeToLore(List<Component> list, boolean shift) {

		MutableComponent comp = Modifier.makeComp(this.name(), this.color());

		list.add(comp);
	}

	@Override
	public Component writeDetailsToLore(@Nullable Level level) {

		return null;
	}

	@Override
	public boolean compatible(Modifier mod) {
		return true;
	}

	@Override
	public boolean forTool(ToolType type) {
		return type.equals(LootUtils.ToolType.PICKAXE) || type.equals(LootUtils.ToolType.AXE) || type.equals(LootUtils.ToolType.SHOVEL);
	}

	public boolean canLevel() {
		return false;
	}

	public void levelUp() {
		return;
	}
}
