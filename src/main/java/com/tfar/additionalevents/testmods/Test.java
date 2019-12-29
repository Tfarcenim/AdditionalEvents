package com.tfar.additionalevents.testmods;

import com.tfar.additionalevents.event.DropLootEvent;
import com.tfar.additionalevents.event.GetCollisionVoxelShapesEvent;
import com.tfar.additionalevents.event.ItemEntityDamageEvent;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraftforge.eventbus.api.SubscribeEvent;

//uncomment to test
//@Mod.EventBusSubscriber
public class Test {
  @SubscribeEvent
  public static void event1(DropLootEvent e){
   e.getDrops().add(new ItemStack(Items.CLAY_BALL));
  }

  @SubscribeEvent
  public static void event2(GetCollisionVoxelShapesEvent e){
   if (e.getState().getMaterial() == Material.WATER && e.getContext().getEntity() instanceof LivingEntity){
     e.setShape(VoxelShapes.fullCube());
   }
  }

  @SubscribeEvent
  public static void event3(ItemEntityDamageEvent e){
   if (e.getSource() == DamageSource.CACTUS || e.getSource() == DamageSource.LAVA)e.setAmount(0);
  }
}
