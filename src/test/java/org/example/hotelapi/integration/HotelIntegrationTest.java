package org.example.hotelapi.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.hotelapi.dto.*;
import org.example.hotelapi.util.TestDataBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = {"/test-data/hotels.sql", "/test-data/amenities.sql"})
class HotelIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    void testCreateHotel() throws Exception {
        HotelCreateRequest request = TestDataBuilder.createHotelRequest(
                "New Test Hotel", "NewBrand", "NewCity"
        );

        mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("New Test Hotel"));
    }

    @Test
    @Order(2)
    void testGetAllHotels() throws Exception {
        mockMvc.perform(get("/property-view/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    @Order(3)
    void testGetHotelById() throws Exception {
        mockMvc.perform(get("/property-view/hotels/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.name").value("Hilton Minsk"))
                .andExpect(jsonPath("$.brand").value("Hilton"));
    }

    @Test
    @Order(4)
    void testGetHotelById_NotFound() throws Exception {
        mockMvc.perform(get("/property-view/hotels/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(5)
    void testAddAmenities() throws Exception {
        HotelCreateRequest request = TestDataBuilder.createHotelRequest(
                "Hotel For Amenities Test",
                "TestBrand",
                "TestCity"
        );

        String response = mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long newHotelId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/property-view/hotels/{id}", newHotelId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amenities.length()").value(0));


        List<String> amenities = TestDataBuilder.createAmenities();

        mockMvc.perform(post("/property-view/hotels/{id}/amenities", newHotelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(amenities)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/property-view/hotels/{id}", newHotelId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amenities.length()").value(3));
    }

    @Test
    @Order(6)
    void testSearchHotels() throws Exception {
        mockMvc.perform(get("/property-view/search")
                        .param("city", "Moscow"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @Order(7)
    void testHistogramByBrand() throws Exception {
        mockMvc.perform(get("/property-view/histogram/brand"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Hilton").value(2))
                .andExpect(jsonPath("$.Marriott").value(1));
    }

    @Test
    @Order(8)
    void testHistogramByCity() throws Exception {
        mockMvc.perform(get("/property-view/histogram/city"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Minsk").value(1))
                .andExpect(jsonPath("$.Moscow").value(2));
    }

    @Test
    @Order(9)
    void testHistogramByAmenities() throws Exception {
        mockMvc.perform(get("/property-view/histogram/amenities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Pool").value(1));
    }

    @Test
    @Order(10)
    void testSearchByAmenity() throws Exception {
        mockMvc.perform(get("/property-view/search")
                        .param("amenities", "Free WiFi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }
}