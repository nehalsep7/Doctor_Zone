package com.example.android.doctorzone;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class PatientList extends Fragment {

    ListView listView;
    String name;
    String gender;
    String location;
    String age;
    String bloodGroup;
    String contact;
    PatientListData totalInfo;
    PatientListData patientList;
    PatientListData currentPatient;
    PatientAdapter adapter;
    ArrayList<PatientListData> patientListArrayList = new ArrayList<PatientListData>();
    View rootView;
    public PatientList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("Method : ","onCreateView");
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_patient_list, container, false);
        listView = (ListView)rootView.findViewById(R.id.listView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i("Method : ", "OnActivityCreated");
        super.onActivityCreated(savedInstanceState);
        fetchList();
        adapter = new PatientAdapter(getActivity(),patientListArrayList);
        listView.setAdapter(adapter);
    }

    public void fetchList(){
        try {
            JSONObject jsonObject = new JSONObject(loadJsonFromAsset());
            JSONArray jsonArray = jsonObject.getJSONArray("patients");
            for(int i =0 ;i<jsonArray.length();i++){
                JSONObject jsonPart = jsonArray.getJSONObject(i);
                name = jsonPart.getString("name");
                gender = jsonPart.getString("gender");
                location = jsonPart.getString("city");
                age = jsonPart.getString("age");
                bloodGroup=jsonPart.getString("bloodGroup");
                contact = jsonPart.getString("mobileNo");
                totalInfo=new PatientListData(name,gender,location,age,bloodGroup,contact);
                patientListArrayList.add(totalInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String loadJsonFromAsset(){
        String json = "";
        try {
            InputStream inputStream = getActivity().getAssets().open("patient_list_api.json");
            int size = inputStream.available();
            byte buffer[] = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer,"UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }
    public class PatientAdapter extends ArrayAdapter<PatientListData> {

        public PatientAdapter(Context context, ArrayList<PatientListData> patient) {
            super(context, R.layout.list_item,patient);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //Log.i("Method : ","Inside Get View");
            patientList=getItem(position);
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
            }
            TextView patientName = (TextView)convertView.findViewById(R.id.nameView);
            TextView patientGender=(TextView)convertView.findViewById(R.id.genderView);
            TextView patientLocation = (TextView)convertView.findViewById(R.id.placeView);
            TextView patientAge = (TextView)convertView.findViewById(R.id.ageView);
            TextView patientBlood=(TextView)convertView.findViewById(R.id.bloodGroupView);
            Button callButton = (Button)convertView.findViewById(R.id.callButton);
            patientName.setText(patientList.patientName);
            patientGender.setText(patientList.patientGender);
            patientLocation.setText(patientList.patientLocation);
            patientAge.setText(patientList.patientAge);
            patientBlood.setText(patientList.patientBloodGroup);
            callButton.setTag(position);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(getActivity(), "Patient at position" + position, Toast.LENGTH_LONG).show();
                }
            });
            callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int buttonPosition = (Integer)v.getTag();
                    currentPatient = patientListArrayList.get(buttonPosition);
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + currentPatient.patientContact));
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
            });

            return convertView;
        }
    }
}
