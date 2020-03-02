package com.tfar.additionalevents.mixin;

import com.tfar.additionalevents.event.GenerateLootEvent;
import com.tfar.additionalevents.event.SpawnBlockDropsEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(Block.class)
public abstract class MixinBlock {
	@Shadow public abstract ResourceLocation getLootTable();

	@Inject(method = "getDrops(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/storage/loot/LootContext$Builder;)Ljava/util/List;",
					at = @At("HEAD"),
					cancellable = true)
	private void fireGenerateLootEvent(BlockState state, LootContext.Builder builder, CallbackInfoReturnable<List<ItemStack>> cir) {
		// Gather the info we need from the loot context.
		ServerWorld world = builder.getWorld();
		BlockPos pos = builder.get(LootParameters.POSITION);
		builder.withParameter(LootParameters.BLOCK_STATE,state);

		// Create and post a loot generation event to the event bus, then use the event to generate the block drops.
		LootTable table = world.getServer().getLootTableManager().getLootTableFromLocation(getLootTable());
		GenerateLootEvent genEvent = new GenerateLootEvent(world, pos, state, table, builder);
		boolean generate = !MinecraftForge.EVENT_BUS.post(genEvent);
		LootContext context = genEvent.getContextBuilder().build(LootParameterSets.BLOCK);
		table = genEvent.getTable();
		List<ItemStack> drops = new ArrayList<>();
		if (generate) drops.addAll(table.generate(context));

		cir.setReturnValue(drops);
	}

	@Redirect(
					method = "spawnDrops(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/tileentity/TileEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)V",
					at = @At(value = "INVOKE",
									target = "Lnet/minecraft/block/Block;getDrops(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/server/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/tileentity/TileEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)Ljava/util/List;")
	)
	private static List<ItemStack> fireSpawnBlockDropsEvent(BlockState state, ServerWorld world, BlockPos pos, TileEntity tileEntity, Entity entity, ItemStack stack) {
		List<ItemStack> drops = Block.getDrops(state, world, pos, tileEntity, entity, stack);
		SpawnBlockDropsEvent event = new SpawnBlockDropsEvent(world, pos, state, tileEntity,entity,stack,drops);
		MinecraftForge.EVENT_BUS.post(event);
		return event.getDrops();
	}
}
