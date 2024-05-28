package com.example.demo.DTOs.blog;

import com.example.demo.utils.constants.Lengths;
import com.example.demo.utils.constants.RegExp;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class EditBlogDTO {

    @NotBlank(message = "Trường tiêu đề không thể thiếu")
    @Size(min = Lengths.minLengthOfTitleBlog, max = Lengths.maxLengthOfTitleBlog,
        message = "Trường tiêu đề không thể vượt ngoài giới hạn kí tự (" + Lengths.minLengthOfTitleBlog + "-"
            + Lengths.maxLengthOfTitleBlog + ")")
    private String title;

    @NotBlank(message = "Trường nội dung chính blog không thể thiếu")
    @Size(min = Lengths.minLengthOfMainContentBlog, max = Lengths.maxLengthOfMainContentBlog,
        message = "Trường nội dung chính blog không thể vượt ngoài giới hạn kí tự ("
            + Lengths.minLengthOfMainContentBlog + "-" + Lengths.maxLengthOfMainContentBlog + ")")
    private String mainContent;

    @NotBlank(message = "Trường hashtag không thể thiếu")
    @Pattern(regexp = RegExp.hashtagOfBlogRegExp, message = "Trường hashtag có định dạng không hợp lệ")
    @Size(min = Lengths.minLengthOfHashtagBlog, max = Lengths.maxLengthOfHashtagBlog,
        message = "Trường hashtag không thể vượt ngoài giới hạn kí tự (" + Lengths.minLengthOfHashtagBlog + "-"
            + Lengths.maxLengthOfHashtagBlog + ")")
    private String hashtag;

    @NotNull
    private Boolean isPrivate;
}
