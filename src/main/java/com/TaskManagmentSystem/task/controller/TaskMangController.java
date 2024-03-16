package com.TaskManagmentSystem.task.controller;

import com.TaskManagmentSystem.task.exceptions.BadCredentialsException;
import com.TaskManagmentSystem.task.exceptions.InvalidFilterException;
import com.TaskManagmentSystem.task.exceptions.TaskNotFoundException;
import com.TaskManagmentSystem.task.exceptions.UserNotFoundException;
import com.TaskManagmentSystem.task.model.dto.GenericErrorDTO;
import com.TaskManagmentSystem.task.model.dto.TaskDTO;
import com.TaskManagmentSystem.task.services.Imp.LocalizedMessageServiceImp;
import com.TaskManagmentSystem.task.services.Imp.TaskServiceImp;


import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping
@Validated
@Slf4j
public class TaskMangController {

   @Autowired
    TaskServiceImp taskServiceImp;

   @Autowired
   LocalizedMessageServiceImp localizedMessageServiceImp;



    @ExceptionHandler(TaskNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public  GenericErrorDTO handleFailedToGetTaskException(TaskNotFoundException exception) {
        GenericErrorDTO genericError = new GenericErrorDTO();
        genericError.setError(localizedMessageServiceImp.getLocalizedMessage("task.not.found"));
        return genericError;
    }

    @ExceptionHandler(InvalidFilterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public  GenericErrorDTO handleFailedToGetTaskByFilterException(InvalidFilterException exception) {
        GenericErrorDTO genericError = new GenericErrorDTO();
        genericError.setError(localizedMessageServiceImp.getLocalizedMessage("invalid.filter"));
        return genericError;
    }





    @GetMapping("/getAll")
    public ResponseEntity<List<TaskDTO>> getAllTasks (@RequestParam(required = false)String status,
                                                      @RequestParam(required = false)String dateFrom,
                                                      @RequestParam(required = false)String dateTo) throws TaskNotFoundException, InvalidFilterException {
        log.info("<<< Get All Tasks Starting >>>");
        List<TaskDTO> result = taskServiceImp.getAll(status , dateFrom , dateTo);
        log.info("Found {} tasks.", result.size());
        if(result.size() > 0){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS))
                    .body(result);
        }
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS))
                .body(Collections.emptyList());
    }





    @GetMapping("/getTask/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable("id") Long id) throws TaskNotFoundException {
        log.info("Slf");
        log.info("Getting task by ID: {}", id);
        try {
            TaskDTO result = taskServiceImp.getTaskByID(id);
            log.info("Task found with ID {}: {}", id, result);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (TaskNotFoundException e) {
            log.error("Task not found with ID {}", id);
            throw e;
        }

    }

    @PostMapping("/addNewTask")
    public ResponseEntity<TaskDTO> addNewTask(@RequestBody @Validated TaskDTO taskDTO){
        log.info("Adding a new task: {}", taskDTO);
        TaskDTO result = taskServiceImp.addNewTask(taskDTO);
        log.info("New task added: {}", result);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }


    @PutMapping("/updateTask/{id}")
    public ResponseEntity<TaskDTO> updateTask (@PathVariable("id")Long id , @RequestBody @Validated TaskDTO taskDTO) throws BadRequestException, TaskNotFoundException {
        log.info("Updating task with ID {}: {}", id, taskDTO);
        TaskDTO updatedTask = taskServiceImp.updateTaskById(id, taskDTO);
        log.info("Task with ID {} updated: {}", id, updatedTask);
        return ResponseEntity.status(HttpStatus.OK).body(updatedTask);
    }

    @DeleteMapping("/deleteTask/{id}")
    public ResponseEntity<String> deleteTask (@PathVariable Long id) throws BadRequestException, TaskNotFoundException {
        log.info("Deleting task with ID {}", id);
        String deleteMessage = taskServiceImp.deleteTaskById(id);
        log.info("Task with ID {} deleted: {}", id, deleteMessage);
    return ResponseEntity.status(HttpStatus.OK).body(deleteMessage);

    }



}
