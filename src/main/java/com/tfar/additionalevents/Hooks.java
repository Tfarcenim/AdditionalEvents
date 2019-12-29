package com.tfar.additionalevents;

import com.tfar.additionalevents.event.DropLootEvent;
import com.tfar.additionalevents.event.GenerateLootEvent;
import com.tfar.additionalevents.event.GetCollisionVoxelShapesEvent;
import com.tfar.additionalevents.event.ItemEntityDamageEvent;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

public class Hooks {

  public static List<ItemStack> fireBlockDropLootEvent(LootContext.Builder contextBuilder, BlockState state1) {

    List<ItemStack> drops = new ArrayList<>(state1.getBlock().getDrops(state1,contextBuilder));
    // Build the loot context and gather info about the block from it.
    LootContext context = contextBuilder.withParameter(LootParameters.BLOCK_STATE, state1).build(LootParameterSets.BLOCK);
    ServerWorld world = context.getWorld();
    BlockPos pos = context.get(LootParameters.POSITION);
    BlockState state = context.get(LootParameters.BLOCK_STATE);
    Entity dropCause = context.get(LootParameters.THIS_ENTITY);
    PlayerEntity player = dropCause instanceof PlayerEntity ? (PlayerEntity) dropCause : null;

    DropLootEvent dropEvent = new DropLootEvent(world, pos, state, contextBuilder.build(LootParameterSets.BLOCK), player, drops);
    MinecraftForge.EVENT_BUS.post(dropEvent);
    return dropEvent.getDrops();
  }

  public static List<ItemStack> fireBlockGenerateLootEvent(LootTable table, LootContext.Builder contextBuilder)
  {
    // Gather the info we need from the loot context.
    ServerWorld world = contextBuilder.getWorld();
    BlockPos pos = contextBuilder.get(LootParameters.POSITION);
    BlockState state = contextBuilder.get(LootParameters.BLOCK_STATE);

    // Create and post a loot generation event to the event bus, then use the event to generate the block drops.
    GenerateLootEvent genEvent = new GenerateLootEvent(world, pos, state, table, contextBuilder);
    boolean generate = !MinecraftForge.EVENT_BUS.post(genEvent);
    LootContext context = genEvent.getContextBuilder().build(LootParameterSets.BLOCK);
    table = genEvent.getTable();
    List<ItemStack> drops = new ArrayList<>();
    if(generate) drops.addAll(table.generate(context));

    return drops;
  }

  public static VoxelShape fireCollisionShapeEvent(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context){
    VoxelShape shape = state.getBlock().getCollisionShape(state,world,pos,context);
    GetCollisionVoxelShapesEvent getCollisionVoxelShapesEvent = new GetCollisionVoxelShapesEvent(world,state,pos,context,shape);
    MinecraftForge.EVENT_BUS.post(getCollisionVoxelShapesEvent);
    return getCollisionVoxelShapesEvent.getShape();
  }

  public static float onEntityItemDamage(ItemEntity entity, DamageSource source, float amount) {
    ItemEntityDamageEvent event = new ItemEntityDamageEvent(entity, source, amount);
    if (!entity.getItem().isEmpty() && entity.getItem().getItem() == Items.NETHER_STAR && source.isExplosion()) event.setCanceled(true);
    return !MinecraftForge.EVENT_BUS.post(event) ? event.getAmount() : 0F;
  }

  public static void hook(boolean a){

  }
}
