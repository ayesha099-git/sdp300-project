package com.example.medisynchealthcareapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class DoctorDetailsActivity extends AppCompatActivity {

    private String[][] doctor_details1 = {
            {"Doctor Name : Dr. Mahmud Hasan", "Hospital Address : United Hospital, Dhaka", "Exp : 10yrs", "Mobile No: 01711223344", "8001"},
            {"Doctor Name : Dr. Farhana Ahmed", "Hospital Address : Evercare Hospital, Dhaka", "Exp : 12yrs", "Mobile No: 01811223344", "8002"},
            {"Doctor Name : Dr. Tanvir Alam", "Hospital Address : Square Hospital, Dhaka", "Exp : 8yrs", "Mobile No: 01911223344", "8003"},
            {"Doctor Name : Dr. Rashed Karim", "Hospital Address : Popular Diagnostic Center, Chattogram", "Exp : 6yrs", "Mobile No: 01755667788", "8004"},
            {"Doctor Name : Dr. Nilufa Yasmin", "Hospital Address : Labaid Hospital, Sylhet", "Exp : 7yrs", "Mobile No: 01611223344", "8005"}
    };

    private String[][] doctor_details2 = {
            {"Doctor Name : Dr. Nusrat Jahan", "Hospital Address : United Hospital, Dhaka", "Exp : 9yrs", "Mobile No: 01788990011", "8001"},
            {"Doctor Name : Dr. Khalid Rahman", "Hospital Address : Evercare Hospital, Dhaka", "Exp : 14yrs", "Mobile No: 01833445566", "8002"},
            {"Doctor Name : Dr. Sumaiya Haque", "Hospital Address : Square Hospital, Dhaka", "Exp : 7yrs", "Mobile No: 01999887766", "8003"},
            {"Doctor Name : Dr. Arif Hossain", "Hospital Address : Popular Diagnostic Center, Chattogram", "Exp : 5yrs", "Mobile No: 01766554433", "8004"},
            {"Doctor Name : Dr. Jannatul Ferdous", "Hospital Address : Labaid Hospital, Sylhet", "Exp : 11yrs", "Mobile No: 01644556677", "8005"}
    };

    private String[][] doctor_details3 = {
            {"Doctor Name : Dr. Rafiq Azad", "Hospital Address : United Hospital, Dhaka", "Exp : 13yrs", "Mobile No: 01799112233", "8001"},
            {"Doctor Name : Dr. Meherun Nesa", "Hospital Address : Evercare Hospital, Dhaka", "Exp : 15yrs", "Mobile No: 01877665544", "8002"},
            {"Doctor Name : Dr. Shoeb Hossain", "Hospital Address : Square Hospital, Dhaka", "Exp : 10yrs", "Mobile No: 01988776655", "8003"},
            {"Doctor Name : Dr. Mizanur Rahman", "Hospital Address : Popular Diagnostic Center, Chattogram", "Exp : 6yrs", "Mobile No: 01712345678", "8004"},
            {"Doctor Name : Dr. Farzana Chowdhury", "Hospital Address : Labaid Hospital, Sylhet", "Exp : 8yrs", "Mobile No: 01655667788", "8005"}
    };

    private String[][] doctor_details4 = {
            {"Doctor Name : Dr. Nazmul Islam", "Hospital Address : United Hospital, Dhaka", "Exp : 9yrs", "Mobile No: 01755443322", "8001"},
            {"Doctor Name : Dr. Tanima Rahman", "Hospital Address : Evercare Hospital, Dhaka", "Exp : 13yrs", "Mobile No: 01822334455", "8002"},
            {"Doctor Name : Dr. Badrul Hasan", "Hospital Address : Square Hospital, Dhaka", "Exp : 11yrs", "Mobile No: 01911224466", "8003"},
            {"Doctor Name : Dr. Sharmin Akter", "Hospital Address : Popular Diagnostic Center, Chattogram", "Exp : 7yrs", "Mobile No: 01733445566", "8004"},
            {"Doctor Name : Dr. Alimuzzaman", "Hospital Address : Labaid Hospital, Sylhet", "Exp : 6yrs", "Mobile No: 01677889900", "8005"}
    };

    private String[][] doctor_details5 = {
            {"Doctor Name : Dr. Ehsan Hossain", "Hospital Address : United Hospital, Dhaka", "Exp : 10yrs", "Mobile No: 01766554422", "8001"},
            {"Doctor Name : Dr. Moumita Sultana", "Hospital Address : Evercare Hospital, Dhaka", "Exp : 12yrs", "Mobile No: 01899887766", "8002"},
            {"Doctor Name : Dr. Saif Uddin", "Hospital Address : Square Hospital, Dhaka", "Exp : 9yrs", "Mobile No: 01922334455", "8003"},
            {"Doctor Name : Dr. Afsana Begum", "Hospital Address : Popular Diagnostic Center, Chattogram", "Exp : 8yrs", "Mobile No: 01788997755", "8004"},
            {"Doctor Name : Dr. Jalal Uddin", "Hospital Address : Labaid Hospital, Sylhet", "Exp : 5yrs", "Mobile No: 01633445566", "8005"}
    };

    TextView tv;
    Button btn;
    String[][] doctor_details = {};
    ArrayList<HashMap<String, String>> list;
    HashMap<String, String> item;
    SimpleAdapter sa;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doctor_details);

        tv = findViewById(R.id.textViewDDTitle);
        btn = findViewById(R.id.buttonDDBack);
        ListView lst = findViewById(R.id.listViewDD);

        Intent it = getIntent();
        String title = it.getStringExtra("title");
        tv.setText(title);

        if (title.compareTo("Family Physicians") == 0)
            doctor_details = doctor_details1;
        else if (title.compareTo("Dieticians") == 0)
            doctor_details = doctor_details2;
        else if (title.compareTo("Dentist") == 0)
            doctor_details = doctor_details3;
        else if (title.compareTo("Surgeon") == 0)
            doctor_details = doctor_details4;
        else
            doctor_details = doctor_details5;

        btn.setOnClickListener(view -> {
            startActivity(new Intent(DoctorDetailsActivity.this, DoctorActivity.class));
        });

        list = new ArrayList<>();
        for (String[] doctor_detail : doctor_details) {
            item = new HashMap<>();
            item.put("line1", doctor_detail[0]);
            item.put("line2", doctor_detail[1]);
            item.put("line3", doctor_detail[2]);
            item.put("line4", doctor_detail[3]);
            item.put("line5", "Cons Fees: " + doctor_detail[4] + "/-");
            list.add(item);
        }

        sa = new SimpleAdapter(this, list, R.layout.multi_lines,
                new String[]{"line1", "line2", "line3", "line4", "line5"},
                new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e}) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                Button btn = view.findViewById(R.id.btnSeeDetails);
                btn.setOnClickListener(v -> {
                    HashMap<String, String> selected = (HashMap<String, String>) getItem(position);
                    Intent intent = new Intent(DoctorDetailsActivity.this, DoctorFullDetailsActivity.class);
                    intent.putExtra("line1", selected.get("line1"));
                    intent.putExtra("line2", selected.get("line2"));
                    intent.putExtra("line3", selected.get("line3"));
                    intent.putExtra("line4", selected.get("line4"));
                    intent.putExtra("line5", selected.get("line5"));
                    startActivity(intent);
                });
                return view;
            }
        };

        lst.setAdapter(sa);
    }
}
