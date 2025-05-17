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

public class DepartmentSQL {
    private static volatile DepartmentSQL instance;

    public static DepartmentSQL getInstance() {
        if (instance == null) {
            synchronized (DepartmentSQL.class) {
                if (instance == null) {
                    instance = new DepartmentSQL();
                }
            }
        }
        return instance;
    }

    public void addDepartment(String professorName, String departmentHead, Context context) {
        SQLiteDatabaseHelper dbHelper = new SQLiteDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            db.beginTransaction();

            // First insert into Professor table
            ContentValues profValues = new ContentValues();
            profValues.put("NameOfProfessor", professorName);
            long professorId = db.insert("Professor", null, profValues);

            if (professorId == -1) {
                throw new Exception("Failed to insert professor");
            }

            // Then insert into Department table with professor ID reference
            ContentValues deptValues = new ContentValues();
            deptValues.put("DepartmentHead", departmentHead);
            deptValues.put("ProfessorID", professorId); // Reference to Professor
            long departmentId = db.insert("Department", null, deptValues);

            if (departmentId == -1) {
                throw new Exception("Failed to insert department");
            }

            db.setTransactionSuccessful();
            Toast.makeText(context, "Professor and Department added successfully", Toast.LENGTH_SHORT).show();

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

    public void AddDepartmentHead(String departmentHead, Context context) {

        String sql = "INSERT INTO Department (DepartmentHead) VALUES (?)";

        SQLiteDatabaseHelper dbHelper = new SQLiteDatabaseHelper(context);

        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            db.execSQL(sql, new Object[]{departmentHead});
            Toast.makeText(context, "Data inserted successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "An error occurred: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isDepartmentHeadExists(String departmentHead, Context context) {
        SQLiteDatabaseHelper dbHelper = new SQLiteDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT COUNT(*) FROM Department WHERE DepartmentHead = ?";
        String[] selectionArgs = { departmentHead };

        Cursor cursor = db.rawQuery(query, selectionArgs);
        boolean exists = false;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int count = cursor.getInt(0);
                exists = count > 0;
            }
            cursor.close();
        }
        db.close();
        return exists;
    }

    public List<Map<String, Object>> GetData(Context context) {
        SQLiteDatabaseHelper dbHelper = new SQLiteDatabaseHelper(context);
        List<Map<String, Object>> dataList = new ArrayList<>();

        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM Department", null)) {

            if (cursor.moveToFirst()) {
                do {
                    Map<String, Object> department = new HashMap<>();
                    department.put("departmentID", cursor.getInt(cursor.getColumnIndexOrThrow("DepartmentID")));
                    department.put("departmentHead", cursor.getString(cursor.getColumnIndexOrThrow("DepartmentHead")));
                    dataList.add(department);
                } while (cursor.moveToNext());

                Toast.makeText(context, "Data retrieved successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "No department data found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return dataList;
    }


    public void DeleteDepartment(String departmentId, Context context) {
        SQLiteDatabaseHelper dbHelper = new SQLiteDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            String whereClause = "DepartmentID = ?";
            String[] whereArgs = { departmentId };

            int rowsDeleted = db.delete("Department", whereClause, whereArgs);

            if (rowsDeleted > 0) {
                Toast.makeText(context, "Department deleted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "No department found with the given ID", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            db.close();
        }
    }
}