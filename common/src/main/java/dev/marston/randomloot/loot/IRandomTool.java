package dev.marston.randomloot.loot;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockState;

public interface IRandomTool extends ItemLike {
    float getAttackSpeed(ItemStack stack, LootUtils.ToolType type);
    boolean isCorrectToolForDrops(ItemStack stack, BlockState state);
}
