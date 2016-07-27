package com.proj.iplant;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class PhFragment extends Fragment {

    ArrayList<Crop> crops;


    public PhFragment() {
        // Required empty public constructor


        crops = new ArrayList<Crop>();
        crops.add(new Crop("AVOCADO",5.5f, 6.5f));
        crops.add(new Crop("MANGO",5.5f, 7.5f));
        crops.add(new Crop("BANANA",6.0f, 7.5f));
        crops.add(new Crop("PINEAPPLE",4.5f, 5.5f));
        crops.add(new Crop("RAMBUTAN",5.0f, 6.5f));
        crops.add(new Crop("COCONUT",6.0f, 7.5f));
        crops.add(new Crop("CASSAVA",6.0f, 7.5f));
        crops.add(new Crop("SUGAR CANE",5.8f, 7.2f));
        crops.add(new Crop("COFFEE",6.0f, 6.0f));
        crops.add(new Crop("LANZONES",4.3f, 8.0f));
        crops.add(new Crop("SQUASH",5.5f, 7.0f));
        crops.add(new Crop("OKRA",6.0f, 6.5f));
        crops.add(new Crop("EGGPLANT",5.5f, 6.8f));
        crops.add(new Crop("MUNGBEAN",5.8f, 6.5f));
        crops.add(new Crop("RADISH",5.5f, 6.5f));
        crops.add(new Crop("TOMATO",5.5f, 6.5f));
        crops.add(new Crop("CABBAGE",6.0f, 6.5f));
        crops.add(new Crop("CORN", 5.3f, 7.3f));



    }


    TextView tvPHLevel = null;
    CropsAdapter cropsAdapter;

    float phLevel;


    public void setpHLevel(String pHLevel){
//        this.phLevel = Float.valueOf(pHLevel);
        if(tvPHLevel != null) {
            tvPHLevel.setText(pHLevel);
            //this.phLevel = Float.valueOf(pHLevel);
        }
        if(cropsAdapter != null) {
            try {
                cropsAdapter.setpHLevel(Float.valueOf(pHLevel));
                cropsAdapter.notifyDataSetChanged();
            }catch(Exception e){
                //Toast.makeText(this.getActivity(),"Cannot parse data from device:"+pHLevel,Toast.LENGTH_SHORT).show();
            }

        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ph, container, false);
        tvPHLevel = (TextView) view.findViewById(R.id.tv_ph);


        ListView lvCrops = (ListView) view.findViewById(R.id.lv_crops);
        cropsAdapter = new CropsAdapter();
        lvCrops.setAdapter(cropsAdapter);


        ImageButton btnSave = (ImageButton) view.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLocationDialog();
            }
        });
        ImageButton btnBack = (ImageButton) view.findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)PhFragment.this.getActivity()).goToScan();
            }
        });


        return view;
    }

    public void showLocationDialog() {

        View view = this.getActivity().getLayoutInflater().inflate(R.layout.dialog_location,null);

        final EditText etLoc = (EditText) view.findViewById(R.id.et_location);

        new CustomAlertDialogBuilder(this.getActivity())
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String location = etLoc.getText().toString();

                        if(location.isEmpty()){
                            Toast.makeText(PhFragment.this.getActivity(),"Please input a location",Toast.LENGTH_SHORT).show();
                        }else {
                            ((MainActivity)PhFragment.this.getActivity()).addLogToCSV(phLevel, location);
                            dialog.dismiss();
                        }
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    public class CropsAdapter extends BaseAdapter{

        ArrayList<Crop> cropsIncluded;

        public CropsAdapter(){
            cropsIncluded = new ArrayList<Crop>();

        }

        public void setpHLevel(float pHLevel){

            cropsIncluded.clear();

            for(Crop c: crops){
                if(pHLevel <= c.getMax() && pHLevel >= c.getMin()) {
                    cropsIncluded.add(c);
                }
            }

        }


        @Override
        public int getCount() {
            return cropsIncluded.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = PhFragment.this.getActivity().getLayoutInflater().inflate(R.layout.listitem_crop,null);
            TextView tvName = (TextView) view.findViewById(R.id.tv_crop_name);
            tvName.setText(cropsIncluded.get(position).getName());

            return view;
        }
    }

}
