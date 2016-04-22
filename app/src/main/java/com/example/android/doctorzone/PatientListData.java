package com.example.android.doctorzone;

/**
 * Created by kumnehal on 04/22/16.
 */
public class PatientListData {
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
