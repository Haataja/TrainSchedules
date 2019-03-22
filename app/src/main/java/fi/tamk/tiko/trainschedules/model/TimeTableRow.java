package fi.tamk.tiko.trainschedules.model;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime localDateTime = LocalDateTime.parse(scheduledTime, formatter);

            this.scheduledTime =localDateTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("Europe/Helsinki")).toLocalDateTime();
        }
    }

    public LocalDateTime getLiveEstimateTime() {
        return liveEstimateTime;
    }

    public void setLiveEstimateTime(String liveEstimateTime) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime localDateTime = LocalDateTime.parse(liveEstimateTime, formatter);
            this.liveEstimateTime = localDateTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of("Europe/Helsinki")).toLocalDateTime();
        }
    }
}
