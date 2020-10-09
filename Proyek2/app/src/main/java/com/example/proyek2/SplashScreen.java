package com.example.proyek2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.io.File;

public class SplashScreen extends AppCompatActivity {
    public static final String FILENAME="login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isLogin()){
                    Intent intent =  new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent =  new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        },3000);
    }
    boolean isLogin(){
        File sdcard=getFilesDir();
        File file=new File(sdcard, FILENAME);
        if (file.exists()){
            return true;
        }else{
            return false;
        }
    }
}