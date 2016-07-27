package com.proj.iplant;


import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.proj.iplant.ble.BlunoLibraryListActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScanDevicesFragment extends Fragment {


    LinearLayout llScanning;
    ListView lvDevices;
    Button btnScan;

    public ScanDevicesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scan_devices, container, false);
        btnScan  = (Button) view.findViewById(R.id.btn_scan);

        llScanning = (LinearLayout) view.findViewById(R.id.scanning_ll);
        lvDevices = (ListView) view.findViewById(R.id.lv_devices);


        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ((MainActivity) ScanDevicesFragment.this.getActivity()).goToPhPage();
            btnScan.setVisibility(View.GONE);
            llScanning.setVisibility(View.VISIBLE);
            refreshPHPage();

            }
        });


        final ImageButton btnRefresh = (ImageButton) view.findViewById(R.id.btn_refresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshPHPage();
            }
        });


        return view;
    }


    public void refreshPHPage(){
        lvDevices.setAdapter(null);
        ((MainActivity)ScanDevicesFragment.this.getActivity()).
                buttonScanOnClickProcess(lvDevices, new BlunoLibraryListActivity.OnBLEDeviceListClickListener() {
                    @Override
                    public void onSelect(String name, String address) {
                        ((MainActivity) ScanDevicesFragment.this.getActivity()).goToPhPage();
                    }
                });

    }

    public void resetPage(){
        btnScan.setVisibility(View.VISIBLE);
        llScanning.setVisibility(View.GONE);
    }


}
