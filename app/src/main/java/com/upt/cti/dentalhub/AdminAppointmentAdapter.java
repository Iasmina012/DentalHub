package com.upt.cti.dentalhub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdminAppointmentAdapter extends RecyclerView.Adapter<AdminAppointmentAdapter.AppointmentViewHolder> {

    private List<Appointment> appointmentList;
    private Context context;

    public AdminAppointmentAdapter(List<Appointment> appointmentList, Context context) {

        this.appointmentList = appointmentList;
        this.context = context;

    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_admin_appointment, parent, false);
        return new AppointmentViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {

        Appointment appointment = appointmentList.get(position);
        holder.textViewPatient.setText(appointment.getUserName());
        holder.textViewDoctor.setText(appointment.getDoctor());
        holder.textViewService.setText(appointment.getService());
        holder.textViewInsurance.setText(appointment.getInsurance());
        holder.textViewDate.setText(appointment.getDate());
        holder.textViewTime.setText(appointment.getTime());
        holder.textViewLocation.setText(appointment.getLocation());

        holder.buttonReschedule.setOnClickListener(v -> {
            if (context instanceof AdminActivity) {
                ((AdminActivity) context).onAppointmentReschedule(appointment);
            }
        });

        holder.buttonCancel.setOnClickListener(v -> {
            if (context instanceof AdminActivity) {
                ((AdminActivity) context).onAppointmentCancel(appointment);
            }
        });

    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    static class AppointmentViewHolder extends RecyclerView.ViewHolder {

        TextView textViewPatient, textViewDoctor, textViewService, textViewInsurance, textViewDate, textViewTime, textViewLocation;
        Button buttonReschedule, buttonCancel;

        public AppointmentViewHolder(@NonNull View itemView) {

            super(itemView);
            textViewPatient = itemView.findViewById(R.id.textViewPatient);
            textViewDoctor = itemView.findViewById(R.id.textViewDoctor);
            textViewService = itemView.findViewById(R.id.textViewService);
            textViewInsurance = itemView.findViewById(R.id.textViewInsurance);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);
            buttonReschedule = itemView.findViewById(R.id.buttonReschedule);
            buttonCancel = itemView.findViewById(R.id.buttonCancel);

        }

    }

}