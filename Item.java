package com.vgb;

import java.util.UUID;

/**
 * Represents and Item bought by VGB- 
 * "Item" and its subclasses represent
 * the various types of purchases
 * a company can make
 */
public abstract class Item {
	
	protected UUID uuid;
	protected String name;
	
	public Item(UUID uuid, String name) {
		super();
		this.uuid = uuid;
		this.name = name;
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getName() {
		return name;
	}
	
	

	@Override
	public String toString() {
		return String.format(name);
	}

	/**
	 * Gets the amount taxed to the buyer after the purchase is made: 
	 * implemented by subclasses
	 * @return
	 */
	public abstract double getTax();

	
	/**
	 * Gets the total cost without tax, implemented by subclasses
	 * @return
	 */
	public abstract double getSubtotal();

	public double getTotal() {
		return getSubtotal() + getTax();
	}
	
	
	
	
		
}
