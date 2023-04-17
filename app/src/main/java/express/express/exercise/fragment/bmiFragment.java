package express.express.exercise.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.workout.exercise.R;
import express.express.exercise.util.utilhelper;
import java.text.NumberFormat;
import java.util.Locale;
import static android.content.ContentValues.TAG;

public class bmiFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    View view;
    TextView lbl_weight, lbl_height, bmi_result, bmi_result_detail, bmi_result_detail_exp;
    EditText weight, height_cm_feet, age;
    RadioGroup rUnits;
    RadioButton metric, us;
    Button calculate;
    private Context context;

    public bmiFragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bmi_calculator, container, false);
        intializeViews();
        setActionbar();
        return view;
    }

    private void setActionbar() {
        utilhelper.setActionbar(R.drawable.bmi, "Calcular", view, getActivity());
    }

    private void intializeViews() {
        lbl_weight = view.findViewById(R.id.lbl_weight);
        lbl_height = view.findViewById(R.id.lbl_height);
        bmi_result = view.findViewById(R.id.bmi_result);
        bmi_result_detail = view.findViewById(R.id.result_details);
        bmi_result_detail_exp = view.findViewById(R.id.result_details_exp);
        weight = view.findViewById(R.id.bmi_weight);
        height_cm_feet = view.findViewById(R.id.bmi_height_cm);
        age = view.findViewById(R.id.bmi_age);
        rUnits = view.findViewById(R.id.rg_units);
        metric = view.findViewById(R.id.rb_metric_unit);
        us = view.findViewById(R.id.rb_us_unit);
        calculate = view.findViewById(R.id.btn_cal);
        calculate.setOnClickListener(this);
        rUnits.setOnCheckedChangeListener(this);
        context = getContext();
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        if (radioGroup.getCheckedRadioButtonId() == metric.getId()) {

        } else if (radioGroup.getCheckedRadioButtonId() == us.getId()) {

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        if (!weight.getText().toString().isEmpty() && !height_cm_feet.getText().toString().isEmpty()) {
            double w = Double.valueOf(weight.getText().toString());
            double h = Double.valueOf(height_cm_feet.getText().toString());
            double a = Double.valueOf(age.getText().toString());
            double g = 0;
            h /= 100;
            if (rUnits.getCheckedRadioButtonId() == metric.getId()) {
                g = 1;
            } else if (rUnits.getCheckedRadioButtonId() == us.getId()) {
                g = 0;
            }
            Double bmi = w / (h * h);;
            Double gct = (1.20 * bmi) + (0.23 * a) - (10.8 * g) - 5.4;
            Double mlg = w - ((gct * w) / 100);
            bmi = Double.valueOf(String.format("%.2f", bmi).replace(",", "."));
            gct = Double.valueOf(String.format("%.1f", gct).replace(",", "."));
            mlg = Double.valueOf(String.format("%.1f", mlg).replace(",", "."));
            String s = String.valueOf(bmi);
            String t = String.valueOf(gct);
            String u = String.valueOf(mlg);
            Double.parseDouble(s.replace(',', '.'));
            Double.parseDouble(t.replace(',', '.'));
            Double.parseDouble(u.replace(',', '.'));
            bmi_result.setText("Tu IMC" + "\n" + s);
            bmi_result_detail.setText("TU GCT(%): " + "\n" + t + "\n" + "TU MLG(kg): " + "\n" + u);
            bmi_result_detail_exp.setText("GCT: Grasa Corporal Total" + "\n" + "MLG: Masa Libre de Grasa");
        } else {
            Toast.makeText(context, "Valores invalidos", Toast.LENGTH_SHORT).show();
        }
    }
}
