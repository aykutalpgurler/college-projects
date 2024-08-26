import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SmartLamp extends SmartHomeAccessories {

    protected int kelvin;
    protected int brightness;

    /**
     * Constructor for creating a SmartLamp object with given name, status, kelvin and brightness values.
     * @param name the name of the device
     * @param status the status of the device ("On" or "Off")
     * @param kelvin the kelvin value of the lamp
     * @param brightness the brightness value of the lamp
     */
    public SmartLamp(String name, String status, int kelvin, int brightness) {
        super(name, status);
        this.kelvin = kelvin;
        this.brightness = brightness;
    }

    /**
     * switchOperation method overrides the method of its superclass, and performs the switching operation
     * for the SmartLamp object. It sets the switch time of the device to null and updates the status of the
     * device to the given switchStatus.
     * @param switchStatus the new status of the device
     * @param date the date of the switching operation
     */
    @Override
    public void switchOperation(String switchStatus, LocalDateTime date) {
        if (getSwitchTime() != null) {
            setSwitchTime(null); // Switching the status of the device deletes switch time information.
        }
        setStatus(switchStatus);
    }

    /**
     * toString method overrides the method of its superclass, and returns a string representation of
     * the SmartLamp object.
     * @return a string representation of the SmartLamp object
     */
    @Override
    public String toString() {
        return String.format("Smart Lamp %s is %s and its kelvin value is %dK with %d%% brightness, and its time to switch its status is %s.",
                name,
                (status.equals("Off") ? "off" : "on" ),
                getKelvin(),
                getBrightness(),
                ((switchTime == null) ? "null" : switchTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss"))));
    }

    public int getKelvin() {
        return kelvin;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setKelvin(int kelvin) {
        this.kelvin = kelvin;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }
}


