package com.upt.cti.dentalhub;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Activity_Services extends AppCompatActivity {

    private List<Services> servicesItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

        RecyclerView recyclerView = findViewById(R.id.servicesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addServices();

        ServicesAdapter adapter = new ServicesAdapter(servicesItems);
        recyclerView.setAdapter(adapter);

        Button buttonBookNow = findViewById(R.id.buttonBook);
        buttonBookNow.setOnClickListener(v -> {
            Intent intent = new Intent(Activity_Services.this, Activity_Appointment.class);
            startActivity(intent);
        });

    }

    private void addServices() {

        servicesItems = new ArrayList<>();
        servicesItems = new ArrayList<>();
        servicesItems.add(new Services("SPECIALIZED CONSULTATION", "Surgery consultation\nPeriodontology consultation\nOrthodontics and orthopedics consultation\nImplantology consultation", R.drawable.services01));
        servicesItems.add(new Services("DENTAL PROPHYLAXIS", "Scaling, air-flow, professional brushing\nSealing of temporary and permanent teeth", R.drawable.services02));
        servicesItems.add(new Services("DENTAL AESTHETICS", "Teeth whitening\nDirect and indirect dental veneers\nAesthetic light-curing fillings\nCeramic crowns", R.drawable.services03));
        servicesItems.add(new Services("ORTHODONTICS AND DENTO-FACIAL ORTHOPEDICS", "Myofunctional appliances\nRemovable and mobilizable appliances\nFixed dental appliances: metallic, aesthetic", R.drawable.services04));
        servicesItems.add(new Services("PEDODONTICS (PEDIATRIC DENTISTRY)", "Child prophylaxis\nSealing of grooves and pits in primary teeth\nFillings for primary teeth\nEndodontic treatment of primary teeth\nExtraction of primary teeth", R.drawable.services05));
        servicesItems.add(new Services("ODONTOTHERAPY", "Pulp capping\nAesthetic fillings\nDirect veneers", R.drawable.services06));
        servicesItems.add(new Services("ENDODONTICS", "Specific treatments application\nRoot canal treatments\nRoot fillings\nTransdental drainage", R.drawable.services07));
        servicesItems.add(new Services("PERIODONTOLOGY", "Removal of subgingival plaque and tartar\nPeri-implant curettage\nSRP - surface root planing\nSampling for testing pathogens (Periodontitis - Peri-implantitis - Pathogens)", R.drawable.services08));
        servicesItems.add(new Services("DENTAL PROSTHETICS", "Removal of old prosthetic works\nRemovable dentures (telescopic and skeletonized)\nDental crowns or bridges: Metal-ceramic, All-ceramic, Zirconia, Emax\nIndirect fillings - Inlay and Onlay\nCeramic veneers (Veneers)", R.drawable.services09));
        servicesItems.add(new Services("DENTAL SURGERY", "Tooth extractions\nSinus lift (elevation of the sinus membrane)\nBone crest remodeling (bone addition + membrane)\nApical resections\nWisdom tooth odontectomy/Impacted tooth exposure\nFrenectomy and frenoplasty\nDrainage with a drainage blade", R.drawable.services10));
        servicesItems.add(new Services("IMPLANTOLOGY", "Alpha Bio dental implants\nImplantium dental implants\nNobel Biocare dental implants", R.drawable.services11));
        servicesItems.add(new Services("DENTAL RADIOLOGY", "Retroalveolar dental X-ray", R.drawable.services12));

    }

}
