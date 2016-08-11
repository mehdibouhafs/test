package batch.model;

/**
 * Created by MBS on 15/07/2016.
 */
public class Attribute {
    private int id;
    private String type;
    private String name;
    private String size;
    private boolean pK;
    private boolean nonNull;
    private String defautlVal;
    private String commentaire;

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


    public boolean ispK() {
        return pK;
    }

    public void setpK(boolean pK) {
        this.pK = pK;
    }

    public boolean isNonNull() {
        return nonNull;
    }

    public void setNonNull(boolean nonNull) {
        this.nonNull = nonNull;
    }

    public String getDefautlVal() {
        return defautlVal;
    }

    public void setDefautlVal(String defautlVal) {
        this.defautlVal = defautlVal;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", size='" + size + '\'' +
                ", pK=" + pK +
                ", nonNull=" + nonNull +
                ", defautlVal='" + defautlVal + '\'' +
                ", commentaire='" + commentaire + '\'' +
                '}';
    }
}