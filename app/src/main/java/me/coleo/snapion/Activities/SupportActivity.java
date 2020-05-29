package me.coleo.snapion.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import me.coleo.snapion.R;
import me.coleo.snapion.constants.Constants;
import me.coleo.snapion.constants.FormatHelper;
import me.coleo.snapion.server.ServerClass;

/**
 * صفحه‌ی پشتیبانی
 */
public class SupportActivity extends AppCompatActivity {

    TextView telegram, phoneTv;
    EditText commentET;
    Button sendCommentBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        sendCommentBtn = findViewById(R.id.supportSendBtn);
        commentET = findViewById(R.id.commentET);
        phoneTv = findViewById(R.id.phoneTV);
        telegram = findViewById(R.id.telegram_box);


        Typeface font = ResourcesCompat.getFont(getBaseContext(), R.font.vazir);
        String start = "پارکنرز رو در تلگرام دنبال کن :";
        String link = "t.me/parkners";
        SpannableString startSpannableString = new SpannableString(start);
        SpannableString linkSpannableString = new SpannableString(link);


        linkSpannableString.setSpan(new MyClickableSpan(link), 0, linkSpannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        linkSpannableString.setSpan(new ForegroundColorSpan(Color.BLUE), 0, linkSpannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        linkSpannableString.setSpan(new UnderlineSpan(), 0, linkSpannableString.length(), 33);
        linkSpannableString.setSpan(new CustomTypefaceSpan("", font), 0, linkSpannableString.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        startSpannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, startSpannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        startSpannableString.setSpan(new CustomTypefaceSpan("", font), 0, startSpannableString.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        telegram.append(startSpannableString);
        telegram.append(" ");
        telegram.append(linkSpannableString);

        telegram.setMovementMethod(LinkMovementMethod.getInstance());

        phoneTv.setText(Html.fromHtml("<u>" + FormatHelper.toPersianNumber(Constants.SUPPORT_PHONE_NUMBER) + "</u>"));
        phoneTv.setOnClickListener(v ->

        {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Constants.SUPPORT_PHONE_NUMBER));
            startActivity(intent);
        });
        sendCommentBtn.setOnClickListener(view ->

                uploadComment());
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

    static class CustomTypefaceSpan extends TypefaceSpan {

        private final Typeface newType;

        public CustomTypefaceSpan(String family, Typeface type) {
            super(family);
            newType = type;
        }

        private static void applyCustomTypeFace(Paint paint, Typeface tf) {
            int oldStyle;
            Typeface old = paint.getTypeface();
            if (old == null) {
                oldStyle = 0;
            } else {
                oldStyle = old.getStyle();
            }

            int fake = oldStyle & ~tf.getStyle();
            if ((fake & Typeface.BOLD) != 0) {
                paint.setFakeBoldText(true);
            }

            if ((fake & Typeface.ITALIC) != 0) {
                paint.setTextSkewX(-0.25f);
            }

            paint.setTypeface(tf);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            applyCustomTypeFace(ds, newType);
        }

        @Override
        public void updateMeasureState(TextPaint paint) {
            applyCustomTypeFace(paint, newType);
        }
    }

    class MyClickableSpan extends ClickableSpan {

        public MyClickableSpan(String string) {
            super();
        }

        public void onClick(View tv) {
            Intent telegramIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tg://resolve?domain=parkners"));
            startActivity(telegramIntent);
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(false); // set to false to remove underline
        }
    }

}
