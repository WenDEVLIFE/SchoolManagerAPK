package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class StudentSQL {
    private static volatile  StudentSQL instance;

    public static StudentSQL getInstance() {
        if (instance == null) {
            synchronized (StudentSQL.class) {
                if (instance == null) {
                    instance = new StudentSQL();
                }
            }
        }
        return instance;
    }


    public void InsertData(Map<String, Object> studentData, Context context) {
        SQLiteDatabaseHelper dbHelper = new SQLiteDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {

            String dateStr = (String) studentData.get("birthdate");
            // Convert string date to SQLite date format (YYYY-MM-DD)
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = inputFormat.parse(dateStr);
            String formattedDate = outputFormat.format(date);
            ContentValues values = new ContentValues();
            values.put("FirstName", (String) studentData.get("firstName"));
            values.put("LastName", (String) studentData.get("lastName"));
            values.put("BirthDate", formattedDate);
            values.put("EmailAddress", (String) studentData.get("email"));
            values.put("Gender", (String) studentData.get("gender"));
            values.put("PhoneNumber", (String) studentData.get("phone"));
            values.put("CourseID", (String) studentData.get("courseId"));

            long result = db.insert("Student", null, values);
            if (result != -1) {
                Toast.makeText(context, "Student added successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to add student", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            db.close();
        }
    }

    public List<Map<String, Object>> GetStudent(Context context) {
        SQLiteDatabaseHelper dbHelper = new SQLiteDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Map<String, Object>> studentList = new ArrayList<>();

        String query = "SELECT s.*, c.CourseName FROM Student s " +
                "LEFT JOIN Course c ON s.CourseID = c.CourseID";

        try {
            Cursor cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Map<String, Object> studentData = new HashMap<>();
                    studentData.put("id", cursor.getString(cursor.getColumnIndexOrThrow("StudentID")));
                    studentData.put("firstName", cursor.getString(cursor.getColumnIndexOrThrow("FirstName")));
                    studentData.put("lastName", cursor.getString(cursor.getColumnIndexOrThrow("LastName")));
                    studentData.put("birthdate", cursor.getString(cursor.getColumnIndexOrThrow("BirthDate")));
                    studentData.put("email", cursor.getString(cursor.getColumnIndexOrThrow("EmailAddress")));
                    studentData.put("phoneNumber", cursor.getString(cursor.getColumnIndexOrThrow("PhoneNumber")));
                    studentData.put("gender", cursor.getString(cursor.getColumnIndexOrThrow("Gender")));
                    studentData.put("selectedCourse", cursor.getString(cursor.getColumnIndexOrThrow("CourseName")));

                    studentList.add(studentData);
                } while (cursor.moveToNext());
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }

        return studentList;
    }

    public void DeleteStudent(String studentId, Context context) {
        SQLiteDatabaseHelper dbHelper = new SQLiteDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            int rowsDeleted = db.delete("Student", "StudentID = ?", new String[]{studentId});
            if (rowsDeleted > 0) {
                Toast.makeText(context, "Student deleted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to delete student", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            db.close();
        }
    }
}
