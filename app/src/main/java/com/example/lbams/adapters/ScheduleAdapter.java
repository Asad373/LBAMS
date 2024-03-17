package com.example.lbams.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lbams.R;
import com.example.lbams.model.MarkAttendanceModel;
import com.example.lbams.model.ScheduleModel;
import com.google.firebase.database.FirebaseDatabase;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class ScheduleAdapter  extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>{
    ArrayList<ScheduleModel> dataList = new ArrayList<>();
    public ScheduleAdapter(ArrayList<ScheduleModel> list) {
        dataList = list;

    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.att_approve_list, parent, false);
        return new ScheduleAdapter.ScheduleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        holder.Approve.setVisibility(View.GONE);
        holder.AttendanceStatus.setText(dataList.get(position).className);
        holder.email.setText(dataList.get(position).classCode);
        holder.date.setText(dataList.get(position).Day);
        holder.time.setText(dataList.get(position).Time + " " + dataList.get(position).Date);

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class  ScheduleViewHolder extends RecyclerView.ViewHolder{
        TextView AttendanceStatus;
        TextView email;
        TextView date;
        TextView time;
        TextView Approve;
        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            AttendanceStatus = itemView.findViewById(R.id.attenStatus);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            Approve = itemView.findViewById(R.id.detail);
            email = itemView.findViewById(R.id.email);


        }
    }
}
