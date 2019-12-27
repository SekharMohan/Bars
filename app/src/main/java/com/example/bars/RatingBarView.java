package com.example.bars;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RatingBarView extends View {
    private int width;
    private int height;
    private float xPos;
    private float yPos;
    private Paint linePaint;
    private Paint lineSelectedPaint;
    private Paint linePathPaint;
    private Path path;
    private float lineLength;
    private float dividerLineWidth;
    private float viewDivider;


    private boolean isLeftAligned;
    private   float viewOffset ;
    private float dividerOffset;
    public RatingBarView(Context context) {
        super(context);
        init(null);
    }

    public RatingBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }
    public RatingBarView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);

    }

    private void init(AttributeSet attrs) {

        if(attrs != null && attrs.getAttributeCount() > 0) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RatingBarView);

            path = new Path();
            linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            linePaint.setColor(typedArray.getColor(R.styleable.RatingBarView_bar_color, Color.GRAY));
            linePaint.setStrokeWidth(typedArray.getDimension(R.styleable.RatingBarView_line_stroke, 1));
            lineSelectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            lineSelectedPaint.setColor(typedArray.getColor(R.styleable.RatingBarView_bar_selected_color, Color.BLACK));
            lineSelectedPaint.setStrokeWidth(typedArray.getDimension(R.styleable.RatingBarView_divider_stroke, 2));
            isLeftAligned = typedArray.getBoolean(R.styleable.RatingBarView_lefAligned, false);

            linePathPaint = new Paint();
            linePathPaint.setColor(typedArray.getColor(R.styleable.RatingBarView_bar_color, Color.GRAY));
            linePathPaint.setStrokeWidth(typedArray.getDimension(R.styleable.RatingBarView_line_stroke, 1));
            linePathPaint.setStyle(Paint.Style.STROKE);

            viewOffset = typedArray.getDimension(R.styleable.RatingBarView_bar_margin, 8);
            dividerOffset = typedArray.getDimension(R.styleable.RatingBarView_divider_margin, 25);

            typedArray.recycle();
        }

    }
    private void measureView() {
        width = getWidth();
        height = getHeight();
        xPos = isLeftAligned? 0 : dividerOffset;
        yPos = viewOffset;

if(isLeftAligned) {
    lineLength = width-50;
    viewDivider = lineLength + dividerOffset;
    dividerLineWidth = lineLength+dividerOffset;
    xPos = 0;

} else {
    lineLength = width;
    xPos = dividerOffset;
    viewDivider = 0;
    dividerLineWidth = lineLength;

}
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLine(canvas);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        measureView();
    }

    private void drawLine(Canvas canvas) {
        int count = 100;


        for(int i = 1; i <= count; i++) {

            canvas.drawLine(xPos, yPos, lineLength, yPos, linePaint);
            if( i % 10 == 0) {
                drawViewLeftDivider(canvas, viewDivider, yPos);
                yPos = yPos + viewOffset;
                canvas.drawLine(0, yPos, dividerLineWidth, yPos, lineSelectedPaint);

            }

            yPos =  yPos + viewOffset;




        }
    }

    public void setAnimation() {
        linePaint.setColor(Color.GREEN);
        measureView();
        postInvalidate();
    }


    private void drawViewLeftDivider(Canvas canvas, float x, float y) {
        path.moveTo(x, y);
        if(isLeftAligned) {
            path.lineTo(x - 20, y);
            path.lineTo(x - 20, y - 9 * viewOffset);
            path.lineTo(x, y - 9 * viewOffset);
        } else {

            path.lineTo(x +20, y);
            path.lineTo(x +20, y - 9 * viewOffset);
            path.lineTo(x, y - 9 * viewOffset);
        }
        canvas.drawPath(path, linePathPaint);
    }
}
