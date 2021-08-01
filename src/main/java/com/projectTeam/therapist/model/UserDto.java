package com.projectTeam.therapist.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class UserDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String userName;
    private String userPassword;
    private Boolean userEnabled;

    @ManyToMany
    @JoinTable(
            name = "user_role_dto",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<RoleDto> roles = new ArrayList<>();

    public List<RoleDto> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDto> roles) {
        this.roles = roles;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Boolean getUserEnabled() {
        return userEnabled;
    }

    public void setUserEnabled(Boolean userEnabled) {
        this.userEnabled = userEnabled;
    }
}