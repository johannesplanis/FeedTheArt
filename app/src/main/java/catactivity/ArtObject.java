package catactivity;

import android.graphics.Bitmap;

/**
 * Created by JOHANNES on 8/13/2015.
 */
public class ArtObject {

    private String name;
    private String author;
    private int year;
    private String description;
    private Bitmap image;
    private String url;
    private String storageUri;








    public ArtObject(){

    }
    public ArtObject(Bitmap image, String description, int year, String author, String name) {
        this.image = image;
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
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }


}
