package com.example.demo.DTOs.blog;

import com.example.demo.utils.constants.Lengths;
import jakarta.validation.constraints.NotBlank;
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
public class MakeReviewDTO {

    @NotBlank(message = "Trường bình luận không thể thiếu")
    @Size(min = Lengths.minLengthOfCommentBlog, max = Lengths.maxLengthOfCommentBlog,
        message = "Trường tiêu đề không thể vượt ngoài giới hạn kí tự (" + Lengths.minLengthOfCommentBlog + "-"
            + Lengths.maxLengthOfCommentBlog + ")")
    private String comment;
}
