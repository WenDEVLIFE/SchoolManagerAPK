package com.example.schoolmanager;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.GradeAdapter;
import database.GradeSQL;
import database.StudentSQL;
import database.SubjectCourseSQL;
import model.GradeModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GradeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GradeFragment extends Fragment implements  GradeAdapter.onCancelListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    List <GradeModel > gradeList;

    private GradeAdapter gradeAdapter;

    private RecyclerView recyclerView;


    public GradeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GradeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GradeFragment newInstance(String param1, String param2) {
        GradeFragment fragment = new GradeFragment();
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
        View view = inflater.inflate(R.layout.fragment_grade, container, false);

        FloatingActionButton addGradeButton = view.findViewById(R.id.floatingActionButton);
        addGradeButton.setOnClickListener(v -> {
            LinearLayout layout = new LinearLayout(getContext());
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(50, 30, 50, 30);

            // Create spinners and input
            Spinner studentSpinner = new Spinner(getContext());
            Spinner subjectSpinner = new Spinner(getContext());
            EditText gradeInput = new EditText(getContext());

            // Set up student spinner
            List<String> studentNames = new ArrayList<>();
            List<Map<String, Object>> students = StudentSQL.getInstance().GetStudent(getContext());
            for (Map<String, Object> student : students) {
                studentNames.add(student.get("firstName") + " " + student.get("lastName"));
            }
            ArrayAdapter<String> studentAdapter = new ArrayAdapter<>(
                    getContext(),
                    android.R.layout.simple_spinner_item,
                    studentNames
            );
            studentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            studentSpinner.setAdapter(studentAdapter);

            // Set up subject spinner
            List<String> subjectNames = new ArrayList<>();
            List<Map<String, Object>> subjects = SubjectCourseSQL.getInstance().GetSubject(getContext());
            for (Map<String, Object> subject : subjects) {
                subjectNames.add((String) subject.get("subjectName"));
            }
            ArrayAdapter<String> subjectAdapter = new ArrayAdapter<>(
                    getContext(),
                    android.R.layout.simple_spinner_item,
                    subjectNames
            );
            subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            subjectSpinner.setAdapter(subjectAdapter);

            // Set up grade input
            gradeInput.setHint("Enter grade");
            gradeInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

            // Add views to layout
            layout.addView(createTextView("Select Student:"));
            layout.addView(studentSpinner);
            layout.addView(createTextView("Select Subject:"));
            layout.addView(subjectSpinner);
            layout.addView(createTextView("Enter Grade:"));
            layout.addView(gradeInput);

            new AlertDialog.Builder(getContext())
                    .setTitle("Add Student Grade")
                    .setView(layout)
                    .setPositiveButton("Save", (dialog, which) -> {
                        try {
                            // Get selected student
                            int studentPos = studentSpinner.getSelectedItemPosition();
                            Map<String, Object> selectedStudent = students.get(studentPos);

                            // Get selected subject
                            int subjectPos = subjectSpinner.getSelectedItemPosition();
                            Map<String, Object> selectedSubject = subjects.get(subjectPos);

                            // Get grade
                            String grade = gradeInput.getText().toString();

                            if (grade.isEmpty()) {
                                Toast.makeText(getContext(), "Please enter a grade", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // Create enrollment data
                            Map<String, Object> enrollmentData = new HashMap<>();
                            Object studentId = selectedStudent.get("id");
                            Object subjectId = selectedSubject.get("subjectID");

                        // Convert to String before putting in map
                            enrollmentData.put("studentId", String.valueOf(studentId));
                            enrollmentData.put("subjectId", String.valueOf(subjectId));
                            enrollmentData.put("grade", grade);

                            GradeSQL.getInstance().AddGrade(getContext(), enrollmentData);

                            loadGrade();
                        } catch (Exception e) {
                            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create()
                    .show();
        });

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        gradeList = new ArrayList<>();
        gradeAdapter = new GradeAdapter(gradeList);
        gradeAdapter.setOnCancelListener(this);
        recyclerView.setAdapter(gradeAdapter);
        loadGrade();

        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                List <GradeModel> filteredList = new ArrayList<>();
                for (GradeModel grade : gradeList) {
                    if (grade.getStudentName().toLowerCase().contains(query.toLowerCase()) ||
                            grade.getSubjectName().toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(grade);
                    }
                }
                gradeAdapter.searchList(filteredList);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List <GradeModel> filteredList = new ArrayList<>();
                for (GradeModel grade : gradeList) {
                    if (grade.getStudentName().toLowerCase().contains(newText.toLowerCase()) ||
                            grade.getSubjectName().toLowerCase().contains(newText.toLowerCase())) {
                        filteredList.add(grade);
                    }
                }
                gradeAdapter.searchList(filteredList);
                return false;
            }
        });
        return view;
    }
    private TextView createTextView(String text) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setTextSize(16);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 20, 0, 0);
        textView.setLayoutParams(params);
        return textView;
    }


    private void loadGrade() {
        gradeList.clear();
        List<Map<String, Object>> grades = GradeSQL.getInstance().GetGrade(getContext());
        for (Map<String, Object> grade : grades) {
            String gradeID = String.valueOf(grade.get("gradeID"));
            String enrollmentID = String.valueOf(grade.get("enrollmentID"));
            String studentName = (String) grade.get("studentName");
            String subjectName = (String) grade.get("subjectName");
            boolean isEnrolled = (boolean) grade.get("enrollmentStatus");

            GradeModel gradeModel = new GradeModel(gradeID, enrollmentID, studentName, subjectName, isEnrolled);
            gradeList.add(gradeModel);
        }
        gradeAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCancel(int position) {

        GradeModel grade = gradeList.get(position);
        String gradeID = grade.getGradeID();
        String enrollmentID = grade.getEnrollmentID();

        new AlertDialog.Builder(getContext())
                .setTitle("Delete Grade")
                .setMessage("Are you sure you want to delete this grade?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    GradeSQL.getInstance().deleteGrade(getContext(), gradeID, enrollmentID);
                    loadGrade();
                    Toast.makeText(getContext(), "Grade deleted successfully", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();

    }
}