package org.example.hotelapi.service;

import org.example.hotelapi.dto.HotelBriefResponse;
import org.example.hotelapi.dto.HotelCreateRequest;
import org.example.hotelapi.dto.HotelDetailsResponse;

import java.util.List;
import java.util.Map;

/**
 * Service interface for hotel business logic.
 * Defines all hotel-related operations.
 *
 * @author Danila
 * @version 1.0
 */
public interface HotelService {

    /**
     * Retrieves all hotels with brief information.
     *
     * @return list of hotels with basic fields (id, name, description, address, phone)
     */
    List<HotelBriefResponse> getAllHotels();

    /**
     * Retrieves detailed information for a specific hotel.
     *
     * @param id the unique identifier of the hotel
     * @return complete hotel information including address, contacts, arrival time and amenities
     * @throws jakarta.persistence.EntityNotFoundException if hotel not found
     */
    HotelDetailsResponse getHotelById(Long id);

    /**
     * Searches for hotels using multiple optional criteria.
     * All parameters are combined with AND logic.
     *
     * @param name partial match on hotel name (case-insensitive)
     * @param brand exact match on brand (case-insensitive)
     * @param city exact match on city (case-insensitive)
     * @param country exact match on country (case-insensitive)
     * @param amenity exact match on amenity name
     * @return list of hotels matching all provided criteria
     */
    List<HotelBriefResponse> searchHotels(String name, String brand, String city, String country, String amenity);

    /**
     * Creates a new hotel.
     *
     * @param request DTO containing all required hotel information
     * @return created hotel with brief information including generated ID
     */
    HotelBriefResponse createHotel(HotelCreateRequest request);

    /**
     * Adds amenities to an existing hotel.
     * Duplicate amenities are not added again.
     *
     * @param id the unique identifier of the hotel
     * @param amenities list of amenity names to add
     * @throws jakarta.persistence.EntityNotFoundException if hotel not found
     */
    void addAmenities(Long id, List<String> amenities);

    /**
     * Generates a histogram of hotels grouped by specified parameter.
     *
     * @param param grouping parameter: "brand", "city", "country", or "amenities"
     * @return map where key is group value and value is count of hotels
     * @throws IllegalArgumentException if param is not supported
     */
    Map<String, Long> getHistogram(String param);
}
