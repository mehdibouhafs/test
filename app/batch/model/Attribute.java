package batch.model;

import com.avaje.ebean.Model;

import javax.persistence.*;
import java.util.List;

/**
 * Created by MBS on 15/07/2016.
 */
@Entity
@Table(name="A_MBS3_Attribute")
public class Attribute extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String type;
    public String nameo;
    public String sizeo;
    public boolean pko;
    public boolean nonNull;
    public String commentaires;
    public String defaut;
    public String classe;

    public Attribute() {
    }

    public static Model.Finder<Long,Attribute> find = new Model.Finder(Long.class, Attribute.class);

    public static List<Attribute> findInvolving(String classe) {
        //return find.fetch("project").where().eq("done", false).eq("project.members.email", classe).findList();
        return  find.where().eq("classe",classe).findList();
    }

    public static List<Attribute> findInvolvingNotEqual(String classe) {
        //return find.fetch("project").where().eq("done", false).eq("project.members.email", classe).findList();
        return  find.where().eq("classe",classe).ne("type","OBJECT").findList();
    }


    public static void dropInvolving(String classe){
        List<Attribute> attributes = findInvolving(classe);

        for (Attribute attribute:attributes) {
            find.where().eq("classe",attribute.classe).delete();
        }
    }
    public static void dropAttributeByNameo(String nameo){
        find.where().eq("nameo",nameo).findUnique().delete();
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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


    public String getDefaut() {
        return defaut;
    }

    public void setDefaut(String defaut) {
        this.defaut = defaut;
    }

    public String getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(String commentaires) {
        this.commentaires = commentaires;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", nameo='" + nameo + '\'' +
                ", sizeo='" + sizeo + '\'' +
                ", pko=" + pko +
                ", nonNull=" + nonNull +
                ", commentaires='" + commentaires + '\'' +
                ", classe='" + classe + '\'' +
                '}';
    }
}