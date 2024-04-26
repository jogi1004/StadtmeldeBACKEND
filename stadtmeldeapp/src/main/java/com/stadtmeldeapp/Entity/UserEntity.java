package com.stadtmeldeapp.Entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.NoArgsConstructor;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = true)
    private boolean notificationsEnabled;

    @Column(name = "profile_picture_id", nullable = true)
    private Integer profilePictureId;

    @Column
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<RoleEntity> roles = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "isAdminForLocationId", referencedColumnName = "id")
    private ReportingLocationEntity adminForLocation;

    public UserEntity(String username, String password, String email, List<RoleEntity> roles, Integer profilePictureId,
            ReportingLocationEntity adminForLocation) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
        this.profilePictureId = profilePictureId;
        this.notificationsEnabled = false;
        this.adminForLocation = adminForLocation;
    }
}
