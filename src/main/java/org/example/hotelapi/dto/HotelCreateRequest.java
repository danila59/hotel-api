package org.example.hotelapi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class HotelCreateRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Size(max = 2000, message = "Description too long (max 2000 characters)")
    private String description;

    @NotBlank(message = "Brand is required")
    @Size(min = 2, max = 50, message = "Brand must be between 2 and 50 characters")
    private String brand;

    @NotNull(message = "Address is required")
    @Valid
    private AddressDto address;

    @NotNull(message = "Contacts are required")
    @Valid
    private ContactsDto contacts;

    @NotNull(message = "Arrival time is required")
    @Valid
    private ArrivalTimeDto arrivalTime;

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getBrand() { return brand; }
    public AddressDto getAddress() { return address; }
    public ContactsDto getContacts() { return contacts; }
    public ArrivalTimeDto getArrivalTime() { return arrivalTime; }

    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setBrand(String brand) { this.brand = brand; }
    public void setAddress(AddressDto address) { this.address = address; }
    public void setContacts(ContactsDto contacts) { this.contacts = contacts; }
    public void setArrivalTime(ArrivalTimeDto arrivalTime) { this.arrivalTime = arrivalTime; }
}
