import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * This abstract class represents a smart home accessory.
 */
public abstract class SmartHomeAccessories {

    protected String name;
    protected String status;
    protected LocalDateTime switchTime = null;

    /**
     * Constructs a new SmartHomeAccessories object with the specified name and status.
     *
     * @param name the name of the accessory
     * @param status the status of the accessory
     */
    public SmartHomeAccessories(String name, String status) {
        this.name = name;
        this.status = status;
    }

    /**
     * This method performs the operation of switching the accessory on or off.
     *
     * @param switchStatus the status to which the accessory should be switched
     * @param date the date and time at which the switch operation is being performed
     */
    public abstract void switchOperation(String switchStatus, LocalDateTime date);

    /**
     * Returns a string representation of this SmartHomeAccessories object.
     *
     * @return a string representation of this SmartHomeAccessories object
     */
    @Override
    public abstract String toString(); // Each object has its own toString method.

    /**
     * Returns the accessory object with the specified name from the given list of accessories.
     *
     * @param name the name of the accessory to retrieve
     * @param accessoryList the list of accessories to search
     * @return the accessory object with the specified name from the given list of accessories
     */
    public static SmartHomeAccessories getAccessory(String name, ArrayList<SmartHomeAccessories> accessoryList) {
        SmartHomeAccessories returningAccessory = null;
        for (SmartHomeAccessories accessory : accessoryList) {
            if (accessory.getName().equals(name)) {
                returningAccessory = accessory; // If the current accessory has the same name as the given name, set it as the returning accessory
            }
        }
        return returningAccessory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getSwitchTime() {
        return switchTime;
    }

    public void setSwitchTime(LocalDateTime switchTime) {
        this.switchTime = switchTime;
    }
}

