package com.tfar.additionalevents.event;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Cancelable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Cancelable
public class SpawnBlockDropsEvent extends BlockEvent {

	private final TileEntity tileEntity;
	private final Entity entity;
	private final ItemStack stack;
	private final List<ItemStack> drops;

	/**
	 * Use this for controlling if items spawn in world or not, cancelling will return an empty list
	 *
	 * @param world world where harvest occurred
	 * @param pos the position of the harvest
	 * @param state the harvested state
	 * @param tileEntity the block entity attached to the harvested block, can be null
	 * @param entity the harvester, can null in cases such as explosions
	 * @param stack the stack used to cause the drops
	 * @param drops the list of items to spawn in world
	 */
	public SpawnBlockDropsEvent(ServerWorld world, BlockPos pos, BlockState state,
															TileEntity tileEntity, Entity entity, ItemStack stack, List<ItemStack> drops) {
		super(world, pos, state);
		this.tileEntity = tileEntity;
		this.entity = entity;
		this.stack = stack;
		this.drops = drops;
	}

	@Nullable
	public TileEntity getTileEntity() {
		return tileEntity;
	}

	@Nullable
	public Entity getEntity() {
		return entity;
	}

	public ItemStack getStack() {
		return stack;
	}

	public List<ItemStack> getDrops() {
		return isCanceled() ? new ArrayList<>() : drops;
	}
}
