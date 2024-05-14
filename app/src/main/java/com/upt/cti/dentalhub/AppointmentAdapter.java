package com.upt.cti.dentalhub;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private List<Appointment> appointmentList;

    public AppointmentAdapter(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment currentAppointment = appointmentList.get(position);
        holder.textViewDentist.setText(currentAppointment.getDentist());
        holder.textViewService.setText(currentAppointment.getService());
        holder.textViewDate.setText(currentAppointment.getDate());
        holder.textViewTime.setText(currentAppointment.getTime());
        holder.textViewInsurance.setText(currentAppointment.getInsurance());
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    static class AppointmentViewHolder extends RecyclerView.ViewHolder {

        TextView textViewDentist, textViewService, textViewDate, textViewTime, textViewInsurance;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDentist = itemView.findViewById(R.id.textViewDentist);
            textViewService = itemView.findViewById(R.id.textViewService);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            textViewInsurance = itemView.findViewById(R.id.textViewInsurance);

        }

    }

}