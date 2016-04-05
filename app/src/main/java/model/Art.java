package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Art {

    @SerializedName("ID")
    @Expose
    private String ID;
    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("factor")
    @Expose
    private String factor;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("year")
    @Expose
    private String year;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("description")
    @Expose
    private String description;

    /**
     *
     * @return
     * The ID
     */
    public String getID() {
        return ID;
    }

    /**
     *
     * @param ID
     * The ID
     */
    public void setID(String ID) {
        this.ID = ID;
    }

    /**
     *
     * @return
     * The day
     */
    public String getDay() {
        return day;
    }

    /**
     *
     * @param day
     * The day
     */
    public void setDay(String day) {
        this.day = day;
    }

    /**
     *
     * @return
     * The factor
     */
    public String getFactor() {
        return factor;
    }

    /**
     *
     * @param factor
     * The factor
     */
    public void setFactor(String factor) {
        this.factor = factor;
    }

    /**
     *
     * @return
     * The imageUrl
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     *
     * @param imageUrl
     * The image_url
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The year
     */
    public String getYear() {
        return year;
    }

    /**
     *
     * @param year
     * The year
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     *
     * @return
     * The author
     */
    public String getAuthor() {
        return author;
    }

    /**
     *
     * @param author
     * The author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     *
     * @return
     * The type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     * The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     * The location
     */
    public String getLocation() {
        return location;
    }

    /**
     *
     * @param location
     * The location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     *
     * @return
     * The description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public Art(String ID, String day, String factor, String imageUrl, String name, String year, String author, String type, String location, String description) {
        this.ID = ID;
        this.day = day;
        this.factor = factor;
        this.imageUrl = imageUrl;
        this.name = name;
        this.year = year;
        this.author = author;
        this.type = type;
        this.location = location;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Art{" +
                "ID='" + ID + '\'' +
                ", day='" + day + '\'' +
                ", factor='" + factor + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", name='" + name + '\'' +
                ", year='" + year + '\'' +
                ", author='" + author + '\'' +
                ", type='" + type + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
