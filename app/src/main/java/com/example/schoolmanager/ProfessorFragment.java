package com.example.schoolmanager;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import database.DepartmentSQL;
import database.UserSQL;
import model.DepartmentModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfessorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfessorFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<DepartmentModel> departmentList = new ArrayList<>();

    public ProfessorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfessorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfessorFragment newInstance(String param1, String param2) {
        ProfessorFragment fragment = new ProfessorFragment();
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
        View view = inflater.inflate(R.layout.fragment_professor, container, false);

        LoadDepartments();
        FloatingActionButton addProfessorButton = view.findViewById(R.id.floatingActionButton);
        addProfessorButton.setOnClickListener(v-> {
            // Inflate the custom layout
            LayoutInflater inflaters = LayoutInflater.from(getContext());
            View dialogView = inflaters.inflate(R.layout.dialog_add_professor, null);

            EditText professorNameInput = dialogView.findViewById(R.id.professorNameEditText);
            Spinner departmentSpinner = dialogView.findViewById(R.id.departmentSpinner);

            // Load departments into the spinner
            List<String> departmentHeads = new ArrayList<>();
            for (DepartmentModel department : departmentList) {
                departmentHeads.add(department.getDepartmentHead());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, departmentHeads);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            departmentSpinner.setAdapter(adapter);

            // Create and show the AlertDialog
            new AlertDialog.Builder(getContext())
                    .setTitle("Add Professor")
                    .setView(dialogView)
                    .setPositiveButton("Save", (dialog, which) -> {
                        String professorName = professorNameInput.getText().toString();
                        String selectedDepartmentHead = (String) departmentSpinner.getSelectedItem();

                        if (professorName.isEmpty()) {
                            Toast.makeText(getContext(), "Please enter a professor name", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Call the method to add the professor
                        DepartmentSQL.getInstance().AddProfessor(professorName, selectedDepartmentHead, getContext());
                        LoadDepartments();
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();

        });

        return view;
    }

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

    }
}