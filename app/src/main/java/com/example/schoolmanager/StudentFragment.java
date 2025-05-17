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
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import adapter.StudentAdapter;
import database.StudentSQL;
import database.SubjectCourseSQL;
import model.CourseModel;
import model.StudentModel;
import utils.ReplaceFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentFragment extends Fragment implements StudentAdapter .onCancelListener, StudentAdapter.onViewListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    List <CourseModel> courseList = new ArrayList<>();

    List <StudentModel> studentList = new ArrayList<>();

    StudentAdapter studentAdapter;

    private RecyclerView studentRecyclerView;


    public StudentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StudentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentFragment newInstance(String param1, String param2) {
        StudentFragment fragment = new StudentFragment();
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
        View view = inflater.inflate(R.layout.fragment_student, container, false);


        // Load courses from the database
        loadCourse();

        FloatingActionButton addStudentButton = view.findViewById(R.id.floatingActionButton);
        addStudentButton.setOnClickListener(v -> {
            // Inflate the custom layout
            View dialogView = inflater.inflate(R.layout.dialog_add_student, null);

            // Initialize views
            EditText firstNameInput = dialogView.findViewById(R.id.firstName);
            EditText lastNameInput = dialogView.findViewById(R.id.lastName);
            EditText birthdateInput = dialogView.findViewById(R.id.birthdate);
            EditText emailInput = dialogView.findViewById(R.id.editTextTextEmailAddress);
            EditText phoneInput = dialogView.findViewById(R.id.phoneNumber);
            Spinner genderSpinner = dialogView.findViewById(R.id.genderSpinner);
            Spinner courseSpinner = dialogView.findViewById(R.id.courseSpinner);

            // Set up date picker for birthdate
            birthdateInput.setFocusable(false);
            birthdateInput.setOnClickListener(view1 -> {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(),
                        (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                            String selectedDate = String.format(Locale.getDefault(),
                                    "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                            birthdateInput.setText(selectedDate);
                        },
                        year, month, day);

                datePickerDialog.show();
            });

            // Set up gender spinner
            ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(
                    getContext(),
                    R.array.gender_array,
                    android.R.layout.simple_spinner_item
            );
            genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            genderSpinner.setAdapter(genderAdapter);

            // Set up course spinner
            List<String> courseNames = new ArrayList<>();
            for (CourseModel course : courseList) {
                courseNames.add(course.getCourseName());
            }
            ArrayAdapter<String> courseAdapter = new ArrayAdapter<>(
                    getContext(),
                    android.R.layout.simple_spinner_item,
                    courseNames
            );
            courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            courseSpinner.setAdapter(courseAdapter);

            // Show dialog
            new AlertDialog.Builder(getContext())
                    .setTitle("Add Student")
                    .setView(dialogView)
                    .setPositiveButton("Save", (dialog, which) -> {
                        // Validate inputs
                        String firstName = firstNameInput.getText().toString().trim();
                        String lastName = lastNameInput.getText().toString().trim();
                        String birthdate = birthdateInput.getText().toString().trim();
                        String email = emailInput.getText().toString().trim();
                        String phone = phoneInput.getText().toString().trim();
                        String gender = genderSpinner.getSelectedItem().toString();
                        String selectedCourse = courseSpinner.getSelectedItem().toString();

                        if (firstName.isEmpty() || lastName.isEmpty() || birthdate.isEmpty()
                                || email.isEmpty() || phone.isEmpty()) {
                            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Get the selected course ID from courseList
                        String courseId = "";
                        for (CourseModel course : courseList) {
                            if (course.getCourseName().equals(selectedCourse)) {
                                courseId = course.getId();
                                break;
                            }
                        }

                        Map <String, Object> studentData = new HashMap<>();
                        studentData.put("firstName", firstName);
                        studentData.put("lastName", lastName);
                        studentData.put("birthdate", birthdate);
                        studentData.put("email", email);
                        studentData.put("gender", gender);
                        studentData.put("phone", phone);
                        studentData.put("course", selectedCourse);
                        studentData.put("courseId", courseId);
                        // Save student data to database
                        StudentSQL.getInstance()
                                .InsertData(studentData, getContext());

                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();
        });

        FloatingActionButton GoToCourseButton = view.findViewById(R.id.floatingActionButton4);
        GoToCourseButton.setOnClickListener(v -> {
            // Navigate to CourseFragment
          Fragment gradeFragment = new GradeFragment();
            ReplaceFragment.getInstance().replaceFragment(gradeFragment, getParentFragmentManager());
        });

        studentRecyclerView = view.findViewById(R.id.recyclerView);
        studentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        studentList = new ArrayList<>();
        studentAdapter = new StudentAdapter(studentList);
        studentAdapter.setOnCancelListener(this);
        studentAdapter.setOnViewListener(this);
        studentRecyclerView.setAdapter(studentAdapter);

        // Load students from the database
        loadStudents();

        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                List <StudentModel> filteredList = new ArrayList<>();
                for (StudentModel student : studentList) {
                    if (student.getFirstName().toLowerCase().contains(query.toLowerCase()) ||
                            student.getLastName().toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(student);
                    }
                }
                studentAdapter.searchList(filteredList);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List <StudentModel> filteredList = new ArrayList<>();
                for (StudentModel student : studentList) {
                    if (student.getFirstName().toLowerCase().contains(newText.toLowerCase()) ||
                            student.getLastName().toLowerCase().contains(newText.toLowerCase())) {
                        filteredList.add(student);
                    }
                }
                studentAdapter.searchList(filteredList);
                return false;
            }
        });

        return view;
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

    }

    @Override
    public void onCancel(int position) {

        StudentModel student = studentList.get(position);
        String studentId = student.getId();

        // Delete the student from the database
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Delete Student")
                .setMessage("Are you sure you want to delete this student?")
                .setPositiveButton("Yes", (dialog1, which) -> {
                    StudentSQL.getInstance().DeleteStudent(studentId, getContext());
                    loadStudents();
                    Toast.makeText(getContext(), "Student deleted successfully", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .create();
        dialog.show();
    }


    private void loadStudents() {
        studentList.clear();
        List<Map<String, Object>> data = StudentSQL.getInstance().GetStudent(getContext());

        for (Map<String, Object> item : data) {
            StudentModel student = new StudentModel(
                    (String) item.get("id"),
                    (String) item.get("firstName"),
                    (String) item.get("lastName"),
                    (String) item.get("birthdate"),
                    (String) item.get("email"),
                    (String) item.get("phoneNumber"),
                    (String) item.get("gender"),
                    (String) item.get("selectedCourse")
            );
            studentList.add(student);
        }

        studentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onView(int position) {
        StudentModel student = studentList.get(position);

        // Create LinearLayout container
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 30, 50, 30);

        // Create text views for each field
        TextView firstNameText = createTextView("First Name: " + student.getFirstName());
        TextView lastNameText = createTextView("Last Name: " + student.getLastName());
        TextView birthdateText = createTextView("Birthdate: " + student.getBirthdate());
        TextView emailText = createTextView("Email: " + student.getEmail());
        TextView phoneText = createTextView("Phone: " + student.getPhoneNumber());
        TextView genderText = createTextView("Gender: " + student.getGender());
        TextView courseText = createTextView("Course: " + student.getSelectedCourse());

        // Add views to layout
        layout.addView(firstNameText);
        layout.addView(lastNameText);
        layout.addView(birthdateText);
        layout.addView(emailText);
        layout.addView(phoneText);
        layout.addView(genderText);
        layout.addView(courseText);

        // Show dialog
        new AlertDialog.Builder(getContext())
                .setTitle("Student Details")
                .setView(layout)
                .setPositiveButton("Close", null)
                .create()
                .show();
    }


    private TextView createTextView(String text) {
        TextView textView = new TextView(getContext());
        textView.setText(text);
        textView.setTextSize(16);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 20, 0, 20);
        textView.setLayoutParams(params);
        return textView;
    }
}