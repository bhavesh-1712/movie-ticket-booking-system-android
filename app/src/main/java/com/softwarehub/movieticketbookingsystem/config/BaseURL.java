package com.softwarehub.movieticketbookingsystem.config;

public class BaseURL {
    public static String BASE_URL_MAIN = "http://movie-booking.diplomaguru.co.in/api/";
    public static String BASE_IMG = "http://movie-booking.diplomaguru.co.in/";

    public static String REGISTER_USER = BASE_URL_MAIN + "user_register.php";
    public static String LOGIN_USER = BASE_URL_MAIN + "user_login.php";

    public static String GET_MOVIE_LIST = BASE_URL_MAIN + "get_movie_list.php";
    public static String GET_SLOT_LIST = BASE_URL_MAIN + "get_time_slot.php";

    public static String GET_BOOKING = BASE_URL_MAIN + "get_movie_booking.php";
    public static String GET_BOOKING_BY_CUTOMER_ID = BASE_URL_MAIN + "get_movie_booking_customer_id.php";
    public static String BOOKED_SEAT = BASE_URL_MAIN + "booked_ticket.php";
}
