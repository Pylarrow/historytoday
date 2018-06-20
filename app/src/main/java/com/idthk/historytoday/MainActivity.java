package com.idthk.historytoday;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.idthk.historytoday.db.HistoryTodayContentDBTools;
import com.idthk.historytoday.utils.Consant;
import com.idthk.historytoday.utils.FileUtils;
import com.idthk.historytoday.utils.ImageManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks,
        EasyPermissions.RationaleCallbacks{

    private TextView text;
    private static final int RC_READ_PERM = 123;
    private static final int RC_WRITE_PERM = 124;

    private MediaPlayer mp = null;
    private String[] audios={"ABSE_02.mp3","AD.mp3","ABSE_02a.mp3"};
    private int playData=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        readTask();
        writeTask();
    }


    @AfterPermissionGranted(RC_WRITE_PERM)
    private void writeTask() {
        if (hasWritePermission()) {
            // Have permission, do the thing!
            //Toast.makeText(this, "TODO: Write things", Toast.LENGTH_LONG).show();
            Log.d("write","write");
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(
                    this,
                    "",
                    RC_WRITE_PERM,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }
    @AfterPermissionGranted(RC_READ_PERM)
    private void readTask() {
        if (hasReadPermission()) {
            // Have permission, do the thing!
            //Toast.makeText(this, "TODO: Read things", Toast.LENGTH_LONG).show();
            initData();
            playSDAudio();
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(
                    this,
                    "",
                    RC_READ_PERM,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

        private boolean hasWritePermission() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private boolean hasReadPermission() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    private void initView() {
        text = findViewById(R.id.tv_test);
    }

    private void initData() {
        String date=formatDate(new Date());
        HistoryTodayContentDBTools dbTools=new HistoryTodayContentDBTools();
        ArrayList<String> contents = dbTools.queryContentByDb(FileUtils.history_today_db_cn_file, date);
        if(contents!=null&&contents.size()>=1){
            text.setText(contents.get(0));
        }
    }

    private String formatDate(Date d){
        SimpleDateFormat format=new SimpleDateFormat(Consant.DATE_FORMAT, Locale.CHINA);
        String date = format.format(d);
        Log.d("333",date);
        String[] dates = date.split("-");
        String month="";
        String day="";
        if(dates.length>2){
            month = dates[1];
            day = dates[2];
            if(month.startsWith("0")){
                month=month.substring(1);
                Log.d("month",month);
            }
            if(day.startsWith("0")){
                day=day.substring(1);
                Log.d("day",day);
            }
        }
        date=month+Consant.MONTH+day+Consant.DAY;
        return date;
    }

    /**
     * 播放SD卡音频
     */
    private void playSDAudio(){
        try{
            if (mp != null) {
                mp.release();
                mp = null;
            }
            mp = new MediaPlayer();
            mp.setDataSource(FileUtils.audio_file_es_path+audios[0].trim());
            Log.d("playData",playData+"");
            mp.prepare();//准备播放
            mp.start();//播放

            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    ++playData;
                    if (playData < 3) {
                        try {
                            mp.reset();
                            mp.setDataSource(FileUtils.audio_file_es_path + audios[playData].trim());
                            Log.d("playData",playData+"---");
                            mp.prepare();//准备播放
                            mp.start();//播放
                            //图片
                            if (new File(FileUtils.filePath + ""+ Consant.PHOTO_STYLE).exists()) {
                                Bitmap bitmap = ImageManager.getSmallBitmap(MainActivity.this, FileUtils.filePath + "" + Consant.PHOTO_STYLE);
                                BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
                                //viewHolder.eventLineBg.setBackground(bitmapDrawable);
                                //显示本地图片

                            }
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        playData = 0;
                        if (mp != null) {
                            mp.release();
                            mp = null;
                        }
                    }
                }
            });
        }catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        Log.d("111","111");
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        initData();
        playSDAudio();
        Log.d("222","222");
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onRationaleAccepted(int requestCode) {
    }

    @Override
    public void onRationaleDenied(int requestCode) {
    }
}
