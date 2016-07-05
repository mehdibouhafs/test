package model;

import com.avaje.ebean.Model;

import javax.persistence.*;


import java.io.File;

@Entity
public class UploadResult extends Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    // url
    public String url;
    // "data:image/png;base64, ...
    public String thumbnail_url;
    public String name;
    public String type;
    public Long size;
    public String delete_url;
    public String delete_type;
    public String ref;


    public static Finder<Integer, UploadResult> find = new Finder<Integer, UploadResult>(UploadResult.class);



    @Transient
    public File file;


    public UploadResult() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getDelete_url() {
        return delete_url;
    }

    public void setDelete_url(String delete_url) {
        this.delete_url = delete_url;
    }

    public String getDelete_type() {
        return delete_type;
    }

    public void setDelete_type(String delete_type) {
        this.delete_type = delete_type;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }



}
