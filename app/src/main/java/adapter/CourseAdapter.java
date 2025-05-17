package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.schoolmanager.R;

import java.util.List;

import model.CourseModel;
import model.DepartmentModel;
import model.UserModel;


public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.LessonViewHolder> {

    private List<CourseModel> courseList;

    private onCancelListener onCancelListener;



    public CourseAdapter(List<CourseModel> courseList) {
        this.courseList = courseList;
    }

    public void searchList(List<CourseModel> filteredList) {
        courseList = filteredList;
        notifyDataSetChanged();
    }


    public interface onCancelListener {
        void onCancel(int position);
    }


    public void setOnCancelListener(onCancelListener listener) {
        this.onCancelListener = listener;
    }



    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_list, parent, false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        CourseModel info = courseList.get(position);
        holder.bind(info);
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    // ViewHolder class
    public class LessonViewHolder extends RecyclerView.ViewHolder {

        // Views in your item layout
        private final TextView course;

        private ImageButton deleteButton;

        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            course = itemView.findViewById(R.id.textView241);

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

        }

        public void bind(CourseModel info) {
            // Bind data to views
            course.setText("Course Name:" +info.getCourseName());


        }

    }
}