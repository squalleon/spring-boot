package com.myhello.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="users")
@Data
@NoArgsConstructor
public class User {
    @Id
    String id;
    String password;
    String name;

    //Alt+Ins : Constructor
    public User(String id, String password, String name) {
        this.id = id;
        this.password = password;
        this.name = name;
    }
}
