package com.example.schoolmanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import database.SQLiteDatabaseHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initializeConnection();

        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        progressBar.setIndeterminate(true);

        // Simulate a loading process
        new Thread(() -> {
            try {
                Thread.sleep(2000); // Simulate a 2-second loading time
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> {

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            });
        }).start();
    }

    void initializeConnection(){
        SQLiteDatabaseHelper dbHelper = new SQLiteDatabaseHelper(this);
        boolean isConnected = dbHelper.testConnection();

        Toast.makeText(this, isConnected ? "DB Connected" : "DB Not Connected", Toast.LENGTH_SHORT).show();

    }
}