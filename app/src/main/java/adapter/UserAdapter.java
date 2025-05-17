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

import model.UserModel;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.LessonViewHolder> {

    private List<UserModel> userList;

    private onCancelListener onCancelListener;



    public UserAdapter(List<UserModel> userList) {
        this.userList = userList;
    }

    public void searchList(List<UserModel> filteredList) {
        userList = filteredList;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        UserModel info = userList.get(position);
        holder.bind(info);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    // ViewHolder class
    public class LessonViewHolder extends RecyclerView.ViewHolder {

        // Views in your item layout
        private final TextView Name;
        private final TextView role;

        private ImageButton deleteButton;

        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize views
            Name = itemView.findViewById(R.id.textView241);
            Name.setEnabled(false);
            role = itemView.findViewById(R.id.textView11);

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

        public void bind(UserModel info) {
            // Bind data to views
            Name.setText("Username:" + info.getUsername());
            role.setText("Role:" + info.getRole());


        }

    }
}