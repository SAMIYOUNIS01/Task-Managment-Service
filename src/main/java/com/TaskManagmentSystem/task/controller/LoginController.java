package com.TaskManagmentSystem.task.controller;

import com.TaskManagmentSystem.task.exceptions.BadCredentialsException;
import com.TaskManagmentSystem.task.exceptions.UserNotFoundException;
import com.TaskManagmentSystem.task.model.dto.GenericErrorDTO;
import com.TaskManagmentSystem.task.model.dto.UserDTO;
import com.TaskManagmentSystem.task.model.entity.User;
import com.TaskManagmentSystem.task.services.Imp.LocalizedMessageServiceImp;
import com.TaskManagmentSystem.task.services.Imp.LoginServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping
public class LoginController {
@Autowired
PasswordEncoder passwordEncoder;
@Autowired
LoginServiceImp loginServiceImp;

@Autowired
LocalizedMessageServiceImp localizedMessageServiceImp;


    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GenericErrorDTO handleUserNotFoundException(UserNotFoundException exception) {
        GenericErrorDTO genericError = new GenericErrorDTO();
        genericError.setError(localizedMessageServiceImp.getLocalizedMessage("user.not.found"));
        return genericError;
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public  GenericErrorDTO handleInvalidPasswordException(BadCredentialsException exception) {
        GenericErrorDTO genericError = new GenericErrorDTO();
        genericError.setError(localizedMessageServiceImp.getLocalizedMessage("Invalid.password"));
        return genericError;
    }



    @PostMapping("/register")
    public ResponseEntity<UserDTO> register (@RequestBody UserDTO user){
        ResponseEntity<UserDTO > response = null;
        UserDTO savedUser = null;
        Optional<User> alreadyExist;
        try {
            String hashPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(hashPassword);
            user.setCreate_dt(new Date(System.currentTimeMillis()));
            savedUser = loginServiceImp.register(user);
            response = ResponseEntity.status(HttpStatus.CREATED).body(savedUser);

        }catch (Exception exception){
            response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return response;
    }


    @RequestMapping("/user")
    public UserDTO getUserDetailsAfterLogin(Authentication authentication){
        Optional<UserDTO> user = loginServiceImp.findByEmail(authentication.getName());
        return user.orElse(null);
    }

}
