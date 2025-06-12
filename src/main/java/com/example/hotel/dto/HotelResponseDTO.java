package com.example.hotel.dto;

public record HotelResponseDTO (
    Long id,
    String name,
    String descripion,
    String address,
    String phone
) {}
