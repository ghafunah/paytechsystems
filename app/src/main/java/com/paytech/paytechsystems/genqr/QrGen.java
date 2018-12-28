package com.paytech.paytechsystems.genqr;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.paytech.paytechsystems.R;

import static butterknife.internal.Utils.arrayOf;

public class QrGen extends AppCompatActivity implements View.OnClickListener {
    private Button scanningCodeButton, createCodeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrgen);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 0);
        }

        scanningCodeButton = findViewById(R.id.scanBarcodeButton);
        scanningCodeButton.setOnClickListener(this);
//        .OnClickListener {
//            Intent intent = Intent(QrGen.this, BarcodeScanningActivity.class);
//            startActivity(intent)
//        }

        createCodeButton = findViewById(R.id.createBarcodeButton);
        createCodeButton.setOnClickListener(this);
//        .OnClickListener {
//            Intent intent = Intent(QrGen.this, BarcodeGenerationActivity.class)
//            startActivity(intent)
//        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.scanBarcodeButton) {
            //postComment();
            //Intent intent = new Intent(QrGen.this, BarcodeScanningActivity.class);
            //startActivity(intent);
            Toast.makeText(this, "Enter action", Toast.LENGTH_SHORT).show();
        }
        if (i == R.id.createBarcodeButton){
            Intent intent = new Intent(QrGen.this, BarcodeGenerationActivity.class);
            startActivity(intent);
        }
    }

}