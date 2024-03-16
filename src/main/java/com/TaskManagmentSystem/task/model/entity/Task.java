package com.TaskManagmentSystem.task.model.entity;

import com.TaskManagmentSystem.task.model.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;

@Entity
@Table
@Data@AllArgsConstructor@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @Column(name = "due_Date")
    private LocalDate date;
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    public Task(String title, String description, LocalDate date, Status status) {
        this.date = date;
        this.description = description;
        this.status = status;
        this.title = title;
    }
}
