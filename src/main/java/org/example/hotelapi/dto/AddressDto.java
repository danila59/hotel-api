package org.example.hotelapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class AddressDto {

    @NotBlank(message = "House number is required")
    private String houseNumber;

    @NotBlank(message = "Street is required")
    private String street;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "Post code is required")
    @Pattern(regexp = "^[0-9]{5,6}$", message = "Invalid post code format. Expected: 12345 or 123456")
    private String postCode;

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
