package menuactivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by JOHANNES on 8/19/2015.
 */

//https://github.com/jgilfelt/android-sqlite-asset-helper/blob/master/samples/database-v1/src/main/java/com/example/MyDatabase.java
public class PlacesDatabaseHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "placesdb_v1.db";
    private static final int DATABASE_VERSION = 1;


    PlacesDatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);

    }

    public Cursor getPlaces(){

        SQLiteDatabase db = getReadableDatabase();
        if(db==null){
            Log.i("database","database is empty");
        }
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"0 _id","name","url","longitude","latitude","address"};
        String sqlTables = "places";


        qb.setTables(sqlTables);

        Cursor c = qb.query(db,sqlSelect,null,null,null,null,null);
        if (c==null){
            Log.i("query","cursor is empty!");
        }
        c.moveToFirst();
        return c;
    }
    /*

    //Database reading, custom CursorAdapter
    public void populateList(View view){
        db = new PlacesDatabaseHelper(getActivity().getApplicationContext());
        places = db.getPlaces();


        ListView lv = (ListView) view.findViewById(R.id.tutorial_listview);
        PlacesAdapter adapter = new PlacesAdapter(getActivity().getApplicationContext(),places);
        lv.setAdapter(adapter);
        Log.i("tutorial_database","populated view");
    }


    private class PlacesAdapter extends CursorAdapter{
        public PlacesAdapter(Context context, Cursor cursor){
            super(context,cursor,0);
        }
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent){
            return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
        }
        @Override
        public void bindView(View view, Context context, Cursor cursor){

            TextView tvName = (TextView) view.findViewById(R.id.name);
            TextView tvLong = (TextView) view.findViewById(R.id.longitude);
            TextView tvLat = (TextView) view.findViewById(R.id.latitude);

            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String longitude = cursor.getString(cursor.getColumnIndexOrThrow("longitude"));
            String latitude = cursor.getString(cursor.getColumnIndexOrThrow("latitude"));

            Log.i("cursor", "name " + name);

            tvName.setText(name);
            tvLong.setText(longitude);
            tvLat.setText(latitude);


        }
        }

     */
}
