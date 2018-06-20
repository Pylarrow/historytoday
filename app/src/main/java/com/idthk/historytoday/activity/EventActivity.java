package com.idthk.historytoday.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;
import android.widget.LinearLayout;

import com.idthk.historytoday.R;
import com.idthk.historytoday.utils.Consant;
import com.idthk.historytoday.utils.ImageManager;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventActivity extends BaseActivity {
    private ViewHolder viewHolder;
    private String[] photos;
    private String[] audios;
    private int playData = 0;
    //    private String country = "";
    private String title = "";
    private MediaPlayer mp = null;
    private int photoOrAudioLength;

    private String language="";

    @Override
    public void onStop() {
        super.onStop();
        stopMediaPlay();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //StopAudioListener.removeOnToDeviceListener(stopAudioListener);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_event_layout;
    }

    /**
     * 获取本地语言环境
     */
    private void getLocalLanguage() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = getResources().getConfiguration().locale;
        }

        //获取语言的正确姿势
        String lang = locale.getLanguage() + "-" + locale.getCountry();
        if(lang.contains("en")){
            language="english";
        }else if(lang.contains("zh")){
            language="cn";
        }
    }

    @Override
    protected void initView() {

        viewHolder = new ViewHolder(this);
        //StopAudioListener.addOnToDeviceListener(stopAudioListener);
        //CustomApplication.getContext().addActivity(this);
        Intent intent = getIntent();
        if (intent != null) {
            photos = intent.getStringArrayExtra("earth_photo");
            audios = intent.getStringArrayExtra("earth_audio");
            title = intent.getStringExtra("earth_title");
        }

    }

    @Override
    protected void initData() {
        //viewHolder.countryText.setText("");

        photoOrAudioLength = Math.min(audios.length, photos.length);
        if (photoOrAudioLength > 0) {
            //播放SD卡音频
            //playsSdCardAudio(photos, FileUtils.photoFileData_es, audios, FileUtils.audioFileData_es);
//            if (FixConstant.getLanguage(this).equals(FixConstant.CHINESE)) {//选择中文语言时
//                playsSdCardAudio(photos, FileUtils.photoFileData_cn, audios, FileUtils.audioFileData_cn);
//            } else if (FixConstant.getLanguage(this).equals(FixConstant.ENGLISH)) {//选择英文语言时
//                playsSdCardAudio(photos, FileUtils.photoFileData_es, audios, FileUtils.audioFileData_es);
//            } else if (FixConstant.getLanguage(this).equals(FixConstant.JAPANESE)) {//选择英文语言时
//                playsSdCardAudio(photos, FileUtils.photoFileData_jp, audios, FileUtils.audioFileData_jp)R;
//            } else {//选择别的语言时
//
//            }
        }

    }

    public void playsSdCardAudio(final String[] photos, final String photoPath, final String[] audios, final String audioPath) {
//        for (String s : audios) {
//            Log.i("playsSdCardAudio1", s + "::" + audioPath);
//        }

        try {
            if (mp != null) {
                mp.release();
                mp = null;
            }
            mp = new MediaPlayer();
            playData = 0;
            mp.setDataSource(audioPath + audios[playData].trim() + Consant.AUDIO_STYLE);
            mp.prepare();//准备播放
            mp.start();//播放
            Log.i("playsSdCardAudio: ", audioPath + audios[playData].trim() + Consant.AUDIO_STYLE);
            Log.i("playsSdCardAudio:11 ", photoPath + photos[playData].trim() + Consant.PHOTO_STYLE);
            //图片
            if (new File(photoPath + photos[playData].trim() + Consant.AUDIO_STYLE).exists()) {
                Bitmap bitmap = ImageManager.getSmallBitmap(this, photoPath + photos[playData].trim() + Consant.PHOTO_STYLE);
                BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
                //viewHolder.eventLineBg.setBackground(bitmapDrawable);
            }

            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    ++playData;
                    if (playData < photoOrAudioLength) {
                        try {
                            mp.reset();
                            mp.setDataSource(audioPath + audios[playData].trim() + Consant.AUDIO_STYLE);
                            mp.prepare();//准备播放
                            mp.start();//播放
                            //图片
                            if (new File(photoPath + photos[playData].trim() + Consant.PHOTO_STYLE).exists()) {
                                Bitmap bitmap = ImageManager.getSmallBitmap(EventActivity.this, photoPath + photos[playData].trim() + Consant.PHOTO_STYLE);
                                BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
                                //viewHolder.eventLineBg.setBackground(bitmapDrawable);
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
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopMediaPlay() {
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }

//    private StopAudioListener.onStopAudioListner stopAudioListener = new StopAudioListener.onStopAudioListner() {
//        @Override
//        public void toStopAudio(boolean flag) {
//            stopMediaPlay();
//        }
//    };

    static class ViewHolder {

        @BindView(R.id.ll_img_bg)
        LinearLayout ll_img_bg;

        ViewHolder(Activity view) {
            ButterKnife.bind(this, view);
        }
    }
}
