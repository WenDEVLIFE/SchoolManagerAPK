package com.example.schoolmanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import database.SQLiteDatabaseHelper;
import utils.ReplaceFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String username;

    private String role;

    private String userCount;

    private String departmentCount;

    private String studentCount;

    private String subjectCount;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            username = getArguments().getString("username");
            role = getArguments().getString("role");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView welcomeTextView = view.findViewById(R.id.textView3);
        welcomeTextView.setText("Welcome Back " + username);

        // Load counts
        SQLiteDatabaseHelper dbHelper = new SQLiteDatabaseHelper(getContext());

        // Find TextViews for counts
        TextView userCountView = view.findViewById(R.id.textView5);
        TextView departmentCountView = view.findViewById(R.id.textView7);
        TextView studentCountView = view.findViewById(R.id.textView9);
        TextView subjectCountView = view.findViewById(R.id.textView12);

        // Set counts
        userCountView.setText(String.valueOf(dbHelper.getUserCount()));
        departmentCountView.setText(String.valueOf(dbHelper.getDepartmentCount()));
        studentCountView.setText(String.valueOf(dbHelper.getStudentCount()));
        subjectCountView.setText(String.valueOf(dbHelper.getSubjectCount()));


        FloatingActionButton viewUser = view.findViewById(R.id.floatingActionButton);
        viewUser.setOnClickListener( v -> {

            UserFragment userFragment = new UserFragment();
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            bundle.putString("role", role);
            userFragment.setArguments(bundle);
            ReplaceFragment.getInstance().replaceFragment(userFragment, getParentFragmentManager());

        });

        if (role.toLowerCase().equals("admin")) {
            viewUser.setVisibility(View.VISIBLE);
        } else {
            viewUser.setVisibility(View.GONE);
        }



        return view;
    }
}