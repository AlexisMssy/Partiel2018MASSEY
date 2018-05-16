package org.diiage.amassey.partiel2018massey;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.diiage.amassey.partiel2018massey.Adapter.ReleaseAdapter;
import org.diiage.amassey.partiel2018massey.Models.Release;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static org.diiage.amassey.partiel2018massey.ReleaseDbHelper.*;

public class MainActivity extends AppCompatActivity {

    ArrayList<Release> releases;
    ListView listReleases;

    static ReleaseDbHelper helper;
    static SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new ReleaseDbHelper(this);
        db = helper.getWritableDatabase();


        String baseUrlApi = getResources().getString(R.string.base_url_api);
        URL baseUrl = null;
        try{
            baseUrl = new URL(baseUrlApi);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        asyncTask.execute(baseUrlApi);
    }

    @SuppressLint("StaticFieldLeak")
    AsyncTask<String, Void, JSONArray> asyncTask = new AsyncTask<String, Void, JSONArray>() {
        @Override
        protected JSONArray doInBackground(String... strings) {

            URL baseUrl = null;
            try {
                baseUrl = new URL(strings[0]);

                InputStream inputStream = null;

                //Ouverture de la connexion depuis l'url
                HttpURLConnection connection = (HttpURLConnection)baseUrl.openConnection();
                connection.setRequestMethod("GET");
                inputStream = connection.getInputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder stringBuilder = new StringBuilder();

                String lineBuffer = null;
                while ((lineBuffer = bufferedReader.readLine()) != null){
                    stringBuilder.append(lineBuffer);
                }

                String data = stringBuilder.toString();

                return new JSONArray(data);


            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            try {

                releases = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = null;

                    jsonObject = jsonArray.getJSONObject(i);

                    Release release = new Release();

                    release.setArtist(jsonObject.getString("artist"));
                    release.setCatno(jsonObject.getString("catno"));
                    release.setFormat(jsonObject.getString("format"));
                    release.setResourceUrl(jsonObject.getString("resource_url"));
                    release.setStatus(jsonObject.getString("status"));
                    release.setThumb(jsonObject.getString("thumb"));
                    release.setTitle(jsonObject.getString("title"));
                    if (jsonObject.has("year")){
                        release.setYear(jsonObject.getInt("year"));
                    }

                    releases.add(release);
                }

                listReleases = findViewById(R.id.listViewReleases);

                listReleases.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                });

                ReleaseAdapter releaseAdapter = new ReleaseAdapter(MainActivity.this, releases);

                listReleases.setAdapter(releaseAdapter);

                // Click event Synchro
                Button btnSynchro = (Button)findViewById(R.id.buttonSynchro);
                btnSynchro.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SynchronisationApiToDb(releases);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void SynchronisationApiToDb(ArrayList<Release> releases){
        // Delete all
        DeleteTable(db, TABLE_RELEASE);
        DeleteTable(db, TABLE_ARTIST);
        DeleteTable(db, TABLE_LINK_RELEASE_ARTIST);

        // Add releases of API
        for (Release myRelease : releases) {

            //Add Artist
            ContentValues contentValuesArtist = new ContentValues();
            contentValuesArtist.put(TABLE_ARTIST_NOM, myRelease.getArtist());
            long idArtist = db.insert(TABLE_ARTIST, null, contentValuesArtist);

            //Add Release
            ContentValues contentValuesRelease = new ContentValues();
            contentValuesRelease.put(TABLE_RELEASE_ARTIST, myRelease.getArtist());
            contentValuesRelease.put(TABLE_RELEASE_RESSOURCE_URL, myRelease.getResourceUrl());
            contentValuesRelease.put(TABLE_RELEASE_YEAR, myRelease.getYear());
            contentValuesRelease.put(TABLE_RELEASE_CATNO, myRelease.getCatno());
            contentValuesRelease.put(TABLE_RELEASE_TITLE, myRelease.getTitle());
            contentValuesRelease.put(TABLE_RELEASE_FORMAT, myRelease.getFormat());
            contentValuesRelease.put(TABLE_RELEASE_THUMB, myRelease.getThumb());
            contentValuesRelease.put(TABLE_RELEASE_STATUS, myRelease.getStatus());
            contentValuesRelease.put(TABLE_RELEASE_ID_ARTIST, idArtist);
            long idRelease = db.insert(TABLE_RELEASE, null, contentValuesRelease);

            //Add Link artist release
            ContentValues contentValuesLink = new ContentValues();
            contentValuesLink.put(TABLE_LINK_RELEASE_ARTIST_ID_ARTIST, idArtist);
            contentValuesLink.put(TABLE_LINK_RELEASE_ARTIST_ID_RELEASE, idRelease);
            db.insert(TABLE_LINK_RELEASE_ARTIST, null, contentValuesLink);

            Log.e("DEBUG", "Release insérée !");
        }

        Toast toast = Toast.makeText(
                getApplicationContext(),
                "Synchronisation finish !",
                Toast.LENGTH_SHORT);
        toast.show();
    }

    private void DeleteRelease(SQLiteDatabase db, Integer id){
        try {
            db.beginTransaction();
            // Delete beer
            db.delete(TABLE_RELEASE, "id = ?", new String[]{String.valueOf(id)});

            if(true){
                throw new Exception("Impossible de supprimmer les données");
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex){
            db.endTransaction();
        }

        Log.e("DEBUG", "Release supprimée : " + String.valueOf(id));
    }

    private void DeleteTable(SQLiteDatabase db, String table){
        try {
            db.beginTransaction();
            // Delete releases
            db.delete(table, null, null);

            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (Exception ex){
            db.endTransaction();
        }

        Log.e("DEBUG", "Releases supprimées : ");
    }
}
