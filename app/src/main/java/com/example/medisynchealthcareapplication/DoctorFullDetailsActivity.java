package com.example.medisynchealthcareapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DoctorFullDetailsActivity extends AppCompatActivity {

    TextView tvName, tvHospital, tvExp, tvMobile, tvFee;
    Button btnFixAppointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_full_details);

        tvName = findViewById(R.id.tvDocName);
        tvHospital = findViewById(R.id.tvHospital);
        tvExp = findViewById(R.id.tvExp);
        tvMobile = findViewById(R.id.tvMobile);
        tvFee = findViewById(R.id.tvFee);
        btnFixAppointment = findViewById(R.id.btnFixAppointment);

        // Receive data from previous activity (Doctor list)
        String name = getIntent().getStringExtra("line1");
        String hospital = getIntent().getStringExtra("line2");
        String exp = getIntent().getStringExtra("line3");
        String mobile = getIntent().getStringExtra("line4");
        String fee = getIntent().getStringExtra("line5");

        // Set data to the UI
        tvName.setText(name);
        tvHospital.setText(hospital);
        tvExp.setText(exp);
        tvMobile.setText(mobile);
        tvFee.setText(fee);

        // Handle "Fix Appointment" button click
        btnFixAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorFullDetailsActivity.this, AppointmentActivity.class);
                intent.putExtra("line1", tvName.getText().toString());
                startActivity(intent);
            }
        });
    }
}
