package org.pixelexperience.ambient.play.history;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AmbientPlayHistoryDb extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AmbientPlayHistory";
    private static final int DATABASE_VERSION = 1;

    static final String KEY_ID = "_id";
    static final String KEY_TIMESTAMP = "ts";
    static final String KEY_ARTIST = "artist";
    static final String KEY_SONG = "song";

    static final String SQLITE_TABLE = "matched_songs";

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ID + " integer PRIMARY KEY autoincrement," +
                    KEY_TIMESTAMP + "," +
                    KEY_SONG + "," +
                    KEY_ARTIST + ");";


    AmbientPlayHistoryDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
        onCreate(db);
    }


}