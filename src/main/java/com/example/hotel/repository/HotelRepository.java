package com.example.hotel.repository;

import com.example.hotel.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    @Query("SELECT h FROM Hotel h WHERE " +
            "(:name IS NULL OR h.name LIKE %:name%) AND " +
            "(:brand IS NULL OR h.brand = :brand) AND " +
            "(:city IS NULL OR h.address.city = :city) AND " +
            "(:county IS NULL OR h.address.county = :county) AND " +
            "(:amenity IS NULL OR EXISTS (SELECT a FROM h.amenities a WHERE a.name = :amenity))")
    List<Hotel> searchHotels(
        @Param("name") String name,
        @Param("brand") String brand,
        @Param("city") String city,
        @Param("county") String county,
        @Param("amenity") String amenity
    );
}
