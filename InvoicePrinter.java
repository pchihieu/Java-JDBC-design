package com.vgb;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import com.vgb.InvoiceComparators;

public class InvoicePrinter {
	
	public static void printTotals(Map<UUID, Invoice> invoices) {
	try (PrintWriter writer = new PrintWriter(new FileWriter("data/output.txt"))) {
		
		writer.println("+-------------------------------+");
		writer.println("Summary Report :: By Total");
		writer.println("+-------------------------------+");
		String header = String.format("%-36s %-20s %-10s %-10s %-10s", "Invoice #", "Customer", "NumItems", "Tax", "Total");
		writer.println(header);
		double grandTaxTotal = 0;
		double grandTotal = 0;
		for(Invoice i : invoices.values()) {

			ArrayList<Item> invoiceItems = i.getItems();
			double tax = 0;
			double total = 0;
			for(Item item : invoiceItems) {
				tax += item.getTax();
				total += item.getTotal();
			}
			String line = String.format("%-36s %-20s %-10d %-10.2f %-10.2f", 
				    i.getUuid(), i.getCustomer().getName(), invoiceItems.size(), tax, total);			
			writer.println(line);
			grandTotal += total;
			grandTaxTotal += tax;
		}
		writer.println("+-------------------------------++-------------------------------++-------------------------------+");
        String totals = String.format("%-66s %-10.2f %-10.2f", "Grand Totals:", grandTaxTotal, grandTotal);
        writer.println(totals);
		
		writer.println();
		writer.println();
		writer.println("+-------------------------------+");
		writer.println("Company Invoice Summary Report");
		writer.println("+-------------------------------+");
		String companyHeader = String.format("%-25s %-20s %-10s", "Company", "Number of Invoices", "Total");
		writer.println(companyHeader);
		
		double companyTotals = 0;
		for(Invoice i : invoices.values()) {
			Company c = i.getCustomer();
			int numPurchases = i.getItems().size();
			ArrayList<Item> invoiceItems = i.getItems();
			double total = 0;
			for(Item item : invoiceItems) {
				total += item.getTotal();
			}
		
			String companyLine = String.format("%-25s %-20d %-10.2f", c.getName(), numPurchases, total);
			writer.println(companyLine);
			companyTotals += total;
		}
		
		writer.println("+-------------------------------++-------------------------------+");
        String moreTotals = String.format("%-25s %-20d %-10.2f", "Grand Totals:", invoices.values().size(), companyTotals);
        writer.println(moreTotals);
		
		writer.println();
		writer.println();
		
        for(Invoice i : invoices.values()) {
        	     	
	        writer.println(i);    	
	        writer.println();
	        String newHeader = String.format("Items: (%s) %50s %10s", i.getItems().size(), "Tax", "Total");
	        writer.println(newHeader);
	        writer.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-");
	        
			ArrayList<Item> invoiceItems = i.getItems();
			double tax = 0;
			double subtotal = 0;
			for(Item item : invoiceItems) {
				writer.println(item);
				writer.println(String.format("%60.2f %10.2f",item.getTax(), item.getSubtotal()));
				tax += item.getTax();
				subtotal += item.getSubtotal();
			}
			writer.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-");
			writer.println(String.format("Subtotals: %50.2f %10.2f",tax, subtotal));
			writer.println(String.format("Total: %65.2f",tax + subtotal));
			writer.println();
			writer.println();	
			writer.println();
        	
        }
        
        		
    } catch (IOException e) {
        e.printStackTrace();
    }

	}
	
