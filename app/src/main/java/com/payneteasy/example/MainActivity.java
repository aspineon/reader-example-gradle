package com.payneteasy.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.payneteasy.android.sdk.reader.CardReaderInfo;
import com.payneteasy.android.sdk.reader.CardReaderProblem;
import com.payneteasy.android.sdk.reader.CardReaderType;
import com.payneteasy.example.app1.R;
import com.payneteasy.example.util.ActivityUtil;
import com.payneteasy.paynet.processing.response.StatusResponse;

import java.math.BigDecimal;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        ActivityUtil.startActivityForResult(10, this, ReaderActivity.class, CardReaderInfo.TEST, null);
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
        StatusResponse statusResponse = (StatusResponse) extras.get("status-response");
        CardReaderProblem  problem = (CardReaderProblem) extras.get("problem");

        if(problem != null) {
            Toast.makeText(this, "Error: " + problem, Toast.LENGTH_LONG)
                    .show();
        } else if( statusResponse != null) {
            Toast.makeText(this, statusResponse.getOrderId() + " " + statusResponse.getStatus(), Toast.LENGTH_LONG)
                    .show();
        }
    }
}
