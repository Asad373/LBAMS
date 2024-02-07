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
import com.example.lbams.views.AttendanceRecord;
import com.example.lbams.views.StudentDashboard;

import java.util.ArrayList;
import java.util.Objects;

public class StudentAttenAdapter extends  RecyclerView.Adapter<StudentAttenAdapter.MyViewHolder>{
    ArrayList<MarkAttendanceModel> attendance;

    public StudentAttenAdapter(ArrayList<MarkAttendanceModel> list){
        attendance = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.att_record_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       if(attendance.get(position).isCheckedIn.equals("1")){
           holder.AttendanceStatus.setText("Checked In");
           holder.date.setText(attendance.get(position).date);
           holder.time.setText(attendance.get(position).time);
           holder.AttendanceStatus.setTextColor(Color.GREEN);
       }else{
           holder.AttendanceStatus.setText("Checked Out");
           holder.date.setText(attendance.get(position).date);
           holder.time.setText(attendance.get(position).time);
           holder.AttendanceStatus.setTextColor(Color.RED);
       }
    }

    @Override
    public int getItemCount() {
        return attendance.size();
    }

    class MyViewHolder extends  RecyclerView.ViewHolder{
        TextView AttendanceStatus;
        TextView date;
        TextView time;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //
            AttendanceStatus = itemView.findViewById(R.id.attenStatus);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
        }
    }
}


