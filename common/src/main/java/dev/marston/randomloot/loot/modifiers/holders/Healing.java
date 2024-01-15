package dev.marston.randomloot.loot.modifiers.holders;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import dev.marston.randomloot.loot.LootUtils.ToolType;
import dev.marston.randomloot.loot.modifiers.HoldModifier;
import dev.marston.randomloot.loot.modifiers.Modifier;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Healing implements HoldModifier {

	private String name;
	private float power;
	private final static String POWER = "power";

	public Healing(String name, float power) {
		this.name = name;
		this.power = power;
	}

	public Healing() {
		this.name = "Living";
		this.power = 0.005f;
	}

	public Modifier clone() {
		return new Healing();
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
		return new Healing(tag.getString(NAME), tag.getFloat(POWER));
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public String tagName() {
		return "living";
	}

	@Override
	public String color() {
		return ChatFormatting.GREEN.getName();
	}

	@Override
	public String description() {
		return "While holding the tool, it will randomly heal itself";
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
		return true;
	}

	@Override
	public void hold(ItemStack stack, Level level, Entity holder) {

		float f = level.getRandom().nextFloat();
		if (f < power) {

			if (stack.getDamageValue() == 0) {
				return;
			}

			stack.setDamageValue(Math.max(stack.getDamageValue() - 1, 0));

			if (f < power / 5) {
				MutableComponent comp = Modifier.makeComp("pssst...", ChatFormatting.GRAY);
				comp = comp.withStyle(ChatFormatting.ITALIC);

				holder.sendSystemMessage(comp);
			}

		}

	}

	public boolean canLevel() {
		return false;
	}

	public void levelUp() {
		return;
	}
}
