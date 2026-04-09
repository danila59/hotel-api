package org.example.hotelapi.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {
    private String houseNumber;
    private String street;
    private String city;
    private String country;
    private String postCode;

    public Address() {}

    public String getHouseNumber() { return houseNumber; }
    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getCountry() { return country; }
    public String getPostCode() { return postCode; }

    public void setHouseNumber(String houseNumber) { this.houseNumber = houseNumber; }
    public void setStreet(String street) { this.street = street; }
    public void setCity(String city) { this.city = city; }
    public void setCountry(String country) { this.country = country; }
    public void setPostCode(String postCode) { this.postCode = postCode; }
}
