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
public class Post {

    private Long id;
    private String title;
    private String createdAt;
    private String background;
    private String userID;
    private String mainContent;
    private Boolean isPrivate;
    private String hashtag;
    private Boolean deleted;
    private String updateAt;
}
