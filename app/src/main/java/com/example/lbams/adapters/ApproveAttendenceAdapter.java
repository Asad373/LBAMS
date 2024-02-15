package com.example.lbams.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lbams.R;
import com.example.lbams.model.MarkAttendanceModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ApproveAttendenceAdapter extends RecyclerView.Adapter<ApproveAttendenceAdapter.ApproveAttenViewHolder> {
    ArrayList<MarkAttendanceModel> AttenDataList;
    FirebaseDatabase database;
    DatabaseReference dbRef;
    public ApproveAttendenceAdapter(ArrayList<MarkAttendanceModel> list) {
        AttenDataList = list;
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();
    }

    @NonNull
    @Override
    public ApproveAttenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.att_approve_list, parent, false);
        return new ApproveAttendenceAdapter.ApproveAttenViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ApproveAttenViewHolder holder, int position) {
        if(AttenDataList.get(position).isCheckedIn.equals("1") && AttenDataList.get(position).isApproved.equals("0")){
                holder.AttendanceStatus.setText("Checked In");
                holder.email.setText(AttenDataList.get(position).Email);
                holder.date.setText(AttenDataList.get(position).date);
                holder.time.setText(AttenDataList.get(position).time);
                holder.AttendanceStatus.setTextColor(Color.GREEN);

                holder.Approve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        approveAtten(holder,holder.getAbsoluteAdapterPosition());
                    }
                });


        }else {
           // AttenDataList.remove(position);
            //notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return AttenDataList.size();
    }

    public void approveAtten(ApproveAttenViewHolder holder, int position){
        dbRef.child("Attendance").child(AttenDataList.get(position).ClassCode)
                .child(AttenDataList.get(position).Email.replace(".",","))
                .child(AttenDataList.get(position).Id)
                .child("isApproved").setValue("1")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        holder.Approve.setText("Approved");
                        holder.Approve.setBackgroundColor(Color.GRAY);
                        holder.Approve.setClickable(false);
                    }
                });

    }

    class  ApproveAttenViewHolder extends RecyclerView.ViewHolder{
        TextView AttendanceStatus;
        TextView email;
        TextView date;
        TextView time;
        TextView Approve;
        public ApproveAttenViewHolder(@NonNull View itemView) {
            super(itemView);
            AttendanceStatus = itemView.findViewById(R.id.attenStatus);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            Approve = itemView.findViewById(R.id.detail);
            email = itemView.findViewById(R.id.email);

        }
    }
}
