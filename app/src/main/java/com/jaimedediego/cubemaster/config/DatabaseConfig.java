package com.jaimedediego.cubemaster.config;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jaimedediego.cubemaster.methods.DatabaseMethods;
import com.jaimedediego.cubemaster.utils.Constants;

public class DatabaseConfig extends SQLiteOpenHelper {

    private String sqlUsers =
            "CREATE TABLE users " +
                    "(id INTEGER primary key, " +
                    "name TEXT)";

    private String sqlPuzzles =
            "CREATE TABLE puzzles " +
                    "(id INTEGER, " +
                    "user_id INTEGER, " +
                    "name TEXT, " +
                    "primary key (id, user_id), " +
                    "foreign key (user_id) references users(id) on delete cascade on update cascade)";

    private String sqlTimes =
            "CREATE TABLE times " +
                    "(user_id INTEGER, " +
                    "puzzle_id INTEGER, " +
                    "num_solve INTEGER, " +
                    "scramble TEXT, " +
                    "image BLOB, " +
                    "time TEXT, " +
                    "date TEXT, " +
                    "primary key (user_id, puzzle_id, num_solve), " +
                    "foreign key (puzzle_id, user_id) references puzzles(id, user_id) on delete cascade on update cascade)";

    public DatabaseConfig(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlUsers);
        db.execSQL(sqlPuzzles);
        db.execSQL(sqlTimes);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("Database onUpgrade: ", "Updating table from " + oldVersion + " to " + newVersion);

        if (oldVersion < 2) {
            DatabaseMethods.getInstance().upgradeDatabaseToV2();
        }
    }
}
