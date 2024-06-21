package com.upt.cti.dentalhub.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.upt.cti.dentalhub.Models.Doctor;
import com.upt.cti.dentalhub.Interfaces.BookNowClickListener;
import com.upt.cti.dentalhub.R;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {

    private List<Doctor> doctorList;
    private LayoutInflater inflater;
    private BookNowClickListener listener;

    public DoctorAdapter(List<Doctor> doctorList, Context context) {

        this.doctorList = doctorList;
        this.inflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = inflater.inflate(R.layout.card_doctor, parent, false);
        return new DoctorViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {

        Doctor currentDoctor = doctorList.get(position);
        holder.bind(currentDoctor);

    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    public void setFilteredList(List<Doctor> filteredList) {

        this.doctorList = filteredList;
        notifyDataSetChanged();

    }

    public void setOnBookNowClickListener(BookNowClickListener listener) { this.listener = listener; }

    class DoctorViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewDoctor;
        private TextView textViewName, textViewSpecialization, textViewSchedule, textViewEmail, textViewPhoneNumber;

        DoctorViewHolder(View itemView) {

            super(itemView);
            imageViewDoctor = itemView.findViewById(R.id.imageViewDoctor);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewSpecialization = itemView.findViewById(R.id.textViewSpecialization);
            textViewSchedule = itemView.findViewById(R.id.textViewSchedule);
            textViewEmail = itemView.findViewById(R.id.textView_email);
            textViewPhoneNumber = itemView.findViewById(R.id.textView_phoneNumber);
            Button bookNowButton = itemView.findViewById(R.id.bookNowButton);

            bookNowButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onBookNowClick(position);
                }
            });

        }

        void bind(Doctor doctor) {

            imageViewDoctor.setImageResource(doctor.getImageResource());
            textViewName.setText(doctor.getName());
            textViewSpecialization.setText(doctor.getSpecialization());
            textViewSchedule.setText(doctor.getSchedule());
            textViewEmail.setText(doctor.getEmail());
            textViewPhoneNumber.setText(doctor.getPhoneNumber());

        }

    }

}