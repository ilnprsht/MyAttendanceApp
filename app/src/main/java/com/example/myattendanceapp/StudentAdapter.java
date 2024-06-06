package com.example.myattendanceapp;

import android.content.Context;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    ArrayList<StudentItem> studentItems;
    Context context;
    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener {
        void onClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public StudentAdapter(Context context, ArrayList<StudentItem> studentItems) {
        this.studentItems = studentItems;
        this.context = context;
    }
    public static class StudentViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        TextView textId;
        TextView textName;
        TextView textStatus;
        CardView cardView;
        public StudentViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            textId = itemView.findViewById(R.id.text_id);
            textName = itemView.findViewById(R.id.text_name);
            textStatus = itemView.findViewById(R.id.text_status);
            cardView = itemView.findViewById(R.id.card_view);
            itemView.setOnClickListener(v -> onItemClickListener.onClick(getAdapterPosition()));
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(getAdapterPosition(), 0, 0, "ИЗМЕНИТЬ");
            menu.add(getAdapterPosition(), 1, 0, "УДАЛИТЬ");
        }
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_class, parent, false);
        return new StudentViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        holder.textId.setText(studentItems.get(position).getRoll()+"");
        holder.textName.setText(studentItems.get(position).getName());
        holder.textStatus.setText(studentItems.get(position).getStatus());
        holder.cardView.setCardBackgroundColor(getColor(position));
    }

    private int getColor(int position) {
        String status = studentItems.get(position).getStatus();
        if(status.equals("+")) {
            return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context, R.color.green)));
        }
        else if (status.equals("н")) {
            return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context, R.color.red)));
        }
        return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context, R.color.white)));
    }

    @Override
    public int getItemCount() {
        return studentItems.size();
    }
}
