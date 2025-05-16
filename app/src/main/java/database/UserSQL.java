package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.Map;

public class UserSQL {

    private static volatile UserSQL instance;

    public static UserSQL getInstance() {
        if (instance == null) {
            synchronized (UserSQL.class) {
                if (instance == null) {
                    instance = new UserSQL();
                }
            }
        }
        return instance;
    }

    public void InsertData(Map<String, Object> userdata, Context context) {
        String username = (String) userdata.get("username");
        String password = (String) userdata.get("password");
        String role = (String) userdata.get("role");

        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        if (username == null || password == null || role == null) {
            Toast.makeText(context, "Invalid input data", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabaseHelper dbHelper = new SQLiteDatabaseHelper(context);

        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            db.execSQL(sql, new Object[]{username, password, role});
            Toast.makeText(context, "Data inserted successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "An error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}