package com.example.androidotextchanges;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AutofillActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autofill);

        Bundle bundle = getIntent().getExtras();
        setTitle(bundle.getCharSequence("buttonIntent"));
    }

    public void saveDone(View view) {
        super.onBackPressed();
    }
}
