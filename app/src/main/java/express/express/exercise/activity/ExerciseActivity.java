package express.express.exercise.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;

import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import com.workout.exercise.R;
import express.express.exercise.fragment.fragmentLaunchActivity;
import express.express.exercise.sqlite.MyDatabase;
import express.express.exercise.util.adMobManager;
import express.express.exercise.util.utilhelper;
import com.google.android.gms.ads.InterstitialAd;

import java.io.IOException;
import java.io.InputStream;

public class ExerciseActivity extends AppCompatActivity {

    LinearLayout end_btns;
    FrameLayout progress_container;

    InterstitialAd mInterstitialAd;

    ImageView actionhideicon, backbtn, img, restimg;
    TextView actiontitle, rest_title, rest_exe_name, counter_text;
    Button bottom_btn;
    ProgressBar pd, mainpd;

    CountDownTimer ct;
    private Context context;
    Runnable r, r1;
    Handler handler;
    MediaPlayer mp = new MediaPlayer();
    String[] exeList, imgList;
    int i = 0;
    int count = 1;
    int restTime = 10000;
    int exersiceTime;
    int restdelay, exercisedelay;
    Boolean isSound;
    private MyDatabase myDatabase;

    public adMobManager ad = new adMobManager();
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        context = this;
        intializeViews();
        intializeVariables();
        setActionbar("no");
        startThread(restdelay, exercisedelay);

