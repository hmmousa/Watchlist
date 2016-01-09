package fi.toga.watchlist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Toga on 6.8.2015.
 */
public class SQLite extends SQLiteOpenHelper {

    String createSQL = "CREATE TABLE moviedb (title TEXT, director TEXT, actors TEXT, genre TEXT, runtime TEXT, imdbrating TEXT)";

    public SQLite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS moviedb");
        db.execSQL(createSQL);
    }
}
