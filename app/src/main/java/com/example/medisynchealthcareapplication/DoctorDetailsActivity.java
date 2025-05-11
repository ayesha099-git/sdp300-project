package com.example.medisynchealthcareapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;

public class DoctorDetailsActivity extends AppCompatActivity {
    private String[][] doctor_details1 = {
            {"Doctor Name : Ajit Saste", "Hospital Address : Pimpri", "Exp : 5yrs", "Mobile No: 9898989898", "8001"},
            {"Doctor Name : Prasad Pawar", "Hospital Address : Madi", "Exp : 15yrs", "Mobile No: 9898989898", "8002"},
            {"Doctor Name : Sennil Kale", "Hospital Address : Pune", "Exp : 8yrs", "Mobile No: 9898989898", "8003"},
            {"Doctor Name : Deepak Deshmukh", "Hospital Address : Chinchwad", "Exp : 6yrs", "Mobile No: 9898989898", "8004"},
            {"Doctor Name : Ashok Panda", "Hospital Address : Katral", "Exp : 7yrs", "Mobile No: 7777999898", "8005"}
    };
    private String[][] doctor_details2 = {
            {"Doctor Name : Neelam Patil", "Hospital Address : Pimpri", "Exp : 5yrs", "Mobile No: 9898989898", "8001"},
            {"Doctor Name : Swati Pawar", "Hospital Address : Madi", "Exp : 15yrs", "Mobile No: 9898989898", "8002"},
            {"Doctor Name : Neeraja Kale", "Hospital Address : Pune", "Exp : 8yrs", "Mobile No: 9898989898", "8003"},
            {"Doctor Name : Mayuri Deshmukh", "Hospital Address : Chinchwad", "Exp : 6yrs", "Mobile No: 9898989898", "8004"},
            {"Doctor Name : Minakshi Panda", "Hospital Address : Katral", "Exp : 7yrs", "Mobile No: 7777999898", "8005"}
    };
    private String[][] doctor_details3 = {
            {"Doctor Name : Seema Patil", "Hospital Address : Pimpri", "Exp : 5yrs", "Mobile No: 9898989898", "8001"},
            {"Doctor Name : Pnaki Parab", "Hospital Address : Madi", "Exp : 15yrs", "Mobile No: 9898989898", "8002"},
            {"Doctor Name : Monish Jain", "Hospital Address : Pune", "Exp : 8yrs", "Mobile No: 9898989898", "8003"},
            {"Doctor Name : Vishal Deshmukh", "Hospital Address : Chinchwad", "Exp : 6yrs", "Mobile No: 9898989898", "8004"},
            {"Doctor Name : Shrikant Panda", "Hospital Address : Katral", "Exp : 7yrs", "Mobile No: 7777999898", "8005"}
    };
    private String[][] doctor_details4 = {
            {"Doctor Name : Amol Gawade", "Hospital Address : Pimpri", "Exp : 5yrs", "Mobile No: 9898989898", "8001"},
            {"Doctor Name : Prasad Pawar", "Hospital Address : Madi", "Exp : 15yrs", "Mobile No: 9898989898", "8002"},
            {"Doctor Name : Nilesh Kale", "Hospital Address : Pune", "Exp : 8yrs", "Mobile No: 9898989898", "8003"},
            {"Doctor Name : Deepak Deshmukh", "Hospital Address : Chinchwad", "Exp : 6yrs", "Mobile No: 9898989898", "8004"},
            {"Doctor Name : Ashok Singh", "Hospital Address : Katral", "Exp : 7yrs", "Mobile No: 7777999898", "8005"}
    };
    private String[][] doctor_details5 = {
            {"Doctor Name : Nilesh Borate", "Hospital Address : Pimpri", "Exp : 5yrs", "Mobile No: 9898989898", "8001"},
            {"Doctor Name : Pamkaj Pawar", "Hospital Address : Madi", "Exp : 15yrs", "Mobile No: 9898989898", "8002"},
            {"Doctor Name : Swapnil Lele", "Hospital Address : Pune", "Exp : 8yrs", "Mobile No: 9898989898", "8003"},
            {"Doctor Name : Deepak Kumar", "Hospital Address : Chinchwad", "Exp : 6yrs", "Mobile No: 9898989898", "8004"},
            {"Doctor Name : Ankul Panda", "Hospital Address : Katral", "Exp : 7yrs", "Mobile No: 7777999898", "8005"}
    };

    TextView tv;
    Button btn;
String[][] doctor_details = {};
HashMap<String,String> item;
ArrayList list;
SimpleAdapter sa;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doctor_details);

        tv = findViewById(R.id.textViewDDTitle);
        btn = findViewById(R.id.buttonDDBack);
        Intent it = getIntent();
        String title = it.getStringExtra("title");
        tv.setText(title);

        if(title.compareTo("Family Physicians")==0)
            doctor_details = doctor_details1;
        else
        if(title.compareTo("Dieticians")==0)
            doctor_details = doctor_details2;
        else
        if(title.compareTo("Dentist")==0)
            doctor_details = doctor_details3;
        else
        if(title.compareTo("Surgeon ")==0)
            doctor_details = doctor_details4;
        else
            doctor_details = doctor_details5;

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DoctorDetailsActivity.this,DoctorActivity.class));
            }
        });

        list = new ArrayList();
        for(int i=0; i<doctor_details.length; i++)
        {
            item = new HashMap<String,String>();
            item.put("line1", doctor_details[i][0]);
            item.put("line2", doctor_details[i][1]);
            item.put("line3", doctor_details[i][2]);
            item.put("line4", doctor_details[i][3]);
            item.put("line5", "Cons Fees:" +doctor_details[i][4]+"/-");
            list.add(item);
        }

    }
}