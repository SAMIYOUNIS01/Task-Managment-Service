package com.TaskManagmentSystem.task.services.Imp;

import com.TaskManagmentSystem.task.model.dto.UserDTO;
import com.TaskManagmentSystem.task.model.entity.User;
import com.TaskManagmentSystem.task.repository.UserRepository;
import com.TaskManagmentSystem.task.services.interfaces.LoginService;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginServiceImp implements LoginService {

    @Autowired
    UserRepository userRepository;



    @Override
    public UserDTO register(UserDTO user) {
        User newUser = new User(user.getName(), user.getEmail() , user.getMobileNumber() , user.getPassword() , user.getRole() ,user.getCreate_dt());
        return convertToDto(userRepository.save(newUser));
    }

    @Override
    public Optional<UserDTO> findByEmail(String email) {
        return Optional.of(convertToDto(userRepository.findByEmail(email).get()));
    }

    private UserDTO convertToDto(User user){
        UserDTO dto = new UserDTO();
        dto.setRole(user.getRole());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setMobileNumber(user.getMobileNumber());
        dto.setCreate_dt(user.getCreate_dt());
        return dto;


    }
}
