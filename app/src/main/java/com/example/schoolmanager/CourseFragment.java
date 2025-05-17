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

import adapter.CourseAdapter;
import database.SubjectCourseSQL;
import model.CourseModel;
import model.ProfessorModel;
import model.SubjectModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseFragment extends Fragment implements  CourseAdapter.onCancelListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    List<SubjectModel> subjectList = new ArrayList<>();

    List <CourseModel > courseList = new ArrayList<>();

    private CourseAdapter courseAdapter;

    private RecyclerView courseRecyclerView;

    public CourseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CourseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CourseFragment newInstance(String param1, String param2) {
        CourseFragment fragment = new CourseFragment();
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
        View view = inflater.inflate(R.layout.fragment_course, container, false);

        loadSubject();
        FloatingActionButton addSubjectButton = view.findViewById(R.id.floatingActionButton);
        addSubjectButton.setOnClickListener(v -> {
            // Inflate the custom layout
            LayoutInflater inflaters = LayoutInflater.from(getContext());
            View dialogView = inflaters.inflate(R.layout.dialog_add_course, null);


            Spinner assignedSubject = dialogView.findViewById(R.id.subjectSpinner);

            EditText courseName = dialogView.findViewById(R.id.course);

            // Load departments into the spinner
            List<String> subjectNames = new ArrayList<>();
             for (SubjectModel subject : subjectList) {
                 subjectNames.add(subject.getSubjectName());
             }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, subjectNames);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            assignedSubject.setAdapter(adapter);

            // Create and show the AlertDialog
            new AlertDialog.Builder(getContext())
                    .setTitle("Add Course")
                    .setView(dialogView)
                    .setPositiveButton("Save", (dialog, which) -> {

                        String selectedSubject = assignedSubject.getSelectedItem().toString();
                        String courseNameText = courseName.getText().toString();

                        // Check if the course name is empty
                        if (courseNameText.isEmpty()) {
                            Toast.makeText(getContext(), "Please enter a course name", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Check if the subject already exists
                        if (SubjectCourseSQL.getInstance().isCourseExist(selectedSubject, getContext())) {
                            Toast.makeText(getContext(), "Course already exists", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Save the new course to the database
                        SubjectCourseSQL.getInstance().AddCourse(selectedSubject, courseNameText, getContext());
                        Toast.makeText(getContext(), "Course added successfully", Toast.LENGTH_SHORT).show();

                        loadCourse();
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();



        });

        courseRecyclerView = view.findViewById(R.id.recyclerView);
        courseRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        courseList = new  ArrayList<>();
        courseAdapter = new CourseAdapter(courseList);
        courseAdapter.setOnCancelListener(this);
        courseRecyclerView.setAdapter(courseAdapter);

        loadCourse();

        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                List<CourseModel> filteredList = new ArrayList<>();
                for (CourseModel course : courseList) {
                    if (course.getCourseName().toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(course);
                    }
                }
                courseAdapter.searchList(filteredList);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<CourseModel> filteredList = new ArrayList<>();
                for (CourseModel course : courseList) {
                    if (course.getCourseName().toLowerCase().contains(newText.toLowerCase())) {
                        filteredList.add(course);
                    }
                }
                courseAdapter.searchList(filteredList);
                return true;
            }
        });



        return view;
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

    }

    @Override
    public void onCancel(int position) {

        CourseModel courseModel = courseList.get(position);

        String courseId = courseModel.getId();

        // Show confirmation dialog
        new AlertDialog.Builder(getContext())
                .setTitle("Delete Course")
                .setMessage("Are you sure you want to delete this course?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Delete the course from the database
                    SubjectCourseSQL.getInstance().DeleteCourse(courseId, getContext());
                    Toast.makeText(getContext(), "Course deleted successfully", Toast.LENGTH_SHORT).show();
                    loadCourse();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .create()
                .show();

    }

    private void loadCourse() {
        courseList.clear();
        List<Map<String, Object>> data = SubjectCourseSQL.getInstance().GetCourse(getContext());

        for (Map<String, Object> item : data) {
            String courseId = String.valueOf(item.get("courseID"));
            String subjectName = (String) item.get("subjectName");
            String courseName = (String) item.get("courseName");

            CourseModel dataModel = new CourseModel(courseId, courseName, subjectName);
            courseList.add(dataModel);
        }
        courseAdapter.notifyDataSetChanged();
    }
}