package Objects;

/**
 * Created by mmbab on 2/20/2017.
 */

public enum LogEnum {
    x001("FIRE"),
    x002("JIMMY_DEAD"),
    x003("food is finished"),
    x004("temperature not working"),
    x005("pH levels dangerously high!"),
    UNKNOWN("");

    private String url;

    LogEnum(String url) {
        this.url = url;
    }

    public String url() {
        return url;
    }
}