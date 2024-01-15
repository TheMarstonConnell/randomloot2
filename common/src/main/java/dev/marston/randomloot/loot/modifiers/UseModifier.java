package dev.marston.randomloot.loot.modifiers;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public interface UseModifier extends Modifier {
	public void use(UseOnContext ctx);

	public void use(Level level, Player player, InteractionHand hand);

	public boolean useAnywhere();
}
