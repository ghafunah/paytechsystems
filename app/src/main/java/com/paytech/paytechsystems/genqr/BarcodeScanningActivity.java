//package com.paytech.paytechsystems.genqr;
//
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.widget.Toast;
//import me.dm7.barcodescanner.zbar.Result;
//import me.dm7.barcodescanner.zbar.ZBarScannerView;
//
//public class BarcodeScanningActivity extends AppCompatActivity, ZBarScannerView.ResultHandler {
//    /*
//    * Scanner View that will create the layout for scanning a barcode.
//    * If you want a custom layout above the scanner layout, then implement
//    * the scanning code in a fragment and use the fragment inside the activity,
//    * add callbacks to obtain result from the fragment
//    * */
//    //private lateinit var mScannerView: ZBarScannerView
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mScannerView = ZBarScannerView(this);
//        setContentView(mScannerView);
//    }
//
//    /*
//    * It is required to start and stop camera in lifecycle methods
//    * (onResume and onPause)
//    * */
//    @Override
//    public  void onResume() {
//        super.onResume();
//        mScannerView.setResultHandler(this);
//        mScannerView.startCamera();
//    }
//
//    @Override
//    public  void onPause() {
//        super.onPause();
//        mScannerView.stopCamera();
//    }
//
//    /*
//    * Barcode scanning result is displayed here.
//    * (For demo purposes only toast is shown here)
//    * For understanding what more can be done with the result,
//    * visit the GitHub README(https://github.com/dm77/barcodescanner)
//    * */
//    @Override
//    public  void handleResult(result: Result?) {
//        Toast.makeText(this, result?.contents, Toast.LENGTH_SHORT).show();
//
//        //Camera will stop after scanning result, so we need to resume the
//        //preview in order scan more codes
//        mScannerView.resumeCameraPreview(this);
//    }
//}