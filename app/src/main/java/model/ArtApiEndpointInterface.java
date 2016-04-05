package model;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by JOHANNES on 4/5/2016.
 */
public interface ArtApiEndpointInterface {

    @GET("fta.php")
    Call<JsonElement> getArt();

}
