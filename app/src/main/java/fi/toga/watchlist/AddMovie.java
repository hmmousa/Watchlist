package fi.toga.watchlist;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Toga on 6.8.2015.
 */
public class AddMovie extends Activity {
    ArrayList<String> info;
    EditText titleSearch;
    TextView title;
    TextView director;
    TextView actors;
    TextView genre;
    TextView runtime;
    TextView imdbRating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        titleSearch = (EditText) findViewById(R.id.movieTitleSearch);

        title = (TextView) findViewById(R.id.movieTitle);
        director = (TextView) findViewById(R.id.movieDirector);
        actors = (TextView) findViewById(R.id.movieActors);
        genre = (TextView) findViewById(R.id.movieGenre);
        runtime = (TextView) findViewById(R.id.movieRuntime);
        imdbRating = (TextView) findViewById(R.id.movieImdbRating);

        Button searchButton = (Button) findViewById(R.id.movieButtonSearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Search().execute(titleSearch.getText().toString());
            }
        });

        Button addButton = (Button) findViewById(R.id.add_movie);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLite moviedb = new SQLite(AddMovie.this, "moviedb", null, 1);
                SQLiteDatabase db;

                //Open DB
                db = moviedb.getWritableDatabase();

                if (db != null) {
                    //Record values
                    ContentValues newRecord = new ContentValues();

                    newRecord.put("title", getString(R.string.title) + ": " + info.get(0));
                    newRecord.put("director", getString(R.string.director) + ": " + info.get(1));
                    newRecord.put("actors", getString(R.string.actors) + ": " + info.get(2));
                    newRecord.put("genre", getString(R.string.genre) + ": " + info.get(3));
                    newRecord.put("runtime", getString(R.string.runtime) + ": " + info.get(4));
                    newRecord.put("imdbrating", getString(R.string.imdbRating) + ": " + info.get(5));

                    //Insert values
                    db.insert("moviedb", null, newRecord);
                }
                db.close();
                Toast.makeText(getApplicationContext(), R.string.movieAdded, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
    public class Search extends AsyncTask<String, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(String... params) {
            ArrayList<String> info = new ArrayList<>();

            String urlString = "http://www.omdbapi.com/?t=" + params[0] + "&y=&plot=full&r=json";
            urlString = urlString.replace(" ", "%20");
            StringBuffer sb = new StringBuffer("");
            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestProperty("User-Agent", "");
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.connect();

                InputStream inputStream = connection.getInputStream();

                BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                rd.close();

                //JSON PARSE
                JSONObject jsonObject = new JSONObject(sb.toString());

                if (jsonObject.getString("Response").equals("True")) {
                    info.add(jsonObject.getString("Title"));
                    info.add(jsonObject.getString("Director"));
                    info.add(jsonObject.getString("Actors"));
                    info.add(jsonObject.getString("Genre"));
                    info.add(jsonObject.getString("Runtime"));
                    info.add(jsonObject.getString("imdbRating"));
                    info.add(jsonObject.getString("Plot"));
                } else {
                    info.add("No results");
                    info.add("N/A");
                    info.add("N/A");
                    info.add("N/A");
                    info.add("N/A");
                    info.add("N/A");
                    info.add("N/A");
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return info;
        }
        protected void onPostExecute(ArrayList<String> result) {
            Toast.makeText(getBaseContext(), "Done", Toast.LENGTH_LONG).show();
            title.setText(getString(R.string.title) + ": " + result.get(0));
            director.setText(getString(R.string.director) + ": " + result.get(1));
            actors.setText(getString(R.string.actors) + ": " + result.get(2));
            genre.setText(getString(R.string.genre) + ": " + result.get(3));
            runtime.setText(getString(R.string.runtime) + ": " + result.get(4));
            imdbRating.setText(getString(R.string.imdbRating) + ": " + result.get(5));
            info = result;
        }
    }
}
