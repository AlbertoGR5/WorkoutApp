package express.express.exercise.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.workout.exercise.R;

/**
 * Created by appsb on 17-07-2018.
 */

public class utilhelper {


    public static String FILENAME="WorkoutFile";
    public static String SOUND="AppSound";
    public static String DIFFICULTY="ExerciseLevel";
    public static String EXETIME="ExerciseTime";
    public static String REMINDER="ReminderSetting";

    public static void setActionbar(int img, String title, View view, final Activity activity, final adMobManager adm) {
        ImageView actionhideicon, backbtn;
        TextView actiontitle;

        actionhideicon = view.findViewById(R.id.action_img);
        actiontitle = view.findViewById(R.id.action_title);

        backbtn = view.findViewById(R.id.back_btn);
        if (img != -1) {
            actionhideicon.setImageResource(img);
        } else {
            actionhideicon.getLayoutParams().width = 1;
        }

        if (!title.equals("no"))
        {actiontitle.setText(title);}


        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               activity.onBackPressed();
            }
        });
    }

    public static void addAdCounter(Activity activity,int i)
    {
        SharedPreferences sp=activity.getSharedPreferences(utilhelper.FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor e=sp.edit();
        e.putInt("counter",i);
        e.commit();
    }


    public static int getAdCounter(Activity activity)
    {
        SharedPreferences sp=activity.getSharedPreferences(utilhelper.FILENAME, Context.MODE_PRIVATE);
        return sp.getInt("counter",0);
    }


}
