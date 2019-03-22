package fi.tamk.tiko.trainschedules.model;

public class TrainStation {
    private boolean passengerTraffic;
    private String countryCode;
    private String stationName;
    private String stationShortCode;
    private String stationUICCode;
    private double latitude;
    private double longitude;
    private String type;

    public TrainStation() {
    }

    public TrainStation(boolean passangerTraffic, String countryCode, String stationName, String stationShortCode, String stationUICCode, double latitude, double longitude, String type) {
        this.passengerTraffic = passangerTraffic;
        this.countryCode = countryCode;
        this.stationName = stationName;
        this.stationShortCode = stationShortCode;
        this.stationUICCode = stationUICCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
    }

    public boolean isPassengerTraffic() {
        return passengerTraffic;
    }

    public void setPassengerTraffic(boolean passengerTraffic) {
        this.passengerTraffic = passengerTraffic;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationShortCode() {
        return stationShortCode;
    }

    public void setStationShortCode(String stationShortCode) {
        this.stationShortCode = stationShortCode;
    }

    public String getStationUICCode() {
        return stationUICCode;
    }

    public void setStationUICCode(String stationUICCode) {
        this.stationUICCode = stationUICCode;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return stationName + "("+stationShortCode+")";
    }
}
