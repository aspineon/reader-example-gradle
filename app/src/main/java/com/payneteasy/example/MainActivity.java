package com.payneteasy.example;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.payneteasy.android.sdk.reader.CardReaderInfo;
import com.payneteasy.android.sdk.reader.CardReaderType;
import com.payneteasy.example.app1.R;
import com.payneteasy.example.util.ActivityUtil;

import java.math.BigDecimal;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startMiura(View aView) {
        ActivityUtil.startActivity(this
                , ReaderActivity.class
                , new CardReaderInfo("miura", CardReaderType.MIURA, null)
                , new BigDecimal("1.00")
        );
    }

    public void startSpire(View aView) {
        ActivityUtil.startActivity(this, ReaderActivity.class, new CardReaderInfo("spire", CardReaderType.SPIRE_SPM2, null));
    }


    public void startTest(View aView) {
        ActivityUtil.startActivity(this, ReaderActivity.class, CardReaderInfo.TEST);
    }
}
