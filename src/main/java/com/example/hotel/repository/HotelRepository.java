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
            "(:country IS NULL OR h.address.country = :country) AND " +
            "(:amenity IS NULL OR EXISTS (SELECT a FROM h.amenities a WHERE a.name = :amenity))")
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
