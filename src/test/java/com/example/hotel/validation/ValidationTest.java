package com.example.hotel.validation;

import com.example.hotel.dto.HotelCreateDTO;
import com.example.hotel.dto.HotelDetailsDTO;
import com.example.hotel.util.TestDataUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidPhoneNumber() {
        HotelCreateDTO dto = TestDataUtil.createTestHotelCreateDTO();
        Set<ConstraintViolation<HotelCreateDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidPhoneNumber() {
        HotelCreateDTO dto = TestDataUtil.createTestHotelCreateDTO();
        HotelDetailsDTO.ContactDTO invalidContact = new HotelDetailsDTO.ContactDTO(
            "1",
            dto.contacts().email()
        );
        HotelCreateDTO invalidDto = new HotelCreateDTO(
            dto.name(),
            dto.description(),
            dto.brand(),
            dto.address(),
            invalidContact,
            dto.arrivalTime()
        );

        Set<ConstraintViolation<HotelCreateDTO>> violations = validator.validate(invalidDto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().contains("contacts.phone")));
    }

    @Test
    void testValidEmail() {
        HotelCreateDTO dto = TestDataUtil.createTestHotelCreateDTO();
        Set<ConstraintViolation<HotelCreateDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidEmail() {
        HotelCreateDTO dto = TestDataUtil.createTestHotelCreateDTO();
        HotelDetailsDTO.ContactDTO invalidContact = new HotelDetailsDTO.ContactDTO(
            dto.contacts().phone(),
            "invalid-email"
        );
        HotelCreateDTO invalidDto = new HotelCreateDTO(
            dto.name(),
            dto.description(),
            dto.brand(),
            dto.address(),
            invalidContact,
            dto.arrivalTime()
        );

        Set<ConstraintViolation<HotelCreateDTO>> violations = validator.validate(invalidDto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().contains("contacts.email")));
    }

    @Test
    void testValidCheckInTime() {
        HotelCreateDTO dto = TestDataUtil.createTestHotelCreateDTO();
        Set<ConstraintViolation<HotelCreateDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidCheckInTime() {
        HotelCreateDTO dto = TestDataUtil.createTestHotelCreateDTO();
        HotelDetailsDTO.ArrivalTimeDTO invalidArrivalTime = new HotelDetailsDTO.ArrivalTimeDTO(
            "25:00",
            dto.arrivalTime().checkOut()
        );
        HotelCreateDTO invalidDto = new HotelCreateDTO(
            dto.name(),
            dto.description(),
            dto.brand(),
            dto.address(),
            dto.contacts(),
            invalidArrivalTime
        );

        Set<ConstraintViolation<HotelCreateDTO>> violations = validator.validate(invalidDto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().contains("arrivalTime.checkIn")));
    }

    @Test
    void testValidCheckOutTime() {
        HotelCreateDTO dto = TestDataUtil.createTestHotelCreateDTO();
        Set<ConstraintViolation<HotelCreateDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testInvalidCheckOutTime() {
        HotelCreateDTO dto = TestDataUtil.createTestHotelCreateDTO();
        HotelDetailsDTO.ArrivalTimeDTO invalidArrivalTime = new HotelDetailsDTO.ArrivalTimeDTO(
            dto.arrivalTime().checkIn(),
            "13:61"
        );
        HotelCreateDTO invalidDto = new HotelCreateDTO(
            dto.name(),
            dto.description(),
            dto.brand(),
            dto.address(),
            dto.contacts(),
            invalidArrivalTime
        );

        Set<ConstraintViolation<HotelCreateDTO>> violations = validator.validate(invalidDto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().contains("arrivalTime.checkOut")));
    }

    @Test
    void testRequiredFields() {
        HotelCreateDTO dto = TestDataUtil.createTestHotelCreateDTO();
        HotelCreateDTO invalidDto = new HotelCreateDTO(
            null,
            dto.description(),
            dto.brand(),
            dto.address(),
            dto.contacts(),
            dto.arrivalTime()
        );

        Set<ConstraintViolation<HotelCreateDTO>> violations = validator.validate(invalidDto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
            .anyMatch(v -> v.getPropertyPath().toString().equals("name")));
    }

    @Test
    void testValidAddress() {
        HotelCreateDTO dto = TestDataUtil.createTestHotelCreateDTO();
        Set<ConstraintViolation<HotelCreateDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }
} 
