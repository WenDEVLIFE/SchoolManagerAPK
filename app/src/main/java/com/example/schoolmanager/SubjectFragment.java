package com.example.schoolmanager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import adapter.SubjectAdapter;
import database.DepartmentSQL;
import database.SubjectCourseSQL;
import model.DepartmentModel;
import model.ProfessorModel;
import model.SubjectModel;
import utils.ReplaceFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SubjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubjectFragment extends Fragment implements  SubjectAdapter.onCancelListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SubjectAdapter  subjectAdapter;

    private List <SubjectModel > subjectList = new ArrayList<>();

    RecyclerView recyclerView;

    List <ProfessorModel > professorList = new ArrayList<>();

    public SubjectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubjectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubjectFragment newInstance(String param1, String param2) {
        SubjectFragment fragment = new SubjectFragment();
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
        View view = inflater.inflate(R.layout.fragment_subject, container, false);

        LoadProfessor();

        FloatingActionButton addSubject = view.findViewById(R.id.floatingActionButton);
        addSubject.setOnClickListener(v -> {
            // Inflate the custom layout
            LayoutInflater inflaters = LayoutInflater.from(getContext());
            View dialogView = inflaters.inflate(R.layout.dialog_add_subject, null);

            EditText subjectNameInput = dialogView.findViewById(R.id.subjectInput);
            EditText scheduleDateInput = dialogView.findViewById(R.id.schedulateDate);
            scheduleDateInput.setFocusable(false);
            scheduleDateInput.setOnClickListener(view1 -> {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(),
                        (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                            // Format the date
                            String selectedDate = String.format(Locale.getDefault(),
                                    "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                            scheduleDateInput.setText(selectedDate);
                        },
                        year, month, day);

                datePickerDialog.show();
            });
            Spinner assignedProfessor = dialogView.findViewById(R.id.departmentSpinner);

            // Load departments into the spinner
            List<String> professorNames = new ArrayList<>();
            for (ProfessorModel prof : professorList) {
                professorNames.add(prof.getProfessorName());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, professorNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            assignedProfessor.setAdapter(adapter);

            // Create and show the AlertDialog
            new AlertDialog.Builder(getContext())
                    .setTitle("Add  Subject")
                    .setView(dialogView)
                    .setPositiveButton("Save", (dialog, which) -> {

                        String subjectName = subjectNameInput.getText().toString();
                        String scheduleDate = scheduleDateInput.getText().toString();
                        String professorName = assignedProfessor.getSelectedItem().toString();


                        if (subjectName.isEmpty()) {
                            Toast.makeText(getContext(), "Please enter a subject name", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (SubjectCourseSQL.getInstance().isSubjectExists(subjectName, getContext())) {
                            Toast.makeText(getContext(), "Subject already exists", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (scheduleDate.isEmpty()) {
                            Toast.makeText(getContext(), "Please enter a schedule date", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Call the method to add the subject
                        SubjectCourseSQL.getInstance().AddSubject(subjectName, scheduleDate, professorName, getContext());
                        loadSubject();

                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();


        });

        FloatingActionButton goToCourse = view.findViewById(R.id.floatingActionButton2);
        goToCourse.setOnClickListener(v -> {
            // Navigate to CourseFragment
            CourseFragment courseFragment = new CourseFragment();

            ReplaceFragment.getInstance().replaceFragment(courseFragment,getActivity().getSupportFragmentManager());
        });

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
         subjectList = new ArrayList<>();
         subjectAdapter = new SubjectAdapter(subjectList);
        subjectAdapter.setOnCancelListener(this);
        recyclerView.setAdapter(subjectAdapter);

        loadSubject();

        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                List<SubjectModel> filteredList = new ArrayList<>();
                for (SubjectModel subject : subjectList) {
                    if (subject.getSubjectName().toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(subject);
                    }
                }
                subjectAdapter.searchList(filteredList);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<SubjectModel> filteredList = new ArrayList<>();
                for (SubjectModel subject : subjectList) {
                    if (subject.getSubjectName().toLowerCase().contains(newText.toLowerCase())) {
                        filteredList.add(subject);
                    }
                }
                subjectAdapter.searchList(filteredList);
                return true;
            }
        });

        return view;
    }

    private void LoadProfessor() {
        professorList.clear();

        List<Map<String, Object>> data = DepartmentSQL.getInstance().GetProfessors(getContext());

        for (Map<String, Object> item : data) {
            String professorId = String.valueOf(item.get("professorID"));
            String professorName = (String) item.get("professorName");
            String departmentHead = (String) item.get("departmentHead");

            ProfessorModel dataModel = new ProfessorModel(professorId, professorName, departmentHead);
            professorList.add(dataModel);

            Toast.makeText(getContext(), "Professor name: " + professorName, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCancel(int position) {

        SubjectModel subject = subjectList.get(position);
        String subjectId = subject.getId();

        // Show a confirmation dialog before deleting
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Subject")
                .setMessage("Are you sure you want to delete this subject?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Delete the subject from the database
                    SubjectCourseSQL.getInstance().DeleteSubject(subjectId, getContext());
                    loadSubject();
                })
                .setNegativeButton("No", null)
                .show();

    }

    private void loadSubject() {
        subjectList.clear();
        List<Map<String, Object>> data = SubjectCourseSQL.getInstance().GetSubject(getContext());

        for (Map<String, Object> item : data) {
            String subjectId = String.valueOf(item.get("subjectID"));
            String subjectName = (String) item.get("subjectName");
            String schedule = (String) item.get("schedule");
            String professorName = (String) item.get("professorName");


            SubjectModel dataModel = new SubjectModel(subjectId, subjectName, schedule, professorName);
            subjectList.add(dataModel);
        }

        subjectAdapter.notifyDataSetChanged();
    }
}