package com.TaskManagmentSystem.task.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "authorities")
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /*
    * sample on names:
    * ROLE_ADMIN
    * ROLE_USER
    * PLEASE AFTER REGISTER ADD IT MANUALLY */
    @Column(name = "name")
    private String name;

    // Constructors, getters, and setters

    public Authority() {
    }


    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUserId() {
        return user;
    }

    public void SetUserId(User userId) {
        this.user = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
