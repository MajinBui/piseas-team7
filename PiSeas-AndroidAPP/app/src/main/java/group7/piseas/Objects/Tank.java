package group7.piseas.Objects;

/**
 * Created by Sallie on 06/12/2016.
 */
public class Tank {
    private int id;
    private int pw;
    private String name;
    private int type;
    private int size;
    private String desc;

    public Tank(int id, int pw, String name, int type, int size, String desc){
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.type = type;
        this.size = size;
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public int getSize() {
        return size;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public String getName() {
        return name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setType(int type) {
        this.type = type;
    }
}
