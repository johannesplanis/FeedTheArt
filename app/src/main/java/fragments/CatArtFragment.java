package fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.planis.johannes.feedtheart.bambino.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import activities.CatActivity;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import model.Art;
import model.ArtApiEndpointInterface;
import model.Constants;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by JOHANNES on 8/5/2015.
 */
public class CatArtFragment extends Fragment {
    @Bind(R.id.cat_art_header)
    TextView catArtHeader;
    @Bind(R.id.cat_art_image_view)
    ImageView catArtImageView;
    @Bind(R.id.cat_art_container)
    FrameLayout catArtContainer;
    @Bind(R.id.cat_art_title)
    TextView catArtTitle;
    @Bind(R.id.cat_art_author)
    TextView catArtAuthor;
    @Bind(R.id.cat_art_year)
    TextView catArtYear;
    @Bind(R.id.cat_art_location)
    TextView catArtLocation;
    @Bind(R.id.cat_art_type)
    TextView catArtType;
    @Bind(R.id.cat_art_description)
    TextView catArtDescription;

    CatActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.cat_art_fragment,
                container, false);
        ButterKnife.bind(this, view);
        activity = (CatActivity) getActivity();

        downloadArt();

        return view;

    }


    public void toCat() {

        activity.toCatFromArt();
    }


    public void updateArt(Art art) {

        catArtTitle.setText(art.getName());
        catArtAuthor.setText(art.getAuthor());
        catArtYear.setText(art.getYear());
        catArtLocation.setText(art.getLocation());
        catArtType.setText(art.getType());
        catArtDescription.setText(art.getDescription());
        Picasso.with(getActivity()).load(art.getImageUrl()).into(catArtImageView);
    }


    public void refreshCatArt() {
        downloadArt();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.cat_art_back_button, R.id.cat_art_share_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cat_art_back_button:
                toCat();
                break;
            case R.id.cat_art_share_button:
                refreshCatArt();
                break;
        }
    }


    public void downloadArt() {
        Log.d(TAG, "downloadArt: ");
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ArtApiEndpointInterface apiService = retrofit.create(ArtApiEndpointInterface.class);

        Observable<JsonElement> art = apiService.getArt();

        art.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JsonElement>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(JsonElement jsonElement) {
                        String s = jsonElement.toString();

                        try {
                            JSONObject o = new JSONObject(s);
                            JSONObject jsonObject = o.getJSONObject("dailyart");
                            Art art = gson.fromJson(jsonObject.toString(), Art.class);
                            updateArt(art);
                            Log.d(TAG, "onResponse: " + art.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }


    private static final String TAG = "dama";


}
