package com.example.hotel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record HotelCreateDTO (
    @NotBlank String name,
    String description,
    @NotBlank String brand,
    @NotNull HotelDetailsDTO.AddressDTO address,
    @NotNull HotelDetailsDTO.ContactDTO contact,
    HotelDetailsDTO.ArrivalTimeDTO arrivalTime
) {}
