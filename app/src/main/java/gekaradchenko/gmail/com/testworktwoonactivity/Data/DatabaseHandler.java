package gekaradchenko.gmail.com.testworktwoonactivity.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import gekaradchenko.gmail.com.testworktwoonactivity.Model.ToDo;
import gekaradchenko.gmail.com.testworktwoonactivity.Utils.Util;

public class DatabaseHandler extends SQLiteOpenHelper {

    private SimpleDateFormat format, todayFormat, standartFormat;
    private Date date;

    public DatabaseHandler(Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODO_TABLE = "CREATE TABLE " + Util.TABLE_NAME + "("
                + Util.KEY_ID + " INTEGER PRIMARY KEY,"
                + Util.KEY_TITLE + " TEXT,"
                + Util.KEY_TEXT + " TEXT,"
                + Util.KEY_DATE + " INTEGER" + ")";

        db.execSQL(CREATE_TODO_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Util.TABLE_NAME);
        onCreate(db);
    }


    public void addToDo(ToDo toDo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.KEY_TITLE, toDo.getTitle());
        contentValues.put(Util.KEY_TEXT, toDo.getText());
        contentValues.put(Util.KEY_DATE, toDo.getDate());

        db.insert(Util.TABLE_NAME, null, contentValues);
        db.close();
    }


    public ToDo getToDo(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Util.TABLE_NAME,
                new String[]{Util.KEY_ID, Util.KEY_TITLE, Util.KEY_TEXT, Util.KEY_DATE}
                , Util.KEY_ID + "=?"
                , new String[]{String.valueOf(id)}
                , null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        ToDo toDo = new ToDo(Integer.parseInt(cursor.getString(0))
                , cursor.getString(1)
                , cursor.getString(2)
                , cursor.getString(3));

        return toDo;
    }

    public ArrayList<ToDo> getAllReverseToDos() {


        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ToDo> toDosList = new ArrayList<>();
        String selectAllToDos = "SELECT * FROM " + Util.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectAllToDos, null);
        if (cursor.moveToLast()) {
            do {
                ToDo toDo = new ToDo();
                toDo.setId(Integer.parseInt(cursor.getString(0)));
                toDo.setTitle(cursor.getString(1));
                toDo.setText(cursor.getString(2));
                toDo.setDate(convertTime(cursor.getString(3)));

                toDosList.add(toDo);
            }
            while (cursor.moveToPrevious());
        }
        return toDosList;
    }

    public ArrayList<ToDo> getAllToDos() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ToDo> toDosList = new ArrayList<>();
        String selectAllToDos = "SELECT * FROM " + Util.TABLE_NAME;
        Cursor cursor = db.rawQuery(selectAllToDos, null);
        if (cursor.moveToFirst()) {
            do {
                ToDo toDo = new ToDo();
                toDo.setId(Integer.parseInt(cursor.getString(0)));
                toDo.setTitle(cursor.getString(1));
                toDo.setText(cursor.getString(2));
                toDo.setDate(cursor.getString(3));
                toDosList.add(toDo);
            }
            while (cursor.moveToNext());
        }
        return toDosList;
    }

    public int updateToDo(ToDo toDo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Util.KEY_TITLE, toDo.getTitle());
        contentValues.put(Util.KEY_TEXT, toDo.getText());
        contentValues.put(Util.KEY_DATE, toDo.getDate());

        return db.update(Util.TABLE_NAME, contentValues, Util.KEY_ID + "=?",
                new String[]{String.valueOf(toDo.getId())});
    }


    public void deleteToDo(ToDo toDo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Util.TABLE_NAME, Util.KEY_ID + "=?",
                new String[]{String.valueOf(toDo.getId())});

        db.close();
    }

    public int getToDosCount() {
        SQLiteDatabase db = this.getReadableDatabase();

        String countQuery = "SELECT * FROM " + Util.TABLE_NAME;
        Cursor cursor = db.rawQuery(countQuery, null);
        return cursor.getCount();
    }

    public ArrayList<Integer> getArrayListId() {
        ArrayList<Integer> index = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();


        ArrayList<ToDo> toDos = new ArrayList<>();
        Cursor cursor = db.query(Util.TABLE_NAME, null, null, null, null, null, null);
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            toDos.add(new ToDo(
                    cursor.getInt(cursor.getColumnIndex(Util.KEY_ID)),
                    cursor.getString(cursor.getColumnIndex(Util.KEY_TITLE)),
                    cursor.getString(cursor.getColumnIndex(Util.KEY_TEXT)),
                    cursor.getString(cursor.getColumnIndex(Util.KEY_DATE))));
        }


        if (cursor != null) {
            cursor.moveToFirst();
        }
        for (int i = 0; i < toDos.size() + 1; i++) {
            int y = (cursor.getInt(cursor.getColumnIndex(Util.KEY_ID)));
            index.add(y);
            cursor.moveToNext();
        }
        return index;
    }

    private String convertTime(String str) {
        format = new SimpleDateFormat("dd.MM.yyyy");
        todayFormat = new SimpleDateFormat("hh:mm ");
        standartFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        date = new Date();
        String dateForItem;
        try {
            date = standartFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (format.format(new Date()).equals(format.format(date))) {
            dateForItem = todayFormat.format(date);
        } else dateForItem = format.format(date);
        return dateForItem;
    }


}
