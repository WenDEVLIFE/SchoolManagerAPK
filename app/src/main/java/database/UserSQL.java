package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public List<Map<String, Object>> GetData(Context context) {
        SQLiteDatabaseHelper dbHelper = new SQLiteDatabaseHelper(context);
        List<Map<String, Object>> userList = new ArrayList<>();

        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM users", null)) {

            if (cursor.moveToFirst()) {
                do {
                    Map<String, Object> user = new HashMap<>();
                    user.put("id", cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
                    user.put("username", cursor.getString(cursor.getColumnIndexOrThrow("username")));
                    user.put("password", cursor.getString(cursor.getColumnIndexOrThrow("password")));
                    user.put("role", cursor.getString(cursor.getColumnIndexOrThrow("role")));
                    userList.add(user);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "An error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return userList;
    }
    public void DeleteData(String userid, Context context) {
        SQLiteDatabaseHelper dbHelper = new SQLiteDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int userId = Integer.parseInt(userid);

        String sql = "DELETE FROM users WHERE user_id = ?";

        try {
            db.execSQL(sql, new Object[]{userId});
            Toast.makeText(context, "Data deleted successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "An error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            db.close();
        }
    }
}