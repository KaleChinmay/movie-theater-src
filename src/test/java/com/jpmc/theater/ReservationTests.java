package com.jpmc.theater;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationTests {

    @Test
    void totalFeeTest() {
        Customer customer = new Customer("John Doe", "unused-id");
        Showing showing = new Showing(
                new Movie("Spider-Man: No Way Home", "",Duration.ofMinutes(90), 12.5, 1),
                9,
                LocalDateTime.of(LocalDate.of(1,1,1), LocalTime.of(16, 10))
        );

        assertEquals(30,new Reservation(customer, showing, 3).totalFee());
    }
}
