package com.TaskManagmentSystem.task.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.ReadOnlyProperty;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String name;
    @ReadOnlyProperty
    private String password;
    private String role;
    private String mobileNumber;
    private String email;
    private Date create_dt;

}
