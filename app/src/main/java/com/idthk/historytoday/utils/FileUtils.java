package com.idthk.historytoday.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by pengyu.
 * 2018/6/13
 */
public class FileUtils {

    public static String filePath=Environment.getExternalStorageDirectory().getPath()+ File.separator;


    public static File history_today_db_es_file=new File(filePath+File.separator+"history_today_data/db/db_es/history_today.db");
    public static File history_today_db_cn_file=new File(filePath+File.separator+"history_today_data/db/db_cn/history_today.db");

    public static String audio_file_es_path=filePath+File.separator+"history_today_data/MP3/mp3_es/";

    public static String jpg_file_es_path=filePath+File.separator+"history_today_data/jpg/jpg_es/";
    public static String jpg_file_zh_path=filePath+File.separator+"history_today_data/jpg/jpg_zh/";

}
