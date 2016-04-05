package catactivity;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import model.Constants;

/**
 * Created by JOHANNES on 8/13/2015.
 */

public class ArtDownloadRestClient {



    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {

        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {

        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {

        Log.i("REST Api", Constants.baseApiUrl + relativeUrl);
        return Constants.baseApiUrl + relativeUrl;

    }
}
