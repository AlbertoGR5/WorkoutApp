package express.express.exercise.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.workout.exercise.R;

public class Perfil extends AppCompatActivity {

    CardView cardView1, cardView2, cardView3
    private Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        context = this;
        intializeViews();
        setActionbar("no");
    }

    private void intializeViews() {

    }
}
