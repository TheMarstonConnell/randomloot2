package dev.marston.randomloot.loot.modifiers.hurter;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import dev.marston.randomloot.loot.LootUtils.ToolType;
import dev.marston.randomloot.loot.LootUtils;
import dev.marston.randomloot.loot.modifiers.EntityHurtModifier;
import dev.marston.randomloot.loot.modifiers.Modifier;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Draining implements EntityHurtModifier {
	private String name;
	private int points;
	private final static String POINTS = "points";

	public Draining(String name, int points) {
		this.name = name;
		this.points = points;
	}

	public Draining() {
		this.name = "Necrotic";
		this.points = 2;
	}

	public Modifier clone() {
		return new Draining();
	}

	@Override
	public CompoundTag toNBT() {

		CompoundTag tag = new CompoundTag();

		tag.putInt(POINTS, points);
		tag.putString(NAME, name);

		return tag;
	}

	@Override
	public Modifier fromNBT(CompoundTag tag) {
		return new Draining(tag.getString(NAME), tag.getInt(POINTS));
	}

	@Override
	public String name() {
		if (points == 2) {
			return name;
		}
		return name + " " + LootUtils.roman(points - 1);
	}

	@Override
	public String tagName() {
		return "necrotic";
	}

	@Override
	public String color() {
		return ChatFormatting.RED.getName();
	}

	@Override
	public String description() {
		return "Heals " + String.format("%.0f", drain() * 100) + "% of damage dealt to target.";
	}

	public float drain() {
		return ((float) this.points) * 0.05f;
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
		return type.equals(LootUtils.ToolType.SWORD) || type.equals(LootUtils.ToolType.AXE);
	}

	@Override
	public boolean hurtEnemy(ItemStack itemstack, LivingEntity hurtee, LivingEntity hurter) {
		float damage = LootUtils.getAttackDamage(itemstack, LootUtils.getToolType(itemstack));

		hurter.heal(damage * drain());
		return false;
	}

	public boolean canLevel() {
		return this.points < 10;
	}

	public void levelUp() {
		this.points++;
	}
}
