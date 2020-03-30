package me.coleo.snapion.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import me.coleo.snapion.R;
import me.coleo.snapion.constants.Constants;
import me.coleo.snapion.constants.FormatHelper;
import me.coleo.snapion.server.ServerClass;

/**
 * صفحه‌ی پشتیبانی
 */
public class SupportActivity extends AppCompatActivity {

    TextView descTv, phoneTv, titleTV;
    EditText commentET;
    Button sendCommentBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        sendCommentBtn = findViewById(R.id.supportSendBtn);
        commentET = findViewById(R.id.commentET);
        phoneTv = findViewById(R.id.phoneTV);
        phoneTv.setText(Html.fromHtml("<u>" + FormatHelper.toPersianNumber(Constants.SUPPORT_PHONE_NUMBER) + "</u>"));
        phoneTv.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Constants.SUPPORT_PHONE_NUMBER));
            startActivity(intent);
        });
        sendCommentBtn.setOnClickListener(view -> uploadComment());
    }

    /**
     * ارسال نظر به سرور
     */
    private void uploadComment() {
        String text = commentET.getText().toString();
        ServerClass.sendComment(this, text);
    }

    /**
     * نمایش تاییدیه ارسال و بستن صفحه
     */
    public void sent() {
        Toast.makeText(this, "ممنون، تیم پشتیبانی پیام شما را بررسی می کند.", Toast.LENGTH_LONG).show();
        finish();
    }

}
