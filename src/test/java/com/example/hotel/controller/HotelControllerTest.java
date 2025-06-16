package com.example.hotel.controller;

import com.example.hotel.dto.HotelCreateDTO;
import com.example.hotel.dto.HotelDetailsDTO;
import com.example.hotel.service.HotelService;
import com.example.hotel.util.TestDataUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private HotelService hotelService;

    private HotelCreateDTO validCreateDto;
    private HotelDetailsDTO validDetailsDto;

    @BeforeEach
    void setUp() {
        validCreateDto = TestDataUtil.createTestHotelCreateDTO();
        validDetailsDto = TestDataUtil.createTestHotelDetailsDTO();

        when(hotelService.getAllHotels()).thenReturn(List.of(TestDataUtil.createTestHotelResponseDTO()));
        when(hotelService.getHotelById(1L)).thenReturn(validDetailsDto);
        when(hotelService.getHotelById(9999L)).thenThrow(new RuntimeException("Hotel not found"));
        when(hotelService.searchHotels(any(), any(), any(), any(), any())).thenReturn(List.of(TestDataUtil.createTestHotelResponseDTO()));
        when(hotelService.createHotel(any())).thenReturn(TestDataUtil.createTestHotelResponseDTO());
        doNothing().when(hotelService).addAmenities(anyLong(), any());
        when(hotelService.getHistogram(any())).thenReturn(Map.of("TestBrand", 1L));
    }

    @Test
    void testGetAllHotels_Success() throws Exception {
        mockMvc.perform(get("/property-view/hotels")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetHotelById_Success() throws Exception {
        mockMvc.perform(get("/property-view/hotels/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(validDetailsDto.name()))
                .andExpect(jsonPath("$.brand").value(validDetailsDto.brand()));
    }

    @Test
    void testGetHotelById_NotFound() throws Exception {
        mockMvc.perform(get("/property-view/hotels/9999")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Hotel not found"));
    }

    @Test
    void testSearchHotels_Success() throws Exception {
        mockMvc.perform(get("/property-view/search")
                .param("name", validDetailsDto.name())
                .param("brand", validDetailsDto.brand())
                .param("city", validDetailsDto.address().city())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testSearchHotels_NoResults() throws Exception {
        when(hotelService.searchHotels(any(), any(), any(), any(), any())).thenReturn(List.of());

        mockMvc.perform(get("/property-view/search")
                .param("name", "NonExistent")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void testCreateHotel_Success() throws Exception {
        mockMvc.perform(post("/property-view/hotels")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validCreateDto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(validCreateDto.name()));
    }

    @Test
    void testCreateHotel_MissingRequiredField() throws Exception {
        HotelCreateDTO invalidDto = new HotelCreateDTO(
                null,
                validCreateDto.description(),
                validCreateDto.brand(),
                validCreateDto.address(),
                validCreateDto.contacts(),
                validCreateDto.arrivalTime()
        );

        mockMvc.perform(post("/property-view/hotels")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testAddAmenities_Success() throws Exception {
        List<String> amenities = validDetailsDto.amenities();

        mockMvc.perform(post("/property-view/hotels/1/amenities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(amenities))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testAddAmenities_HotelNotFound() throws Exception {
        List<String> amenities = validDetailsDto.amenities();
        doThrow(new RuntimeException("Hotel not found")).when(hotelService).addAmenities(9999L, amenities);

        mockMvc.perform(post("/property-view/hotels/9999/amenities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(amenities))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Hotel not found"));
    }

    @Test
    void testAddAmenities_EmptyList() throws Exception {
        List<String> amenities = List.of();

        mockMvc.perform(post("/property-view/hotels/1/amenities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(amenities))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testGetHistogram_Success() throws Exception {
        mockMvc.perform(get("/property-view/histogram/brand")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetHistogram_InvalidParam() throws Exception {
        when(hotelService.getHistogram("invalid")).thenThrow(new IllegalArgumentException("Invalid histogram parameter"));

        mockMvc.perform(get("/property-view/histogram/invalid")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid histogram parameter"));
    }
}
