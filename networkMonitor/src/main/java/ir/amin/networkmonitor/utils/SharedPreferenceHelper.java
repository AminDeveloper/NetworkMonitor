package ir.amin.networkmonitor.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by Amin on 5/17/2017.
 */

public class SharedPreferenceHelper {
    private static final String NOTIFICATION_COUNT = "notification_count";

    protected final Context context;
    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor editor;


    public SharedPreferenceHelper(Context context) {
        if (context == null)
            throw new NullPointerException("Attemp to create SharedPreferenceHelper with Null context");
        this.context = context;
        sharedPreferences = getSharedPreferences();
        editor = sharedPreferences.edit();
    }

    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE);
    }

    public void setNotificationCount(int count) {
        editor.putInt(NOTIFICATION_COUNT, count);
        editor.commit();
    }

    public int getNotificationCount() {
        return sharedPreferences.getInt(NOTIFICATION_COUNT, 0);
    }


}
