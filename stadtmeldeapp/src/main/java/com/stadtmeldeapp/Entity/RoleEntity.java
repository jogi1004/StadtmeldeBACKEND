/* package com.stadtmeldeapp.Entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class RoleEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToMany(mappedBy = "roles")
    private Set<UserEntity> users;

    @Column(nullable = false)
    private String name;


    public RoleEntity() {
    }

    public RoleEntity(Integer id, Set<UserEntity> users, String name) {
        this.id = id;
        this.users = users;
        this.name = name;
    }
}
 */