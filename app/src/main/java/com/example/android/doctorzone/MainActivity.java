package com.example.android.doctorzone;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<PatientListData> patientListArrayList = new ArrayList<PatientListData>();
    String name;
    String gender;
    String location;
    String age;
    String bloodGroup;
    String contact;
    PatientListData patientList;
    PatientListData totalInfo;
    PatientAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView)findViewById(R.id.listView);
        fetchList();
        adapter = new PatientAdapter(getApplicationContext(),patientListArrayList);
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
            InputStream inputStream = getAssets().open("patient_list_api.json");
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

    public class PatientListData{
        public String patientName;
        public String patientGender;
        public String patientLocation;
        public String patientAge;
        public String patientBloodGroup;
        public String patientContact;
        public PatientListData(){ super();}
        public PatientListData(String patientName,String patientGender,String patientLocation,String patientAge,String patientBloodGroup,String patientContact){
            this.patientName=patientName;
            this.patientGender=patientGender;
            this.patientLocation=patientLocation;
            this.patientAge=patientAge;
            this.patientBloodGroup=patientBloodGroup;
            this.patientContact=patientContact;
        }
    }
    public class PatientAdapter extends ArrayAdapter<PatientListData>{

        public PatientAdapter(Context context, ArrayList<PatientListData> patient) {
            super(context, R.layout.list_item,patient);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
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
            callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + patientList.patientContact));
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                }
            });


            return convertView;
        }
    }

}


