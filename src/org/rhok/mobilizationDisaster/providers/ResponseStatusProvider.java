package org.rhok.mobilizationDisaster.providers;

import java.util.HashMap;

import org.rhok.mobilizationDisaster.providers.ResponseStatus.ResponseStates;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

/**
 * The content provider that stores the response information for the
 * current emergency broadcast.
 */
public class ResponseStatusProvider extends ContentProvider {
	
	public static final String AUTHORITY = "org.rhok.mobilizationDisaster.providers.ResponseStatusProvider";
	
	// name for the log message
	private static final String TAG = "ResponsStatusProvider";
	
	// file name
	private static final String DATABASE_NAME = "responsestatus.db";	
	private static final int DATABASE_VERSION = 1;
	
	private static final String STATUS_TABLE_NAME = "status";
	private static final UriMatcher sUriMatcher;
    private static final int STATUS = 1;
    private static HashMap<String, String> notesProjectionMap;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + STATUS_TABLE_NAME + " (" + ResponseStates.ID
                    + " INTEGER PRIMARY KEY AUTOINCREMENT, " 
                    + ResponseStates.STATE + " INTEGER, "
                    + ResponseStates.TRIES + " INTEGER, "
                    + ResponseStates.TIME  + " TEXT, "
                    + ResponseStates.TEXT  + " LONGTEXT" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion
                    + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + STATUS_TABLE_NAME);
            onCreate(db);
        }
    }

    private DatabaseHelper dbHelper;

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case STATUS:
                count = db.delete(STATUS_TABLE_NAME, where, whereArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case STATUS:
                return ResponseStates.CONTENT_TYPE;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        if (sUriMatcher.match(uri) != STATUS) { throw new IllegalArgumentException("Unknown URI " + uri); }

        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long rowId = db.insert(STATUS_TABLE_NAME, ResponseStates.TEXT, values);
        if (rowId > 0) {
            Uri noteUri = ContentUris.withAppendedId(ResponseStates.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch (sUriMatcher.match(uri)) {
            case STATUS:
                qb.setTables(STATUS_TABLE_NAME);
                qb.setProjectionMap(notesProjectionMap);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case STATUS:
                count = db.update(STATUS_TABLE_NAME, values, where, whereArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, STATUS_TABLE_NAME, STATUS);

        notesProjectionMap = new HashMap<String, String>();
        notesProjectionMap.put(ResponseStates.ID,    ResponseStates.ID);
        notesProjectionMap.put(ResponseStates.STATE, ResponseStates.STATE);
        notesProjectionMap.put(ResponseStates.TRIES, ResponseStates.TRIES);
        notesProjectionMap.put(ResponseStates.TIME,  ResponseStates.TIME);
        notesProjectionMap.put(ResponseStates.TEXT,  ResponseStates.TEXT);

    }

}
