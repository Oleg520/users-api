package ru.oleg520.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.oleg520.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
