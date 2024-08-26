import java.time.format.DateTimeFormatter;

public class SmartColorLamp extends SmartLamp {

    private String colorCode;
    private boolean isColorMode;

    /**
     * Constructs a new SmartColorLamp object with the specified name, status, kelvin, brightness, color code, and color mode.
     * @param name the name of the SmartColorLamp
     * @param status the status of the SmartColorLamp
     * @param kelvin the kelvin value of the SmartColorLamp
     * @param brightness the brightness value of the SmartColorLamp
     * @param colorCode the color code of the SmartColorLamp
     * @param isColorMode the color mode of the SmartColorLamp
     */
    public SmartColorLamp(String name, String status,
                          int kelvin, int brightness,
                          String colorCode, boolean isColorMode) {
        super(name, status, kelvin, brightness);
        this.colorCode = colorCode;
        this.isColorMode = isColorMode;
    }

    /**
     * Returns a String representation of the SmartColorLamp object.
     * @return a String containing the name, status, color or kelvin value, brightness, and time to switch status of the SmartColorLamp.
     */
    @Override
    public String toString() {
        return String.format("Smart Color Lamp %s is %s and its color value is %s with %d%% brightness, and its time to switch its status is %s.",
                name,
                (status.equals("Off") ? "off" : "on" ),
                (isColorMode ? colorCode : kelvin + "K"), // If SmartColorLamp object is in color mode, use colorCode value else kelvin value.
                getBrightness(),
                ((switchTime == null) ? "null" : switchTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss"))));
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
        setColorMode(true); // When color code is set, color mode is enabled.
    }

    public void setColorMode(boolean colorMode) {
        isColorMode = colorMode;
    }

    @Override
    public void setKelvin(int kelvin) {
        super.setKelvin(kelvin);
        setColorMode(false); // When kelvin value is set, color mode is disabled.
    }
}


