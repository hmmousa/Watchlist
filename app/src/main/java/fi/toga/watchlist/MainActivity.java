package fi.toga.watchlist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button addButton = (Button) findViewById(R.id.add_new);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_add = new Intent();
                intent_add.setClass(getBaseContext(), AddMovie.class);
                startActivity(intent_add);
            }
        });

        Button viewButton = (Button) findViewById(R.id.view_list);
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_view = new Intent();
                intent_view.setClass(getBaseContext(), ViewMovies.class);
                startActivity(intent_view);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_delete) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(R.string.clearTitleDialog);
            alert.setMessage(R.string.clearMessageDialog);
            alert.setPositiveButton(R.string.yes, new
                    DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            SQLite moviedb = new SQLite(MainActivity.this, "moviedb", null, 1);
                            SQLiteDatabase db;

                            //Open DB
                            db = moviedb.getWritableDatabase();
                            db.delete("moviedb",null,null);
                            db.close();
                            Toast.makeText(MainActivity.this, R.string.cleared, Toast.LENGTH_LONG).show();
                        }
                    });
            alert.setNegativeButton(R.string.no, new
                    DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        }
                    });
            alert.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.quitTitleDialog);
        alert.setMessage(R.string.quitMessageDialog);
        alert.setPositiveButton(R.string.yes, new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        finish();
                    }
                });
        alert.setNegativeButton(R.string.no, new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
        alert.show();
    }
}
