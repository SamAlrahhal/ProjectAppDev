package com.example.project;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyContentProvider extends ContentProvider {
    DatabaseHelper databaseHelper;
    public static final String AUTHORITY = "com.example.project.provider";
    public static final String BASE_PATH = "people";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Use the DatabaseHelper to run your query
        return databaseHelper.getAllPeople();
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        boolean id = databaseHelper.addPerson(values.getAsString(DatabaseHelper.COLUMN_NAME),
                values.getAsString(DatabaseHelper.COLUMN_BIRTHDATE),
                values.getAsString(DatabaseHelper.COLUMN_PHONE_NUMBER));
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        databaseHelper.deletePerson(Integer.parseInt(selectionArgs[0]));

        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        databaseHelper.updatePerson(Integer.parseInt(selectionArgs[0]),
                values.getAsString(DatabaseHelper.COLUMN_NAME),
                values.getAsString(DatabaseHelper.COLUMN_BIRTHDATE),
                values.getAsString(DatabaseHelper.COLUMN_PHONE_NUMBER));
        return 0;
    }
}
