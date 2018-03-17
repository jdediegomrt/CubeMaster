package com.dediegomrt.cubemaster.Config;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseConfig extends SQLiteOpenHelper {

    private String sqlUsers = "CREATE TABLE users (id INTEGER primary key, name TEXT)";
    private String sqlPuzzles = "CREATE TABLE puzzles (id INTEGER, user_id INTEGER, name TEXT, primary key (id, user_id), foreign key (user_id) references users(id) on delete cascade on update cascade)";
    private String sqlTimes = "CREATE TABLE times (user_id INTEGER, puzzle_id INTEGER, num_solve INTEGER, time TEXT, date TEXT, primary key (user_id, puzzle_id, num_solve), foreign key (puzzle_id, user_id) references puzzles(id, user_id) on delete cascade on update cascade)";

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
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        //NOTA: Por simplicidad del ejemplo aquí utilizamos directamente la opción de
        //      eliminar la tabla anterior y crearla de nuevo vacía con el nuevo formato.
        //      Sin embargo lo normal será que haya que migrar datos de la tabla antigua
        //      a la nueva, por lo que este método debería ser más elaborado.
        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS puzzles");
        db.execSQL("DROP TABLE IF EXISTS times");

        //Se crea la nueva versión de la tabla
        db.execSQL(sqlUsers);
        db.execSQL(sqlPuzzles);
        db.execSQL(sqlTimes);
    }
}
