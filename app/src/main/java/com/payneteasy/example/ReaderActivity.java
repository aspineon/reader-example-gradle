package com.payneteasy.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.WindowManager;
import android.widget.TextView;

import com.payneteasy.android.sdk.reader.CardReaderFactory;
import com.payneteasy.android.sdk.reader.CardReaderInfo;
import com.payneteasy.android.sdk.reader.CardReaderProblem;
import com.payneteasy.android.sdk.reader.ICardReaderManager;
import com.payneteasy.example.app1.R;
import com.payneteasy.example.util.ActivityUtil;
import com.payneteasy.paynet.processing.response.StatusResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class ReaderActivity extends Activity implements ICardView  {

    private final static Logger LOG = LoggerFactory.getLogger(ReaderActivity.class);

    private ICardReaderManager cardReaderManager;
    private TextView statusView;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // keep screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_reader);

        initUi();

        initReader();

        cardReaderManager.onActivityCreate(this, savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cardReaderManager.onActivityResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cardReaderManager.onActivityPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cardReaderManager.onActivityDestroy();
        cardReaderManager  = null;
    }

    private void initReader() {
        CardReaderInfo cardReader = ActivityUtil.getFirstParameter (this, CardReaderInfo.class);
        BigDecimal     amount     = ActivityUtil.getSecondParameter(this, BigDecimal.class);
        if(amount == null) {
            amount = new BigDecimal(3);
        }

        String currency = "RUB";

        SimpleCardReaderPresenter presenter = new SimpleCardReaderPresenter(this, getFilesDir());

        cardReaderManager = CardReaderFactory.findManager(this, cardReader, presenter, amount, currency, null);
    }

    private void initUi() {
        statusView = (TextView) findViewById(R.id.statusView);
    }

    @Override
    public void setStatusText(final String aText) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                statusView.setText(aText);
            }
        });
    }

    @Override
    public void stopReaderManager(StatusResponse aResponse) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("status-response", aResponse);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(20, intent);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(cardReaderManager != null) {
                    finish();
                }
            }
        }, 3000);
    }

    @Override
    public void stopReaderManager(CardReaderProblem aProblem) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("problem", aProblem);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(20, intent);
        finish();
    }
}
