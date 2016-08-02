package model;

/**
 * Created by MBS on 15/07/2016.
 */
public class Attribute {
    private int id;
    private String type;
    private String name;
    private String size;
    private String primaryKey;
    private boolean autoIncrement;

    public Attribute() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Attribute{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", size='" + size + '\'' +
                ", primaryKey='" + primaryKey + '\'' +
                ", autoIncrement=" + autoIncrement +
                ", id=" + id +
                '}';
    }
}