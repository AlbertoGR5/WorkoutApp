package express.express.exercise.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdView;
import com.workout.exercise.R;
import express.express.exercise.sqlite.MyDatabase;
import express.express.exercise.util.adMobManager;
import express.express.exercise.util.utilhelper;
import com.google.android.gms.ads.InterstitialAd;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;

public class myExerciseFragment extends Fragment {

    View view;
    MaterialCalendarView calender;
    private Context context;
    private InterstitialAd mInterstitialAd;
    adMobManager ad = new adMobManager();
    private AdView adView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_exercise, container, false);
        context = getContext();
        intializeViews();
        adView = (AdView) view.findViewById(R.id.adView);

        if (getString(R.string.ads_visibility).equals("yes")) {
            ad.LoadBannerAdd(getActivity(), view);
            ad.LoadInterstitialAd(getActivity());
        } else {
            adView.setVisibility(View.GONE);
        }

        setActionbar(R.drawable.history, "history", view, getActivity());
        return view;
    }

    private void intializeViews() {
        calender = view.findViewById(R.id.calendar);
        calender.setSelectedDate(CalendarDay.today());
        calender.setSelectionMode(MaterialCalendarView.SELECTION_MODE_SINGLE);
        MyDatabase mydb = new MyDatabase(context);
        final ArrayList<CalendarDay> dates = mydb.getDate();
        calender.addDecorator(new DayViewDecorator() {

            @Override
            public boolean shouldDecorate(CalendarDay day) {
                return dates.contains(day);
            }

            @Override
            public void decorate(DayViewFacade view) {
                view.addSpan(new DotSpan(5f, Color.RED));
            }
        });
    }

    private void setActionbar(int img, String title, View view, Activity activity) {
        utilhelper.setActionbar(img, title, view, activity, ad);
    }

}
