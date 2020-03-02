package com.tfar.additionalevents.mixin;

import com.tfar.additionalevents.event.ItemEntityDamageEvent;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(ItemEntity.class)
abstract class MixinItemEntity {

	@ModifyVariable(method = "attackEntityFrom",at = @At("HEAD"))
	private float fireDamageItemEvent(float changedAmount,DamageSource source,float amount){
		ItemEntity entity = (ItemEntity)(Object)this;
		ItemEntityDamageEvent event = new ItemEntityDamageEvent(entity, source, amount);
		if (!entity.getItem().isEmpty() && entity.getItem().getItem() == Items.NETHER_STAR && source.isExplosion()) event.setCanceled(true);
		return !MinecraftForge.EVENT_BUS.post(event) ? event.getAmount() : 0F;
	}
}
