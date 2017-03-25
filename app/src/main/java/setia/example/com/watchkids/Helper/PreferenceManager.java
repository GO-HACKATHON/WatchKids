package setia.example.com.watchkids.Helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by My Computer on 3/25/2017.
 */

public class PreferenceManager {
    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;

    private static Context contextManager;
    private final static String KEY_ID = "id";
    private final static String KEY_NAMA = "nama";
    private final static String KEY_ROLE = "role";

    public static void loadManager(Context context){
        contextManager = context;
        pref = android.preference.PreferenceManager.getDefaultSharedPreferences(contextManager);
    }

    public static String getId() {
        return pref.getString(KEY_ID, "-");
    }

    public static void setId(String id) {
        editor = pref.edit();
        editor.putString(KEY_ID, id);
        editor.commit();
    }

    public static String getNama() {
        return pref.getString(KEY_NAMA, "-");
    }

    public static void setNama(String nama) {
        editor = pref.edit();
        editor.putString(KEY_NAMA, nama);
        editor.commit();
    }

    public static String getRole() {
        return pref.getString(KEY_ROLE, "-");
    }

    public static void setRole(String role) {
        editor = pref.edit();
        editor.putString(KEY_ROLE, role);
        editor.commit();
    }
}
