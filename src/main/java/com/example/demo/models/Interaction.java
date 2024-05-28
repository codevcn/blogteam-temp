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
public class Interaction {

    private int postID;
    private String userID;
    private boolean liked;
    private String createdAt;
    private String updatedAt;
}
