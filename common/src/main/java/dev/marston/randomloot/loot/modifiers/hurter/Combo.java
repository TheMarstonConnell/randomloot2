package dev.marston.randomloot.loot.modifiers.hurter;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import dev.marston.randomloot.loot.LootUtils.ToolType;
import dev.marston.randomloot.loot.LootUtils;
import dev.marston.randomloot.loot.modifiers.EntityHurtModifier;
import dev.marston.randomloot.loot.modifiers.Modifier;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Combo implements EntityHurtModifier {
	private String name;
	private int points;
	private final static String POINTS = "points";
	private long charged;
	private final static String CHARGED = "charged";

	public Combo(String name, int points, long charged) {
		this.name = name;
		this.points = points;
		this.charged = charged;
	}

	public Combo() {
		this.name = "Dexterous";
		this.points = 2;
		this.charged = 0;
	}

	public Modifier clone() {
		return new Combo();
	}

	@Override
	public CompoundTag toNBT() {

		CompoundTag tag = new CompoundTag();

		tag.putInt(POINTS, points);
		tag.putString(NAME, name);
		tag.putLong(CHARGED, charged);
		return tag;
	}

	@Override
	public Modifier fromNBT(CompoundTag tag) {
		return new Combo(tag.getString(NAME), tag.getInt(POINTS), tag.getLong(CHARGED));
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public String tagName() {
		return "combo";
	}

	@Override
	public String color() {
		return ChatFormatting.YELLOW.getName();
	}

	@Override
	public String description() {
		return "Hitting enemies within " + this.points + " seconds after hitting them deals an extra 25% damage.";
	}

	@Override
	public void writeToLore(List<Component> list, boolean shift) {

		MutableComponent comp = Modifier.makeComp(this.name(), this.color());
		list.add(comp);

	}

	private float getCharge(Level level) {
		if (level != null) {
			long time = level.getGameTime();

			long diff = time - charged;

			float rate = (float) (diff) / (float) (points * 20);
			if (rate > 1.0f) {
				rate = 1.0f;
			}

			return rate;
		}

		return 0.0f;
	}

	@Override
	public Component writeDetailsToLore(@Nullable Level level) {

		if (level != null) {
			float charge = getCharge(level);

			String s = "Not Ready";
			if (charge < 1.0f) {
				s = "Ready";
			}

			return Modifier.makeComp(s, ChatFormatting.RED);
		}

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

		Level level = hurtee.level();

		long time = level.getGameTime();

		if (getCharge(level) < 1.0f) {

			float damage = LootUtils.getAttackDamage(itemstack, LootUtils.getToolType(itemstack));

			hurtee.hurt(hurter.damageSources().mobAttack(hurtee), damage * 0.25f);

			Modifier.TrackEntityParticle(level, hurtee, ParticleTypes.CRIT);

		}

		this.charged = time;
		LootUtils.addModifier(itemstack, this);

		return false;
	}

	public boolean canLevel() {
		return false;
	}

	public void levelUp() {
		return;
	}
}
