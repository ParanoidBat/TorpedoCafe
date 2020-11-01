package com.example.android.torpedocafe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btnTest, btnUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTest = findViewById(R.id.btn_test);
        btnUser = findViewById(R.id.btn_user);

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                test();
            }
        });

        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user();
            }
        });
    }

    private void test(){
        Intent intent = new Intent(getBaseContext(), AdminActivity.class);

        startActivity(intent);
    }

    private void user(){
        Intent intent = new Intent(getBaseContext(), SalePointActivity.class);

        startActivity(intent);
    }
}