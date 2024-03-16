package com.TaskManagmentSystem.task.services.Imp;

import com.TaskManagmentSystem.task.exceptions.InvalidFilterException;
import com.TaskManagmentSystem.task.exceptions.TaskNotFoundException;
import com.TaskManagmentSystem.task.model.dto.TaskDTO;
import com.TaskManagmentSystem.task.model.entity.Task;
import com.TaskManagmentSystem.task.model.enums.Status;
import com.TaskManagmentSystem.task.repository.TasksRepository;
import com.TaskManagmentSystem.task.services.interfaces.TaskService;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TaskServiceImp implements TaskService {




    @Autowired
    TasksRepository tasksRepository;
    @Autowired
    LocalizedMessageServiceImp localizedMessageServiceImp;

    @Override
    public TaskDTO getTaskByID(Long id) throws TaskNotFoundException {
        log.info("Fetching task by ID: {}", id);
        Optional<Task> task = tasksRepository.findById(id);
        if(task.isEmpty()){
            log.error("Task with ID {} not found", id);
            throw new TaskNotFoundException("task.not.found");
        }
        log.info("Task with ID {} found", id);
        return convertToDTO(task.get());
    }


    @Override
    public TaskDTO addNewTask(TaskDTO taskDTO) {
        log.info("Adding new task: {}", taskDTO);
        Task savedTask = tasksRepository.save(new Task(taskDTO.getTitle() , taskDTO.getDescription() , taskDTO.getDate() , taskDTO.getStatus()));
        log.info("New task added: {}", savedTask);
        return convertToDTO(savedTask);
    }

    @Override
    public TaskDTO updateTaskById(Long id, TaskDTO taskDTO) throws BadRequestException, TaskNotFoundException {
        log.info("Updating task with ID {}: {}", id, taskDTO);
        Optional<Task> task = tasksRepository.findById(id);
        Task savedTask;
        if(task.isPresent()){
            task.get().setDate(taskDTO.getDate());
            task.get().setDescription(taskDTO.getDescription());
            task.get().setTitle(taskDTO.getTitle());
            task.get().setStatus(taskDTO.getStatus());
            savedTask = tasksRepository.save(task.get());
            log.info("Task with ID {} updated: {}", id, savedTask);
            return convertToDTO(savedTask);
        }
        log.error("Task with ID {} not found", id);
        throw new TaskNotFoundException("task.not.found");
    }

    @Override
    public String deleteTaskById(Long id) throws BadRequestException, TaskNotFoundException {
        log.info("Deleting task with ID {}", id);
        Optional<Task> isExist = tasksRepository.findById(id);
        if(isExist.isPresent()){
            tasksRepository.deleteById(id);
            log.info("Task with ID {} deleted successfully", id);
            return "Task has been deleted successfully";

        }
        log.error("Task with ID {} not found", id);
        throw new TaskNotFoundException("task.not.found");
        }

    @Override
    public List<TaskDTO> getAll(String status , String dateFrom , String dateTo) throws TaskNotFoundException, InvalidFilterException {
        log.info("Fetching all tasks");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Status statusFilter = null;
        if(status != null){
            try {
                statusFilter = Status.valueOf(status);
               log.info("{} Valid" , status);
            } catch (IllegalArgumentException e) {
                log.error("{} Invalid" , status);
                throw new InvalidFilterException("invalid.filter");
            }
        }

        LocalDate dateToFilter = null;
        if (StringUtils.isNotBlank(dateFrom)) {
            try {
                dateToFilter = LocalDate.parse(dateTo , formatter);
                log.info("dateToFilter: " + dateToFilter);
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new InvalidFilterException("invalid.filter");
            }
        }
        LocalDate dateFromFilter = null;
        if (StringUtils.isNotBlank(dateFrom)) {
            try {
                dateFromFilter = LocalDate.parse(dateFrom , formatter);
                log.info("dateFromFilter: " + dateFromFilter);
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new InvalidFilterException("invalid.filter");
            }
        }

        List<Task> allTasks = tasksRepository.findTasksByDateRangeAndStatus( dateFromFilter, dateToFilter , statusFilter);
        if (!allTasks.isEmpty()) {
            List<TaskDTO> DTO_tasks = new ArrayList<>();
            for (Task task : allTasks) {
                DTO_tasks.add(convertToDTO(task));
            }
            log.info("Found {} tasks", allTasks.size());
            return DTO_tasks;
        }
        log.warn("No tasks found");
        return new ArrayList<>();}


    private TaskDTO convertToDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setDate(task.getDate());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setTitle(task.getTitle());
        return dto;
    }


}
