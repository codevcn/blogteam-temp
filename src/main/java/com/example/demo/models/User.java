package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    private String email;
    private String fullName;
    private String gender;
    private String avatar;
    private String password;
    private String roleID;
    private String createdAt;
    private String updatedAt;
}
