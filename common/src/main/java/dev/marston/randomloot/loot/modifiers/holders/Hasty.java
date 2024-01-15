package dev.marston.randomloot.loot.modifiers.holders;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import dev.marston.randomloot.loot.LootUtils.ToolType;
import dev.marston.randomloot.loot.LootUtils;
import dev.marston.randomloot.loot.modifiers.HoldModifier;
import dev.marston.randomloot.loot.modifiers.Modifier;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Hasty implements HoldModifier {

	private String name;
	private int power;
	private final static String POWER = "power";
	private final static String LEVEL = "trait_level";

	private int level = 0;

	public Hasty(String name, int power, int level) {
		this.name = name;
		this.power = power;
		this.level = level;
	}

	public Hasty() {
		this.name = "Hasty";
		this.power = 0;
		this.level = 0;
	}

	public Modifier clone() {
		return new Hasty();
	}

	@Override
	public CompoundTag toNBT() {

		CompoundTag tag = new CompoundTag();

		tag.putString(NAME, name);

		tag.putInt(POWER, power);

		tag.putInt(LEVEL, level);

		return tag;
	}

	@Override
	public Modifier fromNBT(CompoundTag tag) {
		return new Hasty(tag.getString(NAME), tag.getInt(POWER), tag.getInt(LEVEL));
	}

	@Override
	public String name() {

		if (this.level == 0) {
			return this.name;
		}

		return this.name + " " + LootUtils.roman(this.level + 1);

	}

	@Override
	public String tagName() {
		return "hasty";
	}

	@Override
	public String color() {
		return ChatFormatting.BLUE.getName();
	}

	@Override
	public String description() {
		return "While holding the tool, get the Haste " + LootUtils.roman(this.level + 1) + " effect.";
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

	@Override
	public void hold(ItemStack stack, Level level, Entity holder) {
		MobEffectInstance haste = new MobEffectInstance(MobEffects.DIG_SPEED, 2, power, true, false);

		if (holder instanceof LivingEntity) {
			LivingEntity le = (LivingEntity) holder;
			le.addEffect(haste);
		}
		if (holder instanceof Player) {
			Player p = (Player) holder;
			p.addEffect(haste);
		}

	}

	public boolean canLevel() {
		return level == 0;
	}

	public void levelUp() {
		this.level++;
		this.power++;
		return;
	}
}
