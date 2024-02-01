package com.whoisblogger.kinopoisk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    private String title;
    private String type;
    private String review;
    private String date;
    private String author;
    private String userRating;
    private String reviewLikes;
    private String reviewDislikes;
    private String id;
    private String movieId;
    private String authorId;
    private String createdAt;
    private String updatedAt;
}
