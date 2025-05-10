package com.vgb;

import java.util.Objects;

/**
 * Represents an address of a company's headquarters
 */
public class Address {
	
	private String street;
	private String city;
	private String state;
	private String zip;
	
	public Address(String street, String city, String state, String zip) {
		super();
		this.street = street;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}

	public String getStreet() {
		return street;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getZip() {
		return zip;
	}

	@Override
	public int hashCode() {
		return Objects.hash(city, state, street, zip);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address other = (Address) obj;
		return Objects.equals(city, other.city) && Objects.equals(state, other.state)
				&& Objects.equals(street, other.street) && zip == other.zip;
	}

	@Override
	public String toString() {
		String address = String.format("%s %s %s %s", street, city, state, zip);
		return address;
	}
	
	
	
	
	
	

}
