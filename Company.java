package com.vgb;

import java.util.Objects;
import java.util.UUID;

/**
 * Represents a company in business with VGB
 */
public class Company {
	
	private UUID uuid;
	private Person contact;
	private String name;
	private Address address;
	
	public Company(UUID uuid, Person contact, String name, Address address) {
		super();
		this.uuid = uuid;
		this.contact = contact;
		this.name = name;
		this.address = address;
	}

	public UUID getUuid() {
		return uuid;
	}

	public Person getContact() {
		return contact;
	}

	public String getName() {
		return name;
	}

	public Address getAddress() {
		return address;
	}

	@Override
	public int hashCode() {
		return Objects.hash(address, contact, name, uuid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Company other = (Company) obj;
		return Objects.equals(address, other.address) && Objects.equals(contact, other.contact)
				&& Objects.equals(name, other.name) && Objects.equals(uuid, other.uuid);
	}

	@Override
	public String toString() {
		return name;
	}
	
	


}
