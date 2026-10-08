package com.example.demo.domain.repository;

import com.example.demo.domain.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Testcontainers
class UserRepositoryTest {

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private UserRepository userRepository;

    @Test
    void savesAndFindsUserByEmail() {
        User user = userRepository.save(new User("Ada", "Lovelace", "ada@example.com"));

        assertNotNull(user.getId());
        User found = userRepository.findByEmail("ada@example.com");

        assertNotNull(found);
        assertEquals(user.getId(), found.getId());
        assertEquals("Ada", found.getName());
        assertEquals("Lovelace", found.getSurname());
        assertEquals("ada@example.com", found.getEmail());
    }
}
