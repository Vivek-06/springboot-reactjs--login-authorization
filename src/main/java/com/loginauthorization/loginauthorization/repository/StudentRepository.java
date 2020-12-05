package com.loginauthorization.loginauthorization.repository;

import com.loginauthorization.loginauthorization.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByEmail(String email);
    Optional<Student> findByUsernameOrEmail(String username, String email);
    List<Student> findByIdIn(List<Integer> userIds);
    Optional<Student> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
