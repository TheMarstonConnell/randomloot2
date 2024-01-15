package dev.marston.randomloot.loot.modifiers;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import dev.marston.randomloot.loot.LootUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.Level;

public class Unbreaking implements Modifier {

	final String name;
	int level;

	final static String LEVEL = "trait_level";

	public Unbreaking(String name, int level) {
		this.name = name;
		this.level = level;
	}

	public Unbreaking() {
		this("Unbreaking", 0);
	}

	public String tagName() {
		return "unbreaking";
	}

	public void writeToLore(List<Component> list, boolean shift) {
		MutableComponent comp = Modifier.makeComp(this.name(), this.color());
		list.add(comp);
	}

	public Component writeDetailsToLore(@Nullable Level level) {
		return null;
	}

	public String description() {
		return "This tool has a " + String.format("%.0f", chance() * 100) + "% chance of not taking damage.";
	}

	public String name() {
		if (this.level == 0) {
			return this.name;
		}

		if (!this.canLevel()) {
			return "Unbreakable";
		}

		return this.name + " " + LootUtils.roman(this.level + 1);
	}

	public String color() {
		return ChatFormatting.AQUA.getName();
	}

	public Modifier clone() {
		return new Unbreaking(this.name, this.level);
	}

	public CompoundTag toNBT() {
		CompoundTag tag = new CompoundTag();

		tag.putString(NAME, name);
		tag.putInt(LEVEL, level);

		return tag;
	}

	public Modifier fromNBT(CompoundTag tag) {
		return new Unbreaking(tag.getString(NAME), tag.getInt(LEVEL));
	}

	public boolean compatible(Modifier mod) {
		return true;
	}

	public boolean forTool(LootUtils.ToolType type) {
		return true;
	}

	public boolean canLevel() {
		return this.level < 4;
	}

	public void levelUp() {
		this.level++;
	}

	private float chance() {
		return 0.2f * (float) (this.level + 1);
	}

	public boolean test(Level level) {
		float f = level.getRandom().nextFloat();

		return f <= chance();
	}
}
