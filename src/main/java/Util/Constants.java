package Util;

import java.time.LocalTime;

/**
 * Contains all the constants used in Theatre package.
 */
public final class Constants {

    /**
     * Declared private to disable instantiation of Constants class
     */
    private Constants(){

    }
    public static final String CONFIG_FILE = "config.json";
    public static final double SPECIAL_MOVIE_DISCOUNT_FRACTION = 0.2;

    public static final double NO_DISCOUNT = 0.0;
    public static final double FIRST_MOVIE_DISCOUNT = 3;
    public static final double SECOND_MOVIE_DISCOUNT = 2;

    public static final double DAY_OF_MONTH_DISCOUNT = 1;
    public static final double DAYTIME_DISCOUNT_FRACTION = 0.25;

    public static final int DISCOUNT_DAY_OF_MONTH = 7;

    public static final LocalTime DISCOUNT_START_HOUR = LocalTime.of(11, 0);
    public static final LocalTime DISCOUNT_END_HOUR = LocalTime.of(16, 0);
}
