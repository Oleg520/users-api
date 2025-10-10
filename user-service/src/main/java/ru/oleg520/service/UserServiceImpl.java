package ru.oleg520.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.oleg520.dao.UserRepository;
import ru.oleg520.dto.*;
import ru.oleg520.exception.NotFoundException;
import ru.oleg520.exception.ValidationException;
import ru.oleg520.mapper.UserMapper;
import ru.oleg520.model.User;

import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserEventProducer userEventProducer;

    @Override
    @Transactional
    public UserDto create(NewUserDto newUserDto) {
        checkEmailUnique(newUserDto.email());

        User user = userMapper.fromNewUserDto(newUserDto);
        user = userRepository.save(user);

        userEventProducer.sendUserEvent(new UserEvent(user.getEmail(), UserOperationType.CREATE));
        log.info("Добавлен новый пользователь \"{}\" c id {}", user.getName(), user.getId());
        return userMapper.toDto(user);
    }

    @Override
    public UserDto getById(Long userId) {
        User user = getUserByIdOrThrow(userId);
        return userMapper.toDto(user);
    }

    @Override
    public List<UserDto> getAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public UserDto update(Long userId, UpdateUserDto newUser) {
        User existingUser = getUserByIdOrThrow(userId);

        String newEmail = newUser.email();
        if (newEmail != null && !newEmail.equals(existingUser.getEmail())) {
            checkEmailUnique(newEmail);
        }

        userMapper.updateUserFromDto(existingUser, newUser);
        User user = userRepository.save(existingUser);
        log.info("Обновлен пользователь \"{}\" с id {}", user.getName(), user.getId());
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public Long delete(Long userId) {
        User user = getUserByIdOrThrow(userId);
        userRepository.delete(user);

        userEventProducer.sendUserEvent(new UserEvent(user.getEmail(), UserOperationType.DELETE));
        log.info("Удален пользователь \"{}\" с id {}", user.getName(), user.getId());
        return user.getId();
    }

    private void checkEmailUnique(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ValidationException("Email already exists");
        }
    }

    private User getUserByIdOrThrow(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));
    }
}