        adView = (AdView) findViewById(R.id.adView);
        if (getString(R.string.ads_visibility).equals("yes")) {
            ad.LoadInterstitialAd(ExerciseActivity.this);
        } else {
            adView.setVisibility(View.GONE);
        }

    }

    private void setActionbar(String title) {
        if (!title.equals("no")) {
            actiontitle.setText(title);
        }
        actionhideicon.getLayoutParams().width = 1;
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (end_btns.getVisibility()==View.VISIBLE){
                    loadShowAdd("Ins");
                }else {
                    ShowExitDialog();
                }
            }
        });
    }

    private void intializeViews() {
        mainpd = findViewById(R.id.seek_progress_count);
        pd = findViewById(R.id.time_progress);

        end_btns = findViewById(R.id.end_btn);
        backbtn = findViewById(R.id.back_btn);
        bottom_btn = findViewById(R.id.bottom_btn);

        progress_container = findViewById(R.id.progress_container);

        actionhideicon = findViewById(R.id.action_img);
        restimg = findViewById(R.id.complete_img);
        img = findViewById(R.id.ex_img);

        actiontitle = findViewById(R.id.action_title);
        counter_text = findViewById(R.id.time_count_txt);

        rest_title = findViewById(R.id.txt_msg);
        rest_exe_name = findViewById(R.id.long_txt);

        progress_container.setVisibility(View.VISIBLE);
        rest_exe_name.setVisibility(View.GONE);

        loadShowAdd("Banner");

    }

    private void loadShowAdd(String type) {
        adMobManager adm = new adMobManager();

        if (type.equals("Banner")) {

            adm.LoadBannerAdd(ExerciseActivity.this, null);
        } else if (type.equals("Ins")) {

            ad.showInterstitialAd(ExerciseActivity.this);
        } else {
            ad.adView.getLayoutParams().height = 0;
            ad.adView.setVisibility(View.GONE);
        }
    }

    private void intializeVariables() {
        exeList = context.getResources().getStringArray(R.array.exe_names);
        imgList = context.getResources().getStringArray(R.array.images);

        SharedPreferences sp = getSharedPreferences(utilhelper.FILENAME, MODE_PRIVATE);
        exersiceTime = 1000 * sp.getInt(utilhelper.EXETIME, 30);
        isSound = sp.getBoolean(utilhelper.SOUND, true);

        restdelay = restTime;
        exercisedelay = exersiceTime;

        mainpd.setMax(exeList.length * 2);
    }

    public void startThread(final int restdelay, final int exercisedelay) {
        handler = new Handler();
        r = new Runnable() {
            @Override
            public void run() {
                restTime(i);
                r1 = new Runnable() {
                    @Override
                    public void run() {
                        startExercise(i);
                        Log.e("tag", String.valueOf(i));
                        if (i >= exeList.length - 1) {
                            handler.removeCallbacks(r);
                            handler.removeCallbacks(r1);


                            exerciseComplete();
                        } else {
                            i++;
                            handler.postDelayed(r, exercisedelay);
                        }
                    }
                };
                handler.postDelayed(r1, restdelay);
            }
        };
        handler.post(r);
    }

    private void restTime(int i) {
        if (i > exeList.length - 1) {
            handler.removeCallbacks(r);
            handler.removeCallbacks(r1);
            exerciseComplete();
        } else {
            img.setVisibility(View.GONE);
            rest_title.setVisibility(View.VISIBLE);
            pd.setProgress(0);
            pd.setMax(restTime);
            setProgressBar(10);
            String title = null;
            String exe = null;
            String imgname = null;
            if (i == 0) {
                title = exeList[0];
                bottom_btn.setVisibility(View.INVISIBLE);
            } else {
                title = "Rest Time";
                bottom_btn.setVisibility(View.VISIBLE);
                bottom_btn.setText("SKIP");
            }
            exe = exeList[i];
            imgname = imgList[i];
            setActionbar(title);
            rest_title.setText("GET READY FOR" + "\n" + exe.toUpperCase());
            Bitmap bitmap = getBitmap(imgname);
            restimg.setVisibility(View.VISIBLE);
            restimg.setImageBitmap(bitmap);
        }
    }

    private void startExercise(int i) {
        if (i > exeList.length - 1) {
            handler.removeCallbacks(r);
            handler.removeCallbacks(r1);
            exerciseComplete();
        } else {
            restimg.setVisibility(View.GONE);
            rest_title.setVisibility(View.GONE);
            img.setVisibility(View.VISIBLE);

            pd.setProgress(0);
            pd.setMax(exersiceTime);
            setProgressBar(exersiceTime / 1000);

            String title = null;
            String imgname = null;

            title = exeList[i];
            setActionbar(title);

            imgname = imgList[i];
            Bitmap bitmap = getBitmap(imgname);
            img.setImageBitmap(bitmap);

            bottom_btn.setVisibility(View.VISIBLE);
            bottom_btn.setText("DONE");
        }
    }

    private void setProgressBar(final int totalSeconds) {

        if (totalSeconds == 10 && i != 0 && isSound == true) {
            if (mp.isPlaying())
                mp.release();
            playMusic(R.raw.stop);
        } else if ((totalSeconds == 30 || totalSeconds == 40 || totalSeconds == 20) && isSound == true) {
            if (mp.isPlaying())
                mp.release();
            playMusic(R.raw.start);
        }
        //final long totalSeconds = 10;
        ct = new CountDownTimer(totalSeconds * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                counter_text.setText(String.valueOf((int) ((millisUntilFinished) / 1000)));
            }

            public void onFinish() {
                if (i > exeList.length - 1) {
                    handler.removeCallbacks(r);
                    handler.removeCallbacks(r1);
                    exerciseComplete();
                } else {
//                  counter_text.setText(String.valueOf(1));
                    pd.setProgress(0);
                    mainpd.setProgress(count++);
                }
            }
        };
        ct.start();
        //animation for smooth progress bar
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            // will update the "progress" propriety of seekbar until it reaches progress
            ObjectAnimator animation = ObjectAnimator.ofInt(pd, "progress", 1000 * totalSeconds, 0);
            animation.setDuration(totalSeconds * 1000); // 0.5 second
            animation.start();
        }
    }

    public void playMusic(int id) {
        mp = MediaPlayer.create(this, id);
        mp.start();
    }

    private Bitmap getBitmap(String imgname) {
        AssetManager manager = context.getAssets();
        InputStream open = null;
        try {
            open = manager.open(imgname);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(open);
        return bitmap;
    }

    public void bottom_btn_click(View view) {
        ct.cancel();
        if (bottom_btn.getText().equals("DONE")) {
            mainpd.setProgress(count++);
            handler.removeCallbacks(r);
            handler.removeCallbacks(r1);
            startThread(restTime, exersiceTime);
        } else if (bottom_btn.getText().equals("SKIP")) {
            mainpd.setProgress(count++);
            handler.removeCallbacks(r);
            handler.removeCallbacks(r1);
            handler = new Handler();
            r = new Runnable() {
                @Override
                public void run() {
                    startExercise(i);
                    i++;
                    r1 = new Runnable() {
                        @Override
                        public void run() {
                            startThread(restTime, exersiceTime);
                        }
                    };
                    handler.postDelayed(r1, exersiceTime);
                }
            };
            handler.post(r);
        } else if (bottom_btn.getText().equals("MENU")) {
            finish();
            Intent i = new Intent(context, MainActivity.class);
            startActivity(i);
        }
    }

    public void restart(View view) {
        Intent i = new Intent(context, ExerciseActivity.class);
        startActivity(i);
        finish();
    }

    public void setReminder(View view) {
        Intent i = new Intent(context, fragmentLaunchActivity.class);
        i.putExtra("TAG", "settings");
        startActivity(i);
        finish();
    }

    private void exerciseComplete() {
        setActionbar("EXERCISE COMPLETE");
        mainpd.setVisibility(View.INVISIBLE);
        rest_title.setVisibility(View.VISIBLE);
        restimg.setVisibility(View.VISIBLE);
        rest_exe_name.setVisibility(View.VISIBLE);
        end_btns.setVisibility(View.VISIBLE);
        img.setVisibility(View.INVISIBLE);
        progress_container.setVisibility(View.INVISIBLE);

        rest_title.setTextSize(50);
        rest_title.setText("END");
        restimg.setImageResource(R.drawable.complete);

        rest_exe_name.setText("Congratulations! You have done the workout!");
        bottom_btn.setText("MENU");

//        loadShowAdd("Ins");
        addDataInToDatabase();
    }

    private void addDataInToDatabase() {
        myDatabase = new MyDatabase(context);
        Long result = myDatabase.insertData(String.valueOf(System.currentTimeMillis()));
    }

    @Override
    public void onBackPressed() {
        if (end_btns.getVisibility()==View.VISIBLE){
            loadShowAdd("Ins");
        }else {
            ShowExitDialog();
        }
    }

    private void ShowExitDialog() {
        final AlertDialog ab = new AlertDialog.Builder(context)
                .setTitle("Stop")
                .setMessage("Do you want to stop workout ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        handler.removeCallbacks(r);
                        handler.removeCallbacks(r1);
                        ct.cancel();
//                        utilhelper.onBackpress(mInterstitialAd, ExerciseActivity.this,-1);

                        ad.onbackPress(ExerciseActivity.this);


                        ExerciseActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();

        ab.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button b = ab.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setBackgroundResource(R.layout.radio_group_back);
                LinearLayout.LayoutParams p = (LinearLayout.LayoutParams) b.getLayoutParams();
                p.setMargins(10, 10, 10, 0);
                b.setTypeface(null, Typeface.BOLD);

                Button n = ab.getButton(AlertDialog.BUTTON_NEGATIVE);
                n.setBackgroundResource(R.layout.radio_group_back);
                LinearLayout.LayoutParams pr = (LinearLayout.LayoutParams) n.getLayoutParams();
                pr.setMargins(10, 10, 10, 0);
                n.setTypeface(null, Typeface.BOLD);

                TextView tv = ab.findViewById(androidx.appcompat.R.id.alertTitle);
                tv.setTypeface(ResourcesCompat.getFont(context, R.font.common), Typeface.BOLD);

                TextView tvv = ab.findViewById(android.R.id.message);
                tvv.setTypeface(ResourcesCompat.getFont(context, R.font.common));

            }
        });
        ab.show();

    }
}
