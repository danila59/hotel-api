package org.example.hotelapi.repository;

import org.example.hotelapi.entity.Hotel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Sql(scripts = {"/test-data/hotels.sql", "/test-data/amenities.sql"})
class HotelRepositoryTest {

    @Autowired
    private HotelRepository hotelRepository;

    @Test
    void shouldFindAllHotels() {
        List<Hotel> hotels = hotelRepository.findAll();
        assertThat(hotels).hasSize(3);
    }

    @Test
    void shouldFindHotelById() {
        Hotel hotel = hotelRepository.findById(100L).orElse(null);
        assertThat(hotel).isNotNull();
        assertThat(hotel.getName()).isEqualTo("Hilton Minsk");
        assertThat(hotel.getBrand()).isEqualTo("Hilton");
    }

    @Test
    void shouldSearchByBrand() {
        List<Hotel> hotels = hotelRepository.searchHotels(null, "Hilton", null, null, null);
        assertThat(hotels).hasSize(2);
    }

    @Test
    void shouldSearchByCity() {
        List<Hotel> hotels = hotelRepository.searchHotels(null, null, "Moscow", null, null);
        assertThat(hotels).hasSize(2);
    }

    @Test
    void shouldSearchByAmenity() {
        List<Hotel> hotels = hotelRepository.searchHotels(null, null, null, null, "Pool");
        assertThat(hotels).hasSize(1);
        assertThat(hotels.get(0).getName()).isEqualTo("Hilton Minsk");
    }

    @Test
    void shouldSearchByName() {
        List<Hotel> hotels = hotelRepository.searchHotels("Marriott", null, null, null, null);
        assertThat(hotels).hasSize(1);
        assertThat(hotels.get(0).getName()).isEqualTo("Marriott Moscow");
    }
}
