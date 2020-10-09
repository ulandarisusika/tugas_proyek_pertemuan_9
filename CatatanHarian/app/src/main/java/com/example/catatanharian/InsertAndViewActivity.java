package com.example.catatanharian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class InsertAndViewActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int REQUEST_CODE_STORAGE=100;
    int eventID=0;
    EditText editFilename, edtContent;
    Button btnSimpan;
    boolean isEditable=false;
    String filename = "";
    String TempCatatan= "";
    void bacaFile(){
        String path = Environment.getExternalStorageDirectory().toString() + "/kominfo.proyek1";
        File file = new File(path, editFilename.getText().toString());
        if (file.exists()){
            StringBuilder text = new StringBuilder();

            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line = br.readLine();
                while (line != null){
                    text.append(line);
                    line = br.readLine();
                }
                br.close();
            }catch (IOException e){
                System.out.println("Error "+e.getMessage());
            }
            TempCatatan = text.toString();
            edtContent.setText(text.toString());
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_and_view);
//        Toolbar toolbar=findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        editFilename = findViewById(R.id.editFilename);
        edtContent = findViewById(R.id.editContent);
        btnSimpan = findViewById(R.id.btnSimpan);

        btnSimpan.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            filename = extras.getString("filename");
            editFilename.setText(filename);
            getSupportActionBar().setTitle("Ubah Catatan");
        }else{
            getSupportActionBar().setTitle("Tambah Catatan");
        }
        eventID = 1;
        if (Build.VERSION.SDK_INT>=23){
            if (periksaizinpenyimpanan()){
                bacaFile();
            }
        }else{
            bacaFile();
        }
    }
    void OnBackPressed(){
        if(!TempCatatan.equals(edtContent.getText().toString())){
            tampilkanDialogKonfirmasiPenyimpanan();
        }
        super.onBackPressed();
    }
    final void buatdanUbah(){
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)){
            return;
        }
        String path = Environment.getExternalStorageDirectory().toString()+"/kominfo.proyek1";
        File parent = new File(path);
        if (parent.exists()){
            File file = new File(path,editFilename.getText().toString());
            FileOutputStream outputStream=null;
            try {
                file.createNewFile();
                outputStream = new FileOutputStream(file);
                OutputStreamWriter streamWriter = new OutputStreamWriter(outputStream);
                streamWriter.append(edtContent.getText());
                streamWriter.flush();
                streamWriter.close();
                outputStream.flush();
                outputStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            parent.mkdir();
            File file = new File(path, editFilename.getText().toString());
            FileOutputStream outputStream = null;
            try {
                file.createNewFile();
                outputStream = new FileOutputStream(file,false);
                outputStream.write(edtContent.getText().toString().getBytes());
                outputStream.flush();
                outputStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        OnBackPressed();
    }
    void tampilkanDialogKonfirmasiPenyimpanan(){
        new AlertDialog.Builder(this)
                .setTitle("Simpan Catatan")
                .setMessage("Apakah Anda Yakin ingin menyimpan catatan ini ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        buatdanUbah();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }
    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnSimpan:
            eventID=2;
            if (!TempCatatan.equals(edtContent.getText().toString())){
                if (Build.VERSION.SDK_INT >=23){
                    if (periksaizinpenyimpanan()){
                        tampilkanDialogKonfirmasiPenyimpanan();
                    }
                }else{
                    tampilkanDialogKonfirmasiPenyimpanan();
                }
            }
            break;
        }
    }
    public boolean periksaizinpenyimpanan(){
        if (Build.VERSION.SDK_INT>=23){
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                return true;
            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CODE_STORAGE);
                return false;
            }
        }else{
            return true;
        }
    }
    public  void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult){
        super.onRequestPermissionsResult(requestCode,permissions,grantResult);
        switch (requestCode){
            case REQUEST_CODE_STORAGE:
                if (grantResult[0]==PackageManager.PERMISSION_GRANTED){
                    if (eventID==1){
                        bacaFile();
                    }else{
                        tampilkanDialogKonfirmasiPenyimpanan();
                    }
                }
                break;
        }






    }
}