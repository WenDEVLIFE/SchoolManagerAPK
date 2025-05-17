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

import model.DepartmentModel;
import model.GradeModel;
import model.ProfessorModel;
import model.UserModel;


public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.LessonViewHolder> {

    private List<GradeModel> gradeList;

    private onCancelListener onCancelListener;



    public GradeAdapter(List<GradeModel> gradeList) {
        this.gradeList = gradeList;
    }

    public void searchList(List<GradeModel> filteredList) {
        gradeList = filteredList;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grade_item, parent, false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        GradeModel info = gradeList.get(position);
        holder.bind(info);
    }

    @Override
    public int getItemCount() {
        return gradeList.size();
    }

    // ViewHolder class
    public class LessonViewHolder extends RecyclerView.ViewHolder {

        // Views in your item layout

        private  final TextView firstName;
        private final TextView subjectName;

        private ImageButton deleteButton;

        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            firstName = itemView.findViewById(R.id.textView17);
            subjectName = itemView.findViewById(R.id.textView241);

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

        public void bind(GradeModel info) {
            // Bind data to views
            firstName.setText("First Name:" +info.getStudentName());
            subjectName.setText("Subject Name:" +info.getSubjectName());

        }

    }
}

