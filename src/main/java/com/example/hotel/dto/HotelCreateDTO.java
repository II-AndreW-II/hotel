package com.example.hotel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record HotelCreateDTO(
    @NotBlank(message = "Name is required")
    String name,
    String description,
    String brand,
    @NotNull(message = "Address is required")
    HotelDetailsDTO.AddressDTO address,
    @NotNull(message = "Contact is required")
    HotelDetailsDTO.ContactDTO contacts,
    HotelDetailsDTO.ArrivalTimeDTO arrivalTime
) {}
