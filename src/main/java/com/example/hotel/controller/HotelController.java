package com.example.hotel.controller;

import com.example.hotel.dto.*;
import com.example.hotel.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/property-view")
@Tag(name = "Hotel API", description = "API for managing hotels")
public class HotelController {
    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping("/hotels")
    @Operation(summary = "Get all hotels", description = "Retrieve a list of all hotels with brief information")
    public List<HotelResponseDTO> getAllHotels() {
        return hotelService.getAllHotels();
    }

    @GetMapping("/hotels/{id}")
    @Operation(summary = "Get hotel by ID", description = "Retrieve detailed information about a specific hotel")
    public HotelDetailsDTO getHotelById(@PathVariable Long id) {
        return hotelService.getHotelById(id);
    }

    @GetMapping("/search")
    @Operation(summary = "Search hotels", description = "Search hotels by name, brand, city, county, or amenities")
    public List<HotelResponseDTO> searchHotels(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String brand,
        @RequestParam(required = false) String city,
        @RequestParam(required = false) String county,
        @RequestParam(required = false) String amenities
    ) {
        return hotelService.searchHotels(name, brand, city, county, amenities);
    }

    @PostMapping("/hotels")
    @Operation(summary = "Create a new hotel", description = "Create a new hotel with the provided details")
    public HotelResponseDTO createHotel(@Valid @RequestBody HotelCreateDTO dto) {
        return hotelService.createHotel(dto);
    }

    @PostMapping("hotels/{id}/amenities")
    @Operation(summary = "Add amenities to a hotel", description = "Add a list of amenities to a specific hotel")
    public void addAmenities(@PathVariable Long id, @RequestBody List<String> amenities) {
        hotelService.addAmenities(id, amenities);
    }

    @GetMapping("/histogram/{param}")
    @Operation(summary = "Get histogram", description = "Get the count of hotels grouped by the specific parameter")
    public Map<String, Long> getHistogram(@PathVariable String param) {
        return hotelService.getHistogram(param);
    }
}
