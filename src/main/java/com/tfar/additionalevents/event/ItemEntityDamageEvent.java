package com.tfar.additionalevents.event;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.item.ItemEvent;
import net.minecraftforge.eventbus.api.Cancelable;

/**
 * Event that is fired when an EntityItem is damaged from an outside source.
 * This event is called after the EntityItem is checked for invulnerability to the DamageSource.<br>
 * <br>
 * {@link #source} contains the {@link DamageSource} that caused this entity to be damaged.<br>
 * {@link #amount} contains the final amount of damage that will be dealt to the Entity.<br>
 * <br>
 * This event is {@link Cancelable}.<br>
 * If this event is canceled, the EntityItem is not damaged.
 */
@Cancelable
public class ItemEntityDamageEvent extends ItemEvent {
	private final DamageSource source;
	private float amount;

	/**
	 * Creates a new event for an EntityItem that is taking damage.
	 *
	 * @param entityItem The EntityItem being damaged.
	 * @param source     The {@link DamageSource} source of the damage.
	 * @param amount     The amount of damage being dealt.
	 */
	public ItemEntityDamageEvent(ItemEntity entityItem, DamageSource source, float amount) {
		super(entityItem);
		this.source = source;
		this.amount = amount;
	}

	public DamageSource getSource() {
		return source;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}
}