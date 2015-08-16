package catactivity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.planis.johannes.catprototype.R;
import com.squareup.picasso.Picasso;

/**
 * Created by JOHANNES on 8/5/2015.
 */
public class CatArtFragment extends Fragment{
    ArtObject object;
    OnRefreshCatArtListener meaowCallback;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Context context = getActivity().getApplicationContext();
        final View view = inflater.inflate(R.layout.cat_art_fragment,
                container, false);
        Button menuButton = (Button) view.findViewById(R.id.cat_art_back_button);
        Button shareButton = (Button) view.findViewById(R.id.cat_art_share_button);
        ImageView artContainer = (ImageView) view.findViewById(R.id.cat_art_image_view);
        TextView title = (TextView) view.findViewById(R.id.cat_art_title);
        TextView author = (TextView) view.findViewById(R.id.cat_art_author);


        object = ((CatActivity)this.getActivity()).getArtObject();


        updateArt(object, view);

        menuButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                toCat();
            }
        });
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareArt();
                refreshCatArt();
            }
        });


        //artContainer.setImageBitmap(((CatActivity)getActivity()).bitmap);

        return view;

    }
    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);


        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            meaowCallback = (OnRefreshCatArtListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnRefreshCatArtListener");
        }
    }

    public void toCat(){
        Activity act = getActivity(); if (act instanceof CatActivity)
            ((CatActivity) act).toCat();
    }
    public void shareArt(){
        Activity act = getActivity(); if (act instanceof CatActivity)
            ((CatActivity) act).shareArt();
    }
    /*
    Update fields according to ArtObject received
     */

    //use in
    public void updateArt(ArtObject art, View view){

        Context context = getActivity().getApplicationContext();
        TextView title = (TextView) view.findViewById(R.id.cat_art_title);
        TextView author = (TextView) view.findViewById(R.id.cat_art_author);
        ImageView artContainer = (ImageView) view.findViewById(R.id.cat_art_image_view);

        title.setText(art.getName());
        author.setText(art.getAuthor());

        //If the image is saved in device, load it, otherwise download it from url address, else you 're fucked
        if (art.getStorageUri() != null && !art.getStorageUri().isEmpty()){
            Bitmap bitmap;
            ArtDownloader ad = new ArtDownloader(getActivity());
            bitmap = ad.loadImageFromStorage(art.getStorageUri());
            artContainer.setImageBitmap(bitmap);
        }   else if(art.getUrl() != null && !art.getUrl().isEmpty()){
            Picasso.with(context).load(art.getUrl()).into(artContainer);
        }   else{
            System.out.println("No reference to image found!");
        }
        //Picasso.with(context).load(art.getUrl()).into(artContainer);
    }

    public void updateArt(ArtObject art){

        Context context = getActivity().getApplicationContext();
        TextView title = (TextView) getView().findViewById(R.id.cat_art_title);
        TextView author = (TextView) getView().findViewById(R.id.cat_art_author);
        ImageView artContainer = (ImageView) getView().findViewById(R.id.cat_art_image_view);

        title.setText(art.getName());
        author.setText(art.getAuthor());

        //If the image is saved in device, load it, otherwise download it from url address,
        if (art.getStorageUri() != null && !art.getStorageUri().isEmpty()){
            Bitmap bitmap;
            ArtDownloader ad = new ArtDownloader(getActivity());
            bitmap = ad.loadImageFromStorage(art.getStorageUri());
            artContainer.setImageBitmap(bitmap);
        }   else if(art.getUrl()!=null&&!art.getUrl().isEmpty()){
            Picasso.with(context).load(art.getUrl()).into(artContainer);
        }   else{
            System.out.println("No reference to image found!");
        }
        Picasso.with(context).load(art.getUrl()).into(artContainer);
    }


    /*@Override
    public void onActivityCreated(){
        super.onActivityCreated();
        object = ((CatActivity)this.getActivity()).getArtObject();
    }
    }*/

    public void refreshCatArt(){
        meaowCallback.onRefreshSelected();
    }

    public interface OnRefreshCatArtListener{
        public void onRefreshSelected();
    }
}
