package com.example.myattendanceapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity {
    Toolbar toolbar;
    private String className;
    private String subjectName;
    private int position;
    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<StudentItem> studentItems = new ArrayList<>();
    private DBHelper dbHelper;
    private int cid;
    private MyCalendar calendar;
    private TextView subtitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        dbHelper = new DBHelper(this);
        calendar = new MyCalendar();

        Intent intent = getIntent();
        className = intent.getStringExtra("className");
        subjectName = intent.getStringExtra("subjectName");
        position = intent.getIntExtra("position", -1);
        cid = intent.getIntExtra("cid", -1);

        setToolbar();

        loadData();
        recyclerView = findViewById(R.id.student_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new StudentAdapter(this, studentItems);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(position -> changeStatus(position));
        loadStatusData();
    }

    private void loadData() {
        Cursor cursor = dbHelper.getStudentTable(cid);
        studentItems.clear();
        while (cursor.moveToNext()) {
            @SuppressLint("Range") long sid = cursor.getLong(cursor.getColumnIndex(dbHelper.S_ID));
            @SuppressLint("Range") int roll = cursor.getInt(cursor.getColumnIndex(dbHelper.STUDENT_ROLL_KEY));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(dbHelper.STUDENT_NAME_KEY));
            studentItems.add(new StudentItem(sid, roll, name));
        }
        cursor.close();
    }

    private void changeStatus(int position) {
        String status = studentItems.get(position).getStatus();
        if(status.equals("н")) {
            status = "+";
        }
        else {
            status = "н";
        }
        studentItems.get(position).setStatus(status);
        adapter.notifyItemChanged(position);
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton backButton = toolbar.findViewById(R.id.button_back);
        ImageButton saveButton = toolbar.findViewById(R.id.save_button);
        TextView textSave = toolbar.findViewById(R.id.text_save);

        saveButton.setOnClickListener(v -> saveStatus());

        title.setText(className);
        subtitle.setText(subjectName +" | "+calendar.getDate());
        backButton.setOnClickListener(v->getOnBackPressedDispatcher().onBackPressed());
        toolbar.inflateMenu(R.menu.student_menu);
        toolbar.setOnMenuItemClickListener(menuItem -> onMenuItemClick(menuItem));
    }

    private void saveStatus() {
        for(StudentItem studentItem : studentItems) {
            String status = studentItem.getStatus();
            if (status != "+") {
                status = "н";
            }
            long value = dbHelper.addStatus(studentItem.getSid(), calendar.getDate(), status);
            if (value == -1) {
                dbHelper.updateStatus(studentItem.getSid(), calendar.getDate(), status);
            }
        }
    }

    private void loadStatusData() {
        for(StudentItem studentItem : studentItems) {
            String status = dbHelper.getStatus(studentItem.getSid(), calendar.getDate());
            if (status != null) {
                studentItem.setStatus(status);
            }
            adapter.notifyDataSetChanged();
        }
    }

    private boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.add_student) {
            showAddStudentDialog();
        }
        if (menuItem.getItemId() == R.id.show_calendar) {
            showCalendar();
        }
        return true;
    }

    private void showCalendar() {
        calendar.show(getSupportFragmentManager(), "");
        calendar.setOnCalendarOkClickListener(this::onCalendarOkClicked);
    }

    private void onCalendarOkClicked(int year, int month, int day) {
        calendar.setDate(year, month, day);
        subtitle.setText(subjectName +" | "+ calendar.getDate());
        loadStatusData();
    }

    private void showAddStudentDialog() {
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(), MyDialog.STUDENT_ADD_DIALOG);
        dialog.setListener((id, name) -> addStudent(id, name));
    }

    private void addStudent(String roll_string, String name) {
        int roll = Integer.parseInt(roll_string);
        long sid = dbHelper.addStudent(cid, roll, name);
        StudentItem studentItem = new StudentItem(sid, roll, name);
        studentItems.add(studentItem);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                showUpdateStudentDialog(item.getGroupId());
                break;
            case 1:
                deleteStudent(item.getGroupId());

        }
        return super.onContextItemSelected(item);
    }

    private void showUpdateStudentDialog(int position) {
        MyDialog dialog = new MyDialog(studentItems.get(position).getRoll(), studentItems.get(position).getName());
        dialog.show(getSupportFragmentManager(), MyDialog.STUDENT_UPDATE_DIALOG);
        dialog.setListener((roll_string, name) -> updateStudent(position, name));
    }

    private void updateStudent(int position, String name) {
        dbHelper.updateStudent(studentItems.get(position).getSid(), name);
        studentItems.get(position).setName(name);
        adapter.notifyItemChanged(position);
    }

    private void deleteStudent(int position) {
        dbHelper.deleteStudent(studentItems.get(position).getSid());
        studentItems.remove(position);
        adapter.notifyItemRemoved(position);
    }
}