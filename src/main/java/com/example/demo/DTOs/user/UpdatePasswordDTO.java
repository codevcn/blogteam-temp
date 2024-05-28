package com.example.demo.DTOs.user;

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

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UpdatePasswordDTO {

    @NotBlank(message = "Trường mật khẩu không thể thiếu")
    @Size(min = Lengths.minLengthOfPassword, max = Lengths.maxLengthOfPassword,
        message = "Trường mật khẩu không thể vượt ngoài giới hạn kí tự (" + Lengths.minLengthOfPassword + "-"
            + Lengths.maxLengthOfPassword + ")")
    @Pattern(regexp = RegExp.passwordRegExp, message = "Trường mật khẩu có định dạng không hợp lệ")
    private String newPassword;

    @NotBlank(message = "Trường mật khẩu không thể thiếu")
    private String oldPassword;
}


