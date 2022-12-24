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
import java.util.HashMap;
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
    private HashMap<Integer,Showing> schedule;

    public Theater(LocalDateProvider provider) {
        this.provider = provider;
        this.schedule = new HashMap<>();
    }


    /**
     * Get and parse from JSON file in resource folder. Uses simple json parser.
     * @return Returns list of showings
     */
    public void initialize(String fileName){
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
                Integer showingSequence = ((Long) showingJson.get("sequence")).intValue();
                Showing showing = new Showing(
                        movie,
                        showingSequence,
                        LocalDateTime.of(currentDate, LocalTime.parse((String) showingJson.get("time"),formatter))
                );
                schedule.put(showingSequence,showing);
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
            showing = schedule.get(sequence);
            if (showing==null){
                throw new IllegalStateException("Not able to find any showing for given sequence " + sequence);
            }
        } catch (RuntimeException ex) {
            LOGGER.info("Failed to reserve");
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
        schedule.forEach((sequence,showing) ->
                System.out.println(showing)
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

    public LocalDateProvider getProvider() {
        return provider;
    }

    public void setProvider(LocalDateProvider provider) {
        this.provider = provider;
    }

    public HashMap<Integer, Showing> getSchedule() {
        return schedule;
    }

    public void setSchedule(HashMap<Integer, Showing> schedule) {
        this.schedule = schedule;
    }

    /**
     * Entry point
     * @param args No default args set
     */
    public static void main(String[] args) {
        Theater theater = new Theater(LocalDateProvider.singleton());
        theater.initialize(CONFIG_FILE);
        theater.printScheduleText();
        theater.printScheduleJson();
    }
}
