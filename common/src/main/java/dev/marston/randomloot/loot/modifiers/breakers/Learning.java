package dev.marston.randomloot.loot.modifiers.breakers;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import dev.marston.randomloot.loot.LootUtils.ToolType;
import dev.marston.randomloot.loot.LootUtils;
import dev.marston.randomloot.loot.modifiers.BlockBreakModifier;
import dev.marston.randomloot.loot.modifiers.Modifier;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Learning implements BlockBreakModifier {

	private String name;
	private int count;
	private final static String COUNT = "count";
	private static final int max = 10;
	private int points;
	private final static String POINTS = "points";

	public Learning(String name, int count, int points) {
		this.name = name;
		this.count = count;
		this.points = points;
	}

	public Learning() {
		this.name = "Learning";
		this.count = 0;
		this.points = 3;
	}

	public Modifier clone() {
		return new Learning();
	}

	@Override
	public boolean startBreak(ItemStack itemstack, BlockPos pos, LivingEntity entity) {

		if (!(entity instanceof Player)) {
			return false;
		}

		Player player = (Player) entity;

		this.count++;

		while (count >= max) {
			count = count - max;
			player.giveExperiencePoints(this.points);
		}

		LootUtils.addModifier(itemstack, this);
		return false;
	}

	@Override
	public CompoundTag toNBT() {

		CompoundTag tag = new CompoundTag();

		tag.putInt(COUNT, count);
		tag.putInt(POINTS, points);
		tag.putString(NAME, name);

		return tag;
	}

	@Override
	public Modifier fromNBT(CompoundTag tag) {
		return new Learning(tag.getString(NAME), tag.getInt(COUNT), tag.getInt(POINTS));
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public String tagName() {
		return "learning";
	}

	@Override
	public String color() {
		return "green";
	}

	@Override
	public String description() {
		return "After breaking " + max + " blocks as allowed by this tool, gain " + points + " experience points.";
	}

	@Override
	public void writeToLore(List<Component> list, boolean shift) {

		MutableComponent comp = Modifier.makeComp(this.name(), this.color());
		list.add(comp);

	}

	@Override
	public Component writeDetailsToLore(@Nullable Level level) {

		float amt = ((float) count) / ((float) max) * 100;
		String perc = String.format("%.0f%% Learned", amt);

		return Modifier.makeComp(perc, ChatFormatting.GRAY);
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
