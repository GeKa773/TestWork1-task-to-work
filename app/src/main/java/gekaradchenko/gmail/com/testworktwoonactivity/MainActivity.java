package gekaradchenko.gmail.com.testworktwoonactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import gekaradchenko.gmail.com.testworktwoonactivity.Data.DatabaseHandler;
import gekaradchenko.gmail.com.testworktwoonactivity.Model.ToDo;
import gekaradchenko.gmail.com.testworktwoonactivity.Utils.Util;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView dataTextView;
    private ProgressBar progressBar, progressBarHorizontal;
    private FloatingActionButton floatingActionButton;
    private ConstraintLayout constraintLayout;

    private int progress = -1;
    private Date date;
    private SharedPreferences firstRun;
    private final String FIRST_RUN = "first_run";

    private Intent intent;
    private DatabaseHandler databaseHandler;
    private MyProgressBarAsyncTask task;

    private ArrayList<Integer> index;
    private ArrayList<ToDo> toDos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firstRun = getSharedPreferences(FIRST_RUN, MODE_PRIVATE);
        progressBar = findViewById(R.id.progressBar);
        dataTextView = findViewById(R.id.dataTextView);
        floatingActionButton = findViewById(R.id.fab);
        progressBarHorizontal = findViewById(R.id.progressBarHorizontal);
        constraintLayout = findViewById(R.id.constraintLayout);


        dataTextView = findViewById(R.id.dataTextView);
        date = new Date();
        databaseHandler = new DatabaseHandler(this);
        toDos = new ArrayList<>();
        intent = new Intent(this, ToDoItemActivity.class);
        index = new ArrayList<>();
        setRecyclerView();
        progressBar.setVisibility(View.GONE);
        isFirstRun();


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                index = databaseHandler.getArrayListId();
                ArrayList<Integer> reverseIndex = new ArrayList<>();
                for (int i = index.size() - 1; i >= 0; i--) {
                    reverseIndex.add(index.get(i));
                }
                int x = reverseIndex.get(position);
                intent.putExtra("position", x);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                index = databaseHandler.getArrayListId();
                ArrayList<Integer> reverseIndex = new ArrayList<>();
                for (int i = index.size() - 1; i >= 0; i--) {
                    reverseIndex.add(index.get(i));
                }
                int x = reverseIndex.get(viewHolder.getAdapterPosition());
                ToDo deleterToDo = databaseHandler.getToDo(x);
                databaseHandler.deleteToDo(deleterToDo);
                setRecyclerView();
                adapter.notifyDataSetChanged();
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    private void isFirstRun() {
        if (firstRun.getBoolean("firstRun", true)) {
            task = (MyProgressBarAsyncTask) new MyProgressBarAsyncTask().execute();
            firstRun.edit().putBoolean("firstRun", false).commit();
        }
    }

    private void setRecyclerView() {
        if (Util.isOnline(MainActivity.this)) {
            new MyProgressBarHorizontalAsyncTask().execute();
        } else {
            progressBarHorizontal.setVisibility(View.GONE);
            Snackbar.make(constraintLayout, "No internet", Snackbar.LENGTH_LONG).show();
        }
        toDos = databaseHandler.getAllReverseToDos();
        if (toDos.isEmpty()) {
            dataTextView.setVisibility(View.VISIBLE);
            dataTextView.setText("Нет данных");
        } else {
            dataTextView.setVisibility(View.INVISIBLE);
        }
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new RecyclerViewAdapter(toDos);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void addItemRecyclerView() {
        date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm");

        int x = (int) date.getTime();
        databaseHandler.addToDo(new ToDo("Новая заметка", "", format.format(date)));
        setRecyclerView();
        adapter.notifyDataSetChanged();
    }

    public void addItem(View view) {
        addItemRecyclerView();
    }


    private class MyProgressBarAsyncTask extends AsyncTask<Void, Integer, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dataTextView.setText("Нет интернета");
            floatingActionButton.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            progressBarHorizontal.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            dataTextView.setVisibility(View.INVISIBLE);

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            floatingActionButton.setVisibility(View.VISIBLE);
            setRecyclerView();

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            publishProgress(values);
        }


        @Override
        protected Void doInBackground(Void... voids) {
            progress = 0;
            while (progress < 3) {
                if (Util.isOnline(MainActivity.this)) {
                    SystemClock.sleep(1000);
                    progress++;
                } else {
                    progress = 0;
                    SystemClock.sleep(1000);
                }

            }
            return null;
        }
    }

    private class MyProgressBarHorizontalAsyncTask extends AsyncTask<Void, Integer, Void> {

        private int pro = 0;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarHorizontal.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBarHorizontal.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            progressBarHorizontal.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            while (pro < 100) {
                pro++;
                publishProgress(pro);
                SystemClock.sleep(25);

            }
            return null;
        }
    }


}