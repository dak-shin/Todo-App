package com.example.madl_reminder;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayAdapter taskArr;
    DatabaseHelper databaseHelper;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView task_list = findViewById(R.id.taskList);

        ShowTasks(task_list);

        task_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TaskModel clicked_task = (TaskModel) parent.getItemAtPosition(position);
                databaseHelper.deleteTask(clicked_task);
                ShowTasks(task_list);
                Toast.makeText(MainActivity.this, "Deleted successfully",Toast.LENGTH_LONG ).show();
                return false;
            }
        });

    }

    private void ShowTasks(ListView task_list) {
        databaseHelper = new DatabaseHelper(MainActivity.this);
        List<TaskModel> all_tasks = databaseHelper.getAll();
        taskArr = new ArrayAdapter<TaskModel>(MainActivity.this, android.R.layout.simple_list_item_1, all_tasks);
        task_list.setAdapter(taskArr);

    }

    public void handleTask(View v)
    {
        RadioGroup priority_grp = findViewById(R.id.priority);
        Button btn = findViewById(R.id.add_button);

        EditText task = findViewById(R.id.task);
        String temp = task.getText().toString();
        if(!temp.equals(""))
        {
            int color;
            int priority = priority_grp.getCheckedRadioButtonId();


            if (priority!=-1) {


                switch (priority) {
                    case R.id.high:
                        color = getResources().getColor(R.color.red);
                        add(temp, priority);
                        break;

                    case R.id.medium:
                        color = getResources().getColor(R.color.orange);
                        add(temp, priority);
                        break;

                    case R.id.low:
                        color = getResources().getColor(R.color.blue);
                        add(temp, priority);
                        break;

                    default:
                        color = getResources().getColor(R.color.white);
                        break;
                }
            }
            else
                task.setError("Please select a Priority");
            TaskModel task_obj = null;
            databaseHelper = new DatabaseHelper(MainActivity.this);




//            CheckBox textView = new CheckBox(MainActivity.this);
//            textView.setText(temp);
//            textView.setTextSize(30);
//            textView.setTextColor(color);
//            textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT));
//            parent.addView(textView,1);
            ListView task_list = findViewById(R.id.taskList);
            ShowTasks(task_list);
            task.setText("");
            priority_grp.clearCheck();

        }
        else{
            task.setError("Task cannot be empty!!");
        }
    }

    private void add(String temp, int priority) {
        TaskModel task_obj;
        try{
            task_obj = new TaskModel(-1, temp, priority);
//                Toast.makeText(MainActivity.this, task_obj.toString(),Toast.LENGTH_LONG ).show();
            boolean b = databaseHelper.addTask(task_obj);
            Toast.makeText(MainActivity.this, "Added Successfully",Toast.LENGTH_LONG ).show();
        }catch (Exception e){
            Toast.makeText(MainActivity.this, "Error creating task",Toast.LENGTH_LONG ).show();
        }
    }


}