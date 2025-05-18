package com.example.medisynchealthcareapplication;


import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class HealthTipsActivity extends AppCompatActivity {

    EditText editAge, editBP;
    Spinner spinnerSurgery;
    RadioGroup diabetesGroup;
    Button btnGetTips;
    CardView cardTip;
    TextView textTipResult;

    String[] surgeries = {"None", "Heart Surgery", "Kidney Transplant", "Appendix", "Other"};
    String scriptUrl = "https://script.google.com/macros/s/AKfycbwvWYQhjosIuVpGQ_M2ZsQtKZ_1vJTbICtgujLHybhU71khn6ul5JniL_kC4BRLfhU/exec";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_tips);

        editAge = findViewById(R.id.editAge);
        editBP = findViewById(R.id.editBP);
        spinnerSurgery = findViewById(R.id.spinnerSurgery);
        diabetesGroup = findViewById(R.id.diabetesGroup);
        btnGetTips = findViewById(R.id.btnGetTips);
        cardTip = findViewById(R.id.cardTip);
        textTipResult = findViewById(R.id.textTipResult);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, surgeries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSurgery.setAdapter(adapter);

        btnGetTips.setOnClickListener(v -> sendToAI());
    }

    private void sendToAI() {
        String ageStr = editAge.getText().toString().trim();
        String bp = editBP.getText().toString().trim();
        String surgery = spinnerSurgery.getSelectedItem().toString();
        boolean hasDiabetes = diabetesGroup.getCheckedRadioButtonId() == R.id.diabetesYes;

        if (ageStr.isEmpty()) {
            editAge.setError("Enter your age");
            return;
        }

        int age = Integer.parseInt(ageStr);

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, scriptUrl,
                response -> {
                    try {
                        JSONObject obj = new JSONObject(response);
                        String tips = obj.getString("tips");

                        cardTip.setVisibility(View.VISIBLE);
                        textTipResult.setText(tips);

                        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
                        cardTip.startAnimation(fadeIn);
                    } catch (Exception e) {
                        textTipResult.setText("Error: " + e.getMessage());
                        cardTip.setVisibility(View.VISIBLE);
                    }
                },
                error -> {
                    textTipResult.setText("Error connecting to server!");
                    cardTip.setVisibility(View.VISIBLE);
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("age", String.valueOf(age));
                params.put("bp", bp);
                params.put("surgery", surgery);
                params.put("diabetes", hasDiabetes ? "Yes" : "No");
                return params;
            }
        };

        queue.add(request);
    }
}
