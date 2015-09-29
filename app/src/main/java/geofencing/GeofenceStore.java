package geofencing;

import android.content.Context;
import android.content.SharedPreferences;

import cat.Constants;

/**
 * Created by JOHANNES on 9/11/2015.
 */
public class GeofenceStore {

    // The SharedPreferences object in which geofences are stored.
    private final SharedPreferences mPrefs;
    // The name of the SharedPreferences.
    private static final String SHARED_PREFERENCES = "SharedPreferences";

    /**
     * Create the SharedPreferences storage with private access only.
     */
    public GeofenceStore(Context context) {
        mPrefs = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE);
    }

    /**
     * Returns a stored geofence by its id, or returns null if it's not found.
     * @param id The ID of a stored geofence.
     * @return A SimpleGeofence defined by its center and radius, or null if the ID is invalid.
     */
    public VenueGeofence getGeofence(String id) {
        // Get the latitude for the geofence identified by id, or INVALID_FLOAT_VALUE if it doesn't
        // exist (similarly for the other values that follow).
        double lat = mPrefs.getFloat(getGeofenceFieldKey(id, Constants.KEY_LATITUDE),
                Constants.INVALID_FLOAT_VALUE);
        double lng = mPrefs.getFloat(getGeofenceFieldKey(id, Constants.KEY_LONGITUDE),
                Constants.INVALID_FLOAT_VALUE);
        float radius = mPrefs.getFloat(getGeofenceFieldKey(id, Constants.KEY_RADIUS),
                Constants.INVALID_FLOAT_VALUE);
        long expirationDuration =
                mPrefs.getLong(getGeofenceFieldKey(id, Constants.KEY_EXPIRATION_DURATION),
                        Constants.INVALID_LONG_VALUE);
        int transitionType = mPrefs.getInt(getGeofenceFieldKey(id, Constants.KEY_TRANSITION_TYPE),
                Constants.INVALID_INT_VALUE);
        // If none of the values is incorrect, return the object.
        if (lat != Constants.INVALID_FLOAT_VALUE
                && lng != Constants.INVALID_FLOAT_VALUE
                && radius != Constants.INVALID_FLOAT_VALUE
                && expirationDuration != Constants.INVALID_LONG_VALUE
                && transitionType != Constants.INVALID_INT_VALUE) {
            return new VenueGeofence(id, lat, lng, radius, expirationDuration, transitionType);
        }
        // Otherwise, return null.
        return null;
    }

    /**
     * Save a geofence.
     * @param geofence The SimpleGeofence with the values you want to save in SharedPreferences.
     */
    public void setGeofence(String id, VenueGeofence geofence) {
        // Get a SharedPreferences editor instance. Among other things, SharedPreferences
        // ensures that updates are atomic and non-concurrent.
        SharedPreferences.Editor prefs = mPrefs.edit();
        // Write the Geofence values to SharedPreferences.
        prefs.putFloat(getGeofenceFieldKey(id, Constants.KEY_LATITUDE), (float) geofence.getLatitude());
        prefs.putFloat(getGeofenceFieldKey(id, Constants.KEY_LONGITUDE), (float) geofence.getLongitude());
        prefs.putFloat(getGeofenceFieldKey(id, Constants.KEY_RADIUS), geofence.getRadius());
        prefs.putLong(getGeofenceFieldKey(id, Constants.KEY_EXPIRATION_DURATION),
                geofence.getExpirationDuration());
        prefs.putInt(getGeofenceFieldKey(id, Constants.KEY_TRANSITION_TYPE),
                geofence.getTransitionType());
        // Commit the changes.
        prefs.commit();
    }

    /**
     * Remove a flattened geofence object from storage by removing all of its keys.
     */
    public void clearGeofence(String id) {
        SharedPreferences.Editor prefs = mPrefs.edit();
        prefs.remove(getGeofenceFieldKey(id, Constants.KEY_LATITUDE));
        prefs.remove(getGeofenceFieldKey(id, Constants.KEY_LONGITUDE));
        prefs.remove(getGeofenceFieldKey(id, Constants.KEY_RADIUS));
        prefs.remove(getGeofenceFieldKey(id, Constants.KEY_EXPIRATION_DURATION));
        prefs.remove(getGeofenceFieldKey(id, Constants.KEY_TRANSITION_TYPE));
        prefs.commit();
    }

    /**
     * Given a Geofence object's ID and the name of a field (for example, KEY_LATITUDE), return
     * the key name of the object's values in SharedPreferences.
     * @param id The ID of a Geofence object.
     * @param fieldName The field represented by the key.
     * @return The full key name of a value in SharedPreferences.
     */
    private String getGeofenceFieldKey(String id, String fieldName) {
        return Constants.KEY_PREFIX + "_" + id + "_" + fieldName;
    }
}
