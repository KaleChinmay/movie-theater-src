package com.jpmc.theater;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        List<Showing> showingList = theater.initialize("configTest.json");
        assertEquals(showingList.size(),9);
    }

    @Test
    void initializeDataCorrectDataTest() {

        Theater theater = new Theater(LocalDateProvider.singleton());
        LocalDate currentDate = theater.provider.currentDate();
        List<Showing> showingList = theater.initialize("configTest.json");
        assertEquals("Turning Red", showingList.get(0).getMovie().getTitle());
        assertEquals(11.0, showingList.get(0).getMovie().getTicketPrice());
        assertEquals(Duration.ofMinutes(85), showingList.get(0).getMovie().getRunningTime());
        assertEquals(1,showingList.get(0).getSequenceOfTheDay());
        assertEquals( LocalDateTime.of(currentDate, LocalTime.of(9,0)), showingList.get(0).getShowStartTime());
    }

    @Test
    void initializeIncorrectFilenameTest(){
        Theater theater = new Theater(LocalDateProvider.singleton());
        assertThrows(RuntimeException.class,()->theater.initialize("configTestss.json"));
    }

    @Test
    void initializeEmptyFileTest(){
        Theater theater = new Theater(LocalDateProvider.singleton());
        assertThrows(RuntimeException.class,()->theater.initialize("configTestEmpty.json"));
    }

    @Test
    void initializeInvalidJsonTest(){
        Theater theater = new Theater(LocalDateProvider.singleton());
        assertThrows(RuntimeException.class,()->theater.initialize("configTestInvalid.json"));
    }


    @Test
    void reserveSuccessfulTest(){
        Theater theater = new Theater(LocalDateProvider.singleton());
        Showing showing = new Showing(
                new Movie("The Batman", "",Duration.ofMinutes(95), 9, 0),
                9,
                LocalDateTime.of(theater.provider.currentDate(), LocalTime.of(23,00)));
        Customer customer = new Customer("John Doe", "unused-id");
        Reservation myReservation = new Reservation(customer,showing,3);
        assertEquals(myReservation,theater.reserve(customer,9,3));

    }

    @Test
    void reserveFailedIncorrectSequenceTest(){
        Theater theater = new Theater(LocalDateProvider.singleton());
        Showing showing = new Showing(
                new Movie("The Batman", "",Duration.ofMinutes(95), 9, 0),
                9,
                LocalDateTime.of(theater.provider.currentDate(), LocalTime.of(23,00)));
        Customer customer = new Customer("John Doe", "unused-id");
        Reservation myReservation = new Reservation(customer,showing,3);
        Exception exception = assertThrows(IllegalStateException.class,()->theater.reserve(customer,10,3) );
        assertEquals("Not able to find any showing for given sequence "+10,exception.getMessage());
    }
}
