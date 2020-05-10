package bensalcie.likesyou.org.covi_19tracker.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class MyPreferenceClass {
    private static MyPreferenceClass myPreferenceClass;
    private SharedPreferences sharedPreferences;
    public static MyPreferenceClass getInstance(Context context)
    {
        if (myPreferenceClass==null)
        {
            myPreferenceClass=new MyPreferenceClass(context);
        }
        return myPreferenceClass;
    }
    public MyPreferenceClass(Context context)
    {
        sharedPreferences=context.getSharedPreferences("MyPreferences",Context.MODE_PRIVATE);

    }
    public void saveData(String key,String value)

    {
        SharedPreferences.Editor prefsEditor=sharedPreferences.edit();
        prefsEditor.putString(key,value);
        prefsEditor.commit();
    }
    public String getData(String key)
    {
        if (sharedPreferences!=null)
        {
            return sharedPreferences.getString(key,"");

        }
        return "";
    }
}
