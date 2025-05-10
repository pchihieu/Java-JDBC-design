package com.vgb;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Loads CSV data into Java objects
 */
public class CsvParser {
    
    public static Map<UUID, Person> loadPersons() {
        final String PERSON_PATH = "data/Persons.csv";
        Scanner s = null;
        try {
            s = new Scanner(new File(PERSON_PATH));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Failed to load persons data", e);
        }

        Map<UUID, Person> personMap = new HashMap<>();
        
        if (s.hasNextLine()) {
            s.nextLine(); 
        }

        while (s.hasNextLine()) {
            String line = s.nextLine().trim();
            if (line.isEmpty()) continue;
            
            String[] tokens = line.split(",");
            try {
                UUID uuid = UUID.fromString(tokens[0]);
                String firstName = tokens[1];
                String lastName = tokens[2];
                String phone = tokens[3];
                
                List<String> emails = new ArrayList<>();
                for (int i = 4; i < tokens.length; i++) {
                    if (tokens[i] != null && !tokens[i].trim().isEmpty()) {
                        emails.add(tokens[i].trim());
                    }
                }
                
                Person p = new Person(uuid, firstName, lastName, phone, emails);
                personMap.put(uuid, p);
            } catch (Exception e) {
                System.err.println("Error parsing person data: " + line);
                e.printStackTrace();
            }
        }
        s.close();
        return personMap;
    }
    
    public static Map<UUID, Company> loadCompanies(Map<UUID, Person> personMap) {
        final String COMPANIES_PATH = "data/Companies.csv";
        Scanner s = null;
        try {
            s = new Scanner(new File(COMPANIES_PATH));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        
        Map<UUID, Company> companyMap = new HashMap<>();
        s.nextLine();

        while (s.hasNext()) {
            String line = s.nextLine();
            String[] tokens = line.split(",");
            UUID uuid = UUID.fromString(tokens[0]);
            Person contact = personMap.get(UUID.fromString(tokens[1]));
            String name = tokens[2];
            Address address = new Address(tokens[3], tokens[4], tokens[5], tokens[6]);
            companyMap.put(uuid, new Company(uuid, contact, name, address));
        }
        s.close();
        return companyMap;
    }
    
    public static Map<UUID, Item> loadItems(Map<UUID, Person> personMap, Map<UUID, Company> companyMap) {
        final String ITEM_PATH = "data/Items.csv";
        Scanner s = null;
        try {
            s = new Scanner(new File(ITEM_PATH));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        
        Map<UUID, Item> itemMap = new HashMap<>();
        s.nextLine();

        while (s.hasNext()) {
            String line = s.nextLine();
            String[] tokens = line.split(",");
            UUID uuid = UUID.fromString(tokens[0]);
            String type = tokens[1];
            String name = tokens[2];
            
            switch (type) {
                case "E":
                    itemMap.put(uuid, new Purchase(
                        uuid, name, tokens[3], Double.parseDouble(tokens[4])));
                    break;
                case "M":
                    itemMap.put(uuid, new Material(
                        uuid, name, tokens[3], Double.parseDouble(tokens[4]), 0));
                    break;
                case "C":
                    itemMap.put(uuid, new Contract(
                        uuid, name, companyMap.get(UUID.fromString(tokens[3])), 0));
                    break;
            }
        }
        s.close();
        return itemMap;
    }

    public static Map<UUID, Invoice> loadInvoices(Map<UUID, Person> personMap, 
            Map<UUID, Company> companyMap) {
        final String INVOICE_PATH = "data/Invoices.csv";
        Scanner s = null;
        try {
            s = new Scanner(new File(INVOICE_PATH));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        
        Map<UUID, Invoice> invoiceMap = new HashMap<>();
        s.nextLine(); // Skip header

        while (s.hasNext()) {
            String line = s.nextLine();
            String[] tokens = line.split(",");
            UUID uuid = UUID.fromString(tokens[0]);
            Company customer = companyMap.get(UUID.fromString(tokens[1]));
            Person salesperson = personMap.get(UUID.fromString(tokens[2]));
            LocalDate date = LocalDate.parse(tokens[3], DateTimeFormatter.ISO_DATE);
            invoiceMap.put(uuid, new Invoice(uuid, customer, salesperson, date));
        }
        s.close();
        return invoiceMap;
    }

    public static void craftInvoices(Map<UUID, Item> itemMap, Map<UUID, Invoice> invoiceMap) {
        final String ITEM_PATH = "data/InvoiceItems.csv";
        Scanner s = null;
        try {
            s = new Scanner(new File(ITEM_PATH));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        
        s.nextLine(); // Skip header

        while (s.hasNext()) {
            String line = s.nextLine();
            String[] tokens = line.split(",");
            UUID invoiceUuid = UUID.fromString(tokens[0]);
            UUID itemUuid = UUID.fromString(tokens[1]);
            Invoice invoice = invoiceMap.get(invoiceUuid);
            Item item = itemMap.get(itemUuid);
            
            if (invoice == null || item == null) continue;
            
            // Handle different item types
            if (item instanceof Equipment) {
                switch (tokens[2]) {
                    case "P": invoice.addItem(new Purchase((Purchase)item)); break;
                    case "L": invoice.addItem(new Lease((Purchase)item, 
                        LocalDate.parse(tokens[3]), LocalDate.parse(tokens[4]))); break;
                    case "R": invoice.addItem(new Rent((Purchase)item, 
                        Double.parseDouble(tokens[3]))); break;
                }
            } else if (item instanceof Material) {
                invoice.addItem(new Material((Material)item, Double.parseDouble(tokens[2])));
            } else if (item instanceof Contract) {
                invoice.addItem(new Contract((Contract)item, Double.parseDouble(tokens[2])));
            }
        }
        s.close();
    }
}