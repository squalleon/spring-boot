package com.myhello.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_info")
@Data
@NoArgsConstructor
public class UserInfo {
    @Id
    String id;
    String address;
    String phone_number;

    public UserInfo(String id, String address, String phone_number) {
        this.id = id;
        this.address = address;
        this.phone_number = phone_number;
    }
}
