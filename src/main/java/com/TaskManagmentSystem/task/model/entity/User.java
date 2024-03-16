package com.TaskManagmentSystem.task.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Table(name = "customer")
@Entity
@Data
@AllArgsConstructor@NoArgsConstructor
public class User {


    public User(String name, String email, String mobileNumber, String pwd, String role , Date date) {
        this.name = name;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.pwd = pwd;
        this.role = role;
        this.create_dt = date;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "name")
    private String name ;
    @Column(name = "email")
    private String email;
    @Column(name = "mobile_number")
    private String mobileNumber;
    @Column(name = "pwd")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String pwd;
    @Column(name = "role")
    private String role;
    @Column(name = " create_dt")
    private Date create_dt;

    public Set<Authority> getAuthoritySet() {
        return authoritySet;
    }

    public void setAuthoritySet(Set<Authority> authoritySet) {
        this.authoritySet = authoritySet;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "user" , fetch = FetchType.EAGER)
    private Set<Authority> authoritySet;




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getCreate_dt() {
        return create_dt;
    }

    public void setCreate_dt(Date create_dt) {
        this.create_dt = create_dt;
    }
}

