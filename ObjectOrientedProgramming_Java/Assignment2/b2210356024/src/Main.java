import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main {

    public static void main(String[] args) {

        writeToFile(args[1], "", false, false); // Initialize empty output text file.
        String[] inputFile = readFile(args[0], true, true);
        String[][] inputArray = create2DInputArray(Objects.requireNonNull(inputFile), "\t");
        ArrayList<SmartHomeAccessories> accessories = new ArrayList<>();
        LocalDateTime dateTime = null;
        boolean isInitialTimeSet = false;
        int lineIndex = 0;
        boolean isProgramTerminated = false;

        for (String[] commandLine : inputArray) {
            writeToFile(args[1], "COMMAND: " + arrayToCommand(inputFile, lineIndex), true, true);
            if (!isInitialTimeSet || commandLine[0].equals("SetInitialTime")) { // If very first command is not SetInitialTime program must also enter this block and end.
                if (!isInitialTimeSet) {
                    try {
                        if (!(inputArray[0][0].equals("SetInitialTime"))) {
                            throw new RuntimeException();
                        } else if (commandLine.length == 1) {
                            throw new RuntimeException();
                        } else {
                            dateTime = stringToDate(commandLine[1]);
                        }
                        writeToFile(args[1], "SUCCESS: Time has been set to " + dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss")) + "!", true, true);
                    } catch (DateTimeParseException e) {
                        writeToFile(args[1], "ERROR: Format of the initial date is wrong! Program is going to terminate!", true, true);
                        isProgramTerminated = true;
                        break; // Terminate the program.
                    } catch (Exception e) {
                        writeToFile(args[1], "ERROR: First command must be set initial time! Program is going to terminate!", true, true);
                        isProgramTerminated = true;
                        break; // Terminate the program.
                    }
                    isInitialTimeSet = true; // After initial time set firstly (and once) do not allow set again.
                } else {
                    writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                }
            } else if (commandLine[0].equals("SetTime")) {
                if (commandLine.length == 2) {
                    try {
                        stringToDate(commandLine[1]);
                        if (Duration.between(dateTime, stringToDate(commandLine[1])).toNanos() > 0) {
                            dateTime = stringToDate(commandLine[1]);
                            switchAndSort(accessories, dateTime);
                        } else if (Duration.between(dateTime, stringToDate(commandLine[1])).toNanos() < 0) {
                            writeToFile(args[1], "ERROR: Time cannot be reversed!", true, true);
                        } else {
                            writeToFile(args[1], "ERROR: There is nothing to change!", true, true);
                        }
                    } catch (Exception e) {
                        writeToFile(args[1], "ERROR: Time format is not correct!", true, true);
                    }
                } else {
                    writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                }
            } else if (commandLine[0].equals("SkipMinutes")) {
                if (commandLine.length == 2) {
                    try {
                        if (Integer.parseInt(commandLine[1]) < 0) {
                            writeToFile(args[1], "ERROR: Time cannot be reversed!", true, true);
                        } else if (Integer.parseInt(commandLine[1]) == 0) {
                            writeToFile(args[1], "ERROR: There is nothing to skip!", true, true);
                        } else {
                            dateTime = dateTime.plusMinutes(Integer.parseInt(commandLine[1]));
                            switchAndSort(accessories, dateTime);
                        }
                    } catch (Exception e) {
                        writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                    }
                } else {
                    writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                }
            } else if (commandLine[0].equals("Nop")) {
                if (commandLine.length == 1) {
                    if (accessories.size() > 0 && accessories.get(0).getSwitchTime() != null) {
                        dateTime = accessories.get(0).getSwitchTime(); // skips forwards the time to the first switch event.
                        switchAndSort(accessories, dateTime);
                    } else {
                        writeToFile(args[1], "ERROR: There is nothing to switch!", true, true);
                    }
                } else {
                    writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                }
            } else if (commandLine[0].equals("Add")) {
                switch (commandLine[1]) {
                    case "SmartPlug":
                        if (!(isNameUsed(commandLine[2], accessories))) {
                            if (commandLine.length == 3) {
                                accessories.add(new SmartPlug(commandLine[2], "Off", 0, dateTime));
                            } else if (commandLine.length == 4) {
                                if (commandLine[3].equals("On") || commandLine[3].equals("Off")) {
                                    accessories.add(new SmartPlug(commandLine[2], commandLine[3], 0, dateTime));
                                } else {
                                    writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                                }
                            } else if (commandLine.length == 5) {
                                if (commandLine[3].equals("On") || commandLine[3].equals("Off")) {
                                    try {
                                        if (Double.parseDouble(commandLine[4]) > 0) {
                                            accessories.add(new SmartPlug(commandLine[2], commandLine[3], Double.parseDouble(commandLine[4]), dateTime));
                                        } else {
                                            writeToFile(args[1], "ERROR: Ampere value must be a positive number!", true, true);
                                        }
                                    } catch (Exception e) {
                                        writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                                    }
                                } else {
                                    writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                                }
                            } else {
                                writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                            }
                        } else if (!(commandLine.length == 3 || commandLine.length == 4 || commandLine.length == 5)) {
                            writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                        } else {
                            writeToFile(args[1], "ERROR: There is already a smart device with same name!", true, true);
                        }
                        break;
                    case "SmartCamera":
                        if (!isNameUsed(commandLine[2], accessories)) {
                            if (commandLine.length == 4) {
                                try {
                                    if (Double.parseDouble(commandLine[3]) > 0) {
                                        accessories.add(new SmartCamera(commandLine[2], "Off", Double.parseDouble(commandLine[3]), dateTime));
                                    } else {
                                        writeToFile(args[1], "ERROR: Megabyte value must be a positive number!", true, true);
                                    }
                                } catch (Exception e) {
                                    writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                                }
                            } else if (commandLine.length == 5) {
                                if (commandLine[4].equals("On") || commandLine[4].equals("Off")) {
                                    try {
                                        if (Double.parseDouble(commandLine[3]) > 0) {
                                            accessories.add(new SmartCamera(commandLine[2], commandLine[4], Double.parseDouble(commandLine[3]), dateTime));
                                        } else {
                                            writeToFile(args[1], "ERROR: Megabyte value must be a positive number!", true, true);
                                        }
                                    } catch (Exception e) {
                                        writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                                    }
                                } else {
                                    writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                                }
                            } else {
                                writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                            }
                        } else if (!(commandLine.length == 4 || commandLine.length == 5)) {
                            writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                        } else {
                            writeToFile(args[1], "ERROR: There is already a smart device with same name!", true, true);
                        }
                        break;
                    case "SmartLamp":
                        if (!isNameUsed(commandLine[2], accessories)) {
                            if (commandLine.length == 3) {
                                accessories.add(new SmartLamp(commandLine[2], "Off", 4000, 100));
                            } else if (commandLine.length == 4) {
                                if (commandLine[3].equals("On") || commandLine[3].equals("Off")) {
                                    accessories.add(new SmartLamp(commandLine[2], commandLine[3], 4000, 100));
                                } else {
                                    writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                                }
                            } else if (commandLine.length == 6) {
                                try {
                                    Integer.parseInt(commandLine[4]);
                                    Integer.parseInt(commandLine[5]);
                                    if (Integer.parseInt(commandLine[4]) >= 2000 && Integer.parseInt(commandLine[4]) <= 6500) {
                                        if (Integer.parseInt(commandLine[5]) >= 0 && Integer.parseInt(commandLine[5]) <= 100) {
                                            accessories.add(new SmartLamp(commandLine[2], commandLine[3], Integer.parseInt(commandLine[4]), Integer.parseInt(commandLine[5])));
                                        } else {
                                            writeToFile(args[1], "ERROR: Brightness must be in range of 0%-100%!", true, true);
                                        }
                                    } else {
                                        writeToFile(args[1], "ERROR: Kelvin value must be in range of 2000K-6500K!", true, true);
                                    }
                                } catch (Exception e) {
                                    writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                                }
                            } else {
                                writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                            }
                        } else if (!(commandLine.length == 3 || commandLine.length == 4 || commandLine.length == 6)) {
                            writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                        } else {
                            writeToFile(args[1], "ERROR: There is already a smart device with same name!", true, true);
                        }
                        break;
                    case "SmartColorLamp":
                        if (!isNameUsed(commandLine[2], accessories)) {
                            if (commandLine.length == 3) {
                                accessories.add(new SmartColorLamp(commandLine[2], "Off",
                                        4000, 100,
                                        "0x000000", false));
                            } else if (commandLine.length == 4) {
                                if (commandLine[3].equals("On") || commandLine[3].equals("Off")) {
                                    accessories.add(new SmartColorLamp(commandLine[2], commandLine[3],
                                            4000, 100,
                                            "0x000000", false));
                                } else {
                                    writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                                }
                            } else if (commandLine.length == 6) {
                                if (commandLine[4].contains("x")) {
                                    try {
                                        Integer.parseInt(commandLine[4].substring(commandLine[4].indexOf("x") + 1), 16);
                                        Integer.parseInt(commandLine[5]);
                                        if (commandLine[4].substring(commandLine[4].indexOf("x") + 1).length() <= 6) {
                                            if (Integer.parseInt(commandLine[5]) >= 0 && Integer.parseInt(commandLine[5]) <= 100) {
                                                accessories.add(new SmartColorLamp(commandLine[2], commandLine[3],
                                                        4000, Integer.parseInt(commandLine[5]), commandLine[4], true));
                                            } else {
                                                writeToFile(args[1], "ERROR: Brightness must be in range of 0%-100%!", true, true);
                                            }
                                        } else {
                                            writeToFile(args[1], "ERROR: Color code value must be in range of 0x0-0xFFFFFF!", true, true);
                                        }
                                    } catch (Exception e) {
                                        writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                                    }
                                } else {
                                    try {
                                        Integer.parseInt(commandLine[4]);
                                        Integer.parseInt(commandLine[5]);
                                        if (Integer.parseInt(commandLine[4]) >= 2000 && Integer.parseInt(commandLine[4]) <= 6500) {
                                            if (Integer.parseInt(commandLine[5]) >= 0 && Integer.parseInt(commandLine[5]) <= 100) {
                                                accessories.add(new SmartColorLamp(commandLine[2], commandLine[3],
                                                        Integer.parseInt(commandLine[4]), Integer.parseInt(commandLine[5]),
                                                        "0x000000", false));
                                            } else {
                                                writeToFile(args[1], "ERROR: Brightness must be in range of 0%-100%!", true, true);
                                            }
                                        } else {
                                            writeToFile(args[1], "ERROR: Kelvin value must be in range of 2000K-6500K!", true, true);
                                        }
                                    } catch (Exception e) {
                                        writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                                    }
                                }
                            } else {
                                writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                            }
                        } else if (!(commandLine.length == 3 || commandLine.length == 4 || commandLine.length == 6)) {
                            writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                        } else {
                            writeToFile(args[1], "ERROR: There is already a smart device with same name!", true, true);
                        }
                        break;
                    default:
                        writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                        break;
                }
            } else if (commandLine[0].equals("Remove")) {
                if (isNameUsed(commandLine[1], accessories) && commandLine.length == 2) {
                    for (SmartHomeAccessories accessory : accessories) {
                        if (accessory.getName().equals(commandLine[1])) {
                            accessory.switchOperation("Off", dateTime);
                            accessories.remove(accessory);
                            writeToFile(args[1], "SUCCESS: Information about removed smart device is as follows:", true, true);
                            writeToFile(args[1], accessory.toString(), true, true);
                            break; // statement is used to prevent ConcurrentModificationException.
                        }
                    }
                } else {
                    writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                }
            } else if (commandLine[0].equals("SetSwitchTime")) {
                if (commandLine.length == 3 && isNameUsed(commandLine[1], accessories)) {
                    try {
                        if (Duration.between(dateTime, stringToDate(commandLine[2])).toNanos() > 0) {
                            SmartHomeAccessories.getAccessory(commandLine[1], accessories).setSwitchTime(stringToDate(commandLine[2]));
                        } else if (Duration.between(dateTime, stringToDate(commandLine[2])).toNanos() == 0) {
                            // If accessory's switchTime equals current time, firstly, set its switchTime and then do required switch and sort operations.
                            SmartHomeAccessories.getAccessory(commandLine[1], accessories).setSwitchTime(stringToDate(commandLine[2]));
                            switchAndSort(accessories, dateTime);
                        } else {
                            writeToFile(args[1], "ERROR: Switch time cannot be in the past!", true, true);
                        }
                    } catch (Exception e) {
                        writeToFile(args[1], "ERROR: Format of the date is wrong!", true, true);
                    }
                } else if (commandLine.length == 2 && !isNameUsed(commandLine[1], accessories)) {
                    writeToFile(args[1], "ERROR: There is not such a device!", true, true);
                } else {
                    writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                }
            } else if (commandLine[0].equals("Switch")) {
                if (commandLine.length == 3 && isNameUsed(commandLine[1], accessories)) {
                    if (commandLine[2].equals("Off") || commandLine[2].equals("On")) {
                        if (SmartHomeAccessories.getAccessory(commandLine[1], accessories).getStatus().equals("On") && commandLine[2].equals("On")) {
                            writeToFile(args[1], "ERROR: This device is already switched on!", true, true);
                        } else if (SmartHomeAccessories.getAccessory(commandLine[1], accessories).getStatus().equals("Off") && commandLine[2].equals("Off")) {
                            writeToFile(args[1], "ERROR: This device is already switched off!", true, true);
                        } else { // no errors.
                            SmartHomeAccessories.getAccessory(commandLine[1], accessories).switchOperation(commandLine[2], dateTime); // abstract method.
                        }
                    } else {
                        writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                    }
                } else if (!isNameUsed(commandLine[1], accessories)) {
                    writeToFile(args[1], "ERROR: There is not such a device!", true, true);
                } else {
                    writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                }
            } else if (commandLine[0].equals("ChangeName")) {
                if (commandLine.length == 3) {
                    if (isNameUsed(commandLine[2], accessories) && !(commandLine[1].equals(commandLine[2]))) {
                        writeToFile(args[1], "ERROR: There is already a smart device with same name!", true, true);
                    } else if (commandLine[1].equals(commandLine[2])) {
                        writeToFile(args[1], "ERROR: Both of the names are the same, nothing changed!", true, true);
                    } else {
                        SmartHomeAccessories.getAccessory(commandLine[1], accessories).setName(commandLine[2]);
                    }
                } else {
                    writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                }
            } else if (commandLine[0].equals("PlugIn")) {
                if (commandLine.length == 3 && isNameUsed(commandLine[1], accessories)) {
                    if (SmartHomeAccessories.getAccessory(commandLine[1], accessories) instanceof SmartPlug) {
                        if (((SmartPlug) SmartHomeAccessories.getAccessory(commandLine[1], accessories)).isPlugged()) {
                            writeToFile(args[1], "ERROR: There is already an item plugged in to that plug!", true, true);
                        } else {
                            if (Integer.parseInt(commandLine[2]) > 0) {
                                ((SmartPlug) SmartHomeAccessories.getAccessory(commandLine[1], accessories)).plugOperation("PlugIn", Double.parseDouble(commandLine[2]), dateTime);
                            } else {
                                writeToFile(args[1], "ERROR: Ampere value must be a positive number!", true, true);
                            }
                        }
                    } else {
                        writeToFile(args[1], "ERROR: This device is not a smart plug!", true, true);
                    }
                } else if (!isNameUsed(commandLine[1], accessories)) {
                    writeToFile(args[1], "ERROR: There is not such a device!", true, true);
                } else {
                    writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                }
            } else if (commandLine[0].equals("PlugOut")) {
                if (commandLine.length == 2 && isNameUsed(commandLine[1], accessories)) {
                    if (SmartHomeAccessories.getAccessory(commandLine[1], accessories) instanceof SmartPlug) {
                        if (((SmartPlug) SmartHomeAccessories.getAccessory(commandLine[1], accessories)).isPlugged()) {
                            ((SmartPlug) SmartHomeAccessories.getAccessory(commandLine[1], accessories)).plugOperation("PlugOut",
                                    ((SmartPlug) SmartHomeAccessories.getAccessory(commandLine[1], accessories)).getAmpere(), dateTime);
                        } else {
                            writeToFile(args[1], "ERROR: This plug has no item to plug out from that plug!", true, true);
                        }
                    } else {
                        writeToFile(args[1], "ERROR: This device is not a smart plug!", true, true);
                    }
                } else if (!isNameUsed(commandLine[1], accessories)) {
                    writeToFile(args[1], "ERROR: There is not such a device!", true, true);
                } else {
                    writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                }
            } else if (commandLine[0].equals("SetKelvin")) {
                if (commandLine.length == 3 && isNameUsed(commandLine[1], accessories)) {
                    if (SmartHomeAccessories.getAccessory(commandLine[1], accessories) instanceof SmartLamp) {
                        try {
                            if (Integer.parseInt(commandLine[2]) >= 2000 && Integer.parseInt(commandLine[2]) <= 6500) {
                                ((SmartLamp) SmartHomeAccessories.getAccessory(commandLine[1], accessories)).setKelvin(Integer.parseInt(commandLine[2]));
                            } else {
                                writeToFile(args[1], "ERROR: Kelvin value must be in range of 2000K-6500K!", true, true);
                            }
                        } catch (Exception e) {
                            writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                        }
                    } else {
                        writeToFile(args[1], "ERROR: This device is not a smart lamp!", true, true);
                    }
                } else {
                    writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                }
            } else if (commandLine[0].equals("SetBrightness")) {
                if (commandLine.length == 3 && isNameUsed(commandLine[1], accessories)) {
                    if (SmartHomeAccessories.getAccessory(commandLine[1], accessories) instanceof SmartLamp) {
                        try {
                            if (Integer.parseInt(commandLine[2]) >= 0 && Integer.parseInt(commandLine[2]) <= 100) {
                                ((SmartLamp) SmartHomeAccessories.getAccessory(commandLine[1], accessories)).setBrightness(Integer.parseInt(commandLine[2]));
                            } else {
                                writeToFile(args[1], "ERROR: Brightness must be in range of 0%-100%!", true, true);
                            }
                        } catch (Exception e) {
                            writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                        }
                    } else {
                        writeToFile(args[1], "ERROR: This device is not a smart lamp!", true, true);
                    }
                } else {
                    writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                }
            } else if (commandLine[0].equals("SetColorCode")) {
                if (commandLine.length == 3 && isNameUsed(commandLine[1], accessories)) {
                    if (SmartHomeAccessories.getAccessory(commandLine[1], accessories) instanceof SmartColorLamp) {
                        try {
                            Integer.parseInt(commandLine[2].substring(commandLine[2].indexOf("x") + 1), 16);
                            if (commandLine[2].substring(commandLine[2].indexOf("x") + 1).length() <= 6) {
                                ((SmartColorLamp) SmartHomeAccessories.getAccessory(commandLine[1], accessories)).setColorCode(commandLine[2]);
                            } else {
                                writeToFile(args[1], "ERROR: Color code value must be in range of 0x0-0xFFFFFF!", true, true);
                            }
                        } catch (Exception e) {
                            writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                        }
                    } else {
                        writeToFile(args[1], "ERROR: This device is not a smart color lamp!", true, true);
                    }
                } else {
                    writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                }
            } else if (commandLine[0].equals("SetWhite")) {
                if (commandLine.length == 4 && isNameUsed(commandLine[1], accessories)) {
                    if (SmartHomeAccessories.getAccessory(commandLine[1], accessories) instanceof SmartLamp) {
                        try {
                            Integer.parseInt(commandLine[2]); // kelvin
                            Integer.parseInt(commandLine[3]); // brightness
                            if (Integer.parseInt(commandLine[2]) >= 2000 && Integer.parseInt(commandLine[2]) <= 6500) {
                                if (Integer.parseInt(commandLine[3]) >= 0 && Integer.parseInt(commandLine[3]) <= 100) {
                                    ((SmartLamp) SmartHomeAccessories.getAccessory(commandLine[1], accessories)).setKelvin(Integer.parseInt(commandLine[2]));
                                    ((SmartLamp) SmartHomeAccessories.getAccessory(commandLine[1], accessories)).setBrightness(Integer.parseInt(commandLine[3]));
                                } else {
                                    writeToFile(args[1], "ERROR: Brightness must be in range of 0%-100%!", true, true);
                                }
                            } else {
                                writeToFile(args[1], "ERROR: Kelvin value must be in range of 2000K-6500K!", true, true);
                            }
                        } catch (Exception e) {
                            writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                        }
                    } else {
                        writeToFile(args[1], "ERROR: This device is not a smart lamp!", true, true);
                    }
                } else {
                    writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                }
            } else if (commandLine[0].equals("SetColor")) {
                if (commandLine.length == 4 && isNameUsed(commandLine[1], accessories)) {
                    if (SmartHomeAccessories.getAccessory(commandLine[1], accessories) instanceof SmartColorLamp) {
                        try {
                            Integer.parseInt(commandLine[2].substring(commandLine[2].indexOf("x") + 1), 16);
                            Integer.parseInt(commandLine[3]);
                            if (commandLine[2].substring(commandLine[2].indexOf("x") + 1).length() <= 6) {
                                if (Integer.parseInt(commandLine[3]) >= 0 && Integer.parseInt(commandLine[3]) <= 100) {
                                    ((SmartColorLamp) SmartHomeAccessories.getAccessory(commandLine[1], accessories)).setColorCode(commandLine[2]);
                                    ((SmartColorLamp) SmartHomeAccessories.getAccessory(commandLine[1], accessories)).setBrightness(Integer.parseInt(commandLine[3]));
                                } else {
                                    writeToFile(args[1], "ERROR: Brightness must be in range of 0%-100%!", true, true);
                                }
                            } else {
                                writeToFile(args[1], "ERROR: Color code value must be in range of 0x0-0xFFFFFF!", true, true);
                            }
                        } catch (Exception e) {
                            writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                        }
                    } else {
                        writeToFile(args[1], "ERROR: This device is not a smart color lamp!", true, true);
                    }
                } else {
                    writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                }
            } else if (commandLine[0].equals("ZReport")) {
                if (commandLine.length == 1) {
                    writeToFile(args[1], "Time is:\t" + dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss")), true, true);
                    for (SmartHomeAccessories sha : accessories) {
                        writeToFile(args[1], sha.toString(), true, true);
                    }
                } else {
                    writeToFile(args[1], "ERROR: Erroneous command!", true, true);
                }
            } else {
                writeToFile(args[1], "ERROR: Erroneous command!", true, true);
            }
            sortAccessoriesBySwitchTime(accessories);
            lineIndex++; // To write commands to output file, need to keep the line index.
        }
        if (!inputArray[inputArray.length - 1][0].equals("ZReport") && !isProgramTerminated) { // If last command is not ZReport.
            writeToFile(args[1], "ZReport:", true, true);
            writeToFile(args[1], "Time is:\t" + dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss")), true, true);
            for (SmartHomeAccessories sha : accessories) {
                writeToFile(args[1], sha.toString(), true, true);
            }
        }
    }

    /**
     * Switches on the smart home accessories according to their switch time and sorts them by switch time
     *
     * @param accessories an ArrayList of SmartHomeAccessories to be switched on and sorted
     * @param dateTime    the current LocalDateTime to switch the accessories to
     */
    public static void switchAndSort(ArrayList<SmartHomeAccessories> accessories, LocalDateTime dateTime) {
        ArrayList<LocalDateTime> switchTimes = new ArrayList<>();
        for (SmartHomeAccessories accessory : accessories) {
            if (accessory.getSwitchTime() != null && hasTimeAdded(accessory.getSwitchTime(), switchTimes)) {
                switchTimes.add(accessory.getSwitchTime());
            }
        }
        for (LocalDateTime time : switchTimes) {
            for (SmartHomeAccessories accessory : accessories) {
                if (accessory.getSwitchTime() != null && accessory.getSwitchTime().equals(time)) {
                    switchTheAccessory(dateTime, accessory);
                }
            }
            sortAccessoriesBySwitchTime(accessories);
        }
    }

    /**
     * Checks if a given LocalDateTime is present in a List of LocalDateTime objects.
     *
     * @param dateTime the LocalDateTime object to check if it's present in the List.
     * @param times    the List of LocalDateTime objects to check against.
     * @return true if the given LocalDateTime is present in the List, false otherwise.
     */
    public static boolean hasTimeAdded(LocalDateTime dateTime, List<LocalDateTime> times) {
        for (LocalDateTime time : times) {
            if (Duration.between(dateTime, time).toNanos() == 0) { // Actually this simply means, times are equal.
                return false;
            }
        }
        return true;
    }

    /**
     * This method toggles the status of each SmartHomeAccessories object in the accessories array
     * that has a scheduled switch time that has already passed, using the current date and time (dateTime) as the reference.
     *
     * @param dateTime  the current date and time to use as the reference for checking if the scheduled switch time has passed
     * @param accessory the SmartHomeAccessories object which is going to be processed.
     */
    private static void switchTheAccessory(LocalDateTime dateTime, SmartHomeAccessories accessory) {
        if (accessory.getSwitchTime() != null && Duration.between(accessory.getSwitchTime(), dateTime).toNanos() >= 0) { // Check if the accessory has a scheduled switch time and if it has passed
            // If the scheduled switch time has passed, set the switch time to null
            accessory.setSwitchTime(null);
            // Toggle the accessory status by calling its switchOperation method with the dateTime parameter first and the accessory second
            accessory.switchOperation(accessory.getStatus().equals("On") ? "Off" : "On", dateTime);
        }
    }

    /**
     * Sorts a list of SmartHomeAccessories by their switch time in ascending order.
     * Accessories with no switch time are placed at the end of the list.
     *
     * @param accessories The list of SmartHomeAccessories to sort.
     */
    public static void sortAccessoriesBySwitchTime(ArrayList<SmartHomeAccessories> accessories) {
        // Sorts the given ArrayList by the switch time of each accessory using a lambda expression
        accessories.sort((a1, a2) -> {
            if (a1.getSwitchTime() == null && a2.getSwitchTime() == null) {
                // If both accessories have no switch time, return 0 (equivalent)
                return 0;
            } else if (a1.getSwitchTime() == null) {
                // If only the first accessory has no switch time, return 1 (a1 is greater than a2)
                return 1;
            } else if (a2.getSwitchTime() == null) {
                // If only the second accessory has no switch time, return -1 (a2 is greater than a1)
                return -1;
            } else {
                // If both accessories have switch times, compare them using the compareTo method of the LocalDateTime class
                return a1.getSwitchTime().compareTo(a2.getSwitchTime());
            }
        });
    }

    /**
     * Checks whether a given name is already used by an accessory in a list of SmartHomeAccessories.
     *
     * @param name          The name to check for.
     * @param accessoryList The list of SmartHomeAccessories to check in.
     * @return True if the name is already used by an accessory in the list, false otherwise.
     */
    public static boolean isNameUsed(String name, List<SmartHomeAccessories> accessoryList) {
        for (SmartHomeAccessories accessory : accessoryList) {
            if (accessory.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the line at the specified index from a given array of strings.
     *
     * @param file The array of strings to retrieve the line from.
     * @param line The index of the line to retrieve.
     * @return The line at the specified index from the input array.
     */
    public static String arrayToCommand(String[] file, int line) {
        return file[line];
    }

    /**
     * Converts a string representing a date in the format "yyyy-M-d_H:m:s" to a LocalDateTime object.
     *
     * @param dateString The string representation of the date to be converted.
     * @return A LocalDateTime object representing the date and time in the input string.
     */
    public static LocalDateTime stringToDate(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d_H:m:s"); // To handle both padded and unpadded inputs.
        return LocalDateTime.parse(dateString, formatter);
    }

    /**
     * Reads a file at the specified path and returns an array of strings representing each line of the file.
     *
     * @param path              The path of the file to read.
     * @param discardEmptyLines If true, empty lines will be discarded from the returned array.
     * @param trim              If true, leading and trailing whitespace will be removed from each line in the returned array.
     * @return An array of strings representing each line of the file, with empty lines and/or leading/trailing whitespace removed if specified.
     * If the file cannot be read, returns null.
     */
    public static String[] readFile(String path, boolean discardEmptyLines, boolean trim) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(path)); //Gets the content of file to the list.
            if (discardEmptyLines) { //Removes the lines that are empty with respect to trim.
                lines.removeIf(line -> line.trim().equals(""));
            }
            if (trim) { //Trims each line.
                lines.replaceAll(String::trim);
            }
            return lines.toArray(new String[0]);
        } catch (IOException e) { //Returns null if there is no such a file.
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Creates a 2D array of strings from an array of strings using the specified splitter to split each string into elements.
     *
     * @param data     The array of strings to convert to a 2D array.
     * @param splitter The splitter to use to split each string into elements.
     * @return A 2D array of strings representing the input array, with each string split into elements using the splitter.
     */
    public static String[][] create2DInputArray(String[] data, String splitter) {
        // create a 2D array of strings to represent the command lines.
        String[][] inputArray = new String[data.length][data[0].split(splitter).length];
        // loop through each string in the input array and split it into individual elements
        for (int i = 0; i < data.length; i++) {
            inputArray[i] = data[i].split(splitter);
        }
        return inputArray;
    }

    /**
     * Writes content to a file at the specified path. If the file already exists, the content can be appended or overwritten.
     *
     * @param path    The path of the file to write to.
     * @param content The content to write to the file.
     * @param append  If true, the content will be appended to the file. If false, the file will be overwritten with the content.
     * @param newLine If true, a newline character will be added to the end of the content.
     */
    public static void writeToFile(String path, String content, boolean append, boolean newLine) {
        PrintStream ps = null;
        try {
            ps = new PrintStream(new FileOutputStream(path, append));
            ps.print(content + (newLine ? "\n" : ""));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) { //Flushes all the content and closes the stream if it has been successfully created.
                ps.flush();
                ps.close();
            }
        }
    }
}
