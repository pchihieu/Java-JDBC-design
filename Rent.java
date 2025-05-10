package com.vgb;

import java.util.UUID;

/**
 * Represents a type of equipment that is 
 * rented out 
 */
public class Rent extends Equipment{

	private double hoursRented;
	

	public Rent(UUID uuid, String name, String modelNumber, double retailPrice, double hoursRented) {
		super(uuid, name, modelNumber, retailPrice);
		this.hoursRented = hoursRented;
	}


	public Rent(Purchase foundItem, double hoursRented) {
		super(foundItem.getUuid(), foundItem.getName(), foundItem.getModelNumber(), foundItem.getRetailPrice());
		this.hoursRented = hoursRented;
	}


	public double getHoursRented() {
		return hoursRented;
	}
	
	/**
	 * Calculates the subtotal of a rent, 
	 * using the per hour charge and the 
	 * hours rented
	 */
	@Override
	public double getSubtotal() {
	    double perHourCharge = 0.001 * getRetailPrice();
	    double unrounded = perHourCharge * getHoursRented();
	    double roundedValue = Math.round(unrounded * 100) / 100.00;
	    return roundedValue;
	}

	/**
	 * Gets the tax total of the rented equipment
	 * 
	 */
	@Override
	public double getTax() {
		double unrounded = getSubtotal() * 0.0438;		
        double roundedValue = Math.round(unrounded * 100) / 100.00;

        return roundedValue;
	}


	@Override
	public String toString() {
		String matString = String.format("%s (Rental) %s-%s\n%10.2f hours @ %.2f an hour", uuid, name, modelNumber, hoursRented, retailPrice);
		return matString;
	}
	
	

}
