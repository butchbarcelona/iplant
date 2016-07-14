package com.proj.iplant;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.proj.iplant.ble.BlunoLibrary;


/**
 * A simple {@link Fragment} subclass.
 */
public class ScanDevices extends Fragment {


    LinearLayout llScanning;

    public ScanDevices() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scan_devices, container, false);
        final Button btnScan = (Button) view.findViewById(R.id.btn_scan);

        llScanning = (LinearLayout) view.findViewById(R.id.scanning_ll);



        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnScan.setVisibility(View.GONE);
                llScanning.setVisibility(View.VISIBLE);

                ((MainActivity)ScanDevices.this.getActivity()).
                    buttonScanOnClickProcess();

            }
        });



        return view;
    }

}
