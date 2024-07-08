package com.example.timemanagementtool;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    EditText name, num, Email, Pass;
    Button Reg;
    TextView btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);

        name = findViewById(R.id.name);
        num = findViewById(R.id.num);
        Email = findViewById(R.id.Email);
        Pass = findViewById(R.id.Pass);

        Reg = findViewById(R.id.Reg);
        btn1 = findViewById(R.id.btn1);

        Reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name, Num, Mail, passw;

                Name = name.getText().toString();
                Num = num.getText().toString();
                Mail = Email.getText().toString();
                passw = Pass.getText().toString();

                if (Name.equals(""));{
                    Toast.makeText(Register.this, "Registered Successful", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent R = new Intent(Register.this, MainActivity.class);
                startActivity(R);
                finish();
            }
        });


    }
}