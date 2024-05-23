package com.upt.cti.dentalhub;

import android.content.Context;
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

    private static final String TAG = "AppointmentAdapter";
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
        holder.textViewInsurance.setText(appointment.getInsurance());
        holder.textViewDate.setText(appointment.getDate());
        holder.textViewTime.setText(appointment.getTime());
        holder.textViewLocation.setText(appointment.getLocation());

        //days left
        String daysLeftText = calculateDaysLeft(appointment.getDate());
        holder.textViewLeft.setText(daysLeftText);

        holder.buttonReschedule.setOnClickListener(v -> {
            if (context instanceof Activity_ViewAppointment) {
                ((Activity_ViewAppointment) context).onAppointmentReschedule(appointment);
            }
        });

        holder.buttonCancel.setOnClickListener(v -> {
            if (context instanceof Activity_ViewAppointment) {
                ((Activity_ViewAppointment) context).onAppointmentCancel(appointment);
            }
        });

    }

    @Override
    public int getItemCount() { return appointmentList.size(); }

    private String calculateDaysLeft(String appointmentDate) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {

            Date date = dateFormat.parse(appointmentDate);

            if (date == null) {
                Log.e(TAG, "Parsed date is null for appointment date: " + appointmentDate);
                return "Invalid date";
            }

            Date today = new Date();
            long diff = date.getTime() - today.getTime();
            long daysLeft = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

            if (daysLeft > 1) {
                return "In " + daysLeft + " days";
            }else if(daysLeft == 1){
                return "In " + daysLeft + " day";
            }else if (daysLeft == 0) {
                return "Today";
            } else {
                return "Missed";
            }
        } catch (ParseException e) {
            Log.e(TAG, "Date parsing failed for appointment date: " + appointmentDate, e);
            return "Invalid date";
        }

    }

    static class AppointmentViewHolder extends RecyclerView.ViewHolder {

        TextView textViewDoctor, textViewService, textViewInsurance, textViewDate, textViewTime, textViewLocation, textViewLeft;
        Button buttonReschedule, buttonCancel;

        public AppointmentViewHolder(@NonNull View itemView) {

            super(itemView);
            textViewDoctor = itemView.findViewById(R.id.textViewDoctor);
            textViewService = itemView.findViewById(R.id.textViewService);
            textViewInsurance = itemView.findViewById(R.id.textViewInsurance);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewTime = itemView.findViewById(R.id.textViewTime);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);
            textViewLeft = itemView.findViewById(R.id.textViewLeft);
            buttonReschedule = itemView.findViewById(R.id.buttonReschedule);
            buttonCancel = itemView.findViewById(R.id.buttonCancel);

        }

    }

}