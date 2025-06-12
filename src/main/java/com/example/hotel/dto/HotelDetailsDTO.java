package com.example.hotel.dto;

import java.util.List;

public record HotelDetailsDTO (
    Long id,
    String name,
    String brand,
    AddressDTO address,
    ContactDTO contact,
    ArrivalTimeDTO arrivalTime,
    List<String> amentities
) {
    public record AddressDTO (
        Integer houseNumber,
        String street,
        String city,
        String country,
        String postCode
    ) {}

    public record ContactDTO (
        String phone,
        String email
    ) {}

    public record ArrivalTimeDTO (
        String checkIn,
        String checkOut
    ) {}
}
