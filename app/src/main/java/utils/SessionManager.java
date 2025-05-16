package utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "SchoolManagerPref";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_ROLE = "role";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(String username, String role) {
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_ROLE, role);
        editor.apply();
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }

    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, null);
    }

    public String getRole() {
        return sharedPreferences.getString(KEY_ROLE, null);
    }


    public boolean isLoggedIn() {
        if (sharedPreferences.getString(KEY_USERNAME, null) != null) {
            return sharedPreferences.contains(KEY_USERNAME) && sharedPreferences.contains(KEY_ROLE);
        } else {
            return false;
        }
    }


}
