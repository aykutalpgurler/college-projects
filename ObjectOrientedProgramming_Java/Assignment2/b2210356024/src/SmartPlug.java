import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class represents a smart plug device that can be controlled through a smart home system.
 * It tracks the energy consumption of the device and its on/off status.
 */
public class SmartPlug extends SmartHomeAccessories {

    private double ampere;
    private boolean isPlugged;
    private double totalEnergyConsumption = 0;
    private LocalDateTime startingDate;

    /**
     * Constructor for creating a new SmartPlug object.
     * @param name the name of the smart plug
     * @param status the status of the smart plug (on or off)
     * @param ampere the amount of current (in amperes) used by the device
     * @param date the date and time when the device was turned on
     */
    public SmartPlug(String name, String status, double ampere, LocalDateTime date) {
        super(name, status);
        this.ampere = ampere;
        if (getAmpere() > 0) {
            setPlugged(true); // If the ampere is defined, set the plugged field to true
        }
        if (status.equals("On") && isPlugged()) {
            this.startingDate = date; // If the status is "On" and the accessory is plugged, set the startingDate to the given date
        } else {
            this.startingDate = null;
        }
    }

    /**
     * Method for changing the plug status (plugged in or out) and updating energy consumption accordingly.
     * @param plugStatus the new status of the plug (plug in or plug out)
     * @param ampere the amount of current (in amperes) used by the device
     * @param date the date and time when the plug status was changed
     */
    public void plugOperation(String plugStatus, double ampere, LocalDateTime date) {
        setAmpere(ampere);
        if (plugStatus.equals("PlugIn")) {
            setPlugged(true);
            if (isPlugged() && getStatus().equals("On")) { // If the accessory is both plugged in and turned on, set the startingDate to the given date
                setStartingDate(date);
            }
        } else { //PlugOut
            setPlugged(false);
            if (getAmpere() != 0 && getStatus().equals("On")) {
                // Calculate the energy consumption based on the startingDate and the given date, and add it to the total energy consumption field
                // Volt * Ampere * Time (hours)
                this.totalEnergyConsumption = getTotalEnergyConsumption() +
                        (getStartingDate() == null ? 0 : (220 * getAmpere() * (Duration.between(getStartingDate(), date).toMinutes() / 60.00)));
            }
        }
    }

    /**
     * Method for changing the switch status (on or off) and updating energy consumption accordingly.
     * @param switchStatus the new status of the switch (on or off)
     * @param date the date and time when the switch status was changed
     */
    @Override
    public void switchOperation(String switchStatus, LocalDateTime date) {
        if (getSwitchTime() != null) {
            setSwitchTime(null); // Switching the status of the device deletes switch time information.
        }
        if (switchStatus.equals("On")) {
            setStatus("On");
            if (isPlugged() && getStatus().equals("On")) {
                setStartingDate(date);
            }
        } else { // Off, errors handled in main.
            setStatus("Off");
            if (getAmpere() != 0 && isPlugged()) {
                this.totalEnergyConsumption = getTotalEnergyConsumption() +
                        (getStartingDate() == null ? 0 : (220 * getAmpere() * (Duration.between(getStartingDate(), date).toMinutes() / 60.00)));

            }
        }
    }

    /**
     * Returns a string representation of the SmartPlug object.
     * The returned string includes the name, status, total energy consumption,
     * and switch time of the SmartPlug.
     *
     * @return a string representation of the SmartPlug object.
     */
    @Override
    public String toString() {
        return String.format("Smart Plug %s is %s and consumed %.2fW so far (excluding current device), and its time to switch its status is %s.",
                name,
                (status.equals("Off") ? "off" : "on" ), // same usage: status.toLowerCase()
                totalEnergyConsumption,
                ((switchTime == null) ? "null" : switchTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss"))));
    }

    public double getAmpere() {
        return ampere;
    }

    public void setAmpere(double ampere) {
        this.ampere = ampere;
    }

    public boolean isPlugged() {
        return isPlugged;
    }

    public void setPlugged(boolean plugged) {
        isPlugged = plugged;
    }

    public double getTotalEnergyConsumption() {
        return totalEnergyConsumption;
    }

    public LocalDateTime getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(LocalDateTime startingDate) {
        this.startingDate = startingDate;
    }
}


