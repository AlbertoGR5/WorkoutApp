package express.express.exercise.fragment;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.workout.exercise.R;

public class fragmentLaunchActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_launch);

        context = this;
        Intent i = getIntent();
        Fragment f = null;
        String activityName = i.getStringExtra("TAG");
        if (activityName.equals("exercise")) {
            f = new exerciseListFragment();
        } else if (activityName.equals("bmi")) {
            f = new bmiFragment();
        } else if (activityName.equals("settings")) {

        } else if (activityName.equals("myExercise")) {
            f = new myExerciseFragment();
        }
        getSupportFragmentManager().beginTransaction().add(R.id.frm_layout, f, "list").commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}


