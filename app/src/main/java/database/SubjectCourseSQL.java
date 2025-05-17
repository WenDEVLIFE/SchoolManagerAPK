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

    public boolean isCourseExist(String courseName, Context context) {
        SQLiteDatabaseHelper dbHelper = new SQLiteDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        boolean exists = false;

        String query = "SELECT * FROM Course WHERE CourseName = ?";
        Cursor cursor = db.rawQuery(query, new String[]{courseName});

        if (cursor != null) {
            exists = cursor.getCount() > 0;
            cursor.close();
        }

        db.close();
        return exists;
    }

    public List <Map <String, Object>> GetSubject(Context context) {
        SQLiteDatabaseHelper dbHelper = new SQLiteDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Map<String, Object>> subjectList = new ArrayList<>();

        String query = "SELECT SubjectID, SubjectName, Schedule, ProfessorID FROM Subject";
        String getProfessorNameQuery = "SELECT NameOfProfessor FROM Professor WHERE ProfessorID = ?";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {

                int subjectId = cursor.getInt(cursor.getColumnIndexOrThrow("SubjectID"));
                int professorId = cursor.getInt(cursor.getColumnIndexOrThrow("ProfessorID"));

                String subjectName = cursor.getString(cursor.getColumnIndexOrThrow("SubjectName"));
                String schedule = cursor.getString(cursor.getColumnIndexOrThrow("Schedule"));

                // Get the professor name using the professor ID
                Cursor professorCursor = db.rawQuery(getProfessorNameQuery, new String[]{String.valueOf(professorId)});
                String professorName = "";
                if (professorCursor != null && professorCursor.moveToFirst()) {
                    professorName = professorCursor.getString(professorCursor.getColumnIndexOrThrow("NameOfProfessor"));
                    professorCursor.close();
                }
                // Create a map to hold the subject data
                Map<String, Object> subjectData = new HashMap<>();
                subjectData.put("subjectID", subjectId);
                subjectData.put("subjectName", subjectName);
                subjectData.put("schedule", schedule);
                subjectData.put("professorName", professorName);
                // Add the map to the list
                subjectList.add(subjectData);
            }
            cursor.close();
        }

        db.close();
        return subjectList;
    }

    public void DeleteSubject(String subjectId, Context context) {
        SQLiteDatabaseHelper dbHelper = new SQLiteDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            db.beginTransaction();
            int rowsAffected = db.delete("Subject", "SubjectID = ?", new String[]{subjectId});

            if (rowsAffected > 0) {
                db.setTransactionSuccessful();
                Toast.makeText(context, "Subject deleted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to delete subject", Toast.LENGTH_SHORT).show();
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

    public void AddCourse(String selectedSubject, String courseNameText, Context context) {
        SQLiteDatabaseHelper dbHelper = new SQLiteDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            db.beginTransaction();

            // First get the subject ID
            String query = "SELECT SubjectID FROM Subject WHERE SubjectName = ?";
            Cursor cursor = db.rawQuery(query, new String[]{selectedSubject});

            if (cursor != null && cursor.moveToFirst()) {
                int subjectId = cursor.getInt(cursor.getColumnIndexOrThrow("SubjectID"));
                cursor.close();

                // Insert into Course table
                ContentValues values = new ContentValues();
                values.put("CourseName", courseNameText);
                values.put("SubjectID", subjectId);
                values.put("GradeStatus", false);

                long result = db.insert("Course", null, values);

                if (result != -1) {
                    db.setTransactionSuccessful();
                    Toast.makeText(context, "Course added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to add course", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Subject not found", Toast.LENGTH_SHORT).show();
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

    public List<Map <String, Object>> GetCourse(Context context) {
        SQLiteDatabaseHelper dbHelper = new SQLiteDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Map<String, Object>> courseList = new ArrayList<>();

        String query = "SELECT CourseID, CourseName, SubjectID FROM Course";
        String getSubjectNameQuery = "SELECT SubjectName FROM Subject WHERE SubjectID = ?";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {

                int courseId = cursor.getInt(cursor.getColumnIndexOrThrow("CourseID"));
                int subjectId = cursor.getInt(cursor.getColumnIndexOrThrow("SubjectID"));

                String courseName = cursor.getString(cursor.getColumnIndexOrThrow("CourseName"));

                // Get the subject name using the subject ID
                Cursor subjectCursor = db.rawQuery(getSubjectNameQuery, new String[]{String.valueOf(subjectId)});
                String subjectName = "";
                if (subjectCursor != null && subjectCursor.moveToFirst()) {
                    subjectName = subjectCursor.getString(subjectCursor.getColumnIndexOrThrow("SubjectName"));
                    subjectCursor.close();
                }
                // Create a map to hold the course data
                Map<String, Object> courseData = new HashMap<>();
                courseData.put("courseID", courseId);
                courseData.put("courseName", courseName);
                courseData.put("subjectName", subjectName);
                // Add the map to the list
                courseList.add(courseData);
            }
            cursor.close();
        }

        db.close();
        return courseList;
    }

    public void DeleteCourse(String courseId, Context context) {
        SQLiteDatabaseHelper dbHelper = new SQLiteDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            db.beginTransaction();
            int rowsAffected = db.delete("Course", "CourseID = ?", new String[]{courseId});

            if (rowsAffected > 0) {
                db.setTransactionSuccessful();
                Toast.makeText(context, "Course deleted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to delete course", Toast.LENGTH_SHORT).show();
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
}
