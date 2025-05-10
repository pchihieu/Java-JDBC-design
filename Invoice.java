package com.vgb;
 
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;
 
public class Invoice {
	
	private UUID uuid;
	private Company customer;
	private Person salesperson;
	private LocalDate date;
	private ArrayList<Item> items; 
	
	public Invoice(UUID uuid, Company customer, Person salesperson, LocalDate date) {
		this.uuid = uuid;
		this.customer = customer;
		this.salesperson = salesperson;
		this.date = date;
		this.items = new ArrayList<Item>();
	}
 
	public UUID getUuid() {
		return uuid;
	}
 
	public Company getCustomer() {
		return customer;
	}
 
	public Person getSalesperson() {
		return salesperson;
	}
 
	public LocalDate getDate() {
		return date;
	}
	
	public ArrayList<Item> getItems() {
		return items;
	}
 
    public void addItem(Item item) {
        items.add(item);
    }
 
    @Override
    public String toString() {
        String customerName = (customer != null) ? customer.getName() : "Unknown";
        String customerUuid = (customer != null) ? customer.getUuid().toString() : "Unknown";
        String contactName = (customer != null && customer.getContact() != null) ? 
            customer.getContact().getFirstName() + " " + customer.getContact().getLastName() : "Unknown";
        String contactEmails = (customer != null && customer.getContact() != null) ? 
            String.join(", ", customer.getContact().getEmails()) : "Unknown";
        String address = (customer != null && customer.getAddress() != null) ? 
            customer.getAddress().toString() : "Unknown";
        String salespersonName = (salesperson != null) ? 
            salesperson.getFirstName() + " " + salesperson.getLastName() : "Unknown";
        String salespersonEmails = (salesperson != null) ? 
            String.join(", ", salesperson.getEmails()) : "Unknown";

        return String.format("Invoice #    %s\nDate:  %s\n"
                + "Customer:  %s (%s)\n"
                + "%s, (%s)\nAddress: %s\n\n"
                + "Salesperson: %s (%s)", 
                getUuid(), getDate(), customerName, customerUuid, 
                contactName, contactEmails, address,
                salespersonName, salespersonEmails);
    }
	
    /**
     * Calculates the subtotal of every item in the invoice
     * @return
     */
    public double calculateSubtotal() {
        double subtotal = 0;
        for (Item item : items) {
            subtotal += item.getSubtotal();
        }
        return subtotal;
    }
 
    /**
     * Calculates the total taxes of every item in the invoice
     * @return
     */
    public double calculateTaxTotal() {
        double taxTotal = 0;
        for (Item item : items) {
        	taxTotal += item.getTax();
        }
        return taxTotal;
    }
 
    public double calculateGrandTotal() {
        return calculateSubtotal() + calculateTaxTotal();
    }

	/**
	 * Calculate the total item of one invoice
	 */
    public int countItem() {
    	return this.items.size();
    }
    
	
	
 
 
}