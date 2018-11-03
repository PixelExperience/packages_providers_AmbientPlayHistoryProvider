package org.pixelexperience.ambient.play.history;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class AmbientPlayHistoryProvider extends ContentProvider {
    private static final int SONGS = 1;
    private static final int SONG = 2;
    private static String MANAGE_HISTORY_PERMISSION = "org.pixelexperience.ambient.play.history.MANAGE_HISTORY";
    private static final String AUTHORITY = "org.pixelexperience.ambient.play.history.provider";
    private static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/songs");
    private static final String[] PROJECTION = {AmbientPlayHistoryDb.KEY_ID, AmbientPlayHistoryDb.KEY_TIMESTAMP, AmbientPlayHistoryDb.KEY_SONG, AmbientPlayHistoryDb.KEY_ARTIST};
    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "songs", SONGS);
        uriMatcher.addURI(AUTHORITY, "songs/#", SONG);
    }

    private AmbientPlayHistoryDb dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new AmbientPlayHistoryDb(getContext());
        return false;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case SONGS:
                //do nothing
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        long isLastSongAlreadyMatched = isLastSongAlreadyMatched(values);
        long id = isLastSongAlreadyMatched != -1 ? isLastSongAlreadyMatched : db.insert(AmbientPlayHistoryDb.SQLITE_TABLE, null, values);
        return Uri.parse(CONTENT_URI + "/" + id);
    }

    private int isLastSongAlreadyMatched(ContentValues values) {
        try (Cursor cursor = query(AmbientPlayHistoryProvider.CONTENT_URI, AmbientPlayHistoryProvider.PROJECTION, null, null, null)) {
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    return values.get(AmbientPlayHistoryDb.KEY_SONG).equals(cursor.getString(2)) && values.get(AmbientPlayHistoryDb.KEY_ARTIST).equals(cursor.getString(3)) ? cursor.getInt(0) : -1;
                }
            }
        }
        return -1;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(AmbientPlayHistoryDb.SQLITE_TABLE);

        switch (uriMatcher.match(uri)) {
            case SONGS:
                break;
            case SONG:
                String id = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(AmbientPlayHistoryDb.KEY_ID + "=" + id);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        return queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, AmbientPlayHistoryDb.KEY_ID + " DESC");

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case SONGS:
                break;
            case SONG:
                String id = uri.getPathSegments().get(1);
                selection = AmbientPlayHistoryDb.KEY_ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ?
                        " AND (" + selection + ')' : "");
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        return db.delete(AmbientPlayHistoryDb.SQLITE_TABLE, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return 0;
    }

}
