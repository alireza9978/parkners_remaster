package me.coleo.snapion.Activities;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import me.coleo.snapion.R;

public class SupportActivity extends AppCompatActivity {

    TextView descTv, phoneTv, titleTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        phoneTv = findViewById(R.id.phoneTV);
        phoneTv.setText(Html.fromHtml("<u>" + phoneTv.getText().toString() + "</u>"));
    }
}
