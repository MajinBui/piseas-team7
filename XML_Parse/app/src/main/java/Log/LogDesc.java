package Log;

/**
 * Created by mmbab on 2/22/2017.
 *
 * Enum structure for log description
 *
 */

public enum LogDesc {
    TEMPRANGE("Temperature out of range. "),
    PHRANGE("pH out of range."),
    WATERCHANGE("Full manual water change required."),
    SOURCEDEPLEATED("Water source depleted."),
    WATEREMPTY("Tank is empty, can not drain tank. "),
    WATERFULL("Tank water level full, can not fill tank. "),
    FOODEMPTY("Out of food."),
    MANFEEDING("Manual feeding."),
    LIGHTMANOFF("Manual light off."),
    LIGHTMANON("Manual light on."),
    WATERMANFILL("Manual water fill."),
    WATERMANDRAIN("Manual water drain."),
    HEATAUTOON("Automatic heater on."),
    HEATAUTOOFF("Automatic heater off"),
    FANAUTOON("Automatic fan on."),
    FANAUTOOFF("Automatic fan off."),
    PUMPINAUTOON("Automatic intake pump on."),
    PUMPINAUTOOFF("Automatic intake pump off."),
    PUMPOUTAUTOON("Automatic outtake pump on."),
    PUMPOUTAUTOOFF("Automatic outtake pump off."),
    LIGHTAUTOON("Auotmatic light on."),
    LIGHTAUTOOFF("Automatic light off"),
    DAILYDRAIN("Daily partial drain on."),
    DAILYFILL("Daily partial drain off."),
    FEEDACTIVE("Activate feeder."),
    ALARMACTIVE("Activate alarm.");

    private String description;

    LogDesc(String dd) {
        this.description = dd;
    }

    public static String fromString(String text) {
        for (LogDesc ll: LogDesc.values()) {
            if (ll.name().equals(text)) {
                return ll.description;
            }
        }
        return "Error, unknown code.";
    }
}

