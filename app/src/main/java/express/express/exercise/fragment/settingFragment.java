package express.express.exercise.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.workout.exercise.R;

import express.express.exercise.Const;
import express.express.exercise.alarm.Models.AlarmDetails;
import express.express.exercise.alarm.Services.AlertReceiver;

import express.express.exercise.util.adMobManager;
import express.express.exercise.util.utilhelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

public class settingFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {

    RadioGroup difficultyGrp;
    Switch soundSwitch, reminderSwitch;
    View view;
    LinearLayout reminderContainer;
    adMobManager ad = new adMobManager();
    private AdView adView;
    TextView mActionSign;
    SharedPreferences sp;
    SharedPreferences.Editor e;
    private TimePicker timePicker;
    Switch sound_switch, reminder_switch;
    private int hours, minutes, pos, count = 0;
    private ArrayList<AlarmDetails> Items;
    private CheckBox Sun, Sat, Mon, Tue, Wed, Thurs, Fri;
    private int[] checked = new int[7];

    public settingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.settings, container, false);
        initializeView();

        loadData();

        setViews();
        adView = (AdView) view.findViewById(R.id.adView);
        mActionSign = view.findViewById(R.id.action_ok);
        if (getString(R.string.ads_visibility).equals("yes")) {
            ad.LoadBannerAdd(getActivity(), view);
            ad.LoadInterstitialAd(getActivity());
        } else {
            adView.setVisibility(View.GONE);
        }


        setActionbar();
        return view;
    }

    private void initializeView() {
        difficultyGrp = view.findViewById(R.id.difficulty);
        soundSwitch = view.findViewById(R.id.sound_switch);
        reminderSwitch = view.findViewById(R.id.reminder_switch);
        reminderContainer = view.findViewById(R.id.reminderContainer);

        timePicker = view.findViewById(R.id.timePicker);

        Sun = view.findViewById(R.id.checkSun);
        Sat = view.findViewById(R.id.checkSat);
        Mon = view.findViewById(R.id.checkMon);
        Tue = view.findViewById(R.id.checkTue);
        Wed = view.findViewById(R.id.checkWed);
        Thurs = view.findViewById(R.id.checkThurs);
        Fri = view.findViewById(R.id.checkFri);

        sound_switch = view.findViewById(R.id.sound_switch);
        reminder_switch = view.findViewById(R.id.reminder_switch);




        sound_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Const.saveValueBoolean(getActivity(),utilhelper.SOUND, true );

                } else {
                    Const.saveValueBoolean(getActivity(),utilhelper.SOUND, false );

                }
            }
        });
        reminder_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    setReminderSetting(view.VISIBLE, true);
                    Const.saveValueBoolean(getActivity(),utilhelper.REMINDER, true );
                } else {
                    setReminderSetting(view.INVISIBLE, false);
                    Const.saveValueBoolean(getActivity(),utilhelper.REMINDER, false );
                    removeReminder();
                }
            }
        });


        for (int i = 0; i < 7; i++) {
            checked[i] = 1;
        }
        mActionSign = view.findViewById(R.id.action_ok);
        mActionSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Sun.isChecked()) {
                    checked[0] = 0;
                } else {
                    checked[0] = 1;
                }


                if (!Mon.isChecked()) {
                    checked[1] = 0;
                } else {
                    checked[1] = 1;
                }

                if (!Tue.isChecked()) {
                    checked[2] = 0;
                } else {
                    checked[2] = 1;
                }

                if (!Wed.isChecked()) {
                    checked[3] = 0;
                } else {
                    checked[3] = 1;
                }

                if (!Thurs.isChecked()) {
                    checked[4] = 0;
                } else {
                    checked[4] = 1;
                }

                if (!Fri.isChecked()) {
                    checked[5] = 0;
                } else {
                    checked[5] = 1;
                }
                if (!Sat.isChecked()) {
                    checked[6] = 0;
                } else {
                    checked[6] = 1;
                }

                hours = timePicker.getCurrentHour();
                minutes = timePicker.getCurrentMinute();
                count = 0;
                for (int i = 0; i < 7; i++) {
                    if (checked[i] == 0) {
                        count += 1;
                    }
                }

                if (count == 7) {
                    Toast.makeText(getContext(), "Choose a day !", Toast.LENGTH_SHORT).show();
                    count = 0;
                } else {
                    sendInput(hours, minutes, checked, pos);
                }
            }
        });


    }

    public void sendInput(int mHours, int mMinutes, int[] mChecked, int tone) {


        int[] reqCode = new int[7];

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlertReceiver.class);
        intent.putExtra("Value", tone);

        Calendar now = Calendar.getInstance();
        now.set(Calendar.SECOND, 0);
        now.set(Calendar.MILLISECOND, 0);

        for (int i = 0; i < 7; i++) {
            if (mChecked[i] == 1) {

                Calendar c = Calendar.getInstance();
                c.set(Calendar.DAY_OF_WEEK, i + 1);
                c.set(Calendar.HOUR_OF_DAY, mHours);
                c.set(Calendar.MINUTE, mMinutes);
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MILLISECOND, 0);

                if (c.before(now)) {
                    c.add(Calendar.DATE, 7);
                }

                reqCode[i] = (int) System.currentTimeMillis();

                PendingIntent pendingIntent;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    pendingIntent = PendingIntent.getBroadcast(getContext(), reqCode[i], intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                } else {
                    pendingIntent = PendingIntent.getBroadcast(getContext(), reqCode[i], intent, PendingIntent.FLAG_UPDATE_CURRENT);
                }

                alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
            } else {
                reqCode[i] = 0;
            }
        }

        if (mMinutes < 10) {
            Items.add(new AlarmDetails(mHours + ":0" + mMinutes, reqCode, mHours, mMinutes, tone, 3));
        } else {
            Items.add(new AlarmDetails(mHours + ":" + mMinutes, reqCode, mHours, mMinutes, tone, 3));
        }

        Toast toast = Toast.makeText(getContext(), "Alarm has been set successfully !", Toast.LENGTH_SHORT);
        toast.show();

    }


    private void saveData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(Items);
        editor.putString("task list", json);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("shared preferences", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<AlarmDetails>>() {
        }.getType();
        Items = gson.fromJson(json, type);
        if (Items == null) {
            Items = new ArrayList<>();
        }
    }

    private void setActionbar() {
        utilhelper.setActionbar(R.drawable.settings, "settings", this.view, getActivity(), ad);
    }

    private void setViews() {
        sp = getActivity().getSharedPreferences(utilhelper.FILENAME, Context.MODE_PRIVATE);
        setDifficulty(-1, 0);
        soundSwitch.setChecked(Const.getPreferenceBoolean(getContext(), utilhelper.SOUND, true));

        Boolean reminder = Const.getPreferenceBoolean(getContext(), utilhelper.REMINDER, false);
        setReminderSetting(-1, reminder);
    }

    private void setDifficulty(int pos, int exeTime) {
        if (pos == -1) {
            int difficulty = sp.getInt(utilhelper.DIFFICULTY, 1);
            RadioButton rb = (RadioButton) difficultyGrp.getChildAt(difficulty);
            rb.setChecked(true);
        } else {
            e.putInt(utilhelper.DIFFICULTY, pos);
            e.putInt(utilhelper.EXETIME, exeTime);
            e.commit();
        }
    }

    private void setReminderSetting(int visibility, boolean b) {
        if (visibility == -1) {
            if (b) {
                reminderContainer.setVisibility(View.VISIBLE);
            } else {
                reminderContainer.setVisibility(View.INVISIBLE);
            }

            reminderSwitch.setChecked(b);
        } else {
            reminderContainer.setVisibility(visibility);
        }
    }


    private void removeReminder() {
//            for (int i = 0; i < weekDays.getChildCount(); i++) {
//                CheckBox day = (CheckBox) weekDays.getChildAt(i);
//                day.setChecked(false);
//            }
//            Intent intent = new Intent(context, ReminderBroadcast.class);
//            if (set != null) {
//                for (Iterator<String> it = set.iterator(); it.hasNext(); ) {
//                    String f = it.next();
//                    PendingIntent pd = PendingIntent.getBroadcast(context, Integer.parseInt(f), intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                    AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//                    manager.cancel(pd);
//                    Toast.makeText(context, "Reminder remove", Toast.LENGTH_SHORT).show();
//
//                }
//                set.clear();
//            }
//            e.remove(utilhelper.REMINDER);
//            e.remove("days");
//            e.commit();
    }


    //for radioGroup
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        int pos, exeTime;
        if (radioGroup.getCheckedRadioButtonId() == R.id.one) {
            pos = 0;
            exeTime = 20;
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.two) {
            pos = 1;
            exeTime = 30;
        } else {
            pos = 2;
            exeTime = 40;
        }
        setDifficulty(pos, exeTime);
    }

    @Override
    public void onStop() {
        super.onStop();
        saveData();
    }


}
