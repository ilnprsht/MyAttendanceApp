package com.example.myattendanceapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    RecyclerView recyclerView;
    ClassAdapter classAdapter;
    ArrayList<ClassItem> classItems = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    Toolbar toolbar;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        fab = findViewById(R.id.fab_main);
        fab.setOnClickListener(v -> showDialog());

        loadData();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        classAdapter = new ClassAdapter(this, classItems);
        recyclerView.setAdapter(classAdapter);
        classAdapter.setOnItemClickListener(position -> goToItemActivity(position));

        setToolbar();
    }

    private void loadData() {
        Cursor cursor = dbHelper.getClassTable();

        classItems.clear();
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(DBHelper.C_ID));
            @SuppressLint("Range") String className = cursor.getString(cursor.getColumnIndex(DBHelper.CLASS_NAME_KEY));
            @SuppressLint("Range") String subjectName = cursor.getString(cursor.getColumnIndex(DBHelper.SUBJECT_NAME_KEY));
            classItems.add(new ClassItem(id, className, subjectName));
        }
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        TextView title = toolbar.findViewById(R.id.title_toolbar);
        TextView subtitle = toolbar.findViewById(R.id.subtitle_toolbar);
        ImageButton backButton = toolbar.findViewById(R.id.button_back);
        ImageButton saveButton = toolbar.findViewById(R.id.save_button);
        TextView textSave = toolbar.findViewById(R.id.text_save);

        title.setText(getString(R.string.app_name));
        subtitle.setVisibility(View.GONE);
        backButton.setVisibility(View.INVISIBLE);
        saveButton.setVisibility(View.INVISIBLE);
        textSave.setVisibility(View.INVISIBLE);
    }

    private void goToItemActivity(int position) {
        Intent intent = new Intent(this, StudentActivity.class);

        intent.putExtra("className", classItems.get(position).getClassName());
        intent.putExtra("subjectName", classItems.get(position).getSubjectName());
        intent.putExtra("position", position);
        intent.putExtra("cid", classItems.get(position).getCid());

        startActivity(intent);
    }

    private void showDialog() {
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(), MyDialog.CLASS_ADD_DIALOG);
        dialog.setListener((className, subjectName) -> addClass(className, subjectName));
    }

    private void addClass(String className, String subjectName) {
        long cid = dbHelper.addClass(className, subjectName);
        ClassItem classItem = new ClassItem(cid, className, subjectName);
        classItems.add(classItem);
        classAdapter.notifyDataSetChanged();
        
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 0:
                showUpdateDialog(item.getGroupId());
                break;
            case 1:
                deleteClass(item.getGroupId());
        }
        return super.onContextItemSelected(item);
    }

    private void showUpdateDialog(int position) {
        MyDialog dialog = new MyDialog();
        dialog.show(getSupportFragmentManager(), MyDialog.CLASS_UPDATE_DIALOG);
        dialog.setListener((className, subjectName) -> updateClass(position, className, subjectName));
    }

    private void updateClass(int position, String className, String subjectName) {
        dbHelper.updateClass(classItems.get(position).getCid(), className, subjectName);
        classItems.get(position).setClassName(className);
        classItems.get(position).setSubjectName(subjectName);
        classAdapter.notifyItemChanged(position);
    }

    private void deleteClass(int position) {
        dbHelper.deleteClass(classItems.get(position).getCid());
        classItems.remove(position);
        classAdapter.notifyItemRemoved(position);
    }
}