package geofencing;

import android.util.Log;

import com.google.android.gms.location.Geofence;

/**
 * Created by JOHANNES on 9/11/2015.
 */
public class VenueGeofence {
    private final String mId;
    private final double mLatitude;
    private final double mLongitude;
    private final float mRadius;
    private long mExpirationDuration;
    private int mTransitionType;


    public VenueGeofence(String mId, double mLatitude, double mLongitude, float mRadius, long mExpirationDuration, int mTransitionType) {
        this.mId = mId;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        this.mRadius = mRadius;
        this.mExpirationDuration = mExpirationDuration;
        this.mTransitionType = mTransitionType;
    }

    public String getId() {
        return mId;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public long getExpirationDuration() {
        return mExpirationDuration;
    }

    public int getTransitionType() {
        return mTransitionType;
    }

    public float getRadius() {
        return mRadius;
    }

    public Geofence toGeofence() {
        // Build a new Geofence object.
        Log.i("GEOFENCE","BUILT WITH ID: "+mId);
        return new Geofence.Builder()
                .setRequestId(mId)
                .setTransitionTypes(mTransitionType)
                .setCircularRegion(mLatitude, mLongitude, mRadius)
                .setExpirationDuration(mExpirationDuration)
                .setLoiteringDelay(3000)
                .build();

    }
}
