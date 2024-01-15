package dev.marston.randomloot.loot.modifiers.breakers;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import dev.marston.randomloot.loot.LootUtils.ToolType;
import dev.marston.randomloot.loot.LootUtils;
import dev.marston.randomloot.loot.modifiers.BlockBreakModifier;
import dev.marston.randomloot.loot.modifiers.Modifier;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;

public class Explode implements BlockBreakModifier {

	private String name;
	private float power;
	private final static String POWER = "power";

	public Explode(String name, float power) {
		this.name = name;
		this.power = power;
	}

	public Explode() {
		this.name = "Explosive";
		this.power = 2.0f;
	}

	public Modifier clone() {
		return new Explode();
	}

	@Override
	public boolean startBreak(ItemStack itemstack, BlockPos pos, LivingEntity player) {

		Level l = player.level();

		l.explode(player, null, null, pos.getX(), pos.getY(), pos.getZ(), power, false, ExplosionInteraction.BLOCK);

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
		return new Explode(tag.getString(NAME), tag.getFloat(POWER));
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public String tagName() {
		return "explode";
	}

	@Override
	public String color() {
		return "red";
	}

	@Override
	public String description() {
		return "Upon breaking a block (allowed by tool type), the current block position will explode causing damage to surrounding blocks.";
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
