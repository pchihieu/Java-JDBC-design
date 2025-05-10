package com.vgb;

import java.util.Comparator;
import java.util.Map;

public class InvoiceComparators {
	/*
	 * sort by total for invoices
	 * */
    public static class InvoiceTotalComparator implements Comparator<Invoice> {
        public int compare(Invoice a, Invoice b) {
            int cmp = Double.compare(b.calculateGrandTotal(), a.calculateGrandTotal());
            return cmp != 0 ? cmp : a.getUuid().compareTo(b.getUuid());
        }
    }

    /*
	 * sort by name for invoices
	 * */
    public static class InvoiceCustomerComparator implements Comparator<Invoice> {
        public int compare(Invoice a, Invoice b) {
            return a.getCustomer().getName().compareTo(b.getCustomer().getName());
        }
    }
     
}  


   

