package com.jpmc.theater;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static Util.Constants.CONFIG_FILE;

/**
 * Theatre class
 * Contains Showing list, LocalDateProvider object, main function, reserve function and initialize json data function.
 */
public class Theater {
    private static final Logger LOGGER = Logger.getLogger(Theater.class.getName());
    LocalDateProvider provider;
    private List<Showing> schedule;

    public Theater(LocalDateProvider provider) {
        this.provider = provider;
        schedule = this.initialize(CONFIG_FILE);
    }


    /**
     * Get and parse from JSON file in resource folder. Uses simple json parser.
     * @return Returns list of showings
     */
    public List<Showing> initialize(String fileName){
        LOGGER.info("Initializing... Getting movie showings data from file");
        List<Showing> inputShowings = new ArrayList<>();
        LocalDate currentDate = provider.currentDate();
        JSONParser parser = new JSONParser();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        try {
            //Config is part of resource folder
            FileReader configFile = new FileReader(getClass().getResource("/"+fileName).getPath());
            JSONObject parsedJsonObject = (JSONObject)parser.parse(configFile);
            JSONArray showingsJsonArray = (JSONArray) parsedJsonObject.get("showings");
            //Parse showings from json object array
            for (Object showingObj : showingsJsonArray){
                JSONObject  showingJson = (JSONObject) showingObj;
                JSONObject movieJson = (JSONObject) showingJson.get("movie");
                Movie movie = new Movie(
                        (String) movieJson.get("title"),
                        (String) movieJson.get("description"),
                        Duration.ofMinutes((Long)movieJson.get("runningTime")),
                        (Double) movieJson.get("ticketPrice"),
                        ((Long) movieJson.get("specialCode")).intValue()
                );
                Showing showing = new Showing(
                        movie,
                        ((Long) showingJson.get("sequence")).intValue(),
                        LocalDateTime.of(currentDate, LocalTime.parse((String) showingJson.get("time"),formatter))
                );
                inputShowings.add(showing);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
        throw new RuntimeException(e);
    }
        LOGGER.info("Successfully parsed JSON file into Showings object");
        return inputShowings;
    }

    /**
     * Reserve tickets for a customer
     * @param customer customer Object
     * @param sequence Sequence of the showing
     * @param howManyTickets count of tickets for that customer
     * @return Returns reservation
     */
    public Reservation reserve(Customer customer, int sequence, int howManyTickets) {
        Showing showing;
        LOGGER.info("Reserving...");
        try {
            //Return available showing
            //Since array starts with zero index, do sequence -1.
            showing = schedule.get(sequence - 1);
        } catch (RuntimeException ex) {
            throw new IllegalStateException("Not able to find any showing for given sequence " + sequence);
        }
        LOGGER.info(howManyTickets+" tickets reserved Successfully");
        return new Reservation(customer, showing, howManyTickets);
    }

    /**
     * Prints readable text format
     */
    public void printScheduleText() {
        System.out.println(provider.currentDate());
        System.out.println("===================================================");
        schedule.forEach(s ->
                System.out.println(s)
        );
        System.out.println("===================================================");
    }

    /**
     * Prints pretty Json String using gson library
     */
    public void printScheduleJson(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String scheduleJson = gson.toJson(schedule);
        System.out.println(scheduleJson);
    }


    /**
     * Entry point
     * @param args No default args set
     */
    public static void main(String[] args) {
        Theater theater = new Theater(LocalDateProvider.singleton());
        theater.printScheduleText();
        theater.printScheduleJson();
    }
}
