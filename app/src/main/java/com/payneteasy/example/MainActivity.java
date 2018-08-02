package com.payneteasy.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.payneteasy.android.sdk.reader.CardErrorType;
import com.payneteasy.android.sdk.reader.CardReaderInfo;
import com.payneteasy.android.sdk.reader.CardReaderProblem;
import com.payneteasy.android.sdk.reader.CardReaderType;
import com.payneteasy.android.sdk.reader.CardReaderVersion;
import com.payneteasy.android.sdk.usb.UsbPermissionResolver;
import com.payneteasy.example.app1.R;
import com.payneteasy.example.util.ActivityUtil;
import com.payneteasy.paynet.processing.response.StatusResponse;

import java.math.BigDecimal;

public class MainActivity extends Activity {

    private final UsbPermissionResolver usbPermissionResolver = new UsbPermissionResolver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Example, sdk version " + CardReaderVersion.getSdkVersion());
        usbPermissionResolver.checkPermission(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        usbPermissionResolver.checkPermission(intent, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        usbPermissionResolver.unregister(this);
    }

    public void startMiura(View aView) {
        ActivityUtil.startActivityForResult(10
                , this
                , ReaderActivity.class
                , new CardReaderInfo("miura", CardReaderType.MIURA, null)
                , new BigDecimal("1.00")
        );
    }

    public void startSpire(View aView) {
        ActivityUtil.startActivityForResult(10, this, ReaderActivity.class, new CardReaderInfo("spire", CardReaderType.SPIRE_SPM2, null), null);
    }


    public void startTest(View aView) {
//        ActivityUtil.startActivityForResult(10, this, ReaderActivity.class, CardReaderInfo.TEST, null);
    }

    public void startVerifoneUsb(View aView) {
        ActivityUtil.startActivityForResult(10
                , this
                , ReaderActivity.class
                , new CardReaderInfo("verifone", CardReaderType.INPAS_VERIFONE_USB, null)
                , new BigDecimal("1.02")
        );
    }

    public void startPaxUsb(View aView) {
        ActivityUtil.startActivityForResult(10
                , this
                , ReaderActivity.class
                , new CardReaderInfo("pax", CardReaderType.INPAS_PAX_USB, null)
                , new BigDecimal("1.02")
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data == null) {
            return;
        }
        Bundle extras = data.getExtras();
        if(extras == null) {
            return;
        }

        StatusResponse     statusResponse = (StatusResponse) extras.get("status-response");
        CardReaderProblem  problem        = (CardReaderProblem) extras.get("problem");
        CardErrorType      errorType      = (CardErrorType) extras.get("card-error-type");

        String message;
        if(problem != null) {
            message = "Error: " + problem;
        } else if( statusResponse != null) {
            message = statusResponse.getOrderId() + " " + statusResponse.getStatus();
        } else if( errorType != null ) {
            message = "Error: " + errorType;
        } else {
            message = "Unknown error";
        }

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
