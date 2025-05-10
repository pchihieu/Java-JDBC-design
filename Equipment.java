package com.vgb;

import java.util.UUID;

/**
 * Represents physical equipment assets to be bought and sold
 */

public abstract class Equipment extends Item{

	protected String modelNumber;
	protected double retailPrice;

	public Equipment(UUID uuid, String name, String modelNumber, double retailPrice) {
		super(uuid, name);
		this.modelNumber = modelNumber;
		this.retailPrice = retailPrice;
	}


	public String getModelNumber() {
		return modelNumber;
	}

	public double getRetailPrice() {
		return retailPrice;
	}
	

	@Override
	public String toString() {
		return String.format("%s--- UUID: %s, Model Number: %s, Price: %.2d", name, uuid, modelNumber, retailPrice);
	}
	
	
	
	
	
	
}
