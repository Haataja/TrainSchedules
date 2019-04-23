package fi.tamk.tiko.trainschedules.model;

import android.os.Build;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Java model of the train.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Train  implements Comparable<Train>{
    private int trainNumber;
    private String trainType;
    private String trainCategory;
    private String commuterLineID;
    private List<TimeTableRow> timeTableRows;
    private String destination;

    /**
     * Default constructor
     */
    public Train() {
    }

    /**
     * Constructor with parameters
     * @param trainNumber The number of the train.
     * @param trainType The type of the train.
     * @param trainCategory The category of the train.
     * @param commuterLineID If train is commuter tells what is the letter of the train.
     */
    public Train(int trainNumber, String trainType, String trainCategory, String commuterLineID) {
        this.trainNumber = trainNumber;
        this.trainType = trainType;
        this.trainCategory = trainCategory;
        this.commuterLineID = commuterLineID;
    }

    /**
     * Gets the number of the train.
     * @return the number of the train.
     */
    public int getTrainNumber() {
        return trainNumber;
    }

    /**
     * Sets the number of the train.
     * @param trainNumber the number of the train.
     */
    public void setTrainNumber(int trainNumber) {
        this.trainNumber = trainNumber;
    }

    /**
     * Gets the type of the train.
     * @return the type of the train.
     */
    public String getTrainType() {
        return trainType;
    }

    /**
     * Sets the type of the train.
     * @param trainType the type of the train.
     */
    public void setTrainType(String trainType) {
        this.trainType = trainType;
    }

    /**
     * Gets the category of the train.
     * @return the category of the train.
     */
    public String getTrainCategory() {
        return trainCategory;
    }

    /**
     * Sets the category of the train.
     * @param trainCategory the category of the train.
     */
    public void setTrainCategory(String trainCategory) {
        this.trainCategory = trainCategory;
    }

    /**
     * Gets the commuter id of the train.
     * @return the commuter id of the train.
     */
    public String getCommuterLineID() {
        return commuterLineID;
    }

    /**
     * Sets the commuter id of the train.
     * @param commuterLineID the commuter id of the train.
     */
    public void setCommuterLineID(String commuterLineID) {
        this.commuterLineID = commuterLineID;
    }

    /**
     * Gets the time table rows of the train.
     * @return the time table rows of the train.
     */
    public List<TimeTableRow> getTimeTableRows() {
        return timeTableRows;
    }

    /**
     * Sets the time table rows of the train.
     * @param timeTableRows the time table rows of the train.
     */
    public void setTimeTableRows(List<TimeTableRow> timeTableRows) {
        this.timeTableRows = timeTableRows;
    }

    /**
     * Gets the destination of the train or where the train is from.
     * @return the destination of the train.
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Sets the destination of the train.
     * @param destination the destination of the train
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }


    /**
     * Compares trains to other trains by comparing arrival or departure dates.
     * @param o The other object.
     * @return integer.
     */
    @Override
    public int compareTo(Train o) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return getTimeTableRows().get(0).getScheduledTime().compareTo(o.getTimeTableRows().get(0).getScheduledTime());
        }
        return 0;
    }
}
