package org.example.hotelapi.mapper;

import org.example.hotelapi.dto.*;
import org.example.hotelapi.entity.*;
import org.example.hotelapi.util.TestDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class HotelMapperTest {

    @Autowired
    private HotelMapper hotelMapper;

    @Test
    void toBriefResponse_ShouldMapCorrectly() {
        Hotel hotel = TestDataBuilder.createHotelEntity(1L, "Hilton Minsk", "Hilton", "Minsk");

        HotelBriefResponse response = hotelMapper.toBriefResponse(hotel);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Hilton Minsk");
        assertThat(response.getDescription()).isEqualTo("Test description for Hilton Minsk");
        assertThat(response.getPhone()).isEqualTo("+123456789");
    }

    @Test
    void toDetailsResponse_ShouldMapAllFields() {
        Hotel hotel = TestDataBuilder.createHotelEntity(1L, "Hilton Minsk", "Hilton", "Minsk");
        hotel.getAmenities().addAll(List.of("WiFi", "Parking"));

        HotelDetailsResponse response = hotelMapper.toDetailsResponse(hotel);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Hilton Minsk");
        assertThat(response.getBrand()).isEqualTo("Hilton");
        assertThat(response.getAddress()).isNotNull();
        assertThat(response.getAddress().getCity()).isEqualTo("Minsk");
        assertThat(response.getContacts()).isNotNull();
        assertThat(response.getContacts().getPhone()).isEqualTo("+123456789");
        assertThat(response.getArrivalTime()).isNotNull();
        assertThat(response.getArrivalTime().getCheckIn()).isEqualTo("14:00");
        assertThat(response.getAmenities()).hasSize(2);
    }

    @Test
    void toEntity_ShouldMapCreateRequestToHotel() {
        HotelCreateRequest request = TestDataBuilder.createHotelRequest("New Hotel", "New Brand", "New City");

        Hotel hotel = hotelMapper.toEntity(request);

        assertThat(hotel).isNotNull();
        assertThat(hotel.getId()).isNull();
        assertThat(hotel.getName()).isEqualTo("New Hotel");
        assertThat(hotel.getBrand()).isEqualTo("New Brand");
        assertThat(hotel.getAddress()).isNotNull();
        assertThat(hotel.getAddress().getCity()).isEqualTo("New City");
        assertThat(hotel.getContacts()).isNotNull();
        assertThat(hotel.getContacts().getEmail()).isEqualTo("test@test.com");
        assertThat(hotel.getArrivalTime()).isNotNull();
        assertThat(hotel.getArrivalTime().getCheckIn()).isEqualTo("14:00");
    }

    @Test
    void formatAddress_ShouldReturnFormattedString() {
        Hotel hotel = TestDataBuilder.createHotelEntity(1L, "Test Hotel", "Test Brand", "Test City");

        HotelBriefResponse response = hotelMapper.toBriefResponse(hotel);

        assertThat(response.getAddress()).isEqualTo("1 Test Street, Test City, 12345, TestCountry");
    }

    @Test
    void toBriefResponse_NullHotel_ShouldReturnNull() {
        assertThat(hotelMapper.toBriefResponse(null)).isNull();
    }

    @Test
    void toDetailsResponse_NullHotel_ShouldReturnNull() {
        assertThat(hotelMapper.toDetailsResponse(null)).isNull();
    }

    @Test
    void toEntity_NullRequest_ShouldReturnNull() {
        assertThat(hotelMapper.toEntity(null)).isNull();
    }
}