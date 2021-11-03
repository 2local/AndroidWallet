package com.android.l2l.twolocal.ui.scanner;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.l2l.twolocal.R;
import com.android.l2l.twolocal.model.enums.CryptoCurrencyType;
import com.android.l2l.twolocal.ui.wallet.send.SendTokenActivity;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

public class ScanFragment extends Fragment {

    boolean once = true;
    private CodeScanner mCodeScanner;
    private View view;
    private CodeScannerView scannerView;

    private void scan(@NotNull CodeScannerView scannerView, CodeScanner mCodeScanner, DecodeCallback decodeCallback) {
        mCodeScanner.setDecodeCallback(
                decodeCallback
        );
        scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Timber.i("onView stateRestored");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_scan, container, false);
        try {
            scannerView = view.findViewById(R.id.scanner_view);
            mCodeScanner = new CodeScanner(requireContext(), scannerView);
            scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
            scan(scannerView, mCodeScanner, result -> openSendActivity(result.getText()));

        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        return view;
    }


    private void requestPermission() {
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Timber.i("on permission method granted");
                        scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
                        mCodeScanner.startPreview();
                        if (once){
                            Toast.makeText(getContext(), "Tap to scan", Toast.LENGTH_SHORT).show();
                            once = false;
                        }
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Timber.i("on permission method denied");
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




    @Override
    public void onResume() {
        super.onResume();
        requestPermission();
    }

    @Override
    public void onPause() {
        if (mCodeScanner != null)
            mCodeScanner.releaseResources();
        super.onPause();

    }



    public void openSendActivity(String result) {
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            Intent i = new Intent(requireContext(), SendTokenActivity.class);
            Bundle b = new Bundle();
            b.putSerializable(SendTokenActivity.WALLET_TYPE_KEY, CryptoCurrencyType.TwoLC);
            b.putString(SendTokenActivity.WALLET_ADDRESS_KEY, result);
            i.putExtras(b);
            startActivity(i);
        }, 1000);
    }


}
