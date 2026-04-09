package org.example.hotelapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.example.hotelapi.dto.*;
import org.example.hotelapi.service.HotelService;
import org.example.hotelapi.util.TestDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HotelController.class)
class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private HotelService hotelService;

    @Test
    void getAllHotels_ShouldReturnList() throws Exception {
        HotelBriefResponse hotel1 = TestDataBuilder.createBriefResponse(1L, "Hilton Minsk", "Minsk, Belarus", "+375171234567");
        HotelBriefResponse hotel2 = TestDataBuilder.createBriefResponse(2L, "Marriott Moscow", "Moscow, Russia", "+74951234567");
        List<HotelBriefResponse> hotels = Arrays.asList(hotel1, hotel2);

        when(hotelService.getAllHotels()).thenReturn(hotels);

        mockMvc.perform(get("/property-view/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Hilton Minsk"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Marriott Moscow"));
    }

    @Test
    void getHotelById_ShouldReturnHotel() throws Exception {
        HotelDetailsResponse hotel = new HotelDetailsResponse();
        hotel.setId(1L);
        hotel.setName("Hilton Minsk");
        hotel.setBrand("Hilton");

        when(hotelService.getHotelById(1L)).thenReturn(hotel);

        mockMvc.perform(get("/property-view/hotels/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Hilton Minsk"));
    }

    @Test
    void getHotelById_NotFound_ShouldReturn404() throws Exception {
        when(hotelService.getHotelById(999L))
                .thenThrow(new EntityNotFoundException("Hotel not found with id: 999"));

        mockMvc.perform(get("/property-view/hotels/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void searchHotels_ShouldReturnFilteredList() throws Exception {
        HotelBriefResponse hotel = TestDataBuilder.createBriefResponse(1L, "Hilton Minsk", "Minsk, Belarus", "+375171234567");
        List<HotelBriefResponse> hotels = List.of(hotel);

        when(hotelService.searchHotels(any(), any(), eq("Minsk"), any(), any())).thenReturn(hotels);

        mockMvc.perform(get("/property-view/search")
                        .param("city", "Minsk"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Hilton Minsk"));
    }

    @Test
    void createHotel_ShouldReturnCreated() throws Exception {
        HotelCreateRequest request = TestDataBuilder.createHotelRequest("New Hotel", "New Brand", "New City");

        HotelBriefResponse response = TestDataBuilder.createBriefResponse(1L, "New Hotel", "New City, TestCountry", "+123456789");

        when(hotelService.createHotel(any(HotelCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("New Hotel"));
    }

    @Test
    void createHotel_InvalidData_ShouldReturn400() throws Exception {
        HotelCreateRequest request = new HotelCreateRequest();

        mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addAmenities_ShouldReturnOk() throws Exception {
        List<String> amenities = TestDataBuilder.createAmenities();

        mockMvc.perform(post("/property-view/hotels/1/amenities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(amenities)))
                .andExpect(status().isOk());
    }

    @Test
    void getHistogram_ShouldReturnMap() throws Exception {
        Map<String, Long> histogram = new HashMap<>();
        histogram.put("Minsk", 1L);
        histogram.put("Moscow", 2L);

        when(hotelService.getHistogram("city")).thenReturn(histogram);

        mockMvc.perform(get("/property-view/histogram/city"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Minsk").value(1))
                .andExpect(jsonPath("$.Moscow").value(2));
    }

    @Test
    void getHistogram_InvalidParam_ShouldReturn400() throws Exception {
        when(hotelService.getHistogram("invalid"))
                .thenThrow(new IllegalArgumentException("Invalid parameter: invalid"));

        mockMvc.perform(get("/property-view/histogram/invalid"))
                .andExpect(status().isBadRequest());
    }

    // ============ ТЕСТЫ ВАЛИДАЦИИ ============

    @Test
    void createHotel_InvalidEmail_ShouldReturn400() throws Exception {
        HotelCreateRequest request = TestDataBuilder.createHotelRequest("Test Hotel", "Test Brand", "Test City");
        request.getContacts().setEmail("invalid-email");

        mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$['contacts.email']").exists());
    }

    @Test
    void createHotel_InvalidPhone_ShouldReturn400() throws Exception {
        HotelCreateRequest request = TestDataBuilder.createHotelRequest("Test Hotel", "Test Brand", "Test City");
        request.getContacts().setPhone("123");

        mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$['contacts.phone']").exists());
    }

    @Test
    void createHotel_InvalidPostCode_ShouldReturn400() throws Exception {
        HotelCreateRequest request = TestDataBuilder.createHotelRequest("Test Hotel", "Test Brand", "Test City");
        request.getAddress().setPostCode("abc");

        mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$['address.postCode']").exists());
    }

    @Test
    void createHotel_InvalidCheckInTime_ShouldReturn400() throws Exception {
        HotelCreateRequest request = TestDataBuilder.createHotelRequest("Test Hotel", "Test Brand", "Test City");
        request.getArrivalTime().setCheckIn("25:00");

        mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$['arrivalTime.checkIn']").exists());
    }

    @Test
    void createHotel_NameTooShort_ShouldReturn400() throws Exception {
        HotelCreateRequest request = TestDataBuilder.createHotelRequest("T", "Test Brand", "Test City");

        mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").exists());
    }

    @Test
    void createHotel_MissingRequiredFields_ShouldReturn400() throws Exception {
        HotelCreateRequest request = new HotelCreateRequest();

        mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
