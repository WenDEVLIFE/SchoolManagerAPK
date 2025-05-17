package com.example.schoolmanager;

import android.annotation.SuppressLint;
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
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.DepartmentAdapter;
import database.DepartmentSQL;
import database.UserSQL;
import model.DepartmentModel;
import model.UserModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DepartmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DepartmentFragment extends Fragment implements  DepartmentAdapter.onCancelListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private DepartmentAdapter departmentAdapter;

    List<DepartmentModel> departmentList = new ArrayList<>();

    private RecyclerView departmentRecyclerView;

    public DepartmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DepartmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DepartmentFragment newInstance(String param1, String param2) {
        DepartmentFragment fragment = new DepartmentFragment();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_department, container, false);

        FloatingActionButton addProfessorButton = view.findViewById(R.id.floatingActionButton);
        addProfessorButton.setOnClickListener( v-> {
            // Inflate the custom layout
            LayoutInflater inflaters = LayoutInflater.from(getContext());
            View dialogView = inflaters.inflate(R.layout.dialig_add_department, null);

            EditText DepartmentHead = dialogView.findViewById(R.id.departmentNameInput);

            // Create and show the AlertDialog
            new AlertDialog.Builder(getContext())
                    .setTitle("Add Department")
                    .setView(dialogView)
                    .setPositiveButton("Save", (dialog, which) -> {
                String departmentHead = DepartmentHead.getText().toString();


                if (departmentHead.isEmpty()) {
                    Toast.makeText(getContext(), "Please enter a department head", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (DepartmentSQL.getInstance().isDepartmentHeadExists(departmentHead, getContext())){
                    Toast.makeText(getContext(), "Department head already exists", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Call the method to add the department head

                        DepartmentSQL.getInstance().AddDepartmentHead(departmentHead, getContext());
                        LoadDepartments();
            })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();
        });

        departmentRecyclerView = view.findViewById(R.id.recyclerView);
        departmentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        departmentList = new ArrayList<>();
        departmentAdapter = new DepartmentAdapter(departmentList);
        departmentAdapter.setOnCancelListener(this);
        departmentRecyclerView.setAdapter(departmentAdapter);
        LoadDepartments();

        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                List<DepartmentModel> filteredList = new ArrayList<>();
                for (DepartmentModel item : departmentList) {
                    if (item.getDepartmentHead().toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(item);
                    }
                }
                departmentAdapter.searchList(filteredList);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<DepartmentModel> filteredList = new ArrayList<>();
                for (DepartmentModel item : departmentList) {
                    if (item.getDepartmentHead().toLowerCase().contains(newText.toLowerCase())) {
                        filteredList.add(item);
                    }
                }
                departmentAdapter.searchList(filteredList);
                return true;
            }
        });


        return view;
    }

    @Override
    public void onCancel(int position) {

        DepartmentModel departmentModel = departmentList.get(position);
        String departmentId = departmentModel.getId();

        // Show a confirmation dialog before deleting
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Department")
                .setMessage("Are you sure you want to delete this department?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Delete the department
                    DepartmentSQL.getInstance().DeleteDepartment(departmentId, getContext());
                    LoadDepartments();
                })
                .setNegativeButton("No", null)
                .show();

    }

    @SuppressLint("NotifyDataSetChanged")
    private void LoadDepartments() {
        departmentList.clear();

        List<Map<String, Object>> data = DepartmentSQL.getInstance().GetData(getContext());

        for (Map<String, Object> item : data) {
            String departmentId = String.valueOf(item.get("departmentID"));
            String departmentHead = (String) item.get("departmentHead");

            DepartmentModel dataModel = new DepartmentModel(departmentId, departmentHead);
            departmentList.add(dataModel);

            Toast.makeText(getContext(), "Department head: " + departmentHead, Toast.LENGTH_SHORT).show();
        }

        departmentAdapter.notifyDataSetChanged();
    }
}