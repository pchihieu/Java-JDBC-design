package com.vgb;

import java.util.Map;
import java.util.UUID;
import com.vgb.SortedList;
import com.vgb.InvoiceComparators;
import com.vgb.InvoiceComparators.InvoiceTotalComparator;

  
public class InvoiceReport {
 
public static void main(String[] args) {

    
    Map<Integer, Person> dataPersons = DatabaseConnector.loadAllPersons();
    Map<Integer, Company> dataCompanies = DatabaseConnector.loadAllCompanies(dataPersons);
    Map<Integer, Item> dataItems = DatabaseConnector.loadAllItems(dataPersons, dataCompanies);
    Map<Integer, Invoice> dataInvoices = DatabaseConnector.loadAllInvoices(dataPersons, dataCompanies);
    Map<Integer, Invoice> newDataInvoices = DatabaseConnector.craftInvoices(dataItems, dataInvoices);
    
    
    InvoiceComparators.InvoiceTotalComparator totalsComparator = new InvoiceComparators.InvoiceTotalComparator();
    InvoiceComparators.InvoiceCustomerComparator customerComparator = new InvoiceComparators.InvoiceCustomerComparator();
      
    SortedList<Invoice> byTotal = new SortedList<>(totalsComparator);
    SortedList<Invoice> byCustomer = new SortedList<>(customerComparator);

    
    for (Invoice inv : newDataInvoices.values()) {
    	byTotal.add(inv);
    	byCustomer.add(inv);
    }
    
    
    InvoicePrinter.printSortedList("Invoices by Total", byTotal);
    InvoicePrinter.printSortedList("Invoices by Customer", byCustomer);   
    InvoicePrinter.printCustomerInvoiceTotals(newDataInvoices);
    
    /*
     * InvoicePrinter.printDataTotals(newDataInvoices);
      PrintToStandardOutput.printThisPuppy();
      
      This 2 lines is for testing but i keep it in case of testing each invoice
     * */
      
    
    
}

}