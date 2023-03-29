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

import com.google.android.gms.ads.AdView;
import com.workout.exercise.R;

import express.express.exercise.util.adMobManager;
import express.express.exercise.util.utilhelper;

import com.google.android.gms.ads.InterstitialAd;

import java.text.NumberFormat;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class bmiFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    View view;
    TextView lbl_weight, lbl_height, bmi_result, bmi_result_detail;
    EditText weight, height_cm_feet, height_inch;
    RadioGroup rUnits;
    RadioButton metric, us;
    Button calculate;
    private Context context;
    private InterstitialAd mInterstitialAd;

    adMobManager ad = new adMobManager();
    private AdView adView;

    public bmiFragment() {
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.bmi_calculator, container, false);


        intializeViews();
        adView = (AdView) view.findViewById(R.id.adView);

        if (getString(R.string.ads_visibility).equals("yes")) {
            ad.LoadBannerAdd(getActivity(), view);
            ad.LoadInterstitialAd(getActivity());
        } else {
            adView.setVisibility(View.GONE);
        }

        setActionbar();
        return view;
    }

    private void setActionbar() {
        utilhelper.setActionbar(R.drawable.bmi, "Calculate bmi", view, getActivity(), ad);
    }

    private void intializeViews() {
        lbl_weight = view.findViewById(R.id.lbl_weight);
        lbl_height = view.findViewById(R.id.lbl_height);
        bmi_result = view.findViewById(R.id.bmi_result);
        bmi_result_detail = view.findViewById(R.id.result_details);
        weight = view.findViewById(R.id.bmi_weight);
        height_cm_feet = view.findViewById(R.id.bmi_height_cm);
        height_inch = view.findViewById(R.id.bmi_height_inch);
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
            lbl_weight.setText("WEIGHT(in kg)");
            lbl_height.setText("HEIGHT(in cm)");
            height_cm_feet.setHint("");
            height_inch.setVisibility(View.GONE);
            height_inch.setText("0");
        } else if (radioGroup.getCheckedRadioButtonId() == us.getId()) {
            lbl_weight.setText("WEIGHT(in kg)");
            lbl_height.setText("HEIGHT(in cm)");
            height_cm_feet.setHint("Feet");
            height_inch.setVisibility(View.VISIBLE);
            height_inch.setText("");
            height_inch.setHint("Inch");
        }
        weight.setText("");
        height_cm_feet.setText("");
        bmi_result.setText("");
        bmi_result_detail.setText("");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {

        if (!weight.getText().toString().isEmpty() && !height_cm_feet.getText().toString().isEmpty() && !height_inch.getText().toString().isEmpty()) {
            double w = Double.valueOf(weight.getText().toString());
            double h = Double.valueOf(height_cm_feet.getText().toString());
            if (rUnits.getCheckedRadioButtonId() == metric.getId()) {
                w *= 10000;
            } else if (rUnits.getCheckedRadioButtonId() == us.getId()) {
                w *= 703;
                h = (h * 12) + Double.valueOf(height_inch.getText().toString());
            }
            Double bmi = w / (h * h);
            String result[] = getBmiState(bmi);
            bmi = Double.valueOf(String.format("%.2f", bmi).replace(",", "."));
            String s = String.valueOf(bmi);
            Double.parseDouble(s.replace(',', '.'));
            bmi_result.setText("Your BMI" + "\n" + s + "\n" + result[0]);
            bmi_result_detail.setText(result[1]);
        } else {
            Toast.makeText(context, "Invalid Values", Toast.LENGTH_SHORT).show();
        }
    }

    private String[] getBmiState(Double bmi) {
        String res[] = new String[2];
        if (bmi <= 18.5) {
            res[0] = "Under Weight";
            res[1] = "Oops! You really need to take care of yours better! Eat more!";
        } else if (bmi > 18.5 && bmi <= 25.0) {
            res[0] = "Normal";
            res[1] = "Congratulations! You are in a good shape!";
        } else if (bmi > 25 && bmi <= 30) {
            res[0] = "Over Weight";
            res[1] = "Oops! You really need to take care of yourself! Workout maybe!";
        } else if (bmi > 30 && bmi <= 35) {
            res[0] = "Obese";
            res[1] = "Oops! You really need to take care of yourself! Workout maybe!";
        } else if (bmi > 35 && bmi <= 40) {
            res[0] = "Severely Obese";
            res[1] = "OMG! You are in a very dangerous condition! Act now!";
        } else {
            res[0] = "Very Severely Obese";
            res[1] = "OMG! You are in a very dangerous condition! Act now!";
        }
        return res;
    }
}
