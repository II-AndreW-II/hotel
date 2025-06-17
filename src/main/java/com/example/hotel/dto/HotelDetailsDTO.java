package com.example.hotel.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

public record HotelDetailsDTO (
    Long id,
    String name,
    String brand,
    AddressDTO address,
    ContactDTO contact,
    ArrivalTimeDTO arrivalTime,
    List<String> amenities
) {
    public record AddressDTO (
        @NotNull(message = "House number is required")
        Integer houseNumber,

        @NotBlank(message = "Street is required")
        @Size(max = 255, message = "Street cannot exceed 255 characters")
        String street,

        @NotBlank(message = "City is required")
        @Size(max = 100, message = "City cannot exceed 100 characters")
        String city,

        @NotBlank(message = "Country is required")
        @Size(max = 100, message = "Country cannot exceed 100 characters")
        String country,

        @NotBlank(message = "Post code is required")
        @Size(max = 20, message = "Post code cannot exceed 20 characters")
        String postCode
    ) {}

    public record ContactDTO (
        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^\\+?[1-9]\\d{0,2}[\\s\\-0-9]{0,16}$", 
                message = "Invalid phone number format (+375 17 309-80-00, 375173098000, +37517 3098000)")
        String phone,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format (e.g. user@example.com)")
        @Size(max = 100, message = "Email cannot exceed 100 characters")
        String email
    ) {}

    public record ArrivalTimeDTO (
        @NotBlank(message = "Check in time is required")
        @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Invalid check-in time format (HH:mm)")
        String checkIn,

        @Nullable
        @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Invalid check-out time format (HH:mm)")
        String checkOut
    ) {}
}
