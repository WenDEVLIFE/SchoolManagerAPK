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

    public void AddProfessor(String professorName, String selectedDepartmentHead, Context context) {
        SQLiteDatabaseHelper dbHelper = new SQLiteDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            db.beginTransaction();

            // First get the department ID
            String query = "SELECT DepartmentID FROM Department WHERE DepartmentHead = ?";
            Cursor cursor = db.rawQuery(query, new String[]{selectedDepartmentHead});

            if (cursor != null && cursor.moveToFirst()) {
                int departmentId = cursor.getInt(cursor.getColumnIndexOrThrow("DepartmentID"));
                cursor.close();

                // Now insert into Professor table with department reference
                ContentValues profValues = new ContentValues();
                profValues.put("NameOfProfessor", professorName);
                profValues.put("DepartmentID", departmentId);  // Reference to Department

                long result = db.insert("Professor", null, profValues);

                if (result != -1) {
                    db.setTransactionSuccessful();
                    Toast.makeText(context, "Professor added successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to add professor", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Department not found", Toast.LENGTH_SHORT).show();
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

    public  List <Map<String, Object>> GetProfessors(Context context) {
        SQLiteDatabaseHelper dbHelper = new SQLiteDatabaseHelper(context);
        List<Map<String, Object>> dataList = new ArrayList<>();

        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.rawQuery("SELECT * FROM Professor", null)) {

            if (cursor.moveToFirst()) {
                do {

                    Cursor departmentCursor = db.rawQuery("SELECT DepartmentHead FROM Department WHERE DepartmentID = ?", new String[]{String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("DepartmentID")))});
                    String departmentHead = null;
                    if (departmentCursor != null && departmentCursor.moveToFirst()) {
                        departmentHead = departmentCursor.getString(departmentCursor.getColumnIndexOrThrow("DepartmentHead"));
                        departmentCursor.close();
                    }
                    Map<String, Object> professor = new HashMap<>();
                    professor.put("professorID", cursor.getInt(cursor.getColumnIndexOrThrow("ProfessorID")));
                    professor.put("professorName", cursor.getString(cursor.getColumnIndexOrThrow("NameOfProfessor")));
                    professor.put("departmentHead", departmentHead);
                    dataList.add(professor);
                } while (cursor.moveToNext());

                Toast.makeText(context, "Data retrieved successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "No professor data found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return dataList;
    }

    public void deleteProfessor(String professorId, Context context) {
        SQLiteDatabaseHelper dbHelper = new SQLiteDatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            String whereClause = "ProfessorID = ?";
            String[] whereArgs = { professorId };

            int rowsDeleted = db.delete("Professor", whereClause, whereArgs);

            if (rowsDeleted > 0) {
                Toast.makeText(context, "Professor deleted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "No professor found with the given ID", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            db.close();
        }
    }
}