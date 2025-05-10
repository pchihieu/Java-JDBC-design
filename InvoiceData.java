package com.vgb;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.UUID;

/**
 * This is a collection of utility methods that define a general API for
 * interacting with the database supporting this application.
 *
 */
public class InvoiceData {

	/**
	 * Removes all records from all tables in the database.
	 */
	public static void clearDatabase() {
		Connection conn = null;
		try {
			conn = DatabaseFactory.openConnection();
			conn.setAutoCommit(false);

			try (Statement stmt = conn.createStatement()) {
				stmt.execute("SET FOREIGN_KEY_CHECKS = 0");

				String[] tables = { "InvoiceItem", "Invoice", "Item", "Email", "Address", "Company", "Person" };
				for (String table : tables) {
					stmt.execute("TRUNCATE TABLE " + table);
				}

				stmt.execute("SET FOREIGN_KEY_CHECKS = 1");
				conn.commit();
			}
		} catch (SQLException e) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			throw new RuntimeException("Failed to clear database", e);
		} finally {
			DatabaseFactory.closeConnection(conn);
		}
	}

	/**
	 * Method to add a person record to the database with the provided data.
	 *
	 * @param personUuid
	 * @param firstName
	 * @param lastName
	 * @param phone
	 */
	public static void addPerson(UUID personUuid, String firstName, String lastName, String phone) {
		String sql = "INSERT INTO Person (uuid, firstName, lastName, phone) VALUES (?, ?, ?, ?)";

		try (Connection conn = DatabaseFactory.openConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, personUuid.toString());
			stmt.setString(2, firstName);
			stmt.setString(3, lastName);
			stmt.setString(4, phone);

			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Failed to add person", e);
		}
	}

	/**
	 * Adds an email record corresponding person record corresponding to the
	 * provided <code>personUuid</code>
	 *
	 * @param personUuid
	 * @param email
	 */
	public static void addEmail(UUID personUuid, String email) {
		if (email == null || email.trim().isEmpty()) {
			return;
		}

		String sql = "INSERT IGNORE INTO Email (personId, address) " + "SELECT personId, ? FROM Person WHERE uuid = ?";

		try (Connection conn = DatabaseFactory.openConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, email.trim());
			stmt.setString(2, personUuid.toString());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Failed to add email", e);
		}
	}

	/**
	 * Adds a company record to the database with the primary contact person
	 * identified by the given code.
	 *
	 * @param companyUuid
	 * @param name
	 * @param contactUuid
	 * @param street
	 * @param city
	 * @param state
	 * @param zip
	 */
	public static void addCompany(UUID companyUuid, UUID contactUuid, String name, String street, String city,
			String state, String zip) {
		Connection conn = null;
		try {
			conn = DatabaseFactory.openConnection();
			conn.setAutoCommit(false);

			// First insert company
			String companySql = "INSERT INTO Company (uuid, name, contactId) "
					+ "SELECT ?, ?, personId FROM Person WHERE uuid = ?";
			try (PreparedStatement stmt = conn.prepareStatement(companySql, Statement.RETURN_GENERATED_KEYS)) {
				stmt.setString(1, companyUuid.toString());
				stmt.setString(2, name);
				stmt.setString(3, contactUuid.toString());
				stmt.executeUpdate();

				ResultSet rs = stmt.getGeneratedKeys();
				if (!rs.next()) {
					throw new SQLException("Failed to get company ID");
				}
				int companyId = rs.getInt(1);

				// Then insert address
				String addressSql = "INSERT INTO Address (companyId, street, city, state, zip) VALUES (?, ?, ?, ?, ?)";
				try (PreparedStatement addrStmt = conn.prepareStatement(addressSql)) {
					addrStmt.setInt(1, companyId);
					addrStmt.setString(2, street);
					addrStmt.setString(3, city);
					addrStmt.setString(4, state);
					addrStmt.setString(5, zip);
					addrStmt.executeUpdate();
				}
			}

			conn.commit();
		} catch (SQLException e) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			throw new RuntimeException("Failed to add company", e);
		} finally {
			if (conn != null) {
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Adds an equipment record to the database of the given values.
	 *
	 * @param equipmentUuid
	 * @param name
	 * @param modelNumber
	 * @param retailPrice
	 */
	public static void addEquipment(UUID equipmentUuid, String name, String modelNumber, double retailPrice) {
		String sql = "INSERT INTO Item (uuid, type, name, modelNumber, price) " + "VALUES (?, 'E', ?, ?, ?)";

		try (Connection conn = DatabaseFactory.openConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, equipmentUuid.toString());
			stmt.setString(2, name);
			stmt.setString(3, modelNumber);
			stmt.setDouble(4, retailPrice);
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Failed to add equipment", e);
		}

	}

	/**
	 * Adds an material record to the database of the given values.
	 *
	 * @param materialUuid
	 * @param name
	 * @param unit
	 * @param pricePerUnit
	 */
	public static void addMaterial(UUID materialUuid, String name, String unit, double pricePerUnit) {
		String sql = "INSERT INTO Item (uuid, type, name, unit, price) VALUES (?, 'M', ?, ?, ?)";

		try (Connection conn = DatabaseFactory.openConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, materialUuid.toString());
			stmt.setString(2, name);
			stmt.setString(3, unit);
			stmt.setDouble(4, pricePerUnit);

			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Failed to add material", e);
		}

	}

	/**
	 * Adds an contract record to the database of the given values.
	 *
	 * @param contractUuid
	 * @param name
	 * @param unit
	 * @param pricePerUnit
	 */
	public static void addContract(UUID contractUuid, String name, UUID servicerUuid) {
		String sql = "INSERT INTO Item (uuid, type, name, subcontractorId) "
				+ "SELECT ?, 'C', ?, companyId FROM Company WHERE uuid = ? ";

		try (Connection conn = DatabaseFactory.openConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, contractUuid.toString());
			stmt.setString(2, name);
			stmt.setString(3, servicerUuid.toString());

			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Failed to add contract", e);
		}

	}

	/**
	 * Adds an Invoice record to the database with the given data.
	 *
	 * @param invoiceUuid
	 * @param customerUuid
	 * @param salesPersonUuid
	 * @param date
	 */
	public static void addInvoice(UUID invoiceUuid, UUID customerUuid, UUID salesPersonUuid, LocalDate date) {
		String sql = "INSERT INTO Invoice (uuid, customerId, salespersonId, date) "
				+ "SELECT ?, companyId, personId, ? FROM Company c, Person p " + "WHERE c.uuid = ? AND p.uuid = ?";

		try (Connection conn = DatabaseFactory.openConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, invoiceUuid.toString());
			stmt.setDate(2, Date.valueOf(date));
			stmt.setString(3, customerUuid.toString());
			stmt.setString(4, salesPersonUuid.toString());

			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Failed to add invoice", e);
		}

	}

	/**
	 * Adds an Equipment purchase record to the given invoice.
	 *
	 * @param invoiceUuid
	 * @param itemUuid
	 */
	public static void addEquipmentPurchaseToInvoice(UUID invoiceUuid, UUID itemUuid) {
		addItemToInvoice(invoiceUuid, itemUuid, "P", 0, 0, null, null, 0);

	}

	/**
	 * Adds an Equipment lease record to the given invoice.
	 *
	 * @param invoiceUuid
	 * @param itemUuid
	 * @param start
	 * @param end
	 */
	public static void addEquipmentLeaseToInvoice(UUID invoiceUuid, UUID itemUuid, LocalDate start, LocalDate end) {
		addItemToInvoice(invoiceUuid, itemUuid, "L", 0, 0, start, end, 0);

	}

	/**
	 * Adds an Equipment rental record to the given invoice.
	 *
	 * @param invoiceUuid
	 * @param itemUuid
	 * @param numberOfHours
	 */
	public static void addEquipmentRentalToInvoice(UUID invoiceUuid, UUID itemUuid, double numberOfHours) {
		addItemToInvoice(invoiceUuid, itemUuid, "R", 0, 0, null, null, numberOfHours);

	}

	/**
	 * Adds a material record to the given invoice.
	 *
	 * @param invoiceUuid
	 * @param itemUuid
	 * @param numberOfUnits
	 */
	public static void addMaterialToInvoice(UUID invoiceUuid, UUID itemUuid, int numberOfUnits) {
		addItemToInvoice(invoiceUuid, itemUuid, "M", numberOfUnits, 0, null, null, 0);

	}

	/**
	 * Adds a contract record to the given invoice.
	 *
	 * @param invoiceUuid
	 * @param itemUuid
	 * @param amount
	 */
	public static void addContractToInvoice(UUID invoiceUuid, UUID itemUuid, double amount) {
		addItemToInvoice(invoiceUuid, itemUuid, "C", 0, amount, null, null, 0);

	}

	private static int getInvoiceIdFromUuid(UUID uuid) {
		String sql = "SELECT invoiceId FROM Invoice WHERE uuid = ?";
		try (Connection conn = DatabaseFactory.openConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, uuid.toString());
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("invoiceId");
				} else {
					throw new IllegalArgumentException("No invoice found with UUID: " + uuid);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Failed to retrieve invoiceId from uuid: " + uuid, e);
		}
	}

	private static int getItemIdFromUuid(UUID uuid) {
		String sql = "SELECT itemId FROM Item WHERE uuid = ?";
		try (Connection conn = DatabaseFactory.openConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, uuid.toString());
			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt("itemId");
				} else {
					throw new IllegalArgumentException("No item found with UUID: " + uuid);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Failed to retrieve itemId from uuid: " + uuid, e);
		}
	}

	private static void addItemToInvoice(UUID invoiceUuid, UUID itemUuid, String itemType, double quantity,
			double unitPrice, LocalDate startDate, LocalDate endDate, double hoursRented) {
		int invoiceId = getInvoiceIdFromUuid(invoiceUuid);
		int itemId = getItemIdFromUuid(itemUuid);

		String sql = "INSERT INTO InvoiceItem (invoiceId, itemId, itemType, quantity, unitPrice, startDate, endDate, hoursRented) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection conn = DatabaseFactory.openConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, invoiceId);
			stmt.setInt(2, itemId);
			stmt.setString(3, itemType);
			stmt.setDouble(4, quantity);
			stmt.setDouble(5, unitPrice);
			stmt.setDate(6, startDate != null ? Date.valueOf(startDate) : null);
			stmt.setDate(7, endDate != null ? Date.valueOf(endDate) : null);
			stmt.setDouble(8, hoursRented);

			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Failed to add item to invoice", e);
		}

	}
}