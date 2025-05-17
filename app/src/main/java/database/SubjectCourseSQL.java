package database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class SubjectCourseSQL {

    private static volatile SubjectCourseSQL instance;

    public static SubjectCourseSQL getInstance() {
        if (instance == null) {
            synchronized (SubjectCourseSQL.class) {
                if (instance == null) {
                    instance = new SubjectCourseSQL();
                }
            }
        }
        return instance;
    }


    public void AddSubject(String subjectName, String scheduleDate, String professorName, Context context) {
        SQLiteDatabaseHelper dbHelper = new SQLiteDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            db.beginTransaction();

            // First get the professor ID
            String query = "SELECT ProfessorID FROM Professor WHERE NameOfProfessor = ?";
            Cursor cursor = db.rawQuery(query, new String[]{professorName});

            if (cursor != null && cursor.moveToFirst()) {
                int professorId = cursor.getInt(cursor.getColumnIndexOrThrow("ProfessorID"));
                cursor.close();

                // Insert into Subject table
                ContentValues values = new ContentValues();
                values.put("SubjectName", subjectName);
                values.put("Schedule", scheduleDate);
                values.put("ProfessorID", professorId);

                long result = db.insert("Subject", null, values);

                if (result != -1) {
                    db.setTransactionSuccessful();
                    Toast.makeText(context, "Subject added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to add subject", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Professor not found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (db.inTransaction()) {
                db.endTransaction();
            }
            db.close();
        }
    }

    public boolean isSubjectExists(String subjectName, Context context) {
        SQLiteDatabaseHelper dbHelper = new SQLiteDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        boolean exists = false;

        String query = "SELECT * FROM Subject WHERE SubjectName = ?";
        Cursor cursor = db.rawQuery(query, new String[]{subjectName});

        if (cursor != null) {
            exists = cursor.getCount() > 0;
            cursor.close();
        }

        db.close();
        return exists;
    }

}
