package fi.toga.watchlist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Toga on 6.8.2015.
 */
public class ViewMovies extends Activity {
    //ListView listView;

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    List<String> details;
    SQLite moviedb = new SQLite(ViewMovies.this, "moviedb", null, 1);
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        //Open DB
        db = moviedb.getReadableDatabase();

        Cursor c = db.query("moviedb", null, null, null, null, null, null);

        //ArrayList movies = new ArrayList();
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();


        //We have to check if exist at least one register
        if (c.moveToFirst()) {
        //List all results to an ArrayList
            do {
                listDataHeader.add(c.getString(0));
                details = new ArrayList<String>();
                details.add(c.getString(1));
                details.add(c.getString(2));
                details.add(c.getString(3));
                details.add(c.getString(4));
                details.add(c.getString(5));
                listDataChild.put(c.getString(0), details);
            } while(c.moveToNext());
        }
        c.close();
        db.close();

        TextView movieNumbers = (TextView) findViewById(R.id.viewNumbers);
        movieNumbers.setText(getString(R.string.viewNumbers) + ": " + listDataHeader.size());

        //Testing out ExpandedListView
        //Get the listview
        expListView = (ExpandableListView) findViewById(R.id.expandableViewList);

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        //Setting list adapter
        expListView.setAdapter(listAdapter);

        //On child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //Toast.makeText(getApplicationContext(), listDataHeader.get(groupPosition) + " : " + listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        //On Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //Toast.makeText(getApplicationContext(), listDataHeader.get(groupPosition) + " Expanded", Toast.LENGTH_SHORT).show();
            }
        });
        //On Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                //Toast.makeText(getApplicationContext(), listDataHeader.get(groupPosition) + " Collapsed", Toast.LENGTH_SHORT).show();
            }
        });
        //Cant get this to work!
        /*ImageButton delete = (ImageButton) findViewById(R.id.viewDelete);
        delete.setFocusable(false);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Test",Toast.LENGTH_SHORT).show();
            }
        });*/
        Button viewDelete = (Button) findViewById(R.id.viewDeleteButton);
        viewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText viewDeleteText = (EditText) findViewById(R.id.viewDelete);
                if(listDataHeader.contains(viewDeleteText.getText().toString())) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(ViewMovies.this);
                    alert.setTitle(R.string.deleteTitleDialog);
                    alert.setMessage(getString(R.string.deleteMessageDialog) + " " + viewDeleteText.getText().toString() + "?");
                    alert.setPositiveButton(R.string.yes, new
                            DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    db = moviedb.getWritableDatabase();
                                    //EditText viewDeleteText = (EditText) findViewById(R.id.viewDelete);
                                    try {
                                        db.delete("moviedb", "title=?", new String[] { viewDeleteText.getText().toString() });
                                        Toast.makeText(getApplicationContext(),R.string.viewDeleted ,Toast.LENGTH_SHORT).show();
                                    }
                                    catch (Exception e) {
                                        Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
                                    }
                                    finally {
                                        db.close();
                                        finish();
                                    }
                                }
                            });
                    alert.setNegativeButton(R.string.no, new
                            DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                }
                            });
                    alert.show();
                }
                else {
                    Toast.makeText(getApplicationContext(),R.string.viewNotFound ,Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
}
