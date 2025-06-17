package com.example.hotel.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record HotelCreateDTO(
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    String name,

    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    String description,

    @Size(max = 100, message = "Brand cannot exceed 100 characters")
    String brand,

    @NotNull(message = "Address is required")
    @Valid
    HotelDetailsDTO.AddressDTO address,

    @NotNull(message = "Contact information is required")
    @Valid
    HotelDetailsDTO.ContactDTO contacts,

    @Valid
    HotelDetailsDTO.ArrivalTimeDTO arrivalTime
) {}
