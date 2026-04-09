package org.example.hotelapi.util;

import org.example.hotelapi.dto.*;
import org.example.hotelapi.entity.*;

import java.util.List;

public class TestDataBuilder {

    public static Hotel createHotelEntity(Long id, String name, String brand, String city) {
        Hotel hotel = new Hotel();
        hotel.setId(id);
        hotel.setName(name);
        hotel.setBrand(brand);
        hotel.setDescription("Test description for " + name);

        Address address = new Address();
        address.setHouseNumber("1");
        address.setStreet("Test Street");
        address.setCity(city);
        address.setCountry("TestCountry");
        address.setPostCode("12345");
        hotel.setAddress(address);

        Contacts contacts = new Contacts();
        contacts.setPhone("+123456789");
        contacts.setEmail("test@test.com");
        hotel.setContacts(contacts);

        ArrivalTime arrivalTime = new ArrivalTime();
        arrivalTime.setCheckIn("14:00");
        arrivalTime.setCheckOut("12:00");
        hotel.setArrivalTime(arrivalTime);

        return hotel;
    }

    public static HotelCreateRequest createHotelRequest(String name, String brand, String city) {
        HotelCreateRequest request = new HotelCreateRequest();
        request.setName(name);
        request.setBrand(brand);
        request.setDescription("Test description");

        AddressDto address = new AddressDto();
        address.setHouseNumber("1");
        address.setStreet("Test Street");
        address.setCity(city);
        address.setCountry("TestCountry");
        address.setPostCode("12345");
        request.setAddress(address);

        ContactsDto contacts = new ContactsDto();
        contacts.setPhone("+123456789");
        contacts.setEmail("test@test.com");
        request.setContacts(contacts);

        ArrivalTimeDto arrivalTime = new ArrivalTimeDto();
        arrivalTime.setCheckIn("14:00");
        arrivalTime.setCheckOut("12:00");
        request.setArrivalTime(arrivalTime);

        return request;
    }

    public static HotelBriefResponse createBriefResponse(Long id, String name, String address, String phone) {
        HotelBriefResponse response = new HotelBriefResponse();
        response.setId(id);
        response.setName(name);
        response.setDescription("Test description");
        response.setAddress(address);
        response.setPhone(phone);
        return response;
    }

    public static List<String> createAmenities() {
        return List.of("WiFi", "Parking", "Pool");
    }
}