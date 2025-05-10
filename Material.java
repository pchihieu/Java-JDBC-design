package com.vgb;

import java.util.UUID;

/**
 * Represents materials needed for construction
 */
public class Material extends Item{
	
	private String unit;
	private double costPerUnit;
	private double unitsPurchased;
	
	public Material(UUID uuid, String name, String unit, double costPerUnit, int unitsPurchased) {
		super(uuid, name);
		this.unit = unit;
		this.costPerUnit = costPerUnit;
		this.unitsPurchased = unitsPurchased;
	}

	public Material(Material mat, double unitsPurchased) {
		super(mat.getUuid(), mat.getName());
		this.unit = mat.getUnit();
		this.costPerUnit = mat.getCostPerUnit();
		this.unitsPurchased = unitsPurchased;
	}

	public String getUnit() {
		return unit;
	}

	public double getCostPerUnit() {
		return costPerUnit;
	}
	
	public double getUnitsPurchased() {
		return unitsPurchased;
	}

	/**
	 * returns the tax value of the units purchased
	 */
	@Override
	public double getTax() {
		double unroundedValue = (double) getUnitsPurchased() * (double) getCostPerUnit() * 0.0715;
		double roundedValue = Math.round(unroundedValue * 100) / 100.00;		
		return roundedValue;
	}

	/**
	 * Returns the un-taxed value of the units purchased
	 */
	@Override
	public double getSubtotal() {
	    double unroundedValue = (double) getUnitsPurchased() * (double) getCostPerUnit();
        double roundedValue = (unroundedValue * 100) / 100.00;
        return roundedValue;
	}

	@Override
	public String toString() {
		String matString = String.format("%s (Material) %s\n%10s @ %s/%s", uuid, name, unitsPurchased, costPerUnit, unit);
		return matString;
	}
	
	
	
	
	
	
	
}
