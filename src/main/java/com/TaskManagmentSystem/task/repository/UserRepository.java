package com.TaskManagmentSystem.task.repository;

import com.TaskManagmentSystem.task.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User , Long> {

    Optional<User> findByEmail(String Email);
}