	public static void printDataTotals(Map<Integer, Invoice> invoices) {
	try (PrintWriter writer = new PrintWriter(new FileWriter("data/output.txt"))) {
		writer.println("+-------------------------------+");
		writer.println("Summary Report :: By Total");
		writer.println("+-------------------------------+");
		String header = String.format("%-36s %-20s %-10s %-10s %-10s", "Invoice #", "Customer", "NumItems", "Tax", "Total");
		writer.println(header);
		int countEach = 0;
		int countTotal = 0;
		double grandTaxTotal = 0;
		double grandTotal = 0;
		for(Invoice i : invoices.values()) {
			
			countEach = i.countItem();
			countTotal = countEach + countTotal;
			double tax = i.calculateTaxTotal();
			double total = i.calculateSubtotal();
			String line = String.format("%-36s %-20s %-10d %-10.2f %-10.2f", 
				    i.getUuid(), i.getCustomer().getName(), countEach, tax, total);			
			writer.println(line);
			grandTotal += total;
			grandTaxTotal += tax;
		}

		writer.println("+-------------------------------++-------------------------------++-------------------------------+");
        String totals = String.format("%-66s %-10d %-10.2f %-10.2f", "Grand Totals:", countTotal, grandTaxTotal, grandTotal);
        writer.println(totals);
		
		writer.println();
		writer.println();
		writer.println("+-------------------------------+");
		writer.println("Company Invoice Summary Report");
		writer.println("+-------------------------------+");
		String companyHeader = String.format("%-25s %-20s %-10s", "Company", "Number of Invoices", "Total");
		writer.println(companyHeader);
		
		double companyTotals = 0;
		for(Invoice i : invoices.values()) {
			Company c = i.getCustomer();
			int numPurchases = i.getItems().size();
			ArrayList<Item> invoiceItems = i.getItems();
			double total = 0;
			for(Item item : invoiceItems) {
				total += item.getTotal();
			}
		
			String companyLine = String.format("%-25s %-20d %-10.2f", c.getName(), numPurchases, total);
			writer.println(companyLine);
			companyTotals += total;
		}
		
		writer.println("+-------------------------------++-------------------------------+");
        String moreTotals = String.format("%-25s %-20d %-10.2f", "Grand Totals:", invoices.values().size(), companyTotals);
        writer.println(moreTotals);
		
		writer.println();
		writer.println();
		
        for(Invoice i : invoices.values()) {
        	     	
	        writer.println(i);    	
	        writer.println();
	        String newHeader = String.format("Items: (%s) %50s %10s", i.getItems().size(), "Tax", "Total");
	        writer.println(newHeader);
	        writer.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-");
	        
			ArrayList<Item> invoiceItems = i.getItems();
			double tax = 0;
			double subtotal = 0;
			for(Item item : invoiceItems) {
				writer.println(item);
				writer.println(String.format("%60.2f %10.2f",item.getTax(), item.getSubtotal()));
				tax += item.getTax();
				subtotal += item.getSubtotal();
			}
			writer.println("-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-");
			writer.println(String.format("Subtotals: %50.2f %10.2f",tax, subtotal));
			writer.println(String.format("Total: %65.2f",tax + subtotal));
			writer.println();
			writer.println();	
			writer.println();
			
			
        	
        }
        
        		
    } catch (IOException e) {
        e.printStackTrace();
    }
	
	}
	/*
	 * sort print sorted list of invoices in order of total/name
	 * */
	public static void printSortedList(String title, SortedList<Invoice> sortedInvoices) {
	    System.out.printf("+-------------------------------------------------------------------------+\n");
	    System.out.printf("| %-71s |\n", title);
	    System.out.printf("+-------------------------------------------------------------------------+\n");
	    System.out.printf("%-35s %-25s %10s\n", "Invoice", "Customer", "Total");

	    for (Invoice inv : sortedInvoices) {
	        String invoiceUuid = inv.getUuid().toString();
	        String companyName = inv.getCustomer().getName();
	        double total = inv.calculateGrandTotal();

	        System.out.printf("%-35s %-25s $ %10.2f\n", invoiceUuid, companyName, total);
	    }

	    System.out.println();
	}
	
	/*
	 * sort print sorted list of customers in order of total ascending
	 * */
	public static void printCustomerInvoiceTotals(Map<Integer, Invoice> invoices) {
	    Map<String, Double> totalsMap = new TreeMap<>();
	    Map<String, Integer> countMap = new TreeMap<>();

	    for (Invoice inv : invoices.values()) {
	        String customerName = inv.getCustomer().getName();
	        double total = inv.calculateGrandTotal();

	        totalsMap.put(customerName, totalsMap.getOrDefault(customerName, 0.0) + total);
	        countMap.put(customerName, countMap.getOrDefault(customerName, 0) + 1);
	    }

	    List<String> customerList = new ArrayList<>(totalsMap.keySet());
	    SortedList<String> sortedCustomers = new SortedList<>(
	        new Comparator<String>() {
	            public int compare(String a, String b) {
	            	return Double.compare(totalsMap.get(a), totalsMap.get(b));
	            }
	        }
	    );

	    for (String customer : customerList) {
	        sortedCustomers.add(customer);
	    }

	    System.out.printf("+-------------------------------------------------------------------------+\n");
	    System.out.printf("| %-71s |\n", "Customer Invoice Totals");
	    System.out.printf("+-------------------------------------------------------------------------+\n");
	    System.out.printf("%-30s %-22s %10s\n", "Customer", "Number of Invoices", "Total");

	    for (String customer : sortedCustomers) {
	        int count = countMap.get(customer);
	        double total = totalsMap.get(customer);
	        System.out.printf("%-30s %-22d $ %10.2f\n", customer, count, total);
	    }

	    System.out.println();
	}
	
	
}
