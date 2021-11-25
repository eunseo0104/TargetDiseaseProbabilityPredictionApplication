package com.example.research21;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        final String[] blood = {"혈액형 선택", "A", "B", "O", "AB"};
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, blood);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, MainActivity.class);
                String name = ((EditText)findViewById(R.id.name)).getText().toString();
                int age = Integer.parseInt(((EditText)findViewById(R.id.age)).getText().toString());
                int height = Integer.parseInt(((EditText)findViewById(R.id.age)).getText().toString());
                int weight = Integer.parseInt(((EditText)findViewById(R.id.age)).getText().toString());
                String blood = ((Spinner)findViewById(R.id.spinner)).getSelectedItem().toString();
                intent.putExtra("name", name);
                intent.putExtra("age", age);
                intent.putExtra("height", height);
                intent.putExtra("weight", weight);
                intent.putExtra("blood", blood);
                startActivity(intent);
                ActivityCompat.finishAffinity(UserActivity.this);
            }
        });
    }
}