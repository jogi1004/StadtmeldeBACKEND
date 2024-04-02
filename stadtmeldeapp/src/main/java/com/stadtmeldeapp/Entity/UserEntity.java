package com.stadtmeldeapp.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class UserEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = true)
    private byte[] profilePicture;

    @Column(nullable = true)
    private boolean notificationsEnabled;

    @Column(nullable = true) 
    private int reportingLocationId;

    /*@Column

    @ManyToMany(fetch = FetchType.LAZY)
     @JoinTable(
        name = "users_roles",
        joinColumns = @JoinColumn(
            name = "user_id", 
            referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(
            name = "role_id", 
            referencedColumnName = "id"))
     private Set<RoleEntity> roles;
     */


    public UserEntity(String username, String password, String email, byte[] profilePicture, boolean notificationsEnabled, int reportingLocationId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.profilePicture = profilePicture;
        this.notificationsEnabled = notificationsEnabled;
        this.reportingLocationId = reportingLocationId;
    }
}
