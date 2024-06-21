package com.upt.cti.dentalhub.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.upt.cti.dentalhub.Models.Symptom;
import com.upt.cti.dentalhub.R;

public class SymptomAdapter extends RecyclerView.Adapter<SymptomAdapter.SymptomViewHolder> {

    private List<Symptom> symptomList;

    public SymptomAdapter(List<Symptom> symptomList) {
        this.symptomList = symptomList;
    }

    @NonNull
    @Override
    public SymptomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_symptom, parent, false);
        return new SymptomViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull SymptomViewHolder holder, int position) {

        Symptom symptom = symptomList.get(position);
        holder.checkboxSymptom.setText(symptom.getName());
        holder.checkboxSymptom.setChecked(symptom.isChecked());
        holder.checkboxSymptom.setOnCheckedChangeListener((buttonView, isChecked) -> symptom.setChecked(isChecked));

    }

    @Override
    public int getItemCount() {
        return symptomList.size();
    }

    public List<Symptom> getSymptomList() {
        return symptomList;
    }

    public static class SymptomViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkboxSymptom;

        public SymptomViewHolder(@NonNull View itemView) {

            super(itemView);
            checkboxSymptom = itemView.findViewById(R.id.checkboxSymptom);

        }

    }

}