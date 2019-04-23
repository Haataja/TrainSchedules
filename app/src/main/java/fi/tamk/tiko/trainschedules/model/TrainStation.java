package fi.tamk.tiko.trainschedules.model;

/**
 * Java model of the train station.
 */
public class TrainStation {
    private boolean passengerTraffic;
    private String countryCode;
    private String stationName;
    private String stationShortCode;
    private String stationUICCode;
    private double latitude;
    private double longitude;
    private String type;

    /**
     * Default constructor
     */
    public TrainStation() {
    }

    /**
     * Constructor with parameters.
     * @param passangerTraffic Tells if the train station is used in passenger traffic.
     * @param countryCode Tells in witch country station is.
     * @param stationName The name of the station.
     * @param stationShortCode The short code of the station.
     * @param stationUICCode The UIC code of the staion.
     * @param latitude The latitude of the station.
     * @param longitude The longitude of the station.
     * @param type Type of the station, for example "Stopping point"
     */
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

    /**
     * Tells if the train station is used in passenger traffic.
     * @return boolean
     */
    public boolean isPassengerTraffic() {
        return passengerTraffic;
    }

    /**
     * Sets if the train station is used in passenger traffic.
     * @param passengerTraffic boolean.
     */
    public void setPassengerTraffic(boolean passengerTraffic) {
        this.passengerTraffic = passengerTraffic;
    }

    /**
     * Gets the country code.
     * @return the country code.
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * Sets the country code.
     * @param countryCode the country code.
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * Gets the station name.
     * @return the station name.
     */
    public String getStationName() {
        return stationName;
    }

    /**
     * Sets the station name.
     * @param stationName the station name.
     */
    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    /**
     * Gets The station short code.
     * @return The station short code.
     */
    public String getStationShortCode() {
        return stationShortCode;
    }

    /**
     * Sets The station short code.
     * @param stationShortCode The station short code.
     */
    public void setStationShortCode(String stationShortCode) {
        this.stationShortCode = stationShortCode;
    }

    /**
     * Gets the UIC code of the station.
     * @return the UIC code of the station.
     */
    public String getStationUICCode() {
        return stationUICCode;
    }

    /**
     * Sets the UIC code of the station.
     * @param stationUICCode the UIC code of the station.
     */
    public void setStationUICCode(String stationUICCode) {
        this.stationUICCode = stationUICCode;
    }

    /**
     * Gets the latitude of the station.
     * @return the latitude of the station.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude of the station.
     * @param latitude the latitude of the station.
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets the longitude of the station.
     * @return the longitude of the station.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude of the station.
     * @param longitude the longitude of the station.
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * Gets the type of the station.
     * @return the type of the station.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type of the station.
     * @param type the type of the station.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Expresses the station as string.
     * @return String.
     */
    @Override
    public String toString() {
        return stationName + "("+stationShortCode+")";
    }
}
