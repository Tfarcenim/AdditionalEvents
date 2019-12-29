package com.tfar.additionalevents.event;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 * This event is fired during {@link World#collide(AxisAlignedBB)}
 * and before returning the list in {@link World#getCollisionBoxes(Entity, AxisAlignedBB)}<br>
 * <br>
 * {@link #entity} contains the entity passed in the {@link World#getCollisionBoxes(Entity, AxisAlignedBB)}. <b>Can be null.</b> Calls from {@link World#collidesWithAnyBlock(AxisAlignedBB)} will be null.<br>
 * {@link #context} contains the SelectionContext passed in the method.<br>
 * {@link #shape} contains the current collision shape intersecting. The shape can be modified.<br>
 * <br>
 * This event is not {@link Cancelable}.<br>
 * <br>
 * This event does not have a result. {@link HasResult} <br>
 * <br>
 * This event is fired on the {@link MinecraftForge#EVENT_BUS}.<br>
 **/
public class GetCollisionVoxelShapesEvent extends Event {

  public final IBlockReader world;
  private final BlockState state;
  private final BlockPos pos;
  private final ISelectionContext context;
  private VoxelShape shape;

  public GetCollisionVoxelShapesEvent(IBlockReader world, BlockState state, BlockPos pos, ISelectionContext context,VoxelShape shape) {
    this.world = world;
    this.state = state;
    this.pos = pos;
    this.context = context;
    this.shape = shape;
  }

  public BlockPos getPos(){
    return pos;
  }
  public BlockState getState()
  {
    return state;
  }
  public ISelectionContext getContext()
  {
    return context;
  }
  public VoxelShape getShape()
  {
    return shape;
  }
  public void setShape(VoxelShape shape){
    this.shape = shape;
  }
}

