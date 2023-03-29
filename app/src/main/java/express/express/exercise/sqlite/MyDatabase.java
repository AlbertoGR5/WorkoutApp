package express.express.exercise.sqlite;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by appsb on 13-07-2018.
 */
public class MyDatabase extends SQLiteAssetHelper {


    private static final String DB_NAME="workout_db.db";
    private static final int DATABASE_VERSION=1;
    SQLiteDatabase db;
    private String DATABASE_TABLE="table_workout";
    private String F2="COL_DATE";
    private String F1="id";

    public MyDatabase(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    public long insertData(String date) {
        db=getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(F2, date);
        return db.insert(DATABASE_TABLE, null, cv);
    }


    public ArrayList<CalendarDay> getDate()
    {
        SQLiteDatabase db=getWritableDatabase();
        String[] columns={F1,F2};
        Cursor cursor=db.query(DATABASE_TABLE, columns, null, null, null, null, null);

        ArrayList dates=new ArrayList<CalendarDay>();

        while(cursor.moveToNext()){
            Calendar c=Calendar.getInstance();
            c.clear();
            c.setTimeInMillis(Long.parseLong(cursor.getString(cursor.getColumnIndex(F2))));
            dates.add(CalendarDay.from(c));
        }
        return dates;
    }


}