package gekaradchenko.gmail.com.testworktwoonactivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import gekaradchenko.gmail.com.testworktwoonactivity.Model.ToDo;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewViewHolder> {

    private ArrayList<ToDo> toDos;

    public static class RecyclerViewViewHolder extends RecyclerView.ViewHolder {

        public TextView title, text, date;

        public RecyclerViewViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTextView);
            text = itemView.findViewById(R.id.textTextView);
            date = itemView.findViewById(R.id.dateTextView);
        }
    }

    public RecyclerViewAdapter(ArrayList<ToDo> toDos){
        this.toDos = toDos;
    }
    @NonNull
    @Override
    public RecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        RecyclerViewViewHolder recyclerViewViewHolder = new RecyclerViewViewHolder(view);
        return recyclerViewViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewViewHolder holder, int position) {
        ToDo toDo = toDos.get(position);

        holder.title.setText(toDo.getTitle());
        holder.text.setText(toDo.getText());
        holder.date.setText((String.valueOf(toDo.getDate())));
    }

    @Override
    public int getItemCount() {
        return toDos.size();
    }
}
