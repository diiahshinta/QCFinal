package com.kosme.sjpqrcode.msglow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.FrameLayout;
import com.google.zxing.Result;
import com.kosme.sjpqrcode.R;
import com.kosme.sjpqrcode.api.Api;
import com.kosme.sjpqrcode.api.ApiInterface;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView  mScannerView;
    FrameLayout camera;
    ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);


        apiInterface = Api.getClient().create(ApiInterface.class);

        camera = findViewById(R.id.frame_layout_camera);

        initScannerView();

    }


    @Override
    public void onResume(){
        super.onResume();
        initView();
    }

    private void initScannerView() {
        mScannerView = new ZXingScannerView(ScannerActivity.this);
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

        Intent i = new Intent(ScannerActivity.this, MsglowActivity.class);
        i.putExtra("barcode", rawResult.getText());
        startActivity(i);

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    private void initView(){
        mScannerView.resumeCameraPreview(this::handleResult);
    }
}