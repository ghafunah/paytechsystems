package com.paytech.paytechsystems.genqr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.paytech.paytechsystems.R;
import net.glxn.qrgen.android.QRCode;

import java.util.Arrays;

public class BarcodeGenerationActivity extends AppCompatActivity implements View.OnClickListener {
    private  EditText contentEditText;
    private Button generateBarcode, generateQr;
    private  ImageView generationImageView, generationImageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrgenerate);

        contentEditText = findViewById(R.id.contentEditText);
        generateQr = findViewById(R.id.generateQr);
        generateBarcode = findViewById(R.id.generateBarcode);
        generationImageView = findViewById(R.id.generationImageView);

        //Dummy data for me
        contentEditText.setText("770680112181227123456123");

        generateQr.setOnClickListener(this);
        generateBarcode.setOnClickListener(this);

     }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        String text = contentEditText.getText().toString();

        if (text.isEmpty()) {
            Toast.makeText(this, "Enter something to create a barcode", Toast.LENGTH_SHORT).show();
            return;
        }
        if (i == R.id.generateBarcode) {
            try {
                Bitmap barcode = createBarcodeBitmap(text, 600,150);
                generationImageView.setImageBitmap(barcode);
            }catch (WriterException e){
                Toast.makeText(this, "Barcode Error :" + e, Toast.LENGTH_SHORT).show();
            }
        }

        if (i == R.id.generateQr) {
            Bitmap bitmap = QRCode.from(text).withColor(0xFFFF0000, 0xFFFFFFAA).withSize(1000, 1000).bitmap();
            generationImageView.setImageBitmap(bitmap);
        }
    }

    private Bitmap createBarcodeBitmap(String data, int width, int height) throws WriterException {
        MultiFormatWriter writer = new MultiFormatWriter();
        String finalData = Uri.encode(data);

        // Use 1 as the height of the matrix as this is a 1D Barcode.
        BitMatrix bm = writer.encode(finalData, BarcodeFormat.CODE_128, width, 1);
        int bmWidth = bm.getWidth();

        Bitmap imageBitmap = Bitmap.createBitmap(bmWidth, height, Bitmap.Config.ARGB_8888);

        for (int i = 0; i < bmWidth; i++) {
            // Paint columns of width 1
            int[] column = new int[height];
            Arrays.fill(column, bm.get(i, 0) ? Color.BLACK : Color.WHITE);
            imageBitmap.setPixels(column, 0, 1, i, 0, 1, height);
        }
        return imageBitmap;
    }

//    private Bitmap createBarcodeBitmap(String data, int width, int height) throws WriterException {
//        MultiFormatWriter writer = new MultiFormatWriter();
//        String finalData = Uri.encode(data);
//
//        // Use 1 as the height of the matrix as this is a 1D Barcode.
//        BitMatrix bm = writer.encode(finalData, BarcodeFormat.CODE_128, width, 1);
//        int bmWidth = bm.getWidth();
//
//        Bitmap imageBitmap = Bitmap.createBitmap(bmWidth, height, Config.ARGB_8888);
//
//        for (int i = 0; i < bmWidth; i++) {
//            // Paint columns of width 1
//            int[] column = new int[height];
//            Arrays.fill(column, bm.get(i, 0) ? Color.BLACK : Color.WHITE);
//            imageBitmap.setPixels(column, 0, 1, i, 0, 1, height);
//        }
//
//        return imageBitmap;
//    }
}