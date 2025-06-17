package com.example.hotel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record HotelResponseDTO (
    @NotNull(message = "ID is required")
    Long id,

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    String name,

    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    String descripion,

    @NotBlank(message = "Address is required")
    String address,

    @NotBlank(message = "Phone number is required")
    String phone
) {}
