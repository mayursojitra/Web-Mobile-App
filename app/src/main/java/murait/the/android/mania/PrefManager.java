package murait.the.android.mania;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    public static SharedPreferences pref;
    public SharedPreferences.Editor editor;
    public final static int ADS_SHOW_TIME = 4;

    PrefManager(Activity mContext) {
        if (pref == null) {
            pref = mContext.getSharedPreferences("WebView", Context.MODE_PRIVATE);
        }
    }

    public void addVar() {
        if (pref != null) {
            int var = pref.getInt("XD", 0);
            editor = pref.edit();
            editor.putInt("XD", ++var);
            editor.commit();
        }
    }

    public int getVar() {
        int var = 0;
        if (pref != null) {
            var = pref.getInt("XD", 0);
        }
        return var;
    }
}
