package express.express.exercise;

import android.content.Context;
import android.content.SharedPreferences;


public class Const {


    private static String preferenceName = "workout";


    public static void saveValueBoolean(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getPreferenceBoolean(Context context, String key, boolean b) {
        return context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE).getBoolean(key, b);
    }

}
