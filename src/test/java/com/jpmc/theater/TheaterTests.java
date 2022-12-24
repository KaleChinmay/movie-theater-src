package com.jpmc.theater;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TheaterTests {
    @Test
    void totalFeeForCustomerTest() {
        Theater theater = new Theater(LocalDateProvider.singleton());
        HashMap<Integer, Showing> map = new HashMap<>();
        Integer sequence = 9;
        map.put(sequence,new Showing(
                new Movie("Spider-Man: No Way Home", "",Duration.ofMinutes(90), 10, 1),
                sequence,
                LocalDateTime.of(LocalDate.of(1,1,1), LocalTime.of(16, 10))
        ));
        theater.setSchedule(map);
        Customer john = new Customer("John Doe", "id-12345");
        Reservation reservation = theater.reserve(john, sequence, 4);
        assertEquals(reservation.totalFee(), 32);
    }

    @Test
    void printMovieScheduleTest() {
        Theater theater = new Theater(LocalDateProvider.singleton());
        theater.initialize("configTest.json");
        theater.printScheduleText();
    }

    @Test
    void printMovieScheduleJsonTest() {
        Theater theater = new Theater(LocalDateProvider.singleton());
        theater.initialize("configTest.json");
        theater.printScheduleJson();
    }

    @Test
    void initializeDataCorrectSizeTest() {
        //initialize is called inside constructor
        Theater theater = new Theater(LocalDateProvider.singleton());
        theater.initialize("configTest.json");
        assertEquals(theater.getSchedule().size(),9);
    }

    @Test
    void initializeDataCorrectDataTest() {
        //initialize is called inside constructor
        Theater theater = new Theater(LocalDateProvider.singleton());
        theater.initialize("configTest.json");
        LocalDate currentDate = theater.provider.currentDate();
        HashMap<Integer, Showing> showingList = theater.getSchedule();
        Integer sequence = 1;
        assertEquals("Turning Red", showingList.get(sequence).getMovie().getTitle());
        assertEquals(11.0, showingList.get(sequence).getMovie().getTicketPrice());
        assertEquals(Duration.ofMinutes(85), showingList.get(sequence).getMovie().getRunningTime());
        assertEquals(1,showingList.get(sequence).getSequenceOfTheDay());
        assertEquals( LocalDateTime.of(currentDate, LocalTime.of(9,0)), showingList.get(sequence).getShowStartTime());
    }

    @Test
    void initializeIncorrectFilenameTest(){
        Theater theater = new Theater(LocalDateProvider.singleton());
        assertThrows(RuntimeException.class,()->theater.initialize("config111.json"));
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
        HashMap<Integer,Showing> schedule = new HashMap<>();
        schedule.put(9,showing);
        theater.setSchedule(schedule);
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
        HashMap<Integer,Showing> schedule = new HashMap<>();
        schedule.put(9,showing);
        theater.setSchedule(schedule);
        Customer customer = new Customer("John Doe", "unused-id");
        Reservation myReservation = new Reservation(customer,showing,3);
        Exception exception = assertThrows(IllegalStateException.class,()->theater.reserve(customer,10,3) );
        assertEquals("Not able to find any showing for given sequence "+10,exception.getMessage());
    }
}
