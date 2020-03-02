package com.tfar.additionalevents.event;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Cancelable;

/**
 * Fired just before a block's loot is generated,
 * used for modifying the loot context and loot table that will be used to generate the loot for a block.
 * <p>
 * Cancelling this event will skip the loot generation entirely and simply pass an empty list to the block.
 */
@Cancelable
public class GenerateLootEvent extends BlockEvent {
	private final LootContext.Builder contextBuilder;
	private LootTable table;

	public GenerateLootEvent(World world, BlockPos pos, BlockState state, LootTable table, LootContext.Builder contextBuilder) {
		super(world, pos, state);
		this.table = table;
		this.contextBuilder = contextBuilder;
	}

	/**
	 * Get the loot context builder that will be used to generate the block's loot.
	 * Used to modify or gather data from the final loot context that will be built.
	 *
	 * @return The loot context builder that will be used for the generation of loot.
	 */
	public LootContext.Builder getContextBuilder() {
		return contextBuilder;
	}

	/**
	 * Get the loot table that was selected to generate loot for the block.
	 *
	 * @return the loot table being used to generate block loot.
	 */
	public LootTable getTable() {
		return table;
	}

	/**
	 * Set the loot table that will be used to generate loot for the block.
	 *
	 * @param table the new loot table that will be used to generate block loot.
	 */
	public void setTable(LootTable table) {
		this.table = table;
	}
}