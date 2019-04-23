package fi.tamk.tiko.trainschedules.model;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Time table row java model.
 */
@RequiresApi(api = Build.VERSION_CODES.O)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeTableRow {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private String stationShortCode;
    private String type; // arrival vs departure
    private String commercialTrack;
    private boolean cancelled;
    private LocalDateTime scheduledTime;
    private LocalDateTime liveEstimateTime;

    /**
     * Default constructor
     */
    public TimeTableRow() {
    }

    /**
     * Constructor with the variables.
     * @param stationShortCode Stations short code.
     * @param type Tells if trains is arriving or departing.
     * @param commercialTrack Track that train leaves or arrives.
     * @param cancelled Tells if train is cancelled.
     */
    public TimeTableRow(String stationShortCode, String type, String commercialTrack, boolean cancelled) {
        this.stationShortCode = stationShortCode;
        this.type = type;
        this.commercialTrack = commercialTrack;
        this.cancelled = cancelled;
    }

    /**
     * Gets the station short code.
     * @return the station short code.
     */
    public String getStationShortCode() {
        return stationShortCode;
    }

    /**
     * Sets the station short code.
     * @param stationShortCode the station short code.
     */
    public void setStationShortCode(String stationShortCode) {
        this.stationShortCode = stationShortCode;
    }

    /**
     * Gets the type of the scheduled event.
     * @return type of the scheduled event
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the scheduled event.
     * @param type type of the scheduled event.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the track that train is arriving or departing.
     * @return the track that train is arriving or departing
     */
    public String getCommercialTrack() {
        return commercialTrack;
    }

    /**
     * Sets the track that train is arriving or departing.
     * @param commercialTrack The track that train is arriving or departing.
     */
    public void setCommercialTrack(String commercialTrack) {
        this.commercialTrack = commercialTrack;
    }

    /**
     * Tells if train is cancelled.
     * @return boolean
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancelled boolean.
     * @param cancelled boolean.
     */
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Gets the scheduled time that train should leave or arrive to station.
     * @return the scheduled time
     */
    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    /**
     * Sets the scheduled time.
     * @param scheduledTime Time as string.
     */
    public void setScheduledTime(String scheduledTime) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime localDateTime = LocalDateTime.parse(scheduledTime, formatter);

            this.scheduledTime =localDateTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("Europe/Helsinki")).toLocalDateTime();
        }
    }

    /**
     * Gets the estimate when the train will leave or arrive to station.
     * @return the estimate.
     */
    public LocalDateTime getLiveEstimateTime() {
        return liveEstimateTime;
    }

    /**
     * Sets the estimate when the train will leave or arrive to station.
     * @param liveEstimateTime Time as String.
     */
    public void setLiveEstimateTime(String liveEstimateTime) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime localDateTime = LocalDateTime.parse(liveEstimateTime, formatter);
            this.liveEstimateTime = localDateTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("Europe/Helsinki")).toLocalDateTime();
        }
    }
}
