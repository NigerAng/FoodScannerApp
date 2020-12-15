package com.example.niger.nigerangyidong_150271176_co3320.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import static com.example.niger.nigerangyidong_150271176_co3320.data.Contract.Entry;

public class Provider extends ContentProvider {

    private static final int INGREDIENTS = 100;
    private static final int INGREDIENTS_ID = 101;

    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static{
        mUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_INGREDIENTS, INGREDIENTS);
        mUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.PATH_INGREDIENTS + "/#", INGREDIENTS_ID);
    }

    private DatabaseHelper mDbHelper;
    public static final String LOG_TAG = Provider.class.getSimpleName();

    @Override
    public boolean onCreate(){
        mDbHelper = new DatabaseHelper(getContext());
        return true;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder){
        SQLiteDatabase database= mDbHelper.getReadableDatabase();
        Cursor cursor;

        //check if URI match to a specific code
        int match = mUriMatcher.match(uri);
        switch (match){
            case INGREDIENTS:
                cursor = database.query(Entry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case INGREDIENTS_ID:
                cursor = database.query(Entry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                    throw  new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri){
        final int match = mUriMatcher.match(uri);
        switch (match){
            case INGREDIENTS:
                return Entry.CONTENT_LIST_TYPE;
            case INGREDIENTS_ID:
                return Entry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Override
    public Uri insert( Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri,  ContentValues values,  String selection, String[] selectionArgs) {
        return 0;
    }
}
