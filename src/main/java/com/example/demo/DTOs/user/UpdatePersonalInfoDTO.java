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
public class UpdatePersonalInfoDTO {

    @NotBlank(message = "Trường tên đầy đủ không thể thiếu")
    @Size(min = Lengths.minLengthOfCommentBlog, max = Lengths.maxLengthOfCommentBlog,
        message = "Trường tên đầy đủ không thể vượt ngoài giới hạn kí tự (" + Lengths.minLengthOfCommentBlog + "-"
            + Lengths.maxLengthOfCommentBlog + ")")
    private String fullName;

    @NotBlank(message = "Trường giới tính không thể thiếu")
    @Size(min = Lengths.minLengthOfCommentBlog, max = Lengths.maxLengthOfCommentBlog,
        message = "Trường giới tính không thể vượt ngoài giới hạn kí tự (" + Lengths.minLengthOfCommentBlog + "-"
            + Lengths.maxLengthOfCommentBlog + ")")
    @Pattern(regexp = RegExp.genderRegExp, message = "Trường giới tính có định dạng không hợp lệ")
    private String gender;
}

