package com.tfar.additionalevents.testmods;

import com.tfar.additionalevents.event.*;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

//uncomment to test
//@Mod.EventBusSubscriber
public class Test {
	//@SubscribeEvent
	public static void event1(DropLootEvent e) {
		e.getDrops().add(new ItemStack(Items.CLAY_BALL));
	}

	//@SubscribeEvent
	public static void event2(GetCollisionVoxelShapesEvent e) {
		if (e.getState().getMaterial() == Material.WATER && e.getContext().getEntity() instanceof LivingEntity) {
			e.setShape(VoxelShapes.fullCube());
		}
	}

	//@SubscribeEvent
	public static void event3(GenerateLootEvent e) {
		if (e.getState().getBlock() == Blocks.COBBLESTONE) {
			LootTable table = ((World) e.getWorld()).getServer().getLootTableManager().getLootTableFromLocation(new ResourceLocation("chests/simple_dungeon"));
			e.setTable(table);
		}
	}

	//@SubscribeEvent
	public static void event4(ItemEntityDamageEvent e) {
		if (e.getSource() == DamageSource.CACTUS || e.getSource() == DamageSource.LAVA) e.setAmount(0);
	}

	//@SubscribeEvent
	public static void event5(SpawnBlockDropsEvent e) {
		if (e.getEntity() instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) e.getEntity();
			e.getDrops().removeIf(player::addItemStackToInventory);
		}
	}
}
