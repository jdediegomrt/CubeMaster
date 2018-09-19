package com.jaimedediego.cubemaster.methods;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jaimedediego.cubemaster.config.DatabaseConfig;
import com.jaimedediego.cubemaster.R;
import com.jaimedediego.cubemaster.utils.Detail;
import com.jaimedediego.cubemaster.utils.Session;

import java.util.ArrayList;
import java.util.List;

public class DatabaseMethods {

    private static DatabaseMethods instance;

    private DatabaseMethods() {}

    public static DatabaseMethods getInstance() {
        if (instance == null) {
            instance = new DatabaseMethods();
        }
        return instance;
    }

    private SQLiteDatabase db;
    private DatabaseConfig timesdb;

    public void setDatabase(Context context){
        timesdb = new DatabaseConfig(context, "DBSolves", null, 1);
    }

    private void openDatabase(){
        db = timesdb.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys = ON;");
    }

    private void closeDatabase(){
        db.close();
    }

    public Cursor makeQuery(String query){
        openDatabase();
        return db.rawQuery(query, null);
    }

    private void makeUpdate(String update){
        openDatabase();
        db.beginTransaction();
        db.execSQL(update);
        db.setTransactionSuccessful();
        db.endTransaction();
        closeDatabase();
    }

    public void addUser() {
        Cursor c = makeQuery("select count(id) from users");
        if (c.moveToFirst()) {
            do {
                int newUserId = c.getInt(0)+1;
                makeUpdate("insert into users (id, name) values (" + newUserId + ", '" + "Default" + "')");
                makeUpdate("insert into puzzles (id, user_id, name) values (" + 1 + ", " + newUserId + ", '3x3x3')");
                makeUpdate("insert into puzzles (id, user_id, name) values (" + 2 + ", " + newUserId + ", '2x2x2')");
                makeUpdate("insert into puzzles (id, user_id, name) values (" + 3 + ", " + newUserId + ", '4x4x4')");
                makeUpdate("insert into puzzles (id, user_id, name) values (" + 4 + ", " + newUserId + ", '5x5x5')");
                makeUpdate("insert into puzzles (id, user_id, name) values (" + 5 + ", " + newUserId + ", 'Megaminx')");
                makeUpdate("insert into puzzles (id, user_id, name) values (" + 6 + ", " + newUserId + ", 'Pyraminx')");
                makeUpdate("insert into puzzles (id, user_id, name) values (" + 7 + ", " + newUserId + ", 'Skewb')");
                makeUpdate("insert into puzzles (id, user_id, name) values (" + 8 + ", " + newUserId + ", 'Square-1')");
            } while(c.moveToNext());
        }
        c.close();
        closeDatabase();
    }

    public int countTimes(){
        int num=0;
        Cursor c = makeQuery("SELECT count(num_solve) FROM times WHERE user_id="+ Session.getInstance().currentUserId+" and puzzle_id="+Session.getInstance().currentPuzzleId);
        if (c.moveToFirst()) {
            do {
                num= c.getInt(0);
            } while(c.moveToNext());
        }
        c.close();
        closeDatabase();
        return num;
    }

    public int countAllTimes(){
        int num=0;
        Cursor c = makeQuery("SELECT count(num_solve) FROM times WHERE user_id="+ Session.getInstance().currentUserId);
        if (c.moveToFirst()) {
            do {
                num= c.getInt(0);
            } while(c.moveToNext());
        }
        c.close();
        closeDatabase();
        return num;
    }

    public int countTimesByName(String name){
        int num=0;
        Cursor c = makeQuery("SELECT count(num_solve) FROM times WHERE user_id="+ Session.getInstance().currentUserId+" and puzzle_id in (select id from puzzles where name='"+name+"')");
        if (c.moveToFirst()) {
            do {
                num= c.getInt(0);
            } while(c.moveToNext());
        }
        c.close();
        closeDatabase();
        return num;
    }

    public void saveData(String time, String date) {
        Cursor c = makeQuery("SELECT max(num_solve) FROM times WHERE user_id="+ Session.getInstance().currentUserId+" and puzzle_id="+Session.getInstance().currentPuzzleId);
        if (c.moveToFirst()) {
            do {
                makeUpdate("INSERT INTO times (user_id, puzzle_id, num_solve, time, date) VALUES (" + Session.getInstance().currentUserId + ", " + Session.getInstance().currentPuzzleId + ", " + (c.getInt(0)+1) + ", '" + time + "', '" + date + "')");
            } while(c.moveToNext());
        }
        c.close();
        closeDatabase();
    }

    public int setDefaultCurrentPuzzle(){
        int id=0;
        Cursor c = makeQuery("select min(id) from puzzles where user_id="+Session.getInstance().currentUserId);
        if(c.moveToFirst()){
            do{
                id=c.getInt(0);
            }while (c.moveToNext());
        }
        c.close();
        closeDatabase();
        return id;
    }

    public void usePuzzle(String puzzle){
        Cursor c = makeQuery("select id from puzzles where name='"+puzzle+"' and user_id=" + Session.getInstance().currentUserId);
        if (c.moveToFirst()) {
            do {
                Session.getInstance().currentPuzzleId=c.getInt(0);
            } while(c.moveToNext());
        }
        c.close();
        closeDatabase();
    }

    public void deletePuzzle(String puzzle){
        int deleteId=0;
        Cursor c = makeQuery("select id from puzzles where name='"+puzzle+"' and user_id="+Session.getInstance().currentUserId);
        if(c.moveToFirst()){
            do{
                deleteId=c.getInt(0);
            }while (c.moveToNext());
        }
        c.close();
        closeDatabase();
        makeUpdate("DELETE FROM puzzles where id='"+deleteId+"'");
        if(deleteId == Session.getInstance().currentPuzzleId){
            Session.getInstance().currentPuzzleId = setDefaultCurrentPuzzle();
        }
    }

