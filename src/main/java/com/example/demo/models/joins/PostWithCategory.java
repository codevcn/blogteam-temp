package com.example.demo.models.joins;

public record PostWithCategory(
    String title,
    String userID,
    String categoryID,
    String mainCategoryID,
    String name
) {}
