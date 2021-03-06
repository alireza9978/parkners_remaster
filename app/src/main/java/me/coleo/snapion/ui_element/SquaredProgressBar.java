package me.coleo.snapion.ui_element;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import me.coleo.snapion.R;

/**
 * کتابخانه برای نمایش پر بودن پارکینگ
 */
public class SquaredProgressBar extends View {

    private int[] colors = new int[6];

    private int defaultWidth = getResources().getDimensionPixelSize(R.dimen.squared_progress_bar_default_width);
    private int defaultHeight = getResources().getDimensionPixelSize(R.dimen.squared_progress_bar_default_height);

    private final String TAG = "DEBUG";
    private int squareCount;
    private float progress;
    private int fillCount = 0;
    private Paint paintFull = new Paint();
    private Paint paintEmpty = new Paint();

    public SquaredProgressBar(Context context) {
        super(context);
    }

    public SquaredProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SquaredProgressBar);
        squareCount = typedArray.getInt(R.styleable.SquaredProgressBar_square_count, 2);
        typedArray.recycle();
        colors[0] = R.color.progress_bar_zero;
        colors[1] = R.color.progress_bar_one;
        colors[2] = R.color.progress_bar_two;
        colors[3] = R.color.progress_bar_three;
        colors[4] = R.color.progress_bar_four;
        colors[5] = R.color.progress_bar_five;
    }

    public SquaredProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(float progress) {
        this.progress = progress;
        float temp = 100f;
        temp /= squareCount;
        float state = progress / temp;
        fillCount = (int) Math.ceil(state);
        paintFull.setColor(getResources().getColor(colors[fillCount]));
        paintEmpty.setColor(getResources().getColor(colors[0]));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = defaultWidth;
        int height = defaultHeight;

        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                width = widthSize;
                break;
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                width = defaultWidth;
                break;
        }
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                height = defaultHeight;
                break;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float squareSize = ((getWidth() - (2 * getHeight())) * 0.8f) / squareCount;
        float spaceSize = ((getWidth() - (2 * getHeight())) * 0.2f) / (squareCount - 1);

        float startX = 0f;
        float endX = startX + squareSize;
        float startY = 0f;
        float endY = getHeight();
        Paint paint;
        for (int i = squareCount - 1; i > -1; i--) {
            if (i < fillCount)
                paint = paintFull;
            else
                paint = paintEmpty;
            drawShape(startX, startY, endX, endY, canvas, paint);
            startX = startX + squareSize + spaceSize;
            endX = startX + squareSize;
        }

        super.onDraw(canvas);
    }

    private void drawShape(float startX, float startY, float width, float height, Canvas canvas, Paint paint) {
        RectF rectF = new RectF(startX, startY, startX + height, startY + height);
        canvas.drawArc(rectF, 90f, 180f, true, paint);
        canvas.drawRect(startX + (height / 2), startY, width + (height / 2), height, paint);
        rectF = new RectF(width - height, startY, width + height, startY + height);
        canvas.drawArc(rectF, 270f, 180f, true, paint);
    }

}
