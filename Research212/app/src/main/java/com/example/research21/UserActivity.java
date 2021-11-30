package com.example.research21;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class UserActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        sp= PreferenceManager.getDefaultSharedPreferences(UserActivity.this);
        editor=sp.edit();

        final String[] blood = {"혈액형 선택", "A", "B", "O", "AB"};
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, blood);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        ((EditText)findViewById(R.id.name)).setText(sp.getString("name", ""));
        ((EditText)findViewById(R.id.age)).setText(""+sp.getInt("age", 0));
        ((EditText)findViewById(R.id.height)).setText(""+sp.getFloat("height", 0));
        ((EditText)findViewById(R.id.weight)).setText(""+sp.getFloat("weight", 0));
        ((Spinner) findViewById(R.id.spinner)).setSelection(sp.getInt("blood", 0));

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
                String sage=((EditText)findViewById(R.id.age)).getText().toString();
                String sheight = ((EditText)findViewById(R.id.height)).getText().toString();
                String sweight = ((EditText)findViewById(R.id.weight)).getText().toString();
                String blood = ((Spinner) findViewById(R.id.spinner)).getSelectedItem().toString();
                if(sage.equals("")||sheight.equals("")||sweight.equals("")||name.equals("")||blood.equals("혈액형 선택")){
                    Toast.makeText(getApplicationContext(), "모든 값을 채워주세요.", Toast.LENGTH_LONG).show();
                }else {
                    int age = Integer.parseInt(((EditText) findViewById(R.id.age)).getText().toString());
                    double height = Double.parseDouble(((EditText) findViewById(R.id.height)).getText().toString());
                    double weight = Double.parseDouble(((EditText) findViewById(R.id.weight)).getText().toString());


                    editor.putString("name", name);
                    editor.putInt("age", age);
                    editor.putFloat("height", (float)height);
                    editor.putFloat("weight", (float)weight);
                    editor.putInt("blood", ((Spinner)findViewById(R.id.spinner)).getSelectedItemPosition());
                    editor.apply();

                    intent.putExtra("name", name);
                    intent.putExtra("age", age);
                    intent.putExtra("height", height);
                    intent.putExtra("weight", weight);
                    intent.putExtra("blood", blood);
                    startActivity(intent);
                    ActivityCompat.finishAffinity(UserActivity.this);
                }
            }
        });
    }
}