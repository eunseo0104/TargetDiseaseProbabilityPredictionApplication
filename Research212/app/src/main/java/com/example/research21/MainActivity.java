package com.example.research21;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.research21.API_Service.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        TextView hello = (TextView)findViewById(R.id.hello);
        TextView obesity = findViewById(R.id.obesity);
        ImageView check = findViewById(R.id.check);
        TextView userHeight = findViewById(R.id.userHeight);
        TextView userAge = findViewById(R.id.userAge);
        TextView userWeight = findViewById(R.id.userWeight);
        TextView userBlood = findViewById(R.id.userBlood);

        String name = intent.getStringExtra("name");
        hello.setText(name+"님, 안녕하세요");
        userHeight.setText(""+intent.getDoubleExtra("height", 0.0)+"cm");
        userAge.setText(""+intent.getIntExtra("age", 0)+"세");
        userWeight.setText(""+intent.getDoubleExtra("weight", 0.0)+"Kg");
        userBlood.setText(intent.getStringExtra("blood")+"형");

        double height = intent.getDoubleExtra("height", 1);
        double weight = intent.getDoubleExtra("weight", 1);
        double o_rate = Math.round(weight/((height/100)*(height/100))*100)/100.0;
        String blood = intent.getStringExtra("blood");
        String obesityStr="";
        float checkX=0;
        if(o_rate<=18.5){
            obesityStr="저체중";
            checkX=-350f;

        }else if(o_rate<=23){
            obesityStr="정상";
            checkX=-130f;

        }else if(o_rate<=25){
            obesityStr="과체중";
            checkX=50f;

        }else if(o_rate<=30){
            obesityStr="비만";
            checkX=250f;
        }else{
            obesityStr="고도비만";
            checkX=430f;
        }
        check.setX(checkX);
        obesity.setText(name+"님의 BMI는 "+o_rate+"로 "+obesityStr+"입니다.");

        findViewById(R.id.fileOpenBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");

                //intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 10);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10){
            if(data!=null){
                Uri uri = data.getData();

                submitToServer(uri);
            }
        }

    }
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }
    private void submitToServer(Uri uri){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        }
        String path = Environment.getExternalStorageDirectory()+"/Download/"+getFileName(uri);

        Toast.makeText(getApplicationContext(), path, Toast.LENGTH_LONG).show();
        File file = new File(path);

        if(!file.exists()){
            Toast.makeText(getApplicationContext(), "nofile..", Toast.LENGTH_LONG).show();
            return;
        }

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part uploadFile = MultipartBody.Part.createFormData("file", path, requestFile);
        API_Service api_service = ApiClient.getApiClient().create(API_Service.class);
        Call<List<FileResponse>> call = api_service.uploads(uploadFile);
        call.enqueue(new Callback<List<FileResponse>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<FileResponse>> call, Response<List<FileResponse>> response) {
                List<FileResponse> body = response.body();
                int k = 0;
                if(body==null) {
                    Toast.makeText(getApplicationContext(), "no SNP..", Toast.LENGTH_LONG).show();
                }
                else {
                    for (FileResponse i : body) {
                        int id = getResources().getIdentifier("textView" + k, "id", "com.example.research21");
                        k++;
                        TextView t = (findViewById(id));
                        t.setText("geno" + k + "\nCount: " + i.gCount + "\nMax P.Value: " + i.pMax + "\nMin P.Value: " + i.pMin);
                    }
                }


            }

            @Override
            public void onFailure(Call<List<FileResponse>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();


            }
        });


    }
}
