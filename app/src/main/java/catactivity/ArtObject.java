package catactivity;

import android.graphics.Bitmap;

/**
 * Created by JOHANNES on 8/13/2015.
 */
public class ArtObject {

    private int id;
    private double factor;
    private String name;
    private String author;
    private String year;
    private String type;
    private String location;
    private String image_url;
    private String description;
    private String storageUri;

    public ArtObject(){

    }
    public ArtObject(int id, double factor,
                     String name, String author, String year,
                     String type, String location, String image_url,
                     String description) {
        this.id = id;
        this.factor = factor;
        this.name = name;
        this.author = author;
        this.year = year;
        this.type = type;
        this.location = location;
        this.image_url = image_url;
        this.description = description;
    }
    public ArtObject(Bitmap image, String description, String year, String author, String name) {

        this.description = description;
        this.year = year;
        this.author = author;
        this.name = name;
    }


    public String getStorageUri() {
        return storageUri;
    }

    public void setStorageUri(String storageUri) {
        this.storageUri = storageUri;
    }
    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getFactor() {
        return factor;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
