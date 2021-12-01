package com.softwarehub.movieticketbookingsystem.model;

public class MovieModel {
    String movieId;
    String movieName;
    String movieDetails;
    String movieCharges;
    String movieImage;

    public MovieModel(String movieId, String movieName, String movieDetails, String movieCharges, String movieImage) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.movieDetails = movieDetails;
        this.movieCharges = movieCharges;
        this.movieImage = movieImage;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getMovieDetails() {
        return movieDetails;
    }

    public void setMovieDetails(String movieDetails) {
        this.movieDetails = movieDetails;
    }

    public String getMovieCharges() {
        return movieCharges;
    }

    public void setMovieCharges(String movieCharges) {
        this.movieCharges = movieCharges;
    }

    public String getMovieImage() {
        return movieImage;
    }

    public void setMovieImage(String movieImage) {
        this.movieImage = movieImage;
    }
}
