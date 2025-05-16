package com.example.schoolmanager;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.UserAdapter;
import database.UserSQL;
import model.UserModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment implements UserAdapter.onCancelListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private UserAdapter userAdapter;

    private RecyclerView userRecyclerView;

    private List<UserModel> userList;

    private String username = "";

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        FloatingActionButton addUserButton = view.findViewById(R.id.floatingActionButton);
        addUserButton.setOnClickListener(v -> {
            // Inflate the custom layout
            LayoutInflater inflaters = LayoutInflater.from(getContext());
            View dialogView = inflaters.inflate(R.layout.dialog_add_user, null);

            EditText usernameInput = dialogView.findViewById(R.id.usernameInput);
            EditText passwordInput = dialogView.findViewById(R.id.passwordInput);
            Spinner roleSpinner = dialogView.findViewById(R.id.roleSpinner);

            // Set up the Spinner with roles
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    getContext(),
                    R.array.roles_array, // Define roles in res/values/strings.xml
                    android.R.layout.simple_spinner_item
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            roleSpinner.setAdapter(adapter);

            // Create and show the AlertDialog
            new AlertDialog.Builder(getContext())
                    .setTitle("Add User")
                    .setView(dialogView)
                    .setPositiveButton("Save", (dialog, which) -> {
                        String username = usernameInput.getText().toString().trim();
                        String password = passwordInput.getText().toString().trim();
                        String role = roleSpinner.getSelectedItem().toString();

                        // Handle saving the user (e.g., insert into database)

                        if (username.isEmpty() || password.isEmpty()) {
                            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                        } else {
                            // Call a method to save the user

                            Map<String, Object > user = new HashMap<>();
                            user.put("username", username);
                            user.put("password", password);
                            user.put("role", role);
                            // Save user to database
                            UserSQL.getInstance().InsertData(user, getContext());
                            LoadUserData();
                        }

                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();
        });

        userRecyclerView = view.findViewById(R.id.recyclerView);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userList = new ArrayList<>();
        userAdapter  = new UserAdapter(userList);
        userAdapter.setOnCancelListener(this);
        userRecyclerView.setAdapter(userAdapter);

        LoadUserData();


        return view;
    }

    private void LoadUserData() {
        // Clear existing list
        userList.clear();

        // Get data from SQLite
        List<Map<String, Object>> data = UserSQL.getInstance().GetData(getContext());

        // Convert Map to UserModel and add to userList
        for (Map<String, Object> item : data) {
            UserModel user = new UserModel(
                    String.valueOf(item.get("id")),
                    (String) item.get("username"),
                    (String) item.get("password"),
                    (String) item.get("role")
            );
            userList.add(user);
        }

        // Notify adapter of data change
        userAdapter.notifyDataSetChanged();
    }


    @Override
    public void onCancel(int position) {

        UserModel user = userList.get(position);

        String usernameDelete = user.getUsername();

        if (usernameDelete.equals(username)) {
            Toast.makeText(getContext(), "You cannot delete your own account", Toast.LENGTH_SHORT).show();
        } else {
            // Show confirmation dialog
            new AlertDialog.Builder(getContext())
                    .setTitle("Delete User")
                    .setMessage("Are you sure you want to delete this user?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Handle deletion logic here
                        UserSQL.getInstance().DeleteData(user.getId(), getContext());
                        LoadUserData();
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();
        }


    }
}