package com.jaimedediego.cubemaster.config;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jaimedediego.cubemaster.utils.Session;

import java.util.Arrays;

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
            db.execSQL("ALTER TABLE times ADD COLUMN scramble TEXT");
            db.execSQL("ALTER TABLE times ADD COLUMN image BLOB");
            Cursor c = db.rawQuery("select max(id) from puzzles where user_id=" + Session.getInstance().getCurrentUserId(), null);
            if (c.moveToFirst()) {
                do {
                    int i = 0;
                    for (String puzzle : Arrays.asList("6x6x6", "7x7x7", "Clock")) {
                        i++;
                        boolean exist = false;
                        Cursor c2 = db.rawQuery("select name from puzzles where user_id=" + Session.getInstance().getCurrentUserId(), null);
                        if (c2.moveToFirst()) {
                            do {
                                if (c2.getString(0).equals(puzzle)) {
                                    exist = true;
                                }
                            } while (c2.moveToNext());
                        }
                        c2.close();
                        if (!exist) {
                            db.execSQL("insert into puzzles (id, user_id, name) values (" + (c.getInt(0) + i) + ", " + Session.getInstance().getCurrentUserId() + ", '" + puzzle + "')");
                        }
                    }
                } while (c.moveToNext());
            }
            c.close();
        }
    }
}