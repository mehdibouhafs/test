package batch.model;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by MBS on 15/07/2016.
 */
@Entity
@Table(name="A_MBS3_Attribute")
public class Attribute extends Model {
    private int id;
    private String type;
    private String nameo;
    private String sizeo;
    private boolean pko;
    private boolean nonNull;
    private String defautlVal;
    private String commentaire;
    @ManyToOne
    private Classe classe;

    public Attribute() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNameo() {
        return nameo;
    }

    public void setNameo(String nameo) {
        this.nameo = nameo;
    }

    public String getSizeo() {
        return sizeo;
    }

    public void setSizeo(String sizeo) {
        this.sizeo = sizeo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }

    public boolean isPko() {
        return pko;
    }

    public void setPko(boolean pko) {
        this.pko = pko;
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
                ", name='" + nameo + '\'' +
                ", size='" + sizeo + '\'' +
                ", pK=" + pko +
                ", nonNull=" + nonNull +
                ", defautlVal='" + defautlVal + '\'' +
                ", commentaire='" + commentaire + '\'' +
                '}';
    }
}