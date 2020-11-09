package com.example.android.torpedocafe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button btnAdmin, btnUser;
    private EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdmin = findViewById(R.id.btn_admin);
        btnUser = findViewById(R.id.btn_user);

        etPassword = findViewById(R.id.et_password);

        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!etPassword.getText().toString().equals("RiazAdmin00l")){
                    Toast.makeText(MainActivity.this, "Invalid Password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                admin();
            }
        });

        btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!etPassword.getText().toString().equals("12Torpedo21")){
                    Toast.makeText(MainActivity.this, "Invalid Password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                user();
            }
        });
    }

    private void admin(){
        Intent intent = new Intent(getBaseContext(), AdminActivity.class);

        startActivity(intent);
    }

    private void user(){
        Intent intent = new Intent(getBaseContext(), SalePointActivity.class);

        startActivity(intent);
    }
}