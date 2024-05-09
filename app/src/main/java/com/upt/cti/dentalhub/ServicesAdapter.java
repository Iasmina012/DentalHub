package com.upt.cti.dentalhub;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ViewHolder> {

    private List<Services> servicesList;

    public ServicesAdapter(List<Services> servicesList) {
        this.servicesList = servicesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_service, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Services servicesItem = servicesList.get(position);
        holder.textViewTitle.setText(servicesItem.getTitle());
        holder.textViewDescription.setText(servicesItem.getDescription());
        holder.imageView.setImageResource(servicesItem.getImageResourceId());

    }

    @Override
    public int getItemCount() {return servicesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewTitle;
        public TextView textViewDescription;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewSubtitle);
            imageView = itemView.findViewById(R.id.imageView);

        }

    }

}