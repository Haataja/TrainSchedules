package fi.tamk.tiko.trainschedules.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeTableRow {
    private String stationShortCode;
    private String type; // arrival vs departure
    private String commercialTrack;
    private boolean cancelled;
    private String scheduledTime;
    private String liveEstimateTime;

    public TimeTableRow() {
    }

    public TimeTableRow(String stationShortCode, String type, String commercialTrack, boolean cancelled) {
        this.stationShortCode = stationShortCode;
        this.type = type;
        this.commercialTrack = commercialTrack;
        this.cancelled = cancelled;
    }

    public String getStationShortCode() {
        return stationShortCode;
    }

    public void setStationShortCode(String stationShortCode) {
        this.stationShortCode = stationShortCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCommercialTrack() {
        return commercialTrack;
    }

    public void setCommercialTrack(String commercialTrack) {
        this.commercialTrack = commercialTrack;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        String time = scheduledTime.split("T")[1].substring(0,5);
        this.scheduledTime = time;
    }

    public String getLiveEstimateTime() {
        return liveEstimateTime;
    }

    public void setLiveEstimateTime(String liveEstimateTime) {
        String time = liveEstimateTime.split("T")[1].substring(0,5);
        this.liveEstimateTime = time;
    }
}
