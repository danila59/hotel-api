package org.example.hotelapi.service;

import org.example.hotelapi.dto.*;
import org.example.hotelapi.entity.Hotel;
import org.example.hotelapi.mapper.HotelMapper;
import org.example.hotelapi.repository.HotelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class HotelServiceImpl implements HotelService {

    private static final Logger log = LoggerFactory.getLogger(HotelServiceImpl.class);

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    public HotelServiceImpl(HotelRepository hotelRepository, HotelMapper hotelMapper) {
        this.hotelRepository = hotelRepository;
        this.hotelMapper = hotelMapper;
    }

    @Override
    public List<HotelBriefResponse> getAllHotels() {
        log.info("Getting all hotels");
        List<Hotel> hotels = hotelRepository.findAll();
        log.debug("Found {} hotels", hotels.size());
        return hotels.stream()
                .map(hotelMapper::toBriefResponse)
                .collect(Collectors.toList());
    }

    @Override
    public HotelDetailsResponse getHotelById(Long id) {
        log.info("Getting hotel by id: {}", id);
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Hotel not found with id: {}", id);
                    return new EntityNotFoundException("Hotel not found with id: " + id);
                });
        log.debug("Hotel found: {}", hotel.getName());
        return hotelMapper.toDetailsResponse(hotel);
    }

    @Override
    public List<HotelBriefResponse> searchHotels(String name, String brand, String city, String country, String amenity) {
        log.info("Searching hotels with params - name: {}, brand: {}, city: {}, country: {}, amenity: {}",
                name, brand, city, country, amenity);
        List<Hotel> hotels = hotelRepository.searchHotels(name, brand, city, country, amenity);
        log.debug("Found {} hotels", hotels.size());
        return hotels.stream()
                .map(hotelMapper::toBriefResponse)
                .collect(Collectors.toList());
    }

    @Override
    public HotelBriefResponse createHotel(HotelCreateRequest request) {
        log.info("Creating new hotel with name: {}", request.getName());
        Hotel hotel = hotelMapper.toEntity(request);

        if (hotel.getArrivalTime() != null && hotel.getArrivalTime().getCheckOut() == null) {
            log.debug("Setting default checkOut time to 12:00");
            hotel.getArrivalTime().setCheckOut("12:00");
        }

        Hotel savedHotel = hotelRepository.save(hotel);
        log.info("Hotel created successfully with id: {}", savedHotel.getId());
        return hotelMapper.toBriefResponse(savedHotel);
    }

    @Override
    public void addAmenities(Long id, List<String> amenities) {
        log.info("Adding amenities to hotel id: {}", id);
        log.debug("Amenities to add: {}", amenities);

        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Hotel not found with id: {}", id);
                    return new EntityNotFoundException("Hotel not found with id: " + id);
                });

        List<String> existingAmenities = hotel.getAmenities();
        int addedCount = 0;

        for (String amenity : amenities) {
            if (!existingAmenities.contains(amenity)) {
                existingAmenities.add(amenity);
                addedCount++;
            } else {
                log.debug("Amenity '{}' already exists for hotel {}", amenity, id);
            }
        }

        hotelRepository.save(hotel);
        log.info("Added {} new amenities to hotel {}", addedCount, id);
    }

    @Override
    public Map<String, Long> getHistogram(String param) {
        log.info("Getting histogram for parameter: {}", param);
        List<Hotel> hotels = hotelRepository.findAll();
        log.debug("Processing {} hotels", hotels.size());

        Map<String, Long> result = switch (param.toLowerCase()) {
            case "brand" -> {
                log.debug("Grouping by brand");
                yield hotels.stream()
                        .filter(h -> h.getBrand() != null && !h.getBrand().isEmpty())
                        .collect(Collectors.groupingBy(Hotel::getBrand, Collectors.counting()));
            }
            case "city" -> {
                log.debug("Grouping by city");
                yield hotels.stream()
                        .filter(h -> h.getAddress() != null && h.getAddress().getCity() != null)
                        .collect(Collectors.groupingBy(h -> h.getAddress().getCity(), Collectors.counting()));
            }
            case "country" -> {
                log.debug("Grouping by country");
                yield hotels.stream()
                        .filter(h -> h.getAddress() != null && h.getAddress().getCountry() != null)
                        .collect(Collectors.groupingBy(h -> h.getAddress().getCountry(), Collectors.counting()));
            }
            case "amenities" -> {
                log.debug("Grouping by amenities");
                yield hotels.stream()
                        .flatMap(h -> h.getAmenities().stream())
                        .collect(Collectors.groupingBy(amenity -> amenity, Collectors.counting()));
            }
            default -> {
                log.error("Invalid parameter for histogram: {}", param);
                throw new IllegalArgumentException("Invalid parameter: " + param +
                        ". Supported parameters: brand, city, country, amenities");
            }
        };

        log.info("Histogram generated with {} unique values", result.size());
        return result;
    }
}
