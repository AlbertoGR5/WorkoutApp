package express.express.exercise.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.workout.exercise.R;
import express.express.exercise.fragment.fragmentLaunchActivity;
import express.express.exercise.util.utilhelper;

public class MainActivity extends AppCompatActivity {
    String appPackageName;
    ImageView backbtn;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appPackageName = getPackageName();

        intializeViews();

        context = this;

        setActionbar("no");
    }
    private void setActionbar(String title) {
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void intializeViews() {
        backbtn = findViewById(R.id.back_btn);
    }

    public void startExercise(View view) {
        startActivity(new Intent(this, ExerciseActivity.class));
    }

    public void myExercise(View view) {
        Intent i = new Intent(context, fragmentLaunchActivity.class);
        i.putExtra("TAG", "myExercise");
        startActivity(i);
    }

    public void exerciseOnClick(View view) {
        Intent i = new Intent(context, fragmentLaunchActivity.class);
        i.putExtra("TAG", "exercise");
        startActivity(i);
    }

    public void calculatorOnClick(View view) {
        Intent i = new Intent(context, fragmentLaunchActivity.class);
        i.putExtra("TAG", "bmi");
        startActivity(i);
    }

    public void settingOnClick(View view) {
        Intent i = new Intent(context, fragmentLaunchActivity.class);
        i.putExtra("TAG", "settings");
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(context)
                .setTitle("Exit")
                .setMessage("Quieres salir de la aplicación ?")
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Salir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finishAffinity();
                    }
                })
                .show();


    }
}