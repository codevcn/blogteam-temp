package com.example.demo.DTOs.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LoginRequestDTO {

    @NotBlank(message = "Trường email không thể thiếu")
    private String email;

    @NotBlank(message = "Trường mật khẩu không thể thiếu")
    private String password;
}
