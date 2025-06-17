package com.example.hotel.repository;

import com.example.hotel.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    @Query("SELECT h FROM Hotel h WHERE " +
            "(:name IS NULL OR LOWER(h.name) LIKE LOWER(:name)) AND " +
            "(:brand IS NULL OR LOWER(h.brand) = LOWER(:brand)) AND " +
            "(:city IS NULL OR LOWER(h.address.city) = LOWER(:city)) AND " +
            "(:country IS NULL OR LOWER(h.address.country) = LOWER(:country)) AND " +
            "(:amenity IS NULL OR EXISTS (SELECT a FROM h.amenities a WHERE LOWER(a.name) = LOWER(:amenity)))")
    List<Hotel> searchHotels(
        @Param("name") String name,
        @Param("brand") String brand,
        @Param("city") String city,
        @Param("country") String country,
        @Param("amenity") String amenity
    );

    @Query("SELECT h.brand, COUNT(h) FROM Hotel h GROUP BY h.brand")
    List<Object[]> countByBrand();

    @Query("SELECT h.address.city, COUNT(h) FROM Hotel h GROUP BY h.address.city")
    List<Object[]> countByCity();

    @Query("SELECT h.address.country, COUNT(h) FROM Hotel h GROUP BY h.address.country")
    List<Object[]> countByCountry();

    @Query("SELECT a.name, COUNT(h) FROM Hotel h JOIN h.amenities a GROUP BY a.name")
    List<Object[]> countByAmenity();
}
