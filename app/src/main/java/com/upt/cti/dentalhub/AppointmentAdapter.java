package com.upt.cti.dentalhub;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private List<Appointment> appointmentList;
    private Context context;

    public AppointmentAdapter(List<Appointment> appointmentList, Context context) {

        this.appointmentList = appointmentList;
        this.context = context;

    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_appointment, parent, false);
        return new AppointmentViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {

        Appointment appointment = appointmentList.get(position);
        holder.textViewDoctor.setText(appointment.getDoctor());
        holder.textViewService.setText(appointment.getService());
        holder.textViewDate.setText(appointment.getDate());
        holder.textViewTime.setText(appointment.getTime());
        holder.textViewInsurance.setText(appointment.getInsurance());
        holder.textViewLocation.setText(appointment.getLocation());

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date appointmentDate = sdf.parse(appointment.getDate());
            if (appointmentDate != null) {
                long diffInMillies = Math.abs(appointmentDate.getTime() - System.currentTimeMillis());
                long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                holder.textViewLeft.setText("In " + diff + " days");
            }
        } catch (ParseException e) {
            Log.e("AppointmentAdapter", "Date parsing failed for appointment date: " + appointment.getDate(), e);
        }

        holder.buttonReschedule.setOnClickListener(v -> {
            Intent intent = new Intent(context, Activity_SelectLocation.class);
            intent.putExtra("appointmentId", appointment.getAppointmentId());
            intent.putExtra("selectedDoctor", appointment.getDoctor());
            intent.putExtra("selectedService", appointment.getService());
            intent.putExtra("selectedDate", appointment.getDate());
            intent.putExtra("selectedTime", appointment.getTime());
            intent.putExtra("selectedInsurance", appointment.getInsurance());
            intent.putExtra("selectedLocation", appointment.getLocation());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() { return appointmentList.size(); }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewDoctor, textViewService, textViewDate, textViewTime, textViewInsurance, textViewLocation, textViewLeft;
        public Button buttonReschedule;

        public AppointmentViewHolder(View itemView) {

            super(itemView);
            textViewDoctor = itemView.findViewById(R.id.textViewDoctor);
            textViewService = itemView.findViewById(R.id.textViewService);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            textViewInsurance = itemView.findViewById(R.id.textViewInsurance);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);
            textViewLeft = itemView.findViewById(R.id.textViewLeft);
            buttonReschedule = itemView.findViewById(R.id.buttonReschedule);

        }

    }
}