import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SmartCamera extends SmartHomeAccessories {

    private final double MEGABYTES_CONSUMED_PER_RECORD;
    private LocalDateTime startingDate;
    private double totalStorageUsed;

    /**
     * Constructs a SmartCamera object with a specified name, status, megabytes, and date.
     *
     * @param name         the name of the smart camera
     * @param status       the status of the smart camera, either "On" or "Off"
     * @param megabytes    the amount of storage used by the camera in megabytes
     * @param date         the starting date of the camera recording
     */
    public SmartCamera(String name, String status, double megabytes, LocalDateTime date) {
        super(name, status);
        this.MEGABYTES_CONSUMED_PER_RECORD = megabytes;
        if (status.equals("On")) { // initialize startingDate
            this.startingDate = date;
        } else {
            this.startingDate = null;
        }
    }

    /**
     * Sets the status and starting date of the camera based on the given switchStatus and date.
     * If the switchStatus is "Off", it updates the total amount of storage used by the camera.
     *
     * @param switchStatus  the switch status of the camera, either "On" or "Off"
     * @param date          the date of the switch operation
     */
    @Override
    public void switchOperation(String switchStatus, LocalDateTime date) {
        if (getSwitchTime() != null) {
            setSwitchTime(null); // Switching the status of the device deletes switch time information.
        }
        if (switchStatus.equals("On")) {
            setStatus("On");
            setStartingDate(date);
        } else { // Off, errors handled in main.
            setStatus("Off");
            this.totalStorageUsed = getTotalStorageUsed() + ((Duration.between(getStartingDate(), date).toMinutes()) * getMegabytes());
        }
    }

    /**
     * Returns a string representation of the smart camera object.
     *
     * @return a string representation of the smart camera object
     */
    @Override
    public String toString() {
        return String.format("Smart Camera %s is %s and used %.2f MB of storage so far (excluding current status), and its time to switch its status is %s.",
                name,
                (status.equals("Off") ? "off" : "on" ),
                totalStorageUsed,
                ((switchTime == null) ? "null" : switchTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss"))));
    }

    public double getMegabytes() {
        return MEGABYTES_CONSUMED_PER_RECORD;
    }

    public LocalDateTime getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(LocalDateTime startingDate) {
        this.startingDate = startingDate;
    }

    public double getTotalStorageUsed() {
        return totalStorageUsed;
    }

}
