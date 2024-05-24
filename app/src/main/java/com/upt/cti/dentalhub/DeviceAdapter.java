package com.upt.cti.dentalhub;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {
    private List<Device> devices;
    private List<Device> devicesFull;

    public DeviceAdapter(List<Device> devices) {

        this.devices = devices;
        this.devicesFull = new ArrayList<>(devices);

    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_device, parent, false);
        return new DeviceViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {

        Device device = devices.get(position);
        holder.textViewName.setText(device.getName());
        holder.textViewDescription.setText(device.getDescription());
        holder.imageView.setImageResource(device.getImageResource());

    }

    @Override
    public int getItemCount() { return devices.size(); }

    public void setFilteredList(List<Device> filteredList) {

        this.devices = filteredList;
        notifyDataSetChanged();

    }

    static class DeviceViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textViewName, textViewDescription;

        public DeviceViewHolder(@NonNull View itemView) {

            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewDevice);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);

        }

    }

}
