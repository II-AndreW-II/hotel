package com.example.hotel.util;

import com.example.hotel.model.*;
import com.example.hotel.dto.*;

import java.util.List;

public class TestDataUtil {
    public static Hotel createTestHotel() {
        Hotel hotel = new Hotel();
        hotel.setId(1L);
        hotel.setName("Test Hotel");
        hotel.setDescription("A luxurious hotel");
        hotel.setBrand("TestBrand");

        Address address = new Address();
        address.setId(1L);
        address.setHouseNumber(123);
        address.setStreet("Test Street");
        address.setCity("Test City");
        address.setCountry("Test Country");
        address.setPostCode("12345");
        hotel.setAddress(address);

        Contact contact = new Contact();
        contact.setId(1L);
        contact.setPhone("+1234567890");
        contact.setEmail("test@hotel.com");
        hotel.setContacts(contact);

        ArrivalTime arrivalTime = new ArrivalTime();
        arrivalTime.setId(1L);
        arrivalTime.setCheckIn("14:00");
        arrivalTime.setCheckOut("12:00");
        hotel.setArrivalTime(arrivalTime);

        Amenity amenity = new Amenity();
        amenity.setId(1L);
        amenity.setName("Free WiFi");
        hotel.setAmenities(List.of(amenity));

        return hotel;
    }

    public static HotelCreateDTO createTestHotelCreateDTO() {
        return new HotelCreateDTO(
            "Test Hotel",
            "A luxurious hotel",
            "TestBrand",
            new HotelDetailsDTO.AddressDTO(123, "Test Street", "Test City", "Test Country", "12345"),
            new HotelDetailsDTO.ContactDTO("+1234567890", "test@hotel.com"),
            new HotelDetailsDTO.ArrivalTimeDTO("14:00", "12:00")
        );
    }

    public static HotelResponseDTO createTestHotelResponseDTO() {
        return new HotelResponseDTO(
            1L,
            "Test Hotel",
            "A luxurious hotel",
            "123 Test Street, Test City, 12345, Test",
            "+1234567890"
        );
    }

    public static HotelDetailsDTO createTestHotelDetailsDTO() {
        return new HotelDetailsDTO(
            1L,
            "Test Hotel",
            "TestBrand",
            new HotelDetailsDTO.AddressDTO(123, "Test Street", "Test City", "Test Country", "12345"),
            new HotelDetailsDTO.ContactDTO("+1234567890", "test@hotel.com"),
            new HotelDetailsDTO.ArrivalTimeDTO("14:00", "12:00"),
            List.of("Free WiFi")
        );
    }
}
