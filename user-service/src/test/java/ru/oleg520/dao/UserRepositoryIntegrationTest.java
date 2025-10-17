package ru.oleg520.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.oleg520.model.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@Transactional
class UserRepositoryIntegrationTest {
    @Autowired
    private UserRepository userRepository;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.1")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void create() {
        User user = new User();
        user.setName("Test User");
        user.setEmail("test@gmail.com");

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getId());
        assertNotNull(savedUser.getCreatedAt());
        assertEquals("Test User", savedUser.getName());
    }

    @Test
    void createDuplicateEmailShouldFail() {
        User user1 = new User();
        user1.setName("Alice");
        user1.setEmail("alice@gmail.com");
        userRepository.save(user1);

        User user2 = new User();
        user2.setName("Bob");
        user2.setEmail(user1.getEmail());

        assertThrows(Exception.class, () -> userRepository.save(user2));
    }

    @Test
    void findById() {
        User user = new User();
        user.setName("FindMe");
        user.setEmail("findme@gmail.com");

        userRepository.save(user);

        Optional<User> found = userRepository.findById(user.getId());
        assertTrue(found.isPresent());
        assertEquals("FindMe", found.get().getName());
        assertEquals("findme@gmail.com", found.get().getEmail());
        assertNotNull(found.get().getCreatedAt());
    }

    @Test
    void findAll() {
        User user1 = new User();
        user1.setName("Alice");
        user1.setEmail("alice@gmail.com");

        User user2 = new User();
        user2.setName("Bob");
        user2.setEmail("bob@gmail.com");

        userRepository.save(user1);
        userRepository.save(user2);

        List<User> users = userRepository.findAll();
        assertEquals(2, users.size());

        List<String> names = users.stream().map(User::getName).toList();
        assertTrue(names.contains("Alice"));
        assertTrue(names.contains("Bob"));
    }

    @Test
    void update() {
        User user = new User();
        user.setName("Alice");
        user.setEmail("alice@gmail.com");
        user.setAge(25);
        userRepository.save(user);

        user.setEmail("bob@gmail.com");

        User updatedUser = userRepository.save(user);

        assertEquals("Alice", updatedUser.getName());
        assertEquals("bob@gmail.com", updatedUser.getEmail());
        assertEquals(25, updatedUser.getAge());
        assertNotNull(updatedUser.getCreatedAt());
    }

    @Test
    void updateDuplicateEmailShouldFail() {
        User user1 = new User();
        user1.setName("Alice");
        user1.setEmail("alice@gmail.com");
        userRepository.saveAndFlush(user1);

        User user2 = new User();
        user2.setName("Bob");
        user2.setEmail("bob@gmail.com");
        userRepository.saveAndFlush(user2);

        user2.setEmail(user1.getEmail());

        assertThrows(Exception.class, () -> {
            userRepository.save(user2);
            userRepository.flush();
        });
    }

    @Test
    void deleteShouldRemoveUser() {
        User user = new User();
        user.setName("Charlie");
        user.setEmail("charlie@gmail.com");
        userRepository.save(user);

        userRepository.delete(user);

        Optional<User> deletedUser = userRepository.findById(user.getId());
        assertTrue(deletedUser.isEmpty());
    }

    @Test
    void checkEmailUniqueShouldWork() {
        User user = new User();
        user.setName("David");
        user.setEmail("david@gmail.com");
        userRepository.save(user);

        assertTrue(userRepository.existsByEmail("david@gmail.com"));
        assertFalse(userRepository.existsByEmail("unknown@gmail.com"));
    }
}
