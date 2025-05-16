package database;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example.schoolmanager.LoginActivity;
import com.example.schoolmanager.SchoolActivity;

import utils.SessionManager;

public class LoginSQLite {
    private static final String LOGIN_QUERY = "SELECT * FROM users WHERE username = ? AND password = ?";
    private static volatile LoginSQLite instance;

    public static LoginSQLite getInstance() {
        if (instance == null) {
            synchronized (LoginSQLite.class) {
                if (instance == null) {
                    instance = new LoginSQLite();
                }
            }
        }
        return instance;
    }

    public void IntializeLogin(String username, String password, LoginActivity login) {
        SQLiteDatabaseHelper dbHelper = new SQLiteDatabaseHelper(login);

        try (SQLiteDatabase db = dbHelper.getReadableDatabase()) {
            try (Cursor cursor = db.rawQuery(LOGIN_QUERY, new String[]{username, password})) {
                if (cursor != null && cursor.moveToFirst()) {
                    String role = cursor.getString(cursor.getColumnIndexOrThrow("role"));
                    String usernameFromDB = cursor.getString(cursor.getColumnIndexOrThrow("username"));

                    SessionManager sessionManager = new SessionManager(login);
                    sessionManager.saveSession(usernameFromDB, role);

                    Intent intent = new Intent(login, SchoolActivity.class);
                    intent.putExtra("username", usernameFromDB);
                    intent.putExtra("role", role);

                    Toast.makeText(login, "Login Successful. Role: " + role, Toast.LENGTH_SHORT).show();
                    login.startActivity(intent);
                    login.finish();
                } else {
                    Toast.makeText(login, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(login, "Database Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}