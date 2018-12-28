package com.paytech.paytechsystems.genqr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.paytech.paytechsystems.R;
import net.glxn.qrgen.android.QRCode;

public class BarcodeGenerationActivity extends AppCompatActivity implements View.OnClickListener {
    private  EditText contentEditText;
    private Button generateButton;
    private  ImageView generationImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qrgenerate);

        contentEditText = findViewById(R.id.contentEditText);
        generateButton = findViewById(R.id.generateButton);
        generationImageView = findViewById(R.id.generationImageView);
        //Dummy data for me

        contentEditText.setText("770680112181227123456123");

        generateButton.setOnClickListener(this);

     }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.generateButton) {

            String text = contentEditText.getText().toString();

            if (text.isEmpty()) {
                Toast.makeText(this, "Enter something to create a barcode", Toast.LENGTH_SHORT).show();
                return;
            }
            Bitmap bitmap = QRCode.from(text).withColor(0xFFFF0000, 0xFFFFFFAA).withSize(1000, 1000).bitmap();
            generationImageView.setImageBitmap(bitmap);
        }
    }
}