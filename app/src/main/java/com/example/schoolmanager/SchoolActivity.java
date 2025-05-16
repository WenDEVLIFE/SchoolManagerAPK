package com.example.schoolmanager;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import utils.ReplaceFragment;
import utils.SessionManager;

public class SchoolActivity extends AppCompatActivity {

    private Fragment fragment;

    private Bundle bundle;

    private String username;

    private String role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_school);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        username = getIntent().getStringExtra("username");
        role = getIntent().getStringExtra("role");

        LoadHome();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.home) {
                LoadHome();

            } else if (id == R.id.students) {
                LoadHome();
            } else if (id == R.id.department) {
                LoadHome();
            }
            else if (id == R.id.subject) {
                LoadHome();
            }
            else if (id == R.id.logout) {

                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            finish();

                            Intent intent = new Intent(SchoolActivity.this, LoginActivity.class);
                            startActivity(intent);
                            dialog.dismiss();

                            SessionManager sessionManager = new SessionManager(this);
                            sessionManager.clearSession();
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .create();

                alertDialog.show();
            }
            return false;
        });
    }

    void LoadHome(){
        fragment = new HomeFragment();
        bundle = new Bundle();
        bundle.putString("username", getIntent().getStringExtra("username"));
        bundle.putString("role", getIntent().getStringExtra("role"));
        bundle.putString("userCount", getIntent().getStringExtra("userCount"));
        bundle.putString("departmentCount", getIntent().getStringExtra("departmentCount"));
        bundle.putString("studentCount", getIntent().getStringExtra("studentCount"));
        bundle.putString("subjectCount", getIntent().getStringExtra("subjectCount"));
        fragment.setArguments(bundle);
        ReplaceFragment.getInstance().replaceFragment(fragment, getSupportFragmentManager());

    }
}