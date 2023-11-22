package com.example.project;

import android.content.ContentValues;
import android.net.Uri;

public class MyContentProvider {

    public static final Uri CONTENT_URI = Uri.parse("content://com.example.project/table");

    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }


}
