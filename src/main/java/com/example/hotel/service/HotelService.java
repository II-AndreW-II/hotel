package com.example.hotel.service;

import com.example.hotel.dto.*;
import com.example.hotel.model.*;
import com.example.hotel.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HotelService {
    private final HotelRepository hotelRepository;
    private final AmenityRepository amenityRepository;

    public HotelService(HotelRepository hotelRepository, AmenityRepository amenityRepository) {
        this.hotelRepository = hotelRepository;
        this.amenityRepository = amenityRepository;
    }

    public List<HotelResponseDTO> getAllHotels() {
        return hotelRepository.findAll().stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    public HotelDetailsDTO getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Hotel not found"));
        return toDetailsDTO(hotel);
    }

    public List<HotelResponseDTO> searchHotels(String name, String brand, String city, String country, String amenity) {
        return hotelRepository.searchHotels(name, brand, city, country, amenity).stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    @Transactional
    public HotelResponseDTO createHotel(HotelCreateDTO dto) {
        Hotel hotel = new Hotel();
        hotel.setName(dto.name());
        hotel.setDescription(dto.description());
        hotel.setBrand(dto.brand());
        hotel.setAddress(toAddressEntity(dto.address()));
        hotel.setContacts(toContactEntity(dto.contacts()));
        if(dto.arrivalTime() != null) {
            hotel.setArrivalTime(toArrivalTimeEntity(dto.arrivalTime()));
        }
        hotel = hotelRepository.save(hotel);
        return toResponseDTO(hotel);
    }

    @Transactional
    public void addAmenities(Long hotelId, List<String> amenities) {
        Hotel hotel = hotelRepository.findById(hotelId)
            .orElseThrow(() -> new RuntimeException("Hotel not found"));
        List<Amenity> amenityEntities = amenities.stream()
            .map(name -> amenityRepository.findByName(name)
                .orElseGet(() -> {
                    Amenity amenity = new Amenity();
                    amenity.setName(name);
                    return amenityRepository.save(amenity);
                }))
            .collect(Collectors.toList());
        hotel.getAmenities().addAll(amenityEntities);
        hotelRepository.save(hotel);
    }

    public Map<String, Long> getHistogram(String param) {
        switch (param.toLowerCase()) {
            case "brand":
                return hotelRepository.countByBrand().stream()
                    .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (Long) row[1]
                    ));
            case "city":
                return hotelRepository.countByCity().stream()
                    .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (Long) row[1]
                    ));
            case "country":
                return hotelRepository.countByCountry().stream()
                    .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (Long) row[1]
                    ));
            case "amenities":
                return hotelRepository.countByAmenity().stream()
                    .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> (Long) row[1]
                    ));
            default:
                throw new IllegalArgumentException("Invalid histogram parameter");
        }
    }

    private HotelResponseDTO toResponseDTO(Hotel hotel) {
        String address = String.format("%d %s, %s, %s, %s",
            hotel.getAddress().getHouseNumber(),
            hotel.getAddress().getStreet(),
            hotel.getAddress().getCity(),
            hotel.getAddress().getPostCode(),
            hotel.getAddress().getCountry());
        return new HotelResponseDTO(
            hotel.getId(),
            hotel.getName(),
            hotel.getDescription(),
            address,
            hotel.getContacts().getPhone()
        );
    }

    private HotelDetailsDTO toDetailsDTO(Hotel hotel) {
        return new HotelDetailsDTO(
            hotel.getId(),
            hotel.getName(),
            hotel.getBrand(),
            new HotelDetailsDTO.AddressDTO(
                hotel.getAddress().getHouseNumber(),
                hotel.getAddress().getStreet(),
                hotel.getAddress().getCity(),
                hotel.getAddress().getCountry(),
                hotel.getAddress().getPostCode()
            ),
            new HotelDetailsDTO.ContactDTO(
                hotel.getContacts().getPhone(),
                hotel.getContacts().getEmail()
            ),
            hotel.getArrivalTime() != null ? new HotelDetailsDTO.ArrivalTimeDTO(
                hotel.getArrivalTime().getCheckIn(),
                hotel.getArrivalTime().getCheckOut()
            ) : null,
            hotel.getAmenities().stream().map(Amenity::getName).collect(Collectors.toList())
        );
    }

    private Address toAddressEntity(HotelDetailsDTO.AddressDTO dto) {
        Address address = new Address();
        address.setHouseNumber(dto.houseNumber());
        address.setStreet(dto.street());
        address.setCity(dto.city());
        address.setCountry(dto.country());
        address.setPostCode(dto.postCode());
        return address;
    }

    private Contact toContactEntity(HotelDetailsDTO.ContactDTO dto) {
        Contact contact = new Contact();
        contact.setPhone(dto.phone());
        contact.setEmail(dto.email());
        return contact;
    }

    private ArrivalTime toArrivalTimeEntity(HotelDetailsDTO.ArrivalTimeDTO dto) {
        ArrivalTime arrivalTime = new ArrivalTime();
        arrivalTime.setCheckIn(dto.checkIn());
        arrivalTime.setCheckOut(dto.checkOut());
        return arrivalTime;
    }
}
