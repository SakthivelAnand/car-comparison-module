package com.car360.carcomparison.car_comparison_module.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.car360.carcomparison.car_comparison_module.exception.ResourceNotFoundException;
import com.car360.carcomparison.car_comparison_module.model.User;
import com.car360.carcomparison.car_comparison_module.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testGetUserByUserId_Success() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setUserId(userId);
        user.setUsername("Test User");

        when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));

        // Act
        User result = userServiceImpl.getUserByUserId(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals("Test User", result.getUsername());

        verify(userRepository, times(1)).findByUserId(userId);
    }

    @Test
    public void testGetUserByUserId_NotFound() {
        // Arrange
        Long userId = 1L;

        when(userRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userServiceImpl.getUserByUserId(userId);
        });

        assertEquals("User not found with userId: " + userId, exception.getMessage());
        verify(userRepository, times(1)).findByUserId(userId);
    }
}
