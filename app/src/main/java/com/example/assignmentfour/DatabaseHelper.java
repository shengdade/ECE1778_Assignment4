/***
 * Copyright (c) 2008-2012 CommonsWare, LLC
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain	a copy
 * of the License at http://www.apache.org/licenses/LICENSE-2.0. Unless required
 * by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS,	WITHOUT	WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 * <p/>
 * From _The Busy Coder's Guide to Android Development_
 * https://commonsware.com/Android
 */

package com.example.assignmentfour;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "AssignmentFour.db";
    private static final int SCHEMA = 1;
    static final String TABLE = "AssignmentFour";
    static final String ID = "id";
    static final String NAME = "name";
    static final String BIO = "bio";
    static final String PICTURE = "picture";
    static final String[] COLUMNS = {ID, NAME, BIO, PICTURE};

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, bio TEXT, picture TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);

        // create fresh books table
        this.onCreate(db);
    }

    public int addProfile(Profile profile) {
        int addFlag = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor =
                db.query(TABLE, // a. table
                        COLUMNS, // b. column names
                        " name = ?", // c. selections
                        new String[]{profile.getName()}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        if (cursor.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put(NAME, profile.getName()); // get name
            values.put(BIO, profile.getBio()); // get bio
            values.put(PICTURE, profile.getPicture()); // get picture
            db.insert(TABLE, null, values);
            addFlag = 1;
        }

        cursor.close();
        db.close();
        return (addFlag);
    }

    public Profile getProfile(int position) {
        String query = "SELECT  * FROM " + TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // if we got results get the first one
        if (cursor != null)
            cursor.moveToPosition(position);

        Profile profile = new Profile();
        profile.setId(cursor.getInt(0));
        profile.setName(cursor.getString(1));
        profile.setBio(cursor.getString(2));
        profile.setPicture(cursor.getString(3));

        cursor.close();
        db.close();
        return profile;
    }

    public List<Profile> getAllProfiles() {
        List<Profile> profiles = new LinkedList<Profile>();
        String query = "SELECT  * FROM " + TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Profile profile = null;
        if (cursor.moveToFirst()) {
            do {
                profile = new Profile();
                profile.setId(cursor.getInt(0));
                profile.setName(cursor.getString(1));
                profile.setBio(cursor.getString(2));
                profile.setPicture(cursor.getString(3));
                profiles.add(profile);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return profiles;
    }

    public int updateProfile(Profile profile) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, profile.getName()); // get name
        values.put(BIO, profile.getBio()); // get bio
        values.put(PICTURE, profile.getPicture()); // get picture

        int i = db.update(TABLE, //table
                values, // column/value
                ID + " = ?", // selections
                new String[]{String.valueOf(profile.getId())}); //selection args

        db.close();
        return i;
    }

    public void deleteProfile(Profile profile) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE, //table name
                ID + " = ?",  // selections
                new String[]{String.valueOf(profile.getId())}); //selections args

        db.close();
    }

    public void deleteAllProfiles() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE);
        db.execSQL("delete from sqlite_sequence where name='" + TABLE + "'");//SQLite Reset Primary Key Field
        db.close();
    }

    public int getCount() {
        int result;
        String query = "SELECT  * FROM " + TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        result = cursor.getCount();
        cursor.close();
        db.close();
        return result;
    }
}