package com.example.hotel.repository;

import com.example.hotel.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
class HotelRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private HotelRepository hotelRepository;

    private Hotel testHotel;

    @BeforeEach
    void setUp() {
        Address address = new Address();
        address.setHouseNumber(123);
        address.setStreet("Test Street");
        address.setCity("Test City");
        address.setCountry("Test Country");
        address.setPostCode("12345");
        entityManager.persist(address);
        entityManager.flush();

        Contact contact = new Contact();
        contact.setPhone("+1234567890");
        contact.setEmail("test@hotel.com");
        entityManager.persist(contact);
        entityManager.flush();

        ArrivalTime arrivalTime = new ArrivalTime();
        arrivalTime.setCheckIn("14:00");
        arrivalTime.setCheckOut("12:00");
        entityManager.persist(arrivalTime);
        entityManager.flush();

        Amenity amenity = new Amenity();
        amenity.setName("Free WiFi");
        entityManager.persist(amenity);
        entityManager.flush();

        testHotel = new Hotel();
        testHotel.setName("Test Hotel");
        testHotel.setDescription("A luxurious hotel");
        testHotel.setBrand("TestBrand");
        testHotel.setAddress(address);
        testHotel.setContacts(contact);
        testHotel.setArrivalTime(arrivalTime);
        testHotel.setAmenities(List.of(amenity));
        
        entityManager.persist(testHotel);
        entityManager.flush();
    }

    @Test
    void testSearchHotels_Success() {
        List<Hotel> result = hotelRepository.searchHotels(
            testHotel.getName(),
            testHotel.getBrand(),
            testHotel.getAddress().getCity(),
            testHotel.getAddress().getCountry(),
            testHotel.getAmenities().get(0).getName()
        );

        assertEquals(1, result.size());
        assertEquals(testHotel.getName(), result.get(0).getName());
        assertEquals(testHotel.getBrand(), result.get(0).getBrand());
        assertEquals(testHotel.getAddress().getCity(), result.get(0).getAddress().getCity());
    }

    @Test
    void testSearchHotels_NoResults() {
        List<Hotel> result = hotelRepository.searchHotels("NonExistent", null, null, null, null);

        assertTrue(result.isEmpty());
    }

    @Test
    void testCountByBrand_Success() {
        List<Object[]> result = hotelRepository.countByBrand();

        assertEquals(1, result.size());
        assertEquals(testHotel.getBrand(), result.get(0)[0]);
        assertEquals(1L, result.get(0)[1]);
    }

    @Test
    void testCountByBrand_NoData() {
        hotelRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();

        List<Object[]> result = hotelRepository.countByBrand();

        assertTrue(result.isEmpty());
    }

    @Test
    void testCountByCity_Success() {
        List<Object[]> result = hotelRepository.countByCity();

        assertEquals(1, result.size());
        assertEquals(testHotel.getAddress().getCity(), result.get(0)[0]);
        assertEquals(1L, result.get(0)[1]);
    }

    @Test
    void testCountByCity_NoData() {
        hotelRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();

        List<Object[]> result = hotelRepository.countByCity();

        assertTrue(result.isEmpty());
    }

    @Test
    void testCountByCountry_Success() {
        List<Object[]> result = hotelRepository.countByCountry();

        assertEquals(1, result.size());
        assertEquals(testHotel.getAddress().getCountry(), result.get(0)[0]);
        assertEquals(1L, result.get(0)[1]);
    }

    @Test
    void testCountByCountry_NoData() {
        hotelRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();

        List<Object[]> result = hotelRepository.countByCountry();

        assertTrue(result.isEmpty());
    }

    @Test
    void testCountByAmenity_Success() {
        List<Object[]> result = hotelRepository.countByAmenity();

        assertEquals(1, result.size());
        assertEquals(testHotel.getAmenities().get(0).getName(), result.get(0)[0]);
        assertEquals(1L, result.get(0)[1]);
    }

    @Test
    void testCountByAmenity_NoData() {
        hotelRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();

        List<Object[]> result = hotelRepository.countByAmenity();

        assertTrue(result.isEmpty());
    }

    @Test
    void testSearchHotels_CaseInsensitiveName() {
        List<Hotel> result = hotelRepository.searchHotels(
            "TEST HOTEL",  
            null,
            null,
            null,
            null
        );

        assertEquals(1, result.size());
        assertEquals(testHotel.getName(), result.get(0).getName());
    }

    @Test
    void testSearchHotels_CaseInsensitiveBrand() {
        List<Hotel> result = hotelRepository.searchHotels(
            null,
            "TESTBRAND",  
            null,
            null,
            null
        );

        assertEquals(1, result.size());
        assertEquals(testHotel.getBrand(), result.get(0).getBrand());
    }

    @Test
    void testSearchHotels_CaseInsensitiveCity() {
        List<Hotel> result = hotelRepository.searchHotels(
            null,
            null,
            "TEST CITY",  
            null,
            null
        );

        assertEquals(1, result.size());
        assertEquals(testHotel.getAddress().getCity(), result.get(0).getAddress().getCity());
    }

    @Test
    void testSearchHotels_CaseInsensitiveCountry() {
        List<Hotel> result = hotelRepository.searchHotels(
            null,
            null,
            null,
            "TEST COUNTRY",
            null
        );

        assertEquals(1, result.size());
        assertEquals(testHotel.getAddress().getCountry(), result.get(0).getAddress().getCountry());
    }

    @Test
    void testSearchHotels_CaseInsensitiveAmenity() {
        List<Hotel> result = hotelRepository.searchHotels(
            null,
            null,
            null,
            null,
            "FREE WIFI"
        );

        assertEquals(1, result.size());
        assertEquals(testHotel.getAmenities().get(0).getName(), result.get(0).getAmenities().get(0).getName());
    }

    @Test
    void testSearchHotels_CaseInsensitiveAllParameters() {
        List<Hotel> result = hotelRepository.searchHotels(
            "TEST HOTEL",  
            "TESTBRAND",   
            "TEST CITY",   
            "TEST COUNTRY",
            "FREE WIFI"    
        );

        assertEquals(1, result.size());
        assertEquals(testHotel.getName(), result.get(0).getName());
        assertEquals(testHotel.getBrand(), result.get(0).getBrand());
        assertEquals(testHotel.getAddress().getCity(), result.get(0).getAddress().getCity());
        assertEquals(testHotel.getAddress().getCountry(), result.get(0).getAddress().getCountry());
        assertEquals(testHotel.getAmenities().get(0).getName(), result.get(0).getAmenities().get(0).getName());
    }
}