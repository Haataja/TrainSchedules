package fi.tamk.tiko.trainschedules.model;

import android.os.Build;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Train  implements Comparable<Train>{
    private int trainNumber;
    private String trainType;
    private String trainCategory;
    private String commuterLineID;
    private List<TimeTableRow> timeTableRows;
    private String destination;

    public Train() {
    }

    public Train(int trainNumber, String trainType, String trainCategory, String commuterLineID) {
        this.trainNumber = trainNumber;
        this.trainType = trainType;
        this.trainCategory = trainCategory;
        this.commuterLineID = commuterLineID;
    }

    public int getTrainNumber() {
        return trainNumber;
    }

    public void setTrainNumber(int trainNumber) {
        this.trainNumber = trainNumber;
    }

    public String getTrainType() {
        return trainType;
    }

    public void setTrainType(String trainType) {
        this.trainType = trainType;
    }

    public String getTrainCategory() {
        return trainCategory;
    }

    public void setTrainCategory(String trainCategory) {
        this.trainCategory = trainCategory;
    }

    public String getCommuterLineID() {
        return commuterLineID;
    }

    public void setCommuterLineID(String commuterLineID) {
        this.commuterLineID = commuterLineID;
    }

    public List<TimeTableRow> getTimeTableRows() {
        return timeTableRows;
    }

    public void setTimeTableRows(List<TimeTableRow> timeTableRows) {
        this.timeTableRows = timeTableRows;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }


    @Override
    public int compareTo(Train o) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return getTimeTableRows().get(0).getScheduledTime().compareTo(o.getTimeTableRows().get(0).getScheduledTime());
        }
        return 0;
    }
}
