package com.example.androidotextchanges;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button fontsButton, autoSizeButton, autoFillButton;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fontsButton = findViewById(R.id.button_fonts);
        fontsButton.setOnClickListener(this);
        autoSizeButton = findViewById(R.id.button_auto_size);
        autoSizeButton.setOnClickListener(this);
        autoFillButton = findViewById(R.id.button_auto_fill);
        autoFillButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_fonts) {
            Intent fontsIntent = new Intent(this, FontsActivity.class);
            fontsIntent.putExtra("buttonIntent", fontsButton.getText());
            startActivity(fontsIntent);
        } else if (view.getId() == R.id.button_auto_size) {
            Intent autoSizeIntent = new Intent(this, AutoSizeActivity.class);
            autoSizeIntent.putExtra("buttonIntent", autoSizeButton.getText());
            startActivity(autoSizeIntent);
        } else if (view.getId() == R.id.button_auto_fill) {
            Intent autoFillIntent = new Intent(this, AutofillActivity.class);
            autoFillIntent.putExtra("buttonIntent", autoFillButton.getText());
            startActivity(autoFillIntent);
        }
    }
}
