package ru.oleg520.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.oleg520.dao.UserRepository;
import ru.oleg520.dto.user.NewUserDto;
import ru.oleg520.dto.user.UpdateUserDto;
import ru.oleg520.dto.user.UserDto;
import ru.oleg520.exception.NotFoundException;
import ru.oleg520.exception.ValidationException;
import ru.oleg520.mapper.UserMapper;
import ru.oleg520.mapper.UserMapperImpl;
import ru.oleg520.model.User;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserEventProducer userEventProducer;
    @Mock
    private NotificationService notificationService;

    private final UserMapper userMapper = new UserMapperImpl();

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, userMapper, userEventProducer, notificationService);
    }

    @Test
    void create_whenIsOk() {
        NewUserDto newUserDto = new NewUserDto("Alex", "alex@example.com", 25);

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName(newUserDto.name());
        savedUser.setEmail(newUserDto.email());
        savedUser.setAge(newUserDto.age());

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDto result = userService.create(newUserDto);

        assertEquals(savedUser.getName(), result.name());
        assertEquals(savedUser.getEmail(), result.email());
        assertEquals(savedUser.getAge(), result.age());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void create_whenEmailAlreadyExists_thenThrowException() {
        NewUserDto newUserDto = new NewUserDto("Alex", "alex@example.com", 25);

        when(userRepository.existsByEmail(newUserDto.email())).thenReturn(true);

        assertThrows(ValidationException.class,
                () -> userService.create(newUserDto));

        verify(userRepository, never()).save(any());
    }

    @Test
    void getById_whenIsOk() {
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setName("Alex");
        savedUser.setEmail("alex@example.com");
        savedUser.setAge(25);

        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));

        UserDto result = userService.getById(1L);

        assertEquals(savedUser.getName(), result.name());
        assertEquals(savedUser.getEmail(), result.email());
        assertEquals(savedUser.getAge(), result.age());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getById_whenUserNotFound_thenThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userService.getById(1L));

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getAll_whenIsOk() {
        User user1 = new User();
        user1.setId(1L);
        user1.setName("Alex");
        user1.setEmail("alex@example.com");

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Bob");
        user2.setEmail("bob@example.com");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<UserDto> result = userService.getAll();

        assertEquals(2, result.size());
        assertEquals("Alex", result.get(0).name());
        assertEquals("Bob", result.get(1).name());
    }

    @Test
    void update_whenIsOk() {
        long userId = 1L;
        User oldUser = new User();
        oldUser.setId(userId);
        oldUser.setName("Alex");
        oldUser.setEmail("alex@example.com");
        oldUser.setAge(25);

        UpdateUserDto newUser = new UpdateUserDto("Alexander", null, null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserDto updatedUser = userService.update(userId, newUser);

        assertEquals(newUser.name(), updatedUser.name());
        assertEquals(oldUser.getEmail(), updatedUser.email());
        assertEquals(oldUser.getAge(), updatedUser.age());
    }

    @Test
    void update_whenUserNotFound_thenThrowException() {
        long userId = 1L;
        UpdateUserDto newUser = new UpdateUserDto("Alexander", null, null);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userService.update(userId, newUser));

        verify(userRepository, never()).save(any());
    }

    @Test
    void update_whenEmailAlreadyExists_thenThrowException() {
        long userId = 1L;
        User oldUser = new User();

        UpdateUserDto newUser = new UpdateUserDto(null, "alex@example.com", null);

        when(userRepository.findById(userId)).thenReturn(Optional.of(oldUser));
        when(userRepository.existsByEmail(newUser.email())).thenReturn(true);

        assertThrows(ValidationException.class,
                () -> userService.update(userId, newUser));
        verify(userRepository, never()).save(any());
    }

    @Test
    void delete_whenIsOk() {
        long userId = 1L;
        User existingUser = new User();

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        userService.delete(userId);

        verify(userRepository, times(1)).delete(existingUser);
    }

    @Test
    void delete_whenUserNotFound_thenThrowException() {
        long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userService.delete(userId));

        verify(userRepository, never()).delete(any());
    }
}
