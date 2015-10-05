package geofencing;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.model.LatLng;


/**
 *
 */
public class VenueObject {

    String mId;
    String name;
    String description;
    String url;
    double factor; //how much does it feed at once
    double refactorTime;    //how long before you can feed here again
    double mLatitude;
    double mLongitude;
    float mRadius;
    LatLng coordinates;
    private long mExpirationDuration;
    private int mTransitionType;

    public VenueObject() {
    }


    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getFactor() {
        return factor;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }

    public double getRefactorTime() {
        return refactorTime;
    }

    public void setRefactorTime(double refactorTime) {
        this.refactorTime = refactorTime;
    }

    public double getmLatitude() {
        return mLatitude;
    }

    public void setmLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getmLongitude() {
        return mLongitude;
    }

    public void setmLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public float getmRadius() {
        return mRadius;
    }

    public void setmRadius(float mRadius) {
        this.mRadius = mRadius;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }

    public long getmExpirationDuration() {
        return mExpirationDuration;
    }

    public void setmExpirationDuration(long mExpirationDuration) {
        this.mExpirationDuration = mExpirationDuration;
    }

    public int getmTransitionType() {
        return mTransitionType;
    }

    public void setmTransitionType(int mTransitionType) {
        this.mTransitionType = mTransitionType;
    }

    public VenueObject(String mId,
                       double refactorTime,
                       double factor,
                       String url,
                       String description,
                       String name,
                       float mRadius,
                       double mLongitude,
                       double mLatitude) {

        this.mId = mId;
        this.refactorTime = refactorTime;
        this.factor = factor;
        this.url = url;
        this.description = description;
        this.name = name;
        this.mTransitionType = Geofence.GEOFENCE_TRANSITION_DWELL;
        this.mExpirationDuration =Geofence.NEVER_EXPIRE;
        this.mRadius = mRadius;
        this.mLongitude = mLongitude;
        this.mLatitude = mLatitude;
    }


    public VenueObject(String mId, double refactorTime,
                       double factor, String url,
                       String description, String name,
                       int mTransitionType, long mExpirationDuration,
                       float mRadius, double mLongitude, double mLatitude) {

        this.mId = mId;
        this.refactorTime = refactorTime;
        this.factor = factor;
        this.url = url;
        this.description = description;
        this.name = name;
        this.mTransitionType = mTransitionType;
        this.mExpirationDuration = mExpirationDuration;
        this.mRadius = mRadius;
        this.mLongitude = mLongitude;
        this.mLatitude = mLatitude;
    }


}