    public void deleteSolve(int numSolve){
        makeUpdate("DELETE FROM times where num_solve="+numSolve);
    }

    public void resetPuzzle(String puzzle){
        int deleteId=0;
        Cursor c = makeQuery("select id from puzzles where name='"+puzzle+"' and user_id="+ Session.getInstance().currentUserId);
        if(c.moveToFirst()){
            do{
                deleteId=c.getInt(0);
            }while (c.moveToNext());
        }
        c.close();
        closeDatabase();
        makeUpdate("delete from times where puzzle_id="+deleteId+" and user_id="+Session.getInstance().currentUserId);
    }

    public void deleteLastSolve(int id) {
        Cursor c = makeQuery("SELECT max(num_solve) FROM times WHERE user_id="+Session.getInstance().currentUserId+" and puzzle_id="+id);
        if (c.moveToFirst()) {
            do {
                makeUpdate("delete from times where num_solve=" + c.getInt(0));
            } while(c.moveToNext());
        }
        c.close();
        closeDatabase();
    }

    public void resetCurrentPuzzle() {
        makeUpdate("delete from times where puzzle_id="+Session.getInstance().currentPuzzleId);
    }

    public void fillPuzzlesList(List<String> puzzles, Context context){
        Cursor c = makeQuery("SELECT name FROM puzzles where user_id=" + Session.getInstance().currentUserId + " order by id");
        if (c.moveToFirst()) {
            do {
                puzzles.add(c.getString(0));
            } while(c.moveToNext());
        }
        puzzles.add(context.getResources().getString(R.string.add_new));
        puzzles.add("");
        c.close();
        closeDatabase();
    }

    public String getCurrentPuzzleName(){
        String name = "";
        Cursor c = makeQuery(" SELECT name FROM puzzles where id=" + Session.getInstance().currentPuzzleId + " and user_id=" + Session.getInstance().currentUserId);
        if (c.moveToFirst()) {
            do {
                name=c.getString(0);
            } while(c.moveToNext());
        }
        c.close();
        closeDatabase();
        return name;
    }

    public boolean existPuzzle(String puzzle){
        boolean exist=false;
        Cursor c = makeQuery("select name from puzzles where user_id="+Session.getInstance().currentUserId);
        if (c.moveToFirst()) {
            do {
                if(c.getString(0).equals(puzzle)){
                    exist=true;
                }
            } while (c.moveToNext());
        }
        c.close();
        closeDatabase();
        return exist;
    }

    public void addNewPuzzle(String puzzle){
        Cursor c = makeQuery("select max(id) from puzzles where user_id=" + Session.getInstance().currentUserId);
        if (c.moveToFirst()) {
            do {
                makeUpdate("INSERT INTO puzzles (id, user_id, name) VALUES (" + (c.getInt(0) + 1) + ", " + Session.getInstance().currentUserId + ", '" + puzzle + "')");
            } while (c.moveToNext());
        }
        c.close();
        closeDatabase();
    }


    public List<String> getTimes(){
        List<String> times = new ArrayList<>();
        Cursor c = makeQuery("SELECT time FROM times WHERE user_id="+Session.getInstance().currentUserId+" and puzzle_id="+Session.getInstance().currentPuzzleId+" order by num_solve desc");
        if (c.moveToFirst()) {
            do {
                String time = c.getString(0);
                times.add(time);
            } while(c.moveToNext());
        }
        c.close();
        closeDatabase();
        return times;
    }

    public List<Detail> getTimesDetail(String puzzle, int mode){
        List<Detail> times = new ArrayList<>();
        Cursor c;
        switch (mode){
            case 1: c = DatabaseMethods.getInstance().makeQuery("select time, date, scramble, num_solve from times where user_id="+Session.getInstance().currentUserId+" and puzzle_id in (select id from puzzles where name='"+puzzle+"') order by num_solve asc");
                break;
            case 2: c = DatabaseMethods.getInstance().makeQuery("select time, date, scramble, num_solve from times where user_id="+Session.getInstance().currentUserId+" and puzzle_id in (select id from puzzles where name='"+puzzle+"')");
                break;
            case 3: c = DatabaseMethods.getInstance().makeQuery("select time, date, scramble, num_solve from times where user_id="+Session.getInstance().currentUserId+" and puzzle_id in (select id from puzzles where name='"+puzzle+"')");
                break;
            default: c = DatabaseMethods.getInstance().makeQuery("select time, date, scramble, num_solve from times where user_id="+Session.getInstance().currentUserId+" and puzzle_id in (select id from puzzles where name='"+puzzle+"') order by num_solve desc");
                break;
        }
        if (c.moveToFirst()) {
            do {
                times.add(new Detail(c.getString(0), c.getString(1), null, c.getInt(2)));
            } while(c.moveToNext());
        }
        c.close();
        closeDatabase();
        return times;
    }

    public List<String> getTimesByName(String name){
        List<String> times = new ArrayList<>();
        Cursor c = makeQuery("SELECT time FROM times WHERE user_id="+Session.getInstance().currentUserId+" and puzzle_id in (select id from puzzles where name='"+name+"') order by num_solve desc");
        if (c.moveToFirst()) {
            do {
                String time = c.getString(0);
                times.add(time);
            } while(c.moveToNext());
        }
        c.close();
        closeDatabase();
        return times;
    }

    public int countPuzzles(){
        int num=0;
        Cursor c = makeQuery("SELECT count(id) FROM puzzles WHERE user_id="+Session.getInstance().currentUserId);
        if (c.moveToFirst()) {
            do {
                num = c.getInt(0);
            } while(c.moveToNext());
        }
        c.close();
        closeDatabase();
        return num;
    }
}

