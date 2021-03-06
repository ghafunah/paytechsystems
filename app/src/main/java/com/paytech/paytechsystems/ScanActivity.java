package com.paytech.paytechsystems;

 
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;
 
import com.google.android.gms.vision.barcode.Barcode;
import com.paytech.paytechsystems.R;

import java.util.List;
 
import info.androidhive.barcode.BarcodeReader;
import timber.log.Timber;

public class ScanActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {
 
    BarcodeReader barcodeReader;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
 
        // get the barcode reader instance
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_scanner);
    }
 
    @Override
    public void onScanned(Barcode barcode) {
 
        // playing barcode reader beep sound
        barcodeReader.playBeep();

        //Toast.makeText(getApplicationContext(), "Barcode : " + barcode.displayValue, Toast.LENGTH_LONG).show();
        // ticket details activity by passing barcode
        //Log.d(getApplicationContext(), "Barcode is : "+ barcode.displayValue);
        Timber.d("Barcode is %s", barcode.displayValue);

        Intent intent = new Intent(ScanActivity.this, TicketResultActivity.class);
        intent.putExtra("code", barcode.displayValue);
        startActivity(intent);
    }
 
    @Override
    public void onScannedMultiple(List<Barcode> list) {
 
    }
 
    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {
 
    }
 
//    @Override
//    public void onCameraPermissionDenied() {
//        finish();
//    }
//
    @Override
    public void onScanError(String s) {
        Toast.makeText(getApplicationContext(), "Error occurred while scanning " + s, Toast.LENGTH_SHORT).show();
    }
}