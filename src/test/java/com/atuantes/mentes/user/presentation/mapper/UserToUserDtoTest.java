package com.atuantes.mentes.user.presentation.mapper;

import com.atuantes.mentes.user.domain.entity.Category;
import com.atuantes.mentes.user.domain.entity.User;
import com.atuantes.mentes.user.domain.exception.UserIllegalArgumentException;
import com.atuantes.mentes.user.presentation.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserToUserDtoTest {

    @InjectMocks
    private UserToUserDto mapper;

    private User validUser;

    @BeforeEach
    void setUp() {
        validUser = new User();
        validUser.setId(UUID.randomUUID());
        validUser.setFullName("John Doe");
        validUser.setActive(true);
        validUser.setDocument("12345678900");
        validUser.setEmail("john.doe@example.com");
        validUser.setPhone("11999999999");
        validUser.setBirthdate(LocalDate.of(1990, 1, 1));
        validUser.setCategory(Category.FATHER);
        validUser.setCreatedAt(LocalDateTime.now());
        validUser.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("When mapping valid User Then should return UserDto with all fields")
    void whenMappingValidUser_thenShouldReturnUserDtoWithAllFields() {
        // When
        UserDto result = mapper.toDto(validUser);

        // Then
        assertNotNull(result);
        assertEquals(validUser.getId(), result.getId());
        assertEquals(validUser.getFullName(), result.getFullName());
        assertEquals(validUser.isActive(), result.isActive());
        assertEquals(validUser.getDocument(), result.getDocument());
        assertEquals(validUser.getEmail(), result.getEmail());
        assertEquals(validUser.getPhone(), result.getPhone());
        assertEquals(validUser.getBirthdate(), result.getBirthdate());
        assertEquals(validUser.getCategory(), result.getCategory());
        assertEquals(validUser.getCreatedAt(), result.getCreatedAt());
        assertEquals(validUser.getUpdatedAt(), result.getUpdatedAt());
    }

    @Test
    @DisplayName("When mapping User with null optional fields Then should return UserDto with null optional fields")
    void whenMappingUserWithNullOptionalFields_thenShouldReturnUserDtoWithNullOptionalFields() {
        // Given
        validUser.setPhone(null);
        validUser.setUpdatedAt(null);

        // When
        UserDto result = mapper.toDto(validUser);

        // Then
        assertNotNull(result);
        assertNull(result.getPhone());
        assertNull(result.getUpdatedAt());
    }

    @Test
    @DisplayName("When mapping inactive User Then should return UserDto with active false")
    void whenMappingInactiveUser_thenShouldReturnUserDtoWithActiveFalse() {
        // Given
        validUser.setActive(false);

        // When
        UserDto result = mapper.toDto(validUser);

        // Then
        assertNotNull(result);
        assertFalse(result.isActive());
    }

    @Test
    @DisplayName("When mapping User with different category Then should preserve category")
    void whenMappingUserWithDifferentCategory_thenShouldPreserveCategory() {
        // Given
        validUser.setCategory(Category.MOTHER);

        // When
        UserDto result = mapper.toDto(validUser);

        // Then
        assertNotNull(result);
        assertEquals(Category.MOTHER, result.getCategory());
    }

    @Test
    @DisplayName("When mapping null User Then should throw UserIllegalArgumentException")
    void whenMappingNullUser_thenShouldThrowUserIllegalArgumentException() {
        // When & Then
        UserIllegalArgumentException exception = assertThrows(
                UserIllegalArgumentException.class,
                () -> mapper.toDto(null)
        );

        assertEquals("USER-0008", exception.getCode());
        assertEquals("Erro ao mapear User para UserDto.", exception.getMessage());
    }

    @Test
    @DisplayName("When mapping User with all categories Then should map correctly")
    void whenMappingUserWithAllCategories_thenShouldMapCorrectly() {
        // Given & When & Then
        for (Category category : Category.values()) {
            validUser.setCategory(category);
            UserDto result = mapper.toDto(validUser);
            assertNotNull(result);
            assertEquals(category, result.getCategory());
        }
    }

    @Test
    @DisplayName("When mapping multiple Users Then should return distinct UserDtos")
    void whenMappingMultipleUsers_thenShouldReturnDistinctUserDtos() {
        // Given
        User anotherUser = new User();
        anotherUser.setId(UUID.randomUUID());
        anotherUser.setFullName("Jane Doe");
        anotherUser.setActive(false);
        anotherUser.setDocument("98765432100");
        anotherUser.setEmail("jane.doe@example.com");
        anotherUser.setPhone("11988888888");
        anotherUser.setBirthdate(LocalDate.of(1995, 5, 15));
        anotherUser.setCategory(Category.MOTHER);
        anotherUser.setCreatedAt(LocalDateTime.now());
        anotherUser.setUpdatedAt(LocalDateTime.now());

        // When
        UserDto result1 = mapper.toDto(validUser);
        UserDto result2 = mapper.toDto(anotherUser);

        // Then
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotEquals(result1.getId(), result2.getId());
        assertNotEquals(result1.getFullName(), result2.getFullName());
        assertNotEquals(result1.getDocument(), result2.getDocument());
    }

    @Test
    @DisplayName("When mapping User with minimum required fields Then should return valid UserDto")
    void whenMappingUserWithMinimumRequiredFields_thenShouldReturnValidUserDto() {
        // Given
        User minimalUser = new User();
        minimalUser.setId(UUID.randomUUID());
        minimalUser.setFullName("Minimal User");
        minimalUser.setActive(true);
        minimalUser.setDocument("11111111111");
        minimalUser.setEmail("minimal@example.com");
        minimalUser.setBirthdate(LocalDate.of(2000, 1, 1));
        minimalUser.setCategory(Category.FATHER);
        minimalUser.setCreatedAt(LocalDateTime.now());

        // When
        UserDto result = mapper.toDto(minimalUser);

        // Then
        assertNotNull(result);
        assertEquals(minimalUser.getId(), result.getId());
        assertEquals(minimalUser.getFullName(), result.getFullName());
        assertEquals(minimalUser.getDocument(), result.getDocument());
    }

    @Test
    @DisplayName("When mapping User with timestamps Then should preserve timestamp values")
    void whenMappingUserWithTimestamps_thenShouldPreserveTimestampValues() {
        // Given
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 1, 2, 15, 30);
        validUser.setCreatedAt(createdAt);
        validUser.setUpdatedAt(updatedAt);

        // When
        UserDto result = mapper.toDto(validUser);

        // Then
        assertNotNull(result);
        assertEquals(createdAt, result.getCreatedAt());
        assertEquals(updatedAt, result.getUpdatedAt());
    }
}