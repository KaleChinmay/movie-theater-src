package com.jpmc.theater;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static Util.Constants.*;
import static Util.Constants.DAY_OF_MONTH_DISCOUNT;
import static com.jpmc.theater.Movie.MOVIE_CODE_SPECIAL;

public class Showing {
    private Movie movie;
    private int sequenceOfTheDay;
    private LocalDateTime showStartTime;


    public Movie getMovie() {
        return movie;
    }
    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public int getSequenceOfTheDay() {
        return sequenceOfTheDay;
    }
    public void setSequenceOfTheDay(int sequenceOfTheDay) {
        this.sequenceOfTheDay = sequenceOfTheDay;
    }

    public LocalDateTime getShowStartTime() {
        return showStartTime;
    }

    public void setShowStartTime(LocalDateTime showStartTime) {
        this.showStartTime = showStartTime;
    }

    public Showing(Movie movie, int sequenceOfTheDay, LocalDateTime showStartTime) {
        this.movie = movie;
        this.sequenceOfTheDay = sequenceOfTheDay;
        this.showStartTime = showStartTime;
    }



    public boolean isSequence(int sequence) {
        return this.sequenceOfTheDay == sequence;
    }

    public double getMovieFee() {
        return movie.getTicketPrice();
    }

    public double calculateTicketPriceAfterDiscount() {
        return movie.getTicketPrice() - getDiscount();
    }

    private double getDiscount() {
        List<Double> applicableDiscounts = new ArrayList<>();
        //Added 0 in case of no discount
        applicableDiscounts.add(NO_DISCOUNT);
        if (MOVIE_CODE_SPECIAL == movie.getSpecialCode()) {
            // 20% discount for special movie
            applicableDiscounts.add(movie.getTicketPrice() * SPECIAL_MOVIE_DISCOUNT_FRACTION);
        }

        if (sequenceOfTheDay == 1) {
            // $3 discount for 1st show
            applicableDiscounts.add(FIRST_MOVIE_DISCOUNT);
        } else if (sequenceOfTheDay == 2) {
            // $2 discount for 2nd show
            applicableDiscounts.add(SECOND_MOVIE_DISCOUNT);
        }

        // 25% discount if the movie is between 11am - 4pm
        if (!(showStartTime.toLocalTime().isAfter(DISCOUNT_END_HOUR)
                || showStartTime.toLocalTime().isBefore(DISCOUNT_START_HOUR))){
            applicableDiscounts.add(movie.getTicketPrice() * DAYTIME_DISCOUNT_FRACTION);
        }

        // 1$ discount if movie is on 7th Day of month
        if (showStartTime.getDayOfMonth()==DISCOUNT_DAY_OF_MONTH){
            applicableDiscounts.add(DAY_OF_MONTH_DISCOUNT);
        }

        //If more discounts/offers, add to applicableDiscounts collection
        //...


//        else {
//            throw new IllegalArgumentException("failed exception");
//        }

        // biggest discount wins
        return Collections.max(applicableDiscounts);
    }


    protected static String humanReadableFormat(Duration duration) {
        long hour = duration.toHours();
        long remainingMin = duration.toMinutes() - TimeUnit.HOURS.toMinutes(duration.toHours());

        return String.format("(%s hour%s %s minute%s)"
                , hour, handlePlural(hour), remainingMin, handlePlural(remainingMin));
    }

    // (s) postfix should be added to handle plural correctly
    protected static String handlePlural(long value) {
        if (value == 1) {
            return "";
        }
        else {
            return "s";
        }
    }

    @Override
    public String toString() {
        return this.sequenceOfTheDay + ": " + this.showStartTime.toLocalDate()
                + " " + this.showStartTime.toLocalTime() + " "
                + movie.getTitle() + " " + humanReadableFormat(this.getMovie().getRunningTime())
                + " $" + this.getMovieFee();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Showing showing = (Showing) o;
        return sequenceOfTheDay == showing.sequenceOfTheDay && Objects.equals(movie, showing.movie) && Objects.equals(showStartTime, showing.showStartTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(movie, sequenceOfTheDay, showStartTime);
    }
}
