package org.example.hotelapi.service;

import org.example.hotelapi.dto.*;
import org.example.hotelapi.entity.Hotel;
import org.example.hotelapi.mapper.HotelMapper;
import org.example.hotelapi.repository.HotelRepository;
import org.example.hotelapi.util.TestDataBuilder;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private HotelMapper hotelMapper;

    @InjectMocks
    private HotelServiceImpl hotelService;

    @Test
    void shouldGetAllHotels() {
        Hotel hotel1 = TestDataBuilder.createHotelEntity(1L, "Hilton Minsk", "Hilton", "Minsk");
        Hotel hotel2 = TestDataBuilder.createHotelEntity(2L, "Marriott Moscow", "Marriott", "Moscow");
        List<Hotel> hotels = List.of(hotel1, hotel2);

        HotelBriefResponse response1 = TestDataBuilder.createBriefResponse(1L, "Hilton Minsk", "Minsk", "+123");
        HotelBriefResponse response2 = TestDataBuilder.createBriefResponse(2L, "Marriott Moscow", "Moscow", "+456");

        when(hotelRepository.findAll()).thenReturn(hotels);
        when(hotelMapper.toBriefResponse(hotel1)).thenReturn(response1);
        when(hotelMapper.toBriefResponse(hotel2)).thenReturn(response2);

        List<HotelBriefResponse> result = hotelService.getAllHotels();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Hilton Minsk");
        assertThat(result.get(1).getName()).isEqualTo("Marriott Moscow");
    }

    @Test
    void shouldGetHotelById() {
        Hotel hotel = TestDataBuilder.createHotelEntity(1L, "Hilton Minsk", "Hilton", "Minsk");
        HotelDetailsResponse expectedResponse = new HotelDetailsResponse();
        expectedResponse.setId(1L);
        expectedResponse.setName("Hilton Minsk");

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(hotelMapper.toDetailsResponse(hotel)).thenReturn(expectedResponse);

        HotelDetailsResponse result = hotelService.getHotelById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Hilton Minsk");
    }

    @Test
    void shouldThrowExceptionWhenHotelNotFound() {
        when(hotelRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> hotelService.getHotelById(999L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Hotel not found with id: 999");
    }

    @Test
    void shouldCreateHotel() {
        HotelCreateRequest request = TestDataBuilder.createHotelRequest("New Hotel", "New Brand", "New City");
        Hotel hotel = TestDataBuilder.createHotelEntity(null, "New Hotel", "New Brand", "New City");
        Hotel savedHotel = TestDataBuilder.createHotelEntity(1L, "New Hotel", "New Brand", "New City");
        HotelBriefResponse response = TestDataBuilder.createBriefResponse(1L, "New Hotel", "New City", "+123");

        when(hotelMapper.toEntity(request)).thenReturn(hotel);
        when(hotelRepository.save(hotel)).thenReturn(savedHotel);
        when(hotelMapper.toBriefResponse(savedHotel)).thenReturn(response);

        HotelBriefResponse result = hotelService.createHotel(request);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("New Hotel");
    }

    @Test
    void shouldAddAmenities() {
        Hotel hotel = TestDataBuilder.createHotelEntity(1L, "Hilton Minsk", "Hilton", "Minsk");
        hotel.setAmenities(new ArrayList<>(List.of("WiFi")));

        List<String> newAmenities = List.of("Parking", "Pool");

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(hotelRepository.save(hotel)).thenReturn(hotel);

        hotelService.addAmenities(1L, newAmenities);

        assertThat(hotel.getAmenities()).containsExactly("WiFi", "Parking", "Pool");
        verify(hotelRepository, times(1)).save(hotel);
    }

    @Test
    void shouldGetHistogramByCity() {
        Hotel hotel1 = TestDataBuilder.createHotelEntity(1L, "Hilton Minsk", "Hilton", "Minsk");
        Hotel hotel2 = TestDataBuilder.createHotelEntity(2L, "Marriott Moscow", "Marriott", "Moscow");
        Hotel hotel3 = TestDataBuilder.createHotelEntity(3L, "Hilton Moscow", "Hilton", "Moscow");

        when(hotelRepository.findAll()).thenReturn(List.of(hotel1, hotel2, hotel3));

        Map<String, Long> result = hotelService.getHistogram("city");

        assertThat(result).hasSize(2);
        assertThat(result.get("Minsk")).isEqualTo(1L);
        assertThat(result.get("Moscow")).isEqualTo(2L);
    }

    @Test
    void shouldGetHistogramByBrand() {
        Hotel hotel1 = TestDataBuilder.createHotelEntity(1L, "Hilton Minsk", "Hilton", "Minsk");
        Hotel hotel2 = TestDataBuilder.createHotelEntity(2L, "Marriott Moscow", "Marriott", "Moscow");
        Hotel hotel3 = TestDataBuilder.createHotelEntity(3L, "Hilton Moscow", "Hilton", "Moscow");

        when(hotelRepository.findAll()).thenReturn(List.of(hotel1, hotel2, hotel3));

        Map<String, Long> result = hotelService.getHistogram("brand");

        assertThat(result).hasSize(2);
        assertThat(result.get("Hilton")).isEqualTo(2L);
        assertThat(result.get("Marriott")).isEqualTo(1L);
    }

    // ============ ТЕСТЫ НА NULL ============

    @Test
    void getHotelById_NullId_ShouldThrowException() {
        assertThatThrownBy(() -> hotelService.getHotelById(null))
                .isInstanceOf(Exception.class);
    }

    @Test
    void createHotel_NullRequest_ShouldThrowException() {
        assertThatThrownBy(() -> hotelService.createHotel(null))
                .isInstanceOf(Exception.class);
    }

    @Test
    void addAmenities_NullAmenitiesList_ShouldThrowException() {
        Long hotelId = 1L;
        Hotel hotel = TestDataBuilder.createHotelEntity(hotelId, "Test Hotel", "Test Brand", "Test City");
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));

        assertThatThrownBy(() -> hotelService.addAmenities(hotelId, null))
                .isInstanceOf(Exception.class);
    }

    @Test
    void addAmenities_EmptyList_ShouldDoNothing() {
        Long hotelId = 1L;
        Hotel hotel = TestDataBuilder.createHotelEntity(hotelId, "Test Hotel", "Test Brand", "Test City");
        int originalSize = hotel.getAmenities().size();

        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));

        hotelService.addAmenities(hotelId, List.of());

        assertThat(hotel.getAmenities()).hasSize(originalSize);
        verify(hotelRepository, times(1)).save(hotel);
    }


    @Test
    void searchHotels_AllNullParams_ShouldReturnAllHotels() {
        Hotel hotel1 = TestDataBuilder.createHotelEntity(1L, "Hotel1", "Brand1", "City1");
        Hotel hotel2 = TestDataBuilder.createHotelEntity(2L, "Hotel2", "Brand2", "City2");
        List<Hotel> hotels = List.of(hotel1, hotel2);

        HotelBriefResponse response1 = TestDataBuilder.createBriefResponse(1L, "Hotel1", "Address1", "Phone1");
        HotelBriefResponse response2 = TestDataBuilder.createBriefResponse(2L, "Hotel2", "Address2", "Phone2");

        when(hotelRepository.searchHotels(null, null, null, null, null)).thenReturn(hotels);
        when(hotelMapper.toBriefResponse(hotel1)).thenReturn(response1);
        when(hotelMapper.toBriefResponse(hotel2)).thenReturn(response2);

        List<HotelBriefResponse> result = hotelService.searchHotels(null, null, null, null, null);

        assertThat(result).hasSize(2);
    }
}
