package gekaradchenko.gmail.com.testworktwoonactivity;

import androidx.appcompat.app.AppCompatActivity;
import gekaradchenko.gmail.com.testworktwoonactivity.Data.DatabaseHandler;
import gekaradchenko.gmail.com.testworktwoonactivity.Model.ToDo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ToDoItemActivity extends AppCompatActivity {
    private Bundle bundle;
    private int id;
    private DatabaseHandler databaseHandler;


    private EditText itemTitleEditText, itemTextEditText;
    private TextView itemDateTextView;

    private SimpleDateFormat format, todayFormat, standartFormat;
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_item);
        setTitle("");

        databaseHandler = new DatabaseHandler(this);
        itemDateTextView = findViewById(R.id.itemDateTextView);
        itemTitleEditText = findViewById(R.id.itemTitleEditText);
        itemTextEditText = findViewById(R.id.itemTextEditText);

        bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getInt("position");
        }
        ToDo toDo = databaseHandler.getToDo(id);

        itemDateTextView.setText(convertTime(toDo.getDate()));
        itemTitleEditText.setText(toDo.getTitle());
        itemTextEditText.setText(toDo.getText());


    }

    public void saveItem(MenuItem item) {
        saveItemOnClick();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);

    }

    private String convertTime(String str) {
        format = new SimpleDateFormat("dd.MM.yyyy");
        todayFormat = new SimpleDateFormat("hh:mm ");
        standartFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        date = new Date();
        String dateForItem;
        try {
            date = standartFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (format.format(new Date()).equals(format.format(date))) {
            dateForItem = todayFormat.format(date);
        } else dateForItem = format.format(date);
        return dateForItem;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        return true;
    }

    private void saveItemOnClick() {
        databaseHandler = new DatabaseHandler(this);
        itemDateTextView = findViewById(R.id.itemDateTextView);
        itemTitleEditText = findViewById(R.id.itemTitleEditText);
        itemTextEditText = findViewById(R.id.itemTextEditText);

        bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getInt("position");
        }
        ToDo toDo = databaseHandler.getToDo(id);
        String check = toDo.getText().trim();
        toDo.setTitle(itemTitleEditText.getText().toString().trim());
        toDo.setText(itemTextEditText.getText().toString().trim());
        if (!toDo.getText().equals(check)) {
            standartFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm");
            toDo.setDate(standartFormat.format(new Date()));
        }
        databaseHandler.updateToDo(toDo);
    }


}