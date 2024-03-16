package com.TaskManagmentSystem.task.services.interfaces;

import com.TaskManagmentSystem.task.exceptions.InvalidFilterException;
import com.TaskManagmentSystem.task.exceptions.TaskNotFoundException;
import com.TaskManagmentSystem.task.model.dto.TaskDTO;
import com.TaskManagmentSystem.task.model.entity.Task;
import org.apache.coyote.BadRequestException;

import java.util.List;
import java.util.Optional;

public interface TaskService {


    List<TaskDTO> getAll(String status , String dateTo , String dateFrom) throws TaskNotFoundException, InvalidFilterException;

    TaskDTO getTaskByID(Long id) throws TaskNotFoundException;

    TaskDTO addNewTask(TaskDTO taskDTO);


    TaskDTO updateTaskById(Long id, TaskDTO taskDTO) throws BadRequestException, TaskNotFoundException;

    String deleteTaskById(Long id) throws BadRequestException, TaskNotFoundException;
}
