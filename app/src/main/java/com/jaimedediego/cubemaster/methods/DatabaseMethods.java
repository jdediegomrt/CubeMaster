package com.jaimedediego.cubemaster.methods;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.RectF;

import com.jaimedediego.cubemaster.R;
import com.jaimedediego.cubemaster.config.DatabaseConfig;
import com.jaimedediego.cubemaster.utils.Constants;
import com.jaimedediego.cubemaster.utils.Detail;
import com.jaimedediego.cubemaster.utils.Session;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseMethods {

    private static DatabaseMethods instance;

    private DatabaseMethods() {
    }

    public static DatabaseMethods getInstance() {
        if (instance == null) {
            instance = new DatabaseMethods();
        }
        return instance;
    }

    private SQLiteDatabase db;
    private DatabaseConfig timesdb;
    private static final int DATABASE_VERSION = 2;

    public void setDatabase(Context context) {
        timesdb = new DatabaseConfig(context, "DBSolves", null, DATABASE_VERSION);
    }

    private void openDatabase() {
        db = timesdb.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys = ON;");
    }

    private void closeDatabase() {
        db.close();
    }

    private Cursor makeQuery(String query) {
        openDatabase();
        return db.rawQuery(query, null);
    }

    private void makeUpdate(String update, Object... params) {
        openDatabase();
        db.beginTransaction();
        if (params == null || params.length == 0) {
            db.execSQL(update);
        } else {
            db.execSQL(update, params);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        closeDatabase();
    }

    public void addUserAndDefaultPuzzles() {
        Cursor c = makeQuery("select count(id) from users");
        if (c.moveToFirst()) {
            do {
                int newUserId = c.getInt(0) + 1;
                makeUpdate("insert into users (id, name) values (" + newUserId + ", '" + "Default" + "')");
                for (int i = 0; i < Constants.getInstance().CUBEMASTER_DEFAULT_PUZZLES_NAMES.size(); i++) {
                    makeUpdate("insert into puzzles (id, user_id, name) values (" + (i + 1) + ", " + newUserId + ", '" + Constants.getInstance().CUBEMASTER_DEFAULT_PUZZLES_NAMES.get(i) + "')");
                }
            } while (c.moveToNext());
        }
        c.close();
        closeDatabase();
    }

    public void upgradeDatabaseToV2() {
        makeUpdate("ALTER TABLE times ADD COLUMN scramble TEXT");
        makeUpdate("ALTER TABLE times ADD COLUMN image BLOB");
        Cursor c = makeQuery("select max(id) from puzzles where user_id=" + Session.getInstance().getCurrentUserId());
        if (c.moveToFirst()) {
            do {
                if (!existPuzzle("6x6x6"))
                    makeUpdate("insert into puzzles (id, user_id, name) values (" + (c.getInt(0) + 1) + ", " + Session.getInstance().getCurrentUserId() + ", '6x6x6'");
                if (!existPuzzle("7x7x7"))
                    makeUpdate("insert into puzzles (id, user_id, name) values (" + (c.getInt(0) + 2) + ", " + Session.getInstance().getCurrentUserId() + ", '7x7x7')");
                if (!existPuzzle("Clock"))
                    makeUpdate("insert into puzzles (id, user_id, name) values (" + (c.getInt(0) + 3) + ", " + Session.getInstance().getCurrentUserId() + ", 'Clock')");
            } while (c.moveToNext());
        }
        c.close();
        closeDatabase();
    }

    public int countTimes() {
        int num = 0;
        Cursor c = makeQuery("SELECT count(num_solve) FROM times WHERE user_id=" + Session.getInstance().getCurrentUserId() + " and puzzle_id=" + Session.getInstance().getCurrentPuzzleId());
        if (c.moveToFirst()) {
            do {
                num = c.getInt(0);
            } while (c.moveToNext());
        }
        c.close();
        closeDatabase();
        return num;
    }

    public int countAllTimes() {
        int num = 0;
        Cursor c = makeQuery("SELECT count(num_solve) FROM times WHERE user_id=" + Session.getInstance().getCurrentUserId());
        if (c.moveToFirst()) {
            do {
                num = c.getInt(0);
            } while (c.moveToNext());
        }
        c.close();
        closeDatabase();
        return num;
    }

    public int countTimesByName(String name) {
        int num = 0;
        Cursor c = makeQuery("SELECT count(num_solve) FROM times WHERE user_id=" + Session.getInstance().getCurrentUserId() + " and puzzle_id in (select id from puzzles where name='" + name + "')");
        if (c.moveToFirst()) {
            do {
                num = c.getInt(0);
            } while (c.moveToNext());
        }
        c.close();
        closeDatabase();
        return num;
    }

    public Detail saveData(String time, String date, String scramble, byte[] scrambleImage) {
        Cursor c = makeQuery("SELECT max(num_solve) FROM times WHERE user_id=" + Session.getInstance().getCurrentUserId() + " and puzzle_id=" + Session.getInstance().getCurrentPuzzleId());
        if (c.moveToFirst()) {
            do {
                makeUpdate("INSERT INTO times (user_id, puzzle_id, num_solve, time, date, scramble, image) VALUES (" + Session.getInstance().getCurrentUserId() + ", " + Session.getInstance().getCurrentPuzzleId() + ", " + (c.getInt(0) + 1) + ", '" + time + "', '" + date + "', '" + scramble.replace("'", "*") + "', ?)", (Object) scrambleImage);
            } while (c.moveToNext());
        }
        c.close();
        closeDatabase();
        return getCurrentPuzzleLastSolve();
    }

    public int setDefaultCurrentPuzzle() {
        int id = 0;
        Cursor c = makeQuery("select min(id) from puzzles where user_id=" + Session.getInstance().getCurrentUserId());
        if (c.moveToFirst()) {
            do {
                id = c.getInt(0);
            } while (c.moveToNext());
        }
        c.close();
        closeDatabase();
        return id;
    }

    public void usePuzzle(String puzzle) {
        Cursor c = makeQuery("select id from puzzles where name='" + puzzle + "' and user_id=" + Session.getInstance().getCurrentUserId());
        if (c.moveToFirst()) {
            do {
                Session.getInstance().setCurrentPuzzleId(c.getInt(0));
            } while (c.moveToNext());
        }
        c.close();
        closeDatabase();
    }

    public void deletePuzzle(String puzzle) {
        int deleteId = 0;
        Cursor c = makeQuery("select id from puzzles where name='" + puzzle + "' and user_id=" + Session.getInstance().getCurrentUserId());
        if (c.moveToFirst()) {
            do {
                deleteId = c.getInt(0);
            } while (c.moveToNext());
        }
        c.close();
        closeDatabase();
        makeUpdate("DELETE FROM puzzles where id='" + deleteId + "'");
        if (deleteId == Session.getInstance().getCurrentPuzzleId()) {
            Session.getInstance().setCurrentPuzzleId(setDefaultCurrentPuzzle());
        }
    }

    public void deleteSolve(int numSolve) {
        makeUpdate("DELETE FROM times where num_solve=" + numSolve);
    }

    public void resetPuzzle(String puzzle) {
        int deleteId = 0;
        Cursor c = makeQuery("select id from puzzles where name='" + puzzle + "' and user_id=" + Session.getInstance().getCurrentUserId());
        if (c.moveToFirst()) {
            do {
                deleteId = c.getInt(0);
            } while (c.moveToNext());
        }
        c.close();
        closeDatabase();
        makeUpdate("delete from times where puzzle_id=" + deleteId + " and user_id=" + Session.getInstance().getCurrentUserId());
    }

    public void deleteCurrentPuzzleLastSolve() {
        Cursor c = makeQuery("SELECT max(num_solve) FROM times WHERE user_id=" + Session.getInstance().getCurrentUserId() + " and puzzle_id=" + Session.getInstance().getCurrentPuzzleId());
        if (c.moveToFirst()) {
            do {
                makeUpdate("delete from times where num_solve=" + c.getInt(0));
            } while (c.moveToNext());
        }
        c.close();
        closeDatabase();
    }

    public Detail getCurrentPuzzleLastSolve() {
        Detail time = new Detail();
        Cursor c = makeQuery("select time, date, num_solve from times where user_id=" + Session.getInstance().getCurrentUserId() + " and puzzle_id=" + Session.getInstance().getCurrentPuzzleId() + " order by num_solve desc limit 1");
        if (c.moveToFirst()) {
            do {
                time = new Detail(c.getString(0), c.getString(1), c.getInt(2));
            } while (c.moveToNext());
        }
        c.close();
        closeDatabase();
        return time;
    }

    public void fillPuzzlesList(List<String> puzzles, Context context) {
        Cursor c = makeQuery("SELECT name FROM puzzles where user_id=" + Session.getInstance().getCurrentUserId() + " order by id");
        if (c.moveToFirst()) {
            do {
                puzzles.add(c.getString(0));
            } while (c.moveToNext());
        }
        puzzles.add(context.getResources().getString(R.string.add_new));
        c.close();
        closeDatabase();
    }

    public String getCurrentPuzzleName() {
        String name = "";
        Cursor c = makeQuery(" SELECT name FROM puzzles where id=" + Session.getInstance().getCurrentPuzzleId() + " and user_id=" + Session.getInstance().getCurrentUserId());
        if (c.moveToFirst()) {
            do {
                name = c.getString(0);
            } while (c.moveToNext());
        }
        c.close();
        closeDatabase();
        return name;
    }

    public boolean existPuzzle(String puzzle) {
        boolean exist = false;
        Cursor c = makeQuery("select name from puzzles where user_id=" + Session.getInstance().getCurrentUserId());
        if (c.moveToFirst()) {
            do {
                if (c.getString(0).equals(puzzle)) {
                    exist = true;
                }
            } while (c.moveToNext());
        }
        c.close();
        closeDatabase();
        return exist;
    }

    public void addNewPuzzle(String puzzle) {
        Cursor c = makeQuery("select max(id) from puzzles where user_id=" + Session.getInstance().getCurrentUserId());
        if (c.moveToFirst()) {
            do {
                makeUpdate("INSERT INTO puzzles (id, user_id, name) VALUES (" + (c.getInt(0) + 1) + ", " + Session.getInstance().getCurrentUserId() + ", '" + puzzle + "')");
            } while (c.moveToNext());
        }
        c.close();
        closeDatabase();
    }


    public List<String> getTimes() {
        List<String> times = new ArrayList<>();
        Cursor c = makeQuery("SELECT time FROM times WHERE user_id=" + Session.getInstance().getCurrentUserId() + " and puzzle_id=" + Session.getInstance().getCurrentPuzzleId() + " order by num_solve desc");
        if (c.moveToFirst()) {
            do {
                String time = c.getString(0);
                times.add(time);
            } while (c.moveToNext());
        }
        c.close();
        closeDatabase();
        return times;
    }

    public List<Detail> getTimesDetail(String puzzle, int mode) {
        List<Detail> times = new ArrayList<>();
        Cursor c;
        switch (mode) {
            case 1:
                c = makeQuery("select time, date, scramble, image, num_solve from times where user_id=" + Session.getInstance().getCurrentUserId() + " and puzzle_id in (select id from puzzles where name='" + puzzle + "') order by num_solve asc");
                break;
            case 2:
                c = makeQuery("select time, date, scramble, image, num_solve from times where user_id=" + Session.getInstance().getCurrentUserId() + " and puzzle_id in (select id from puzzles where name='" + puzzle + "')");
                break;
            case 3:
                c = makeQuery("select time, date, scramble, image, num_solve from times where user_id=" + Session.getInstance().getCurrentUserId() + " and puzzle_id in (select id from puzzles where name='" + puzzle + "')");
                break;
            default:
                c = makeQuery("select time, date, scramble, image, num_solve from times where user_id=" + Session.getInstance().getCurrentUserId() + " and puzzle_id in (select id from puzzles where name='" + puzzle + "') order by num_solve desc");
                break;
        }
        if (c.moveToFirst()) {
            do {
                String scramble = c.getString(2);
                if (scramble != null) {
                    scramble = scramble.replace("*", "'");
                }
                byte[] blob = c.getBlob(3);
                Picture picture = null;
                if (blob != null && blob.length != 0) {
                    ByteArrayInputStream input = new ByteArrayInputStream(blob);
                    Bitmap bitmap = BitmapFactory.decodeStream(input);
                    picture = new Picture();
                    Canvas canvas = picture.beginRecording(bitmap.getWidth(), bitmap.getHeight());
                    canvas.drawBitmap(bitmap, null, new RectF(0f, 0f, (float) bitmap.getWidth(), (float) bitmap.getHeight()), null);
                    picture.endRecording();

                }
                times.add(new Detail(c.getString(0), c.getString(1), scramble, picture, c.getInt(4)));
            } while (c.moveToNext());
        }
        c.close();
        closeDatabase();
        return times;
    }

    public List<String> getTimesByName(String name) {
        List<String> times = new ArrayList<>();
        Cursor c = makeQuery("SELECT time FROM times WHERE user_id=" + Session.getInstance().getCurrentUserId() + " and puzzle_id in (select id from puzzles where name='" + name + "') order by num_solve desc");
        if (c.moveToFirst()) {
            do {
                String time = c.getString(0);
                times.add(time);
            } while (c.moveToNext());
        }
        c.close();
        closeDatabase();
        return times;
    }

    public int countPuzzles() {
        int num = 0;
        Cursor c = makeQuery("SELECT count(id) FROM puzzles WHERE user_id=" + Session.getInstance().getCurrentUserId());
        if (c.moveToFirst()) {
            do {
                num = c.getInt(0);
            } while (c.moveToNext());
        }
        c.close();
        closeDatabase();
        return num;
    }
}

