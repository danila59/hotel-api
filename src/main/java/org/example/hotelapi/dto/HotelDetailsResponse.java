package org.example.hotelapi.dto;

import org.example.hotelapi.entity.Address;
import org.example.hotelapi.entity.ArrivalTime;
import org.example.hotelapi.entity.Contacts;

import java.util.List;

public class HotelDetailsResponse {
    private Long id;
    private String name;
    private String description;
    private String brand;
    private Address address;
    private Contacts contacts;
    private ArrivalTime arrivalTime;
    private List<String> amenities;

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getBrand() { return brand; }
    public Address getAddress() { return address; }
    public Contacts getContacts() { return contacts; }
    public ArrivalTime getArrivalTime() { return arrivalTime; }
    public List<String> getAmenities() { return amenities; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setBrand(String brand) { this.brand = brand; }
    public void setAddress(Address address) { this.address = address; }
    public void setContacts(Contacts contacts) { this.contacts = contacts; }
    public void setArrivalTime(ArrivalTime arrivalTime) { this.arrivalTime = arrivalTime; }
    public void setAmenities(List<String> amenities) { this.amenities = amenities; }
}
