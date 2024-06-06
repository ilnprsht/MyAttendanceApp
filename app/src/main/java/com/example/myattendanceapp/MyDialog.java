package com.example.myattendanceapp;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class MyDialog extends DialogFragment {
    public static final String CLASS_ADD_DIALOG = "addClass";
    public static final String CLASS_UPDATE_DIALOG = "updateClass";
    public static final String STUDENT_ADD_DIALOG = "addStudent";
    public static final String STUDENT_UPDATE_DIALOG = "updateStudent";
    private int roll;
    private String name;
    private OnClickListener listener;

    public MyDialog(int roll, String name) {

        this.roll = roll;
        this.name = name;
    }

    public MyDialog() {

    }

    public interface OnClickListener {
        void onClick(String firstText, String secondText);
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }
    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = null;
        if(getTag().equals(CLASS_ADD_DIALOG)) {
            dialog = getAddClassDialog();
        }

        if(getTag().equals(STUDENT_ADD_DIALOG)) {
            dialog = getAddStudentDialog();
        }

        if(getTag().equals(CLASS_UPDATE_DIALOG)) {
            dialog = getUpdateClassDialog();
        }
        if(getTag().equals(STUDENT_UPDATE_DIALOG)) {
            dialog = getUpdateStudentDialog();
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    private Dialog getUpdateStudentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog, null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialog);
        title.setText(R.string.update_student);

        EditText idEdit = view.findViewById(R.id.first_edit);
        EditText nameEdit = view.findViewById(R.id.second_edit);

        idEdit.setHint(R.string.id);
        nameEdit.setHint(R.string.name);

        Button cancelButton = view.findViewById(R.id.cancel_button);
        Button addButton = view.findViewById(R.id.add_button);
        addButton.setText(R.string.update);
        idEdit.setText(roll + "");
        nameEdit.setText(name + "");
        idEdit.setEnabled(false);


        cancelButton.setOnClickListener(v -> dismiss());
        addButton.setOnClickListener(v -> {
            String id = idEdit.getText().toString();
            String name = nameEdit.getText().toString();

            listener.onClick(id, name);
            dismiss();
        });
        return builder.create();
    }

    private Dialog getUpdateClassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog, null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialog);
        title.setText(R.string.update_class);

        EditText classEdit = view.findViewById(R.id.first_edit);
        EditText subjectEdit = view.findViewById(R.id.second_edit);

        classEdit.setHint(R.string.class_name);
        subjectEdit.setHint(R.string.subject_name);

        Button cancelButton = view.findViewById(R.id.cancel_button);
        Button addButton = view.findViewById(R.id.add_button);
        addButton.setText(R.string.update);

        cancelButton.setOnClickListener(v -> dismiss());
        addButton.setOnClickListener(v -> {
            String className = classEdit.getText().toString();
            String subjectName = subjectEdit.getText().toString();
            listener.onClick(className, subjectName);
            dismiss();
        });
        return builder.create();
    }

    private Dialog getAddStudentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog, null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialog);
        title.setText(R.string.add_new_student);

        EditText idEdit = view.findViewById(R.id.first_edit);
        EditText nameEdit = view.findViewById(R.id.second_edit);

        idEdit.setHint(R.string.id);
        nameEdit.setHint(R.string.name);

        Button cancelButton = view.findViewById(R.id.cancel_button);
        Button addButton = view.findViewById(R.id.add_button);

        cancelButton.setOnClickListener(v -> dismiss());
        addButton.setOnClickListener(v -> {
            String id = idEdit.getText().toString();
            String name = nameEdit.getText().toString();
            idEdit.setText(String.valueOf(Integer.parseInt(id)+1));
            nameEdit.setText("");
            listener.onClick(id, name);
        });
        return builder.create();
    }

    private Dialog getAddClassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog, null);
        builder.setView(view);

        TextView title = view.findViewById(R.id.titleDialog);
        title.setText(R.string.add_new_class);

        EditText classEdit = view.findViewById(R.id.first_edit);
        EditText subjectEdit = view.findViewById(R.id.second_edit);

        classEdit.setHint(R.string.class_name);
        subjectEdit.setHint(R.string.subject_name);

        Button cancelButton = view.findViewById(R.id.cancel_button);
        Button addButton = view.findViewById(R.id.add_button);

        cancelButton.setOnClickListener(v -> dismiss());
        addButton.setOnClickListener(v -> {
            String className = classEdit.getText().toString();
            String subjectName = subjectEdit.getText().toString();
            listener.onClick(className, subjectName);
            dismiss();
        });
        return builder.create();
    }
}
