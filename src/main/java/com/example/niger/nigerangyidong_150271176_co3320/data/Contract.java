package com.example.niger.nigerangyidong_150271176_co3320.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class Contract {

    private Contract(){}

    public static final String CONTENT_AUTHORITY = "com.example.niger.nigerangyidong_150271176_co3320";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_INGREDIENTS = "ingredients";

    public static final class Entry implements BaseColumns{

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INGREDIENTS);

        //MIME type of CONTENT_URI for list of ingredients
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INGREDIENTS;

        //MIME type of CONTENT_URI for a single ingredient
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_INGREDIENTS;

        public final static String TABLE_NAME = "ingredients";
        public final static String COLUMN_TYPE = "type";
        public final static String COLUMN_PURPOSE = "purpose";
        public final static String COLUMN_NAME = "name";
    }
}
