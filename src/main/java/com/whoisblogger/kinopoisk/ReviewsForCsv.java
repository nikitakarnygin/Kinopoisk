package com.whoisblogger.kinopoisk;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
        "title",
        "type",
        "review",
        "date",
        "author",
        "userRating",
        "reviewLikes",
        "reviewDislikes"
})
public abstract class ReviewsForCsv {

    @JsonProperty("title")
    private String title;

    @JsonProperty("type")
    private String type;

    @JsonProperty("review")
    private String review;

    @JsonProperty("date")
    private String date;

    @JsonProperty("author")
    private String author;

    @JsonProperty("userRating")
    private String userRating;

    @JsonProperty("reviewLikes")
    private String reviewLikes;

    @JsonProperty("reviewDislikes")
    private String reviewDislikes;

    @JsonIgnore
    private String id;

    @JsonIgnore
    private String movieId;

    @JsonIgnore
    private String authorId;

    @JsonIgnore
    private String createdAt;

    @JsonIgnore
    private String updatedAt;
}
