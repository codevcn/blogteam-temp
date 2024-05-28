package com.example.demo.utils.constants;

public class RegExp {

    public static final String fullNameRegExp = "^.+$";
    public static final String emailRegExp = "^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$";
    public static final String passwordRegExp = "^(?=.*[a-zA-Z])(?=.*\\d).+$";
    public static final String hashtagOfBlogRegExp = "^[a-zA-Z0-9_-]+$";
    public static final String isPrivateRegExp = "^(0|1)$";
    public static final String genderRegExp = "^(female|male|other)$";
}
