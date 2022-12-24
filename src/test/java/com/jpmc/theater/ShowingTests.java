package com.jpmc.theater;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ShowingTests {
    @Test
    void specialMovieWith20PercentDiscountTest() {
        Movie spiderMan = new Movie("Spider-Man: No Way Home", "", Duration.ofMinutes(90),12.5, 1);
        Showing showing = new Showing(spiderMan, 5,
                LocalDateTime.of(LocalDate.of(1,1,1), LocalTime.of(9, 0)));
        assertEquals(10, showing.calculateTicketPriceAfterDiscount());
    }


    @Test
    void firstMovie3DollarDiscountTest(){
        Movie turningRed = new Movie("Turning Red", "",Duration.ofMinutes(85), 11, 0);
        Showing showing = new Showing(turningRed, 1,
                LocalDateTime.of(LocalDate.of(1,1,1), LocalTime.of(9, 0)));
        assertEquals(8, showing.calculateTicketPriceAfterDiscount());
    }


    @Test
    void secondMovie2DollarDiscountTest(){
        Movie turningRed = new Movie("Turning Red", "",Duration.ofMinutes(85), 11, 0);
        Showing showing = new Showing(turningRed, 2,
                LocalDateTime.of(LocalDate.of(1,1,1), LocalTime.of(9, 0)));
        assertEquals(9, showing.calculateTicketPriceAfterDiscount());
    }

    @Test
    void movieAtEleven25PercentDiscountTest(){
        Movie turningRed = new Movie("Turning Red", "",Duration.ofMinutes(85), 12, 0);
        Showing showing = new Showing(turningRed, 3,
                LocalDateTime.of(LocalDate.of(1,1,1), LocalTime.of(11, 0)));
        assertEquals(9, showing.calculateTicketPriceAfterDiscount());
    }

    @Test
    void movieAtFour25PercentDiscountTest(){
        Movie turningRed = new Movie("Turning Red", "",Duration.ofMinutes(85), 12, 0);
        Showing showing = new Showing(turningRed, 3,
                LocalDateTime.of(LocalDate.of(1,1,1), LocalTime.of(16, 0)));
        assertEquals(9, showing.calculateTicketPriceAfterDiscount());
    }

    @Test
    void movieBetweenElevenToFour25PercentDiscountTest(){
        Movie turningRed = new Movie("Turning Red", "",Duration.ofMinutes(85), 12, 0);
        Showing showing = new Showing(turningRed, 3,
                LocalDateTime.of(LocalDate.of(1,1,1), LocalTime.of(15, 0)));
        assertEquals(9, showing.calculateTicketPriceAfterDiscount());
    }

    @Test
    void moviesShownOn7thOfMonthOneDollarDiscountTest(){
        Movie turningRed = new Movie("Turning Red", "",Duration.ofMinutes(85), 12, 0);
        Showing showing = new Showing(turningRed, 3,
                LocalDateTime.of(LocalDate.of(1,1,7), LocalTime.of(9, 0)));
        assertEquals(11, showing.calculateTicketPriceAfterDiscount());
    }

    @Test
    void specialCodeMovieDiscountGreaterThanFirstMovieDiscountTest(){
        Movie turningRed = new Movie("Turning Red", "",Duration.ofMinutes(85), 100, 1);
        Showing showing = new Showing(turningRed, 1,
                LocalDateTime.of(LocalDate.of(1,1,1), LocalTime.of(9, 0)));
        assertEquals(80, showing.calculateTicketPriceAfterDiscount());
    }

    @Test
    void firstMovieDiscountGreaterThanSpecialCodeMovieDiscountTest(){
        Movie turningRed = new Movie("Turning Red", "",Duration.ofMinutes(85), 10, 1);
        Showing showing = new Showing(turningRed, 1,
                LocalDateTime.of(LocalDate.of(1,1,1), LocalTime.of(9, 0)));
        assertEquals(7, showing.calculateTicketPriceAfterDiscount());
    }

    @Test
    void seventhOfMonthDiscountGreaterThanDayTime25PercentDiscountTest(){
        Movie turningRed = new Movie("Turning Red", "",Duration.ofMinutes(85), 2, 2);
        Showing showing = new Showing(turningRed, 3,
                LocalDateTime.of(LocalDate.of(1,1,7), LocalTime.of(11, 0)));
        assertEquals(1, showing.calculateTicketPriceAfterDiscount());
    }

    @Test
    void dayTime25PercentDiscountGreaterThanSeventhOfMonthDiscountTest(){
        Movie turningRed = new Movie("Turning Red", "",Duration.ofMinutes(85), 10, 2);
        Showing showing = new Showing(turningRed, 3,
                LocalDateTime.of(LocalDate.of(1,1,7), LocalTime.of(16, 0)));
        assertEquals(7.5, showing.calculateTicketPriceAfterDiscount());
    }

    @Test
    void multipleEligibleDiscountsButDayTime25PercentDiscountHighestTest(){
        Movie turningRed = new Movie("Turning Red", "",Duration.ofMinutes(85), 100, 1);
        Showing showing = new Showing(turningRed, 1,
                LocalDateTime.of(LocalDate.of(1,1,7), LocalTime.of(16, 0)));
        assertEquals(75, showing.calculateTicketPriceAfterDiscount());
    }

    @Test
    void multipleEligibleDiscountsButFirstMovieDiscountHighestTest(){
        Movie turningRed = new Movie("Turning Red", "",Duration.ofMinutes(85), 10, 1);
        Showing showing = new Showing(turningRed, 1,
                LocalDateTime.of(LocalDate.of(1,1,7), LocalTime.of(16, 0)));
        assertEquals(7, showing.calculateTicketPriceAfterDiscount());
    }

    @Test
    void noDiscountTest(){
        Movie turningRed = new Movie("Turning Red", "",Duration.ofMinutes(85), 10, 0);
        Showing showing = new Showing(turningRed, 3,
                LocalDateTime.of(LocalDate.of(1,1,8), LocalTime.of(16, 10)));
        assertEquals(10, showing.calculateTicketPriceAfterDiscount());
    }

    @Test
    void calculateTicketPriceAfterDiscountNullTest() {
        Showing showing = null;
        assertThrows(NullPointerException.class,()->showing.calculateTicketPriceAfterDiscount());
    }

    @Test
    void humanReadableFormatCorrectOutput100MinutesTest(){
        Duration duration = Duration.ofMinutes(100);
        assertEquals("(1 hour 40 minutes)",Showing.humanReadableFormat(duration));
    }

    @Test
    void humanReadableFormatCorrectOutput0MinutesTest(){
        Duration duration = Duration.ofMinutes(0);
        assertEquals("(0 hours 0 minutes)",Showing.humanReadableFormat(duration));
    }

    @Test
    void humanReadableFormatExceptionTest(){
        assertThrows(NullPointerException.class,()->Showing.humanReadableFormat(null));
    }

    @Test
    void handlePluralWithPluralTest(){
        assertEquals("s",Showing.handlePlural(13));
    }

    @Test
    void handlePluralFor1Test(){
        assertEquals("",Showing.handlePlural(1));
    }
}
