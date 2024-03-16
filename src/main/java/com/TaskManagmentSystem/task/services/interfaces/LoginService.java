package com.TaskManagmentSystem.task.services.interfaces;

import com.TaskManagmentSystem.task.model.dto.UserDTO;
import com.TaskManagmentSystem.task.model.entity.User;
import com.TaskManagmentSystem.task.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;


public interface LoginService {

    UserDTO register(UserDTO user);

    Optional<UserDTO> findByEmail(String email);
}
