package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.schoolmanager.R;

import java.util.List;

import model.SubjectModel;


public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.LessonViewHolder> {

    private List<SubjectModel> subjectList;

    private onCancelListener onCancelListener;



    public SubjectAdapter(List<SubjectModel> subjectList) {
        this.subjectList = subjectList;
    }

    public void searchList(List<SubjectModel> filteredList) {
        subjectList = filteredList;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_item, parent, false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        SubjectModel info = subjectList.get(position);
        holder.bind(info);
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    // ViewHolder class
    public class LessonViewHolder extends RecyclerView.ViewHolder {

        // Views in your item layout

        private  final TextView subjectName;
        private final TextView schedule;

        private final TextView assignedProfessor;

        private ImageButton deleteButton;

        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            subjectName = itemView.findViewById(R.id.textView241);
            schedule = itemView.findViewById(R.id.textView11);
            assignedProfessor = itemView.findViewById(R.id.textView15);

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

        public void bind(SubjectModel info) {
            // Bind data to views
            subjectName.setText("Subject Name:" +info.getSubjectName());
            schedule.setText("Schedule Date:"+info.getScheduleDate());
            assignedProfessor.setText("Assigned:"+info.getProfessorName());



        }

    }
}
