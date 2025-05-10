package com.vgb;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * Represents a lease, a type of equipment
 */
public class Lease extends Equipment{

	private LocalDate startDate;
	private LocalDate endDate;


	public Lease(UUID uuid, String name, String modelNumber, double retailPrice, LocalDate startDate, LocalDate endDate) {
		super(uuid, name, modelNumber, retailPrice);
		this.startDate = startDate;
		this.endDate = endDate;
	}
	

	public Lease(Purchase foundItem, LocalDate startDate, LocalDate endDate) {
        super(foundItem.getUuid(), foundItem.getName(), foundItem.getModelNumber(), foundItem.getRetailPrice());
        this.startDate = startDate;
        this.endDate = endDate;
	}

	public LocalDate getStartDate() {
		return startDate;
	}


	public LocalDate getEndDate() {
		return endDate;
	}
	

	/**
	 * Gets the difference between start date and end date and returns
	 * an ametorized value to be taxed.
	 */
	@Override
	public double getSubtotal(){
		
		//Chronounit gives the difference between days, excluding the last day:
		// hence the +1
        double diffInDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        double ametorizedValue = diffInDays / 365 / 5;
        //Math.round was not working
        double totalValue = (ametorizedValue * getRetailPrice() * 1.5);
        int firstRounding = (int)(totalValue * 100);
        double roundedValue = ((double)firstRounding) / 100.00;
        return roundedValue;
	}

	/**
	 * Returns a basic tax rule
	 */
	@Override
	public double getTax() {
		if(getSubtotal() >= 12500) {
			return 1500;
		
		} else if( (getSubtotal() < 12500) && (getSubtotal() >= 5000 ) ){
			return 500;
		}
		else {
			return 0;
		}
	}


	@Override
	public String toString() {
		String leaseString = String.format("%s (Lease) %s-%s\nStart Date: %s, End Date: %s", uuid, name, modelNumber, startDate, endDate);
		return leaseString;
	}

	
	
	


}
