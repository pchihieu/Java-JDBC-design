package com.vgb;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a person in the VGB system with contact information.
 */
public class Person {
    private final UUID uuid;
    private final String firstName;
    private final String lastName;
    private final String phone;
    private final List<String> emails; 

    /**
     * Creates a new Person with the specified details.
     * 
     * @param uuid The unique identifier
     * @param firstName First name
     * @param lastName Last name
     * @param phone Phone number
     * @param emails List of email addresses (can be empty but not null)
     * @throws IllegalArgumentException if any required field is null
     */
    public Person(UUID uuid, String firstName, String lastName, String phone, List<String> emails) {
        if (uuid == null || lastName == null) {
            throw new IllegalArgumentException("Person fields cannot be null");
        }
        
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.emails = new ArrayList<>(emails); // Defensive copy
    }

    public Person(UUID uuid, String firstName, String lastName, String phone) {
		
    	this.uuid = uuid;
    	this.firstName = firstName;
    	this.lastName = lastName;
        this.phone = phone;
        this.emails = new ArrayList<>();
        
	}

	public UUID getUuid() {
        return uuid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    /**
     * @return An unmodifiable view of the email addresses
     */
    public List<String> getEmails() {
        return List.copyOf(emails);
    }
    
    public void addEmail(String email) {
        emails.add(email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, firstName, lastName, phone, emails);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Person other = (Person) obj;
        return uuid.equals(other.uuid) &&
               firstName.equals(other.firstName) &&
               lastName.equals(other.lastName) &&
               phone.equals(other.phone) &&
               emails.equals(other.emails);
    }

    @Override
    public String toString() {
        return String.format("%s %s : %s\n Phone#:%s\nEmails:\n%s]",
                firstName, lastName, uuid, phone, String.join("\n", emails));
    }
}