package com.jpmc.theater;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TheaterTests {
    @Test
    void totalFeeForCustomerTest() {
        Theater theater = new Theater(LocalDateProvider.singleton());
        Customer john = new Customer("John Doe", "id-12345");
        Reservation reservation = theater.reserve(john, 2, 4);
//        System.out.println("You have to pay " + reservation.getTotalFee());
        assertEquals(reservation.totalFee(), 37.5);
    }

    @Test
    void printMovieScheduleTest() {
        Theater theater = new Theater(LocalDateProvider.singleton());
        theater.printScheduleText();
    }

    @Test
    void printMovieScheduleJsonTest() {
        Theater theater = new Theater(LocalDateProvider.singleton());
        theater.printScheduleJson();
    }

    @Test
    void initializeDataCorrectSizeTest() {
        Theater theater = new Theater(LocalDateProvider.singleton());
        List<Showing> showingList = theater.initialize("config.json");
        assertEquals(showingList.size(),9);
    }

    @Test
    void initializeDataCorrectDataTest() {

        Theater theater = new Theater(LocalDateProvider.singleton());
        LocalDate currentDate = theater.provider.currentDate();
        List<Showing> showingList = theater.initialize("config.json");
        assertEquals(showingList.get(0).getMovie().getTitle(),"Turning Red");
        assertEquals(showingList.get(0).getMovie().getTicketPrice(),11.0);
        assertEquals(showingList.get(0).getMovie().getRunningTime(), Duration.ofMinutes(85));
        assertEquals(showingList.get(0).getSequenceOfTheDay(), 1);
        assertEquals(showingList.get(0).getShowStartTime(), LocalDateTime.of(currentDate, LocalTime.of(9,0)));
    }
}
