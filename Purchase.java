package com.vgb;

import java.util.UUID;

/**
 * Represents the most basic type of equipment business- a Purchase, with flat
 * purchase and flat tax and that's it
 */
public class Purchase extends Equipment {

	public Purchase(UUID uuid, String name, String modelNumber, double retailPrice) {
		super(uuid, name, modelNumber, retailPrice);
	}

	public Purchase(Purchase p) {
		super(p.getUuid(), p.getName(), p.getModelNumber(), p.getRetailPrice());
	}

	/**
	 * Returns the simple tax amount from the purchase
	 */
	@Override
	public double getTax() {
		return Math.round(getRetailPrice() * 0.0525 * 100.0) / 100.0;
	}

	/**
	 * Simply returns the price of the equipment purchased
	 */
	@Override
	public double getSubtotal() {
		return Math.round(getRetailPrice() * 100.0) / 100.0;
	}

	@Override
	public String toString() {
		String purchaseString = String.format("%s (Purchase) %s-%s", uuid, name, modelNumber);
		return purchaseString;
	}

}
