package database;

import android.content.Intent;
import android.database.Cursor;
import android.se.omapi.Session;
import android.widget.Toast;

import com.example.schoolmanager.LoginActivity;
import com.example.schoolmanager.SchoolActivity;

import utils.SessionManager;

public class LoginSQLite {

    String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

    private static volatile  LoginSQLite instance;

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
        boolean isConnected = dbHelper.testConnection();

        if (isConnected) {
            try {
                dbHelper.open();
                Cursor cursor = dbHelper.executeSQL(sql, new String[]{username, password});
                if (cursor.moveToFirst()) {

                    SessionManager sessionManager = new SessionManager(login);

                    String role = cursor.getString(cursor.getColumnIndexOrThrow("role"));
                    String usernameFromDB = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                    Toast.makeText(login, "Login Successful. Role: " + role, Toast.LENGTH_SHORT).show();
                    login.finish(); // Close the login activity
                    Intent intent = new Intent(login, SchoolActivity.class);
                    intent.putExtra("username", usernameFromDB);
                    intent.putExtra("role", role);
                    sessionManager.saveSession(usernameFromDB, role); // Save the session
                    login.startActivity(intent); // Start the SchoolActivity

                    // You can pass the role to another activity or use it as needed
                } else {
                    Toast.makeText(login, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                }
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dbHelper.close();
            }
        } else {
            Toast.makeText(login, "DB Not Connected", Toast.LENGTH_SHORT).show();
        }
    }
}