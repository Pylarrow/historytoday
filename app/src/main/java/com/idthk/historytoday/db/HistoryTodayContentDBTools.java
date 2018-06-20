package com.idthk.historytoday.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by pengyu.
 * 2018/6/13
 */
public class HistoryTodayContentDBTools {
    private String TABLE_NAME = "history_today";// 表名
    private String Voice_ID_Column="Voice_ID";
    private String Date_Column="Date";
    private String CH_Title_Column="CH_Title";
    private String CH_Content_Column="CH_Content";

    public HistoryTodayContentDBTools(){

    }

    public ArrayList<String> queryContentByDb(File file, String date) {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(file, null);
        Cursor cursor = null;
        ArrayList<String> contents = new ArrayList<String>();
        try {
            cursor = db.query(TABLE_NAME, null, Date_Column + "=?", new String[]{date}, null, null, null);
            while (cursor.moveToNext()) {
                String content = cursor.getString(cursor.getColumnIndex(CH_Content_Column));
                Log.i("CH_Content_Column: ",content);
                contents.add(content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return contents;
    }

    /**
     * 查询title
     * @param file
     * @param date
     * @return
     */
    public String queryTitleByDb(File file,String date){
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(file, null);
        Cursor cursor = null;
        String title="";
        try {
            cursor = db.query(TABLE_NAME, null, Date_Column + "=?", new String[]{date}, null, null, null);
            while (cursor.moveToNext()) {
                title = cursor.getString(cursor.getColumnIndex(CH_Title_Column));
                Log.i("CH_Content_Column: ",title);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return title;
    }
}
