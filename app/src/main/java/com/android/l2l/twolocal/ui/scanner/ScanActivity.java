package com.android.l2l.twolocal.ui.scanner;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.l2l.twolocal.R;
import com.android.l2l.twolocal.ui.base.BaseActivity;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import timber.log.Timber;

public class ScanActivity extends BaseActivity {


    public final static String KEY_BUNDLE_RESULT =  "result";
    boolean once = true;
    private CodeScanner mCodeScanner;
    private CodeScannerView scannerView;

    public static void start(Context context){
        Intent intent = new Intent(context, ScanActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_scan);
        scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra(KEY_BUNDLE_RESULT,result.getText());
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
        scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestPermission();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }


    private void requestPermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
                        mCodeScanner.startPreview();
                        if (once){
                            Toast.makeText(ScanActivity.this, "Tap to scan", Toast.LENGTH_SHORT).show();
                            once = false;
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                        scannerView.setOnClickListener(null);
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        Timber.i("on permission method should be shown");
                        scannerView.setOnClickListener(null);
                        token.continuePermissionRequest();
                    }
                }).check();
    }

}
