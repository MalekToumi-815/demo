package com.example.demo.domain.Service;

import com.example.demo.domain.exception.ResourceNotFoundException;
import com.example.demo.domain.model.User;
import com.example.demo.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void getAllUsersReturnsUsersFromRepository() {
        List<User> users = List.of(
                new User("Ada", "Lovelace", "ada@example.com"),
                new User("Grace", "Hopper", "grace@example.com")
        );
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertSame(users, result);
        verify(userRepository).findAll();
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void getUserByIdReturnsUserWhenFound() {
        Long id = 1L;
        User user = new User("Ada", "Lovelace", "ada@example.com");
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User result = userService.getUserById(id);

        assertSame(user, result);
        verify(userRepository).findById(id);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void getUserByIdThrowsWhenUserIsNotFound() {
        Long id = 42L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getUserById(id)
        );

        assertEquals("User not found with id: " + id, exception.getMessage());
        verify(userRepository).findById(id);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void createUserReturnsSavedUser() {
        User user = new User("Ada", "Lovelace", "ada@example.com");
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.createUser(user);

        assertSame(user, result);
        verify(userRepository).save(user);
        verifyNoMoreInteractions(userRepository);
    }
}
