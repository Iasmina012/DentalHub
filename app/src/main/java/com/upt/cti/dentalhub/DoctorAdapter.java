package com.upt.cti.dentalhub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {
    private List<Doctor> doctorList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView specializationTextView;
        TextView scheduleTextView;
        TextView phoneNumberTextView;
        TextView emailTextView;
        ImageView doctorImageView;

        public ViewHolder(View view) {

            super(view);
            nameTextView = view.findViewById(R.id.textView_name);
            specializationTextView = view.findViewById(R.id.textView_specialization);
            scheduleTextView = view.findViewById(R.id.textView_schedule);
            phoneNumberTextView = view.findViewById(R.id.textView_phoneNumber);
            emailTextView = view.findViewById(R.id.textView_email);
            doctorImageView = view.findViewById(R.id.imageView_doctor);

        }
    }

    public DoctorAdapter(List<Doctor> doctorList) {
        this.doctorList = doctorList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_doctor, parent, false);
        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Doctor doctor = doctorList.get(position);

        holder.nameTextView.setText(doctor.getName());
        holder.specializationTextView.setText(doctor.getSpecialization());
        holder.scheduleTextView.setText(doctor.getSchedule());
        holder.phoneNumberTextView.setText(doctor.getPhoneNumber());
        holder.emailTextView.setText(doctor.getEmail());
        holder.doctorImageView.setImageResource(R.drawable.item1);

    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }
}