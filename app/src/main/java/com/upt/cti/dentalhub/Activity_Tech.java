package com.upt.cti.dentalhub;

import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class Activity_Tech extends BaseActivity {
    private List<Device> devices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tech);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewDevice);
        devices = new ArrayList<>();

        addDevices();

        DeviceAdapter adapter = new DeviceAdapter(devices);
        recyclerView.setAdapter(adapter);
    }

    private void addDevices(){

        devices.add(new Device("Centrifuge PRF Duo", "Prepares membranes using the patient's blood to enhance healing and reduce graft rejection.", R.drawable.device01));
        devices.add(new Device("Pentamix Lite", "Automatic mixing unit for even distribution of dental impression materials.", R.drawable.device02));
        devices.add(new Device("Valo Ortho Cordless", "LED lamp for orthodontic photopolymerization, ensuring consistent polymerization on brackets.", R.drawable.device03));
        devices.add(new Device("Surgic XT Plus", "LED physiodispenser for use in implantology and oral surgery.", R.drawable.device04));
        devices.add(new Device("Ivoclar Bluephase Photo Curing Light", "Dual LED curing light that completes polymerization in just 5 seconds.", R.drawable.device05));
        devices.add(new Device("Silver Mix", "Uniformizes dental material capsules via controlled agitation.", R.drawable.device06));
        devices.add(new Device("Laser BTL10", "Features 10 preset programs for various medical and dental recovery conditions.", R.drawable.device07));
        devices.add(new Device("Apex Locator", "Ensures maximum precision for root canal treatment.", R.drawable.device08));
        devices.add(new Device("Canon 70D", "High-quality dental photography with advanced Canon technologies.", R.drawable.device09));
        devices.add(new Device("Irix 70", "Radiology device for assessing dental health and root morphology.", R.drawable.device10));
        devices.add(new Device("Zoom Lamp", "Dental aesthetic tool for effective teeth whitening.", R.drawable.device11));
        devices.add(new Device("Vita Easy Shade", "Determines tooth color by measuring brightness, transparency, and saturation.", R.drawable.device12));
        devices.add(new Device("Hurimix", "Ensures perfect homogenization of alginate for dental models.", R.drawable.device13));
        devices.add(new Device("Satelec Piezotome 2 Surgery", "Combines Piezotome for microsurgery and Newtron for endodontic treatments.", R.drawable.device14));
        devices.add(new Device("Autoclave Euronda E9 MED", "Sterilizes medical equipment using high pressure and controlled temperature.", R.drawable.device15));
        devices.add(new Device("Euronda Sealing Device", "Seals dental equipment to maintain sterilization and prevent contamination.", R.drawable.device16));

    }
}
