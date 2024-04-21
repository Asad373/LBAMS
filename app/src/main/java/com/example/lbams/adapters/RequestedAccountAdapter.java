package com.example.lbams.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lbams.R;
import com.example.lbams.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RequestedAccountAdapter extends RecyclerView.Adapter<RequestedAccountAdapter.RequestedAccountViewHolder> {
    ArrayList<User> UserData;
    FirebaseDatabase database;
    DatabaseReference dbRef;
    public RequestedAccountAdapter(ArrayList<User> list) {
        UserData = list;
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();
    }

    @NonNull
    @Override
    public RequestedAccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_request_account, parent, false);
        return new RequestedAccountAdapter.RequestedAccountViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestedAccountViewHolder holder, int position) {
            holder.AttendanceStatus.setText(UserData.get(position).FirstName +" "+UserData.get(position).LastName);
            holder.email.setText(UserData.get(position).Email);


          /*  holder.Approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    approveAtten(holder,holder.getAbsoluteAdapterPosition());
                }
            });*/



    }

    @Override
    public int getItemCount() {
        return UserData.size();
    }
/*
    public void approveAtten(RequestedAccountViewHolder holder, int position){
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

    }*/

    class  RequestedAccountViewHolder extends RecyclerView.ViewHolder{
        TextView AttendanceStatus;
        TextView email;
        TextView date;
        TextView time;
        TextView Approve;
        public RequestedAccountViewHolder(@NonNull View itemView) {
            super(itemView);
            AttendanceStatus = itemView.findViewById(R.id.attenStatus);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            Approve = itemView.findViewById(R.id.detail);
            email = itemView.findViewById(R.id.email);

        }
    }
}
