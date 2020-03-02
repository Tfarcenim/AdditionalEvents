package com.tfar.additionalevents.mixin;

import com.tfar.additionalevents.event.DropLootEvent;
import com.tfar.additionalevents.event.GetCollisionVoxelShapesEvent;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BlockState.class)
abstract class MixinBlockState {
	@Inject(method = "getCollisionShape(Lnet/minecraft/world/IBlockReader;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/shapes/ISelectionContext;)Lnet/minecraft/util/math/shapes/VoxelShape;",
					at = @At("HEAD"),
					cancellable = true)
	public void fireGetCollisionVoxelShapesEvent(IBlockReader world, BlockPos pos, ISelectionContext context, CallbackInfoReturnable<VoxelShape> cir) {
		BlockState state = (BlockState) (Object) this;
		VoxelShape shape = state.getBlock().getCollisionShape(state, world, pos, context);
		GetCollisionVoxelShapesEvent event = new GetCollisionVoxelShapesEvent(world, state, pos, context, shape);
		MinecraftForge.EVENT_BUS.post(event);
		if (!shape.equals(event.getShape())) {
			cir.setReturnValue(event.getShape());
		}
	}

	@Inject(method = "getDrops",at = @At("RETURN"), cancellable = true)
	public void fireDropLootEvent(LootContext.Builder contextBuilder, CallbackInfoReturnable<List<ItemStack>> cir){
		List<ItemStack> drops = cir.getReturnValue();
		// Build the loot context and gather info about the block from it.
		LootContext context = contextBuilder.withParameter(LootParameters.BLOCK_STATE, (BlockState)(Object)this).build(LootParameterSets.BLOCK);
		ServerWorld world = context.getWorld();
		BlockPos pos = context.get(LootParameters.POSITION);
		BlockState state = context.get(LootParameters.BLOCK_STATE);
		Entity dropCause = context.get(LootParameters.THIS_ENTITY);
		PlayerEntity player = dropCause instanceof PlayerEntity ? (PlayerEntity) dropCause : null;

		DropLootEvent dropEvent = new DropLootEvent(world, pos, state, contextBuilder.build(LootParameterSets.BLOCK), player, drops);
		MinecraftForge.EVENT_BUS.post(dropEvent);
		cir.setReturnValue(dropEvent.getDrops());
	}
}
