package com.TaskManagmentSystem.task.model.dto;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor@NoArgsConstructor
@Builder
@ToString
public class LoginErrorDTO implements Serializable {
    private String error;
    private String reason;
}
