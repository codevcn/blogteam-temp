package com.example.demo.DTOs.auth;

import com.example.demo.utils.constants.Lengths;
import com.example.demo.utils.constants.RegExp;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class CreateAccountDTO {

    @Pattern(regexp = RegExp.fullNameRegExp, message = "Trường tên đầy đủ có định dạng không hợp lệ")
    @Size(min = Lengths.minLengthOfFullName, max = Lengths.maxLengthOfFullName,
        message = "Trường tên đầy đủ không thể vượt ngoài giới hạn kí tự (" + Lengths.minLengthOfFullName + "-"
            + Lengths.maxLengthOfFullName + ")")
    @NotBlank(message = "Trường tên đầy đủ không thể thiếu")
    private String fullName;

    @Pattern(regexp = RegExp.emailRegExp, message = "Trường email có định dạng không hợp lệ")
    @NotBlank(message = "Trường email không thể thiếu")
    private String email;

    @Pattern(regexp = RegExp.passwordRegExp, message = "Trường mật khẩu không thể thiếu")
    @Size(min = Lengths.minLengthOfPassword, max = Lengths.maxLengthOfPassword,
        message = "Trường mật khẩu không thể vượt ngoài giới hạn kí tự (" + Lengths.minLengthOfPassword + "-"
            + Lengths.maxLengthOfPassword + ")")
    @NotBlank(message = "Trường mật khẩu không thể thiếu")
    private String password;

    @NotBlank(message = "Trường giới tính không thể thiếu")
    private String gender;
}
