package com.TaskManagmentSystem.task.model.dto;

import com.TaskManagmentSystem.task.model.enums.Status;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotNull(message = "Date cannot be null")
    @Future(message = "Date should be in the future")
    private LocalDate date;

    @NotNull(message = "Status cannot be null")
    private Status status;

}
