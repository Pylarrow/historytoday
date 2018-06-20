package com.idthk.historytoday.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by williamyin on 2017/12/19.
 */

public class AmazingFactsPhotoAudioDbTools {
    private String TABLE_NAME = "amazingfacts_photo_audio";// 表名
    private String CountryColumn = "Country";
    private String AgeColumn = "Age";
    private String PhotoIDColumn = "PhotoID";
    private String VoiceIDColumn = "VoiceID";
    private String ContentColumn = "Content";

    public AmazingFactsPhotoAudioDbTools() {
    }

    public ArrayList<String> queryPhotoIDByDb(File file, String country, String age) {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(file, null);
        Cursor cursor = null;
        ArrayList<String> photoID = new ArrayList<String>();
        try {
            cursor = db.query(TABLE_NAME, null, CountryColumn + "=? and " + AgeColumn + "=?", new String[]{country + "", age + ""}, null, null, null);
            while (cursor.moveToNext()) {
                String photo = cursor.getString(cursor.getColumnIndex(PhotoIDColumn));
                Log.i("queryPhotoIDByDb: ",photo);
                photoID.add(photo);
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
        return photoID;
    }

    public ArrayList<String> queryVoiceIDByDb(File file, String country,String age) {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(file, null);
        Cursor cursor = null;
        ArrayList<String> voiceID = new ArrayList<String>();
        try {
            cursor = db.query(TABLE_NAME, null, CountryColumn + "=? and " + AgeColumn + "=?", new String[]{country + "", age + ""}, null, null, null);
            while (cursor.moveToNext()) {
                String photo = cursor.getString(cursor.getColumnIndex(VoiceIDColumn));
                Log.i("queryVoiceIDByDb: ",photo);
                voiceID.add(photo);
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
        return voiceID;
    }
}
