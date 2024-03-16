package com.TaskManagmentSystem.task.repository;

import com.TaskManagmentSystem.task.model.entity.Task;
import com.TaskManagmentSystem.task.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface TasksRepository extends JpaRepository<Task , Long> {

    Optional<Task> findById(Long id);


    @Query("SELECT t FROM Task t " +
            "WHERE " +
            "(:dateFrom IS NULL OR t.date >= :dateFrom) " +
            "AND (:dateTo IS NULL OR t.date <= :dateTo) " +
            "AND (:status IS NULL OR t.status = :status)")
    List<Task> findTasksByDateRangeAndStatus(@Param("dateFrom") LocalDate dateFrom,
                                             @Param("dateTo") LocalDate dateTo,
                                             @Param("status") Status status);
}
