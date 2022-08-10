package com.kosme.sjpqrcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.Result;
import com.kosme.sjpqrcode.api.Api;
import com.kosme.sjpqrcode.api.ApiInterface;
import com.kosme.sjpqrcode.model.ProductData;
import com.kosme.sjpqrcode.model.Replace;
import com.kosme.sjpqrcode.model.ResponseCheckUpdate;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView  mScannerView;
    FrameLayout camera;
    String id, code, bar, pd, ed;
    ResponseCheckUpdate test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        id = getIntent().getStringExtra("level");
        code = getIntent().getStringExtra("code");
        bar = getIntent().getStringExtra("old");
        camera = findViewById(R.id.frame_layout_camera);

        initScannerView();

    }


    @Override
    public void onResume(){
        super.onResume();
        initView();
    }

    private void initScannerView() {
        mScannerView = new ZXingScannerView(MainActivity.this);
        mScannerView.setAutoFocus(true);
        mScannerView.setResultHandler(this);
        camera.addView(mScannerView);
        mScannerView.startCamera();
    }

    @Override
    public void onStart() {
        mScannerView.startCamera();
        doRequestPermission();
        super.onStart();
    }

    private void doRequestPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initScannerView();
            } else {

            }
        }
    }

    @Override
    public void onPause(){
        mScannerView.stopCamera();
        super.onPause();
    }

    @Override
    public void handleResult(Result rawResult) {

        if (code != null && code.equals("reject")) {
            Intent i = new Intent(MainActivity.this, RejectActivity.class);
            i.putExtra("barcode", replaceString(rawResult.getText()));
            startActivity(i);
        } else if (code != null && code.equals("qc")){
            Intent i = new Intent(MainActivity.this, CheckerActivity.class);
            i.putExtra("barcode", replaceString(rawResult.getText()));
            startActivity(i);
        } else {
            if (id.equals("4")){
                Intent i = new Intent(MainActivity.this, PaletActivity.class);
                i.putExtra("barcode", replaceString(rawResult.getText()));
                startActivity(i);
            } else if (id.equals("3")){
                Intent i = new Intent(MainActivity.this, MasterBoxActivity.class);
                i.putExtra("barcode", replaceString(rawResult.getText()));
                startActivity(i);
            } else if (id.equals("2")){
                Intent i = new Intent(MainActivity.this, InnerboxActivity.class);
                i.putExtra("barcode", replaceString(rawResult.getText()));
                startActivity(i);
            } else if (id.equals("1")){
                Intent i = new Intent(MainActivity.this, ProductActivity.class);
                i.putExtra("barcode", replaceString(rawResult.getText()));
                startActivity(i);
            }
        }

//        if (code != null && code.equals("reject")) {
//            Intent i = new Intent(MainActivity.this, RejectActivity.class);
//            i.putExtra("barcode", rawResult.getText());
//            startActivity(i);
//        } else if (code != null && code.equals("qc")){
//            Intent i = new Intent(MainActivity.this, CheckerActivity.class);
//            i.putExtra("barcode", rawResult.getText());
//            startActivity(i);
//        } else {
//            if (id.equals("4")){
//                Intent i = new Intent(MainActivity.this, PaletActivity.class);
//                i.putExtra("barcode", rawResult.getText());
//                startActivity(i);
//            } else if (id.equals("3")){
//                Intent i = new Intent(MainActivity.this, MasterBoxActivity.class);
//                i.putExtra("barcode", rawResult.getText());
//                startActivity(i);
//            } else if (id.equals("2")){
//                Intent i = new Intent(MainActivity.this, InnerboxActivity.class);
//                i.putExtra("barcode", rawResult.getText());
//                startActivity(i);
//            } else if (id.equals("1")){
//                Intent i = new Intent(MainActivity.this, ProductActivity.class);
//                i.putExtra("barcode", rawResult.getText());
//                startActivity(i);
//            }
//        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    private void initView(){
        mScannerView.resumeCameraPreview(this::handleResult);
    }


    String replaceString(String string) {
        return string.replaceAll("[^a-zA-Z0-9-/,.]","");
    }

}