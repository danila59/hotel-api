package org.example.hotelapi.controller;

import org.example.hotelapi.dto.HotelBriefResponse;
import org.example.hotelapi.dto.HotelCreateRequest;
import org.example.hotelapi.dto.HotelDetailsResponse;
import org.example.hotelapi.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for managing hotel operations.
 * Provides endpoints for CRUD operations, search, and statistics.
 *
 * @author Danila
 * @version 1.0
 */
@RestController
@RequestMapping("/property-view")
@Validated
@Tag(name = "Hotel Management", description = "Endpoints for managing hotels")
public class HotelController {

    private static final Logger log = LoggerFactory.getLogger(HotelController.class);

    private final HotelService hotelService;

    /**
     * Constructor for dependency injection.
     *
     * @param hotelService the hotel service implementation
     */
    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    /**
     * Retrieves a list of all hotels with brief information.
     *
     * @return ResponseEntity containing list of hotels with HTTP status 200 OK
     */
    @GetMapping("/hotels")
    @Operation(summary = "Get all hotels", description = "Returns list of all hotels with brief information")
    public ResponseEntity<List<HotelBriefResponse>> getAllHotels() {
        log.info("REST request: GET /property-view/hotels");
        long startTime = System.currentTimeMillis();

        List<HotelBriefResponse> response = hotelService.getAllHotels();

        long duration = System.currentTimeMillis() - startTime;
        log.info("GET /hotels completed - {} hotels returned in {} ms", response.size(), duration);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves detailed information about a specific hotel by its ID.
     *
     * @param id the unique identifier of the hotel
     * @return ResponseEntity containing hotel details with HTTP status 200 OK
     * @throws jakarta.persistence.EntityNotFoundException if hotel with given ID not found
     */
    @GetMapping("/hotels/{id}")
    @Operation(summary = "Get hotel by ID", description = "Returns detailed information about a specific hotel")
    public ResponseEntity<HotelDetailsResponse> getHotelById(@PathVariable Long id) {
        log.info("REST request: GET /property-view/hotels/{}", id);
        long startTime = System.currentTimeMillis();

        HotelDetailsResponse response = hotelService.getHotelById(id);

        long duration = System.currentTimeMillis() - startTime;
        log.info("GET /hotels/{} completed in {} ms", id, duration);
        return ResponseEntity.ok(response);
    }

    /**
     * Searches for hotels based on provided criteria.
     * All parameters are optional and can be combined.
     *
     * @param name hotel name (partial match, case-insensitive)
     * @param brand hotel brand (exact match, case-insensitive)
     * @param city city name (exact match, case-insensitive)
     * @param country country name (exact match, case-insensitive)
     * @param amenities amenity name (exact match)
     * @return ResponseEntity containing filtered list of hotels with HTTP status 200 OK
     */
    @GetMapping("/search")
    @Operation(summary = "Search hotels", description = "Search hotels by name, brand, city, country, or amenities")
    public ResponseEntity<List<HotelBriefResponse>> searchHotels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String amenities) {

        log.info("REST request: GET /property-view/search - params: name={}, brand={}, city={}, country={}, amenities={}",
                name, brand, city, country, amenities);
        long startTime = System.currentTimeMillis();

        List<HotelBriefResponse> response = hotelService.searchHotels(name, brand, city, country, amenities);

        long duration = System.currentTimeMillis() - startTime;
        log.info("GET /search completed - {} hotels found in {} ms", response.size(), duration);
        return ResponseEntity.ok(response);
    }

    /**
     * Creates a new hotel.
     *
     * @param request the hotel creation request containing all required fields
     * @return ResponseEntity containing created hotel brief info with HTTP status 201 CREATED
     */
    @PostMapping("/hotels")
    @Operation(summary = "Create new hotel", description = "Creates a new hotel")
    public ResponseEntity<HotelBriefResponse> createHotel(@Valid @RequestBody HotelCreateRequest request) {
        log.info("REST request: POST /property-view/hotels - name: {}, brand: {}, city: {}",
                request.getName(), request.getBrand(),
                request.getAddress() != null ? request.getAddress().getCity() : "null");
        long startTime = System.currentTimeMillis();

        HotelBriefResponse response = hotelService.createHotel(request);

        long duration = System.currentTimeMillis() - startTime;
        log.info("POST /hotels completed - created hotel with id: {} in {} ms", response.getId(), duration);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Adds amenities to an existing hotel.
     * Duplicate amenities are ignored.
     *
     * @param id the unique identifier of the hotel
     * @param amenities list of amenity names to add
     * @return ResponseEntity with HTTP status 200 OK
     * @throws jakarta.persistence.EntityNotFoundException if hotel with given ID not found
     */
    @PostMapping("/hotels/{id}/amenities")
    @Operation(summary = "Add amenities", description = "Adds amenities to a hotel")
    public ResponseEntity<Void> addAmenities(@PathVariable Long id, @RequestBody List<String> amenities) {
        log.info("REST request: POST /property-view/hotels/{}/amenities - amenities to add: {}", id, amenities);
        long startTime = System.currentTimeMillis();

        hotelService.addAmenities(id, amenities);

        long duration = System.currentTimeMillis() - startTime;
        log.info("POST /hotels/{}/amenities completed in {} ms", id, duration);
        return ResponseEntity.ok().build();
    }

    /**
     * Returns a histogram (count) of hotels grouped by the specified parameter.
     *
     * @param param grouping parameter - supports: brand, city, country, amenities
     * @return ResponseEntity containing map of group names to counts with HTTP status 200 OK
     * @throws IllegalArgumentException if parameter is not supported
     */
    @GetMapping("/histogram/{param}")
    @Operation(summary = "Get histogram", description = "Returns count of hotels grouped by parameter (brand, city, country, amenities)")
    public ResponseEntity<Map<String, Long>> getHistogram(@PathVariable String param) {
        log.info("REST request: GET /property-view/histogram/{}", param);
        long startTime = System.currentTimeMillis();

        Map<String, Long> response = hotelService.getHistogram(param);

        long duration = System.currentTimeMillis() - startTime;
        log.info("GET /histogram/{} completed - {} unique values in {} ms", param, response.size(), duration);
        return ResponseEntity.ok(response);
    }
}
