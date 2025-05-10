package com.vgb;

import java.util.UUID;

/**
 * Represents a Contract for an action done by VGB
 */

public class Contract extends Item{
	
	private Company subcontractor;
	private double price;
	
	public Contract(UUID uuid, String name, Company subcontractor, double price) {
		super(uuid, name);
		this.subcontractor = subcontractor;
		this.price = price;
	}
	
	public Contract(Contract c, double price) {
		super(c.getUuid(), c.getName());
		this.subcontractor = c.getSubcontractor();
		this.price = price;
	
	}
	
	public Company getSubcontractor() {
		return subcontractor;
	}
	
	public double getPrice() {
		return price;
	}
	
	/**
	 * These two Overrides return the non-tax and the flat subtotal respectively
	 */
	@Override
	public double getTax() {
		return 0;
	}
	
	@Override
	public double getSubtotal() {
		return getPrice();
	}
	
	@Override
	public String toString() {
		String rentString = String.format("%s (%s)", name, uuid);
		return rentString;
	}

}

