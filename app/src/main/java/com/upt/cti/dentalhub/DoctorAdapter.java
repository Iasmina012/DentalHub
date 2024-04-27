package com.upt.cti.dentalhub;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

interface OnBookNowClickListener {
    void onBookNowClick(int position);
}

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {
    private List<Doctor> doctorList;
    private OnBookNowClickListener bookNowClickListener;

    public void setOnBookNowClickListener(OnBookNowClickListener listener) {
        this.bookNowClickListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView specializationTextView;
        TextView scheduleTextView;
        TextView phoneNumberTextView;
        TextView emailTextView;
        ImageView doctorImageView;

        Button bookNowButton;

        public ViewHolder(View view) {

            super(view);
            nameTextView = view.findViewById(R.id.textView_name);
            specializationTextView = view.findViewById(R.id.textView_specialization);
            scheduleTextView = view.findViewById(R.id.textView_schedule);
            phoneNumberTextView = view.findViewById(R.id.textView_phoneNumber);
            emailTextView = view.findViewById(R.id.textView_email);
            doctorImageView = view.findViewById(R.id.imageView_doctor);


            bookNowButton = itemView.findViewById(R.id.bookNowButton);
            bookNowButton.setOnClickListener(v -> {
                if (bookNowClickListener != null) {
                    bookNowClickListener.onBookNowClick(getAdapterPosition());
                }
            });

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

    public void setFilteredList(List<Doctor> filteredList){

        doctorList = filteredList;
        notifyDataSetChanged();

    }
}