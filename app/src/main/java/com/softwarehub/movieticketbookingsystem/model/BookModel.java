package com.softwarehub.movieticketbookingsystem.model;

public class BookModel {
    String slotId, movieId, noOfSlotBooked;

    public BookModel(String slotId, String movieId, String noOfSlotBooked) {
        this.slotId = slotId;
        this.movieId = movieId;
        this.noOfSlotBooked = noOfSlotBooked;
    }

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getNoOfSlotBooked() {
        return noOfSlotBooked;
    }

    public void setNoOfSlotBooked(String noOfSlotBooked) {
        this.noOfSlotBooked = noOfSlotBooked;
    }
}
