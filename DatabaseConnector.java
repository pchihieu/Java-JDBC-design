package com.vgb;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;


/**
 * Opens connection to Database and retrieves Invoice Data
 */
public class DatabaseConnector {
	
	/**
	 * Opens connection
	 * @return
	 */
    private static Connection getConnection() {
        try {
            
            String username = "hmonahan2";
            String password = "liz3quijaeBi";
            String url = "jdbc:mysql://nuros.unl.edu/hmonahan2";

            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database connection failed", e);
        }
    }

    /** Method to load persons from the database
     * 
     * @return
     */
    public static Map<Integer, Person> loadAllPersons() {
        Map<Integer, Person> personMap = new HashMap<>();
        String query = "SELECT personId, uuid, firstName, lastName, phone FROM Person";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
            	int personId =rs.getInt("personId");          	
                UUID uuid = UUID.fromString(rs.getString("uuid"));
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String phone = rs.getString("phone");
         
                List<String> emails = getEmailsForPerson(personId); // Fetch emails for the person
                Person person = new Person(uuid, firstName, lastName, phone, emails);
                personMap.put(personId, person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return personMap;
    }

    // Method to load companies from the database
    public static Map<Integer, Company> loadAllCompanies(Map<Integer, Person> personMap) {
        Map<Integer, Company> companyMap = new HashMap<>();
        String query = 
        		"SELECT c.companyId, c.uuid, c.name, c.contactId, " +
			       "a.street, a.city, a.state, a.zip " +
			       "FROM Company c " +
			      "JOIN Address a ON c.companyId = a.companyId";


        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
            	int companyId = rs.getInt("companyId");
                UUID uuid = UUID.fromString(rs.getString("uuid"));
                int contactId = rs.getInt("contactId");
                Person contact = personMap.get(contactId);
                String name = rs.getString("name");

                String street = rs.getString("street");
                String city = rs.getString("city");
                String state = rs.getString("state");
                String zip = rs.getString("zip");

                Address address = new Address(street, city, state, zip);

                Company company = new Company(uuid, contact, name, address);
                companyMap.put(companyId, company);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return companyMap;
    }

    /** Method to load items from the database
     * 
     * @param personMap
     * @param companyMap
     * @return
     */
    public static Map<Integer, Item> loadAllItems(Map<Integer, Person> personMap, Map<Integer, Company> companyMap) {
        Map<Integer, Item> itemMap = new HashMap<>();
        String query = "SELECT Item.itemId, subcontractorId AS companyId, Item.uuid, type, name, price FROM Item "
        		+ "LEFT JOIN InvoiceItem ON Item.ItemId = InvoiceItem.ItemId "
        		+ "LEFT JOIN Invoice ON InvoiceItem.invoiceId = Invoice.invoiceId;";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
            	int itemId = rs.getInt("itemId");
                UUID uuid = UUID.fromString(rs.getString("uuid"));
                String type = rs.getString("type");
                String name = rs.getString("name");
                double price = rs.getDouble("price");

                switch (type) {
                    case "E": 
                        itemMap.put(itemId, new Purchase(uuid, name, type, price));
                        break;
                    case "M": 
                        itemMap.put(itemId, new Material(uuid, name, type, price, 0));
                        break;
                    case "C": 
                        itemMap.put(itemId, new Contract(uuid, name, companyMap.get(rs.getInt("companyId")), price));
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemMap;
    }

    /** Method to load invoices from the database
     * 
     * @param personMap
     * @param companyMap
     * @param itemMap
     * @return
     */
    public static Map<Integer, Invoice> loadAllInvoices(Map<Integer, Person> personMap, Map<Integer, Company> companyMap) {
        Map<Integer, Invoice> invoiceMap = new HashMap<>();
        String query = "SELECT invoiceId, uuid, customerId, salespersonId, date FROM Invoice";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
            	int invoiceId = rs.getInt("invoiceId");
                UUID uuid = UUID.fromString(rs.getString("uuid"));
                int customerId = rs.getInt("customerId");
                Company customer = companyMap.get(customerId);
                int salespersonId = rs.getInt("salespersonId");
                Person salesperson = personMap.get(salespersonId);
                Date date = rs.getDate("date");
                Invoice invoice = new Invoice(uuid, customer, salesperson, date.toLocalDate());
                invoiceMap.put(invoiceId, invoice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return invoiceMap;
    }

    /** Method to link items to invoices (crafting invoice items)
     * 
     * @param itemMap
     * @param invoiceMap
     */
    public static Map<Integer, Invoice> craftInvoices(Map<Integer, Item> itemMap, Map<Integer, Invoice> invoiceMap) {
        String query = "SELECT invoiceId, itemId, itemType, quantity, unitPrice, startDate, endDate, hoursRented FROM InvoiceItem";

        Map<Integer, Invoice> newInvoiceMap = new HashMap<Integer, Invoice>(invoiceMap);

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
        	
            while (rs.next()) {     	
            	Integer invoiceId = rs.getInt("invoiceId");
            	Integer itemId = rs.getInt("itemId");
                Invoice invoice = invoiceMap.get(invoiceId);
                Item item = itemMap.get(itemId);
                
                double unitsPurchased = rs.getDouble("quantity");
                double unitPrice = rs.getDouble("unitPrice");
                LocalDate startDate = LocalDate.parse("0001-01-01");
                if(rs.getString("startDate") != null) {
                	startDate = LocalDate.parse(rs.getString("startDate"));
                }       
                LocalDate endDate = LocalDate.parse("0001-01-01");
                if(rs.getString("endDate") != null) {
                	endDate = LocalDate.parse(rs.getString("endDate"));                	
                }
                double hoursRented = rs.getDouble("hoursRented");
                String type = rs.getString("itemType");
                if (item instanceof Equipment) {
                    if (type.equals("P")) {
                    	invoice.addItem(new Purchase((Purchase)item)); 
                    } else if (type.equals("L")) {
                        invoice.addItem(new Lease((Purchase)item, startDate, endDate)); 
                    } else if (type.equals("R")) {
                    	invoice.addItem(new Rent((Purchase)item, hoursRented));
                    }
                } else if (item instanceof Material) {
                    invoice.addItem(new Material((Material)item, unitsPurchased));
                } else if (item instanceof Contract) {
                    invoice.addItem(new Contract((Contract)item, unitPrice));
                }
                newInvoiceMap.put(invoiceId, invoice);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newInvoiceMap;
    }

    // Method to fetch emails for a person
    private static List<String> getEmailsForPerson(int personId) {
        List<String> emails = new ArrayList<>();
        String query = "SELECT address FROM Email WHERE personId = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, personId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                emails.add(rs.getString("address"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return emails;
    }
    


}
