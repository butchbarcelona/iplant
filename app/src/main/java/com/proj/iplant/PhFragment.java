package com.proj.iplant;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

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


    public void setpHLevel(float pHLevel){
        if(cropsAdapter != null) {
            cropsAdapter.setpHLevel(pHLevel);
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
        tvPHLevel = (TextView) view.findViewById(R.id.tv_crop_name);


        ListView lvCrops = (ListView) view.findViewById(R.id.lv_crops);
        cropsAdapter = new CropsAdapter();
        lvCrops.setAdapter(cropsAdapter);



        return view;
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

            this.notifyDataSetChanged();
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
