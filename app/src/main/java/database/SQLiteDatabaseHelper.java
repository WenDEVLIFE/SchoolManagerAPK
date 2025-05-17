package database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SQLiteDatabaseHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "school.db";
    private static String DB_PATH = "";
    private static final int DB_VERSION = 1;
    private final Context context;
    private SQLiteDatabase database;

    public SQLiteDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        createDatabase();
    }

    public SQLiteDatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, Context context1) {
        super(context, name, factory, version);
        this.context = context1;
    }

    private void createDatabase() {
        boolean dbExist = checkDatabase();
        if (!dbExist) {
            this.getReadableDatabase();
            copyDatabase();
        }
    }

    private boolean checkDatabase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    private void copyDatabase() {
        try {
            InputStream input = context.getAssets().open(DB_NAME);
            File dbDir = new File(DB_PATH);
            if (!dbDir.exists()) dbDir.mkdirs();

            OutputStream output = new FileOutputStream(DB_PATH + DB_NAME);
            byte[] buffer = new byte[1024];
            int length;

            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            output.flush();
            output.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getDatabasePath() {
        return context.getDatabasePath(DB_NAME).getAbsolutePath();
    }

    public void open() {
        database = getWritableDatabase();
    }

    public void close() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }

    public Cursor executeSQL(String sql, String[] args) {
        return database.rawQuery(sql, args);
    }
    public int getCount(Cursor cursor) {
        return cursor.getCount();
    }
    public SQLiteDatabase openDatabase() {
        String path = DB_PATH + DB_NAME;
        return SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Not needed for pre-populated DB
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle upgrade if needed
    }

    public void logDatabasePath() {
        String path = DB_PATH + DB_NAME;
        System.out.println("Database path: " + path);
    }

    public void updateDatabaseFromAssets() {
        SQLiteDatabase db = null;
        try {
            // Close any existing connections
            close();

            // Copy updated database from assets
            InputStream input = context.getAssets().open(DB_NAME);
            String outFileName = DB_PATH + DB_NAME;
            OutputStream output = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            output.flush();
            output.close();
            input.close();

            // Verify the update
            db = SQLiteDatabase.openDatabase(outFileName, null, SQLiteDatabase.OPEN_READWRITE);
            Log.d("Database", "Database updated successfully from assets");

        } catch (IOException e) {
            Log.e("Database", "Error updating database: " + e.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public boolean testConnection() {
        SQLiteDatabase db = null;
        try {
            db = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READWRITE);
            if (db != null && db.isOpen()) {
                System.out.println("✅ Connected to SQLite DB");
                return true;
            }
        } catch (Exception e) {
            System.out.println("❌ Connection failed: " + e.getMessage());
        } finally {
            if (db != null && db.isOpen()) db.close();
        }
        return false;
    }

    public int getUserCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM Users", null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public int getDepartmentCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM Department", null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public int getStudentCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM Student", null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public int getSubjectCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM Subject", null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }
}
