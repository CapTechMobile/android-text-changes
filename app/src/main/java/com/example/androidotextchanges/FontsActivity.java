package com.example.androidotextchanges;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.provider.FontRequest;
import android.support.v4.provider.FontsContractCompat;
import android.support.v4.util.ArraySet;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class FontsActivity extends AppCompatActivity {

    static final int WEIGHT_MAX = 1000;

    private static final String TAG = "FontsActivity";

    private Handler mHandler = null;

    private TextView mDownloadableFontTextView;
    private SeekBar mWeightSeekBar;
    private SeekBar mItalicSeekBar;
    private Button mRequestDownloadButton;

    private ArraySet<String> mFamilyNameSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fonts);

        setTitle(getIntent().getExtras().getCharSequence("buttonIntent"));

        initializeSeekBars();
        mFamilyNameSet = new ArraySet<>();
        mFamilyNameSet.addAll(Arrays.asList(getResources().getStringArray(R.array.family_names)));

        mDownloadableFontTextView = findViewById(R.id.textview);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line,
                getResources().getStringArray(R.array.family_names));
        final AutoCompleteTextView autoCompleteFamilyName = findViewById(
                R.id.auto_complete_family_name);
        autoCompleteFamilyName.setAdapter(adapter);

        mRequestDownloadButton = findViewById(R.id.button_request);
        mRequestDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String familyName = autoCompleteFamilyName.getText().toString();
                if (!isValidFamilyName(familyName)) {
                    Toast.makeText(
                            FontsActivity.this,
                            "Invalid Family Name",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                requestDownload(familyName);
                mRequestDownloadButton.setEnabled(false);
            }
        });
    }

    private void requestDownload(String familyName) {
        QueryBuilder queryBuilder = new QueryBuilder(familyName)
                .withWidth(1)
                .withWeight(progressToWeight(mWeightSeekBar.getProgress()))
                .withItalic(progressToItalic(mItalicSeekBar.getProgress()))
                .withBestEffort(true);
        String query = queryBuilder.build();

        Log.d(TAG, "Requesting a font. Query: " + query);
        FontRequest request = new FontRequest(
                "com.google.android.gms.fonts",
                "com.google.android.gms",
                query,
                R.array.com_google_android_gms_fonts_certs);

        final ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        FontsContractCompat.FontRequestCallback callback = new FontsContractCompat
                .FontRequestCallback() {
            @Override
            public void onTypefaceRetrieved(Typeface typeface) {
                mDownloadableFontTextView.setTypeface(typeface);
                progressBar.setVisibility(View.GONE);
                mRequestDownloadButton.setEnabled(true);
            }

            @Override
            public void onTypefaceRequestFailed(int reason) {
                Toast.makeText(FontsActivity.this,
                        getString(R.string.request_failed, reason), Toast.LENGTH_LONG)
                        .show();
                progressBar.setVisibility(View.GONE);
                mRequestDownloadButton.setEnabled(true);
            }
        };
        FontsContractCompat
                .requestFont(FontsActivity.this, request, callback,
                        getHandlerThreadHandler());
    }

    private void initializeSeekBars() {
        mWeightSeekBar = findViewById(R.id.seek_bar_weight);
        float weightValue = (float) 0 / (float) WEIGHT_MAX * 100;
        mWeightSeekBar.setProgress((int) weightValue);
        final TextView weightTextView = findViewById(R.id.textview_weight);
        weightTextView.setText("0");
        mWeightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                weightTextView
                        .setText(String.valueOf(progressToWeight(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mItalicSeekBar = findViewById(R.id.seek_bar_italic);
        mItalicSeekBar.setProgress((int) 0);
        final TextView italicTextView = findViewById(R.id.textview_italic);
        italicTextView.setText("0");
        mItalicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromuser) {
                italicTextView
                        .setText(String.valueOf(progressToItalic(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private boolean isValidFamilyName(String familyName) {
        return familyName != null && mFamilyNameSet.contains(familyName);
    }

    private Handler getHandlerThreadHandler() {
        if (mHandler == null) {
            HandlerThread handlerThread = new HandlerThread("fonts");
            handlerThread.start();
            mHandler = new Handler(handlerThread.getLooper());
        }
        return mHandler;
    }

    /**
     * Converts progress from a SeekBar to the value of weight.
     * @param progress is passed from 0 to 100 inclusive
     * @return the converted weight
     */
    private int progressToWeight(int progress) {
        if (progress == 0) {
            return 1;
        } else if (progress == 100) {
            return WEIGHT_MAX - 1;
        } else {
            return WEIGHT_MAX * progress / 100;
        }
    }

    /**
     * Converts progress from a SeekBar to the value of italic.
     * @param progress is passed from 0 to 100 inclusive.
     * @return the converted italic
     */
    private float progressToItalic(int progress) {
        return (float) progress / 100f;
    }
}
