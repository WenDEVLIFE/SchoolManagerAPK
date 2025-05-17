package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.example.schoolmanager.R;

import java.util.List;

import model.StudentModel;


public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.LessonViewHolder> {

    private List<StudentModel> studentList;

    private onCancelListener onCancelListener;

    private onViewListener onViewListener;



    public StudentAdapter(List<StudentModel> studentList) {
        this.studentList = studentList;
    }

    public void searchList(List<StudentModel> filteredList) {
        studentList = filteredList;
        notifyDataSetChanged();
    }


    public interface onCancelListener {
        void onCancel(int position);
    }

    public interface onViewListener {
        void onView(int position);
    }


    public void setOnCancelListener(onCancelListener listener) {
        this.onCancelListener = listener;
    }


    public  void setOnViewListener(onViewListener listener) {
        this.onViewListener = listener;
    }


    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item, parent, false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        StudentModel info = studentList.get(position);
        holder.bind(info);
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    // ViewHolder class
    public class LessonViewHolder extends RecyclerView.ViewHolder {

        // Views in your item layout
        private final TextView firstName;
        private final TextView lastname;

        private ImageButton deleteButton;

        private ConstraintLayout constraintLayout;

        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            firstName = itemView.findViewById(R.id.textView17);
            firstName.setEnabled(false);
            lastname = itemView.findViewById(R.id.textView241);


            deleteButton = itemView.findViewById(R.id.imageButton);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (onCancelListener != null && position != RecyclerView.NO_POSITION) {
                        onCancelListener.onCancel(position);
                    }
                }
            });

            constraintLayout = itemView.findViewById(R.id.constraintLayout2);
            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (onViewListener != null && position != RecyclerView.NO_POSITION) {
                        onViewListener.onView(position);
                    }
                }
            });





        }

        public void bind(StudentModel info) {
            // Bind data to views
            firstName.setText("First Name:" + info.getFirstName());
            lastname.setText("Last Name" + info.getLastName());


        }

    }
}