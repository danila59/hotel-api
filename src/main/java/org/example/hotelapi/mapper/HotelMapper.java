package org.example.hotelapi.mapper;


import org.example.hotelapi.dto.HotelBriefResponse;
import org.example.hotelapi.dto.HotelCreateRequest;
import org.example.hotelapi.dto.HotelDetailsResponse;
import org.example.hotelapi.entity.Address;
import org.example.hotelapi.entity.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * MapStruct mapper for converting between Hotel entities and DTOs.
 * Handles automatic field mapping and custom address formatting.
 *
 * @author Danila
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface HotelMapper {

    /**
     * Converts Hotel entity to HotelBriefResponse DTO.
     * Formats address as single string and extracts phone from contacts.
     *
     * @param hotel the entity to convert
     * @return brief response DTO
     */
    @Mapping(target = "address", source = "hotel", qualifiedByName = "formatAddress")
    @Mapping(target = "phone", source = "hotel.contacts.phone")
    HotelBriefResponse toBriefResponse(Hotel hotel);

    /**
     * Converts Hotel entity to HotelDetailsResponse DTO.
     * Maps all fields including nested objects.
     *
     * @param hotel the entity to convert
     * @return detailed response DTO with complete hotel information
     */
    HotelDetailsResponse toDetailsResponse(Hotel hotel);

    /**
     * Converts HotelCreateRequest DTO to Hotel entity.
     * Ignores id (auto-generated) and amenities (added separately).
     *
     * @param request the creation request DTO
     * @return hotel entity ready for persistence
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "amenities", ignore = true)
    Hotel toEntity(HotelCreateRequest request);

    /**
     * Formats hotel address into a single readable string.
     * Pattern: "houseNumber street, city, postCode, country"
     *
     * @param hotel the hotel entity containing address
     * @return formatted address string or null if hotel or address is null
     */
    @Named("formatAddress")
    default String formatAddress(Hotel hotel) {
        if (hotel == null || hotel.getAddress() == null) {
            return null;
        }
        Address address = hotel.getAddress();
        return String.format("%s %s, %s, %s, %s",
                address.getHouseNumber(),
                address.getStreet(),
                address.getCity(),
                address.getPostCode(),
                address.getCountry());
    }
}
