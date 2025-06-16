package com.example.hotel.service;

import com.example.hotel.dto.*;
import com.example.hotel.model.Amenity;
import com.example.hotel.model.Hotel;
import com.example.hotel.repository.AmenityRepository;
import com.example.hotel.repository.HotelRepository;
import com.example.hotel.util.TestDataUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {

    @Mock
    private HotelRepository hotelRepository;

    @Mock
    private AmenityRepository amenityRepository;

    @InjectMocks
    private HotelService hotelService;

    private Hotel testHotel;
    private HotelCreateDTO testCreateDto;
    private HotelDetailsDTO testDetailsDto;
    private HotelResponseDTO testResponseDto;

    @BeforeEach
    void setUp() {
        testHotel = TestDataUtil.createTestHotel();
        testCreateDto = TestDataUtil.createTestHotelCreateDTO();
        testDetailsDto = TestDataUtil.createTestHotelDetailsDTO();
        testResponseDto = TestDataUtil.createTestHotelResponseDTO();
    }

    @Test
    void testGetAllHotels_Success() {
        when(hotelRepository.findAll()).thenReturn(List.of(testHotel));

        List<HotelResponseDTO> result = hotelService.getAllHotels();

        assertEquals(1, result.size());
        assertEquals(testResponseDto.name(), result.get(0).name());
        verify(hotelRepository, times(1)).findAll();
    }

    @Test
    void testGetAllHotels_EmptyList() {
        when(hotelRepository.findAll()).thenReturn(List.of());

        List<HotelResponseDTO> result = hotelService.getAllHotels();

        assertTrue(result.isEmpty());
        verify(hotelRepository, times(1)).findAll();
    }

    @Test
    void testGetHotelById_Success() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(testHotel));

        HotelDetailsDTO result = hotelService.getHotelById(1L);

        assertEquals(testDetailsDto.name(), result.name());
        assertEquals(testDetailsDto.brand(), result.brand());
        verify(hotelRepository, times(1)).findById(1L);
    }

    @Test
    void testGetHotelById_NotFound() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> hotelService.getHotelById(1L));
        assertEquals("Hotel not found", exception.getMessage());
        verify(hotelRepository, times(1)).findById(1L);
    }

    @Test
    void testSearchHotels_Success() {
        when(hotelRepository.searchHotels(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(List.of(testHotel));

        List<HotelResponseDTO> result = hotelService.searchHotels(
            testDetailsDto.name(),
            testDetailsDto.brand(),
            testDetailsDto.address().city(),
            testDetailsDto.address().country(),
            testDetailsDto.amenities().get(0)
        );

        assertEquals(1, result.size());
        assertEquals(testResponseDto.name(), result.get(0).name());
        verify(hotelRepository, times(1)).searchHotels(
            testDetailsDto.name(),
            testDetailsDto.brand(),
            testDetailsDto.address().city(),
            testDetailsDto.address().country(),
            testDetailsDto.amenities().get(0)
        );
    }

    @Test
    void testSearchHotels_NoResults() {
        when(hotelRepository.searchHotels(eq("NonExistent"), isNull(), isNull(), isNull(), isNull()))
                .thenReturn(List.of());

        List<HotelResponseDTO> result = hotelService.searchHotels("NonExistent", null, null, null, null);

        assertTrue(result.isEmpty());
        verify(hotelRepository, times(1)).searchHotels("NonExistent", null, null, null, null);
    }

    @Test
    void testCreateHotel_Success() {
        when(hotelRepository.save(any(Hotel.class))).thenReturn(testHotel);

        HotelResponseDTO result = hotelService.createHotel(testCreateDto);

        assertEquals(testResponseDto.name(), result.name());
        verify(hotelRepository, times(1)).save(any(Hotel.class));
    }

    @Test
    void testCreateHotel_NullName() {
        HotelCreateDTO invalidDto = new HotelCreateDTO(
                null,
                testCreateDto.description(),
                testCreateDto.brand(),
                testCreateDto.address(),
                testCreateDto.contacts(),
                testCreateDto.arrivalTime()
        );

        assertThrows(IllegalArgumentException.class, () -> {
            if (invalidDto.name() == null) {
                throw new IllegalArgumentException("Name is required");
            }
            hotelService.createHotel(invalidDto);
        });
        verify(hotelRepository, never()).save(any(Hotel.class));
    }

    @Test
    void testAddAmenities_Success() {
        Amenity amenity = new Amenity();
        amenity.setId(1L);
        amenity.setName(testDetailsDto.amenities().get(0));

        Hotel hotel = TestDataUtil.createTestHotel();
        hotel.setAmenities(new ArrayList<>());

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        when(amenityRepository.findByName(testDetailsDto.amenities().get(0))).thenReturn(Optional.empty());
        when(amenityRepository.save(any(Amenity.class))).thenReturn(amenity);
        when(hotelRepository.save(any(Hotel.class))).thenReturn(hotel);

        hotelService.addAmenities(1L, testDetailsDto.amenities());

        verify(hotelRepository, times(1)).findById(1L);
        verify(amenityRepository, times(1)).findByName(testDetailsDto.amenities().get(0));
        verify(amenityRepository, times(1)).save(any(Amenity.class));
        verify(hotelRepository, times(1)).save(any(Hotel.class));
    }

    @Test
    void testAddAmenities_HotelNotFound() {
        when(hotelRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> hotelService.addAmenities(1L, testDetailsDto.amenities()));
        assertEquals("Hotel not found", exception.getMessage());
        verify(hotelRepository, times(1)).findById(1L);
        verify(amenityRepository, never()).findByName(anyString());
    }

    @Test
    void testGetHistogram_Brand_Success() {
        List<Object[]> brandCounts = new ArrayList<>();
        brandCounts.add(new Object[]{testDetailsDto.brand(), 1L});
        when(hotelRepository.countByBrand()).thenReturn(brandCounts);

        Map<String, Long> result = hotelService.getHistogram("brand");

        assertEquals(1, result.size());
        assertEquals(1L, result.get(testDetailsDto.brand()));
        verify(hotelRepository, times(1)).countByBrand();
        verify(hotelRepository, never()).countByCity();
    }

    @Test
    void testGetHistogram_Amenities_Success() {
        List<Object[]> amenityCounts = new ArrayList<>();
        amenityCounts.add(new Object[]{testDetailsDto.amenities().get(0), 1L});
        when(hotelRepository.countByAmenity()).thenReturn(amenityCounts);

        Map<String, Long> result = hotelService.getHistogram("amenities");

        assertEquals(1, result.size());
        assertEquals(1L, result.get(testDetailsDto.amenities().get(0)));
        verify(hotelRepository, times(1)).countByAmenity();
    }

    @Test
    void testGetHistogram_InvalidParam() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> hotelService.getHistogram("invalid"));
        assertEquals("Invalid histogram parameter", exception.getMessage());
        verify(hotelRepository, never()).countByBrand();
        verify(hotelRepository, never()).countByAmenity();
    }
}
