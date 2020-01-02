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
    private float linePathMargin;

    private int totalUnit;
    private int selectedUnit;


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
            linePathMargin = getResources().getDimension(R.dimen.draw_path_margin);
            path = new Path();
            linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            linePaint.setAntiAlias(true);
            linePaint.setColor(typedArray.getColor(R.styleable.RatingBarView_bar_color, Color.GRAY));
            linePaint.setStrokeWidth(typedArray.getDimension(R.styleable.RatingBarView_line_stroke, 1));
            linePaint.setStyle(Paint.Style.STROKE);

            lineSelectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            lineSelectedPaint.setAntiAlias(true);
            lineSelectedPaint.setColor(typedArray.getColor(R.styleable.RatingBarView_bar_selected_color, Color.BLACK));
            lineSelectedPaint.setStrokeWidth(typedArray.getDimension(R.styleable.RatingBarView_divider_stroke, 1));
            lineSelectedPaint.setStyle(Paint.Style.STROKE);

            isLeftAligned = typedArray.getBoolean(R.styleable.RatingBarView_lefAligned, false);

            linePathPaint = new Paint();
            linePathPaint.setAntiAlias(true);
            linePathPaint.setColor(typedArray.getColor(R.styleable.RatingBarView_bar_color, Color.GRAY));
            linePathPaint.setStrokeWidth(typedArray.getDimension(R.styleable.RatingBarView_line_stroke, 2));
            linePathPaint.setStyle(Paint.Style.STROKE);

            viewOffset = typedArray.getDimension(R.styleable.RatingBarView_bar_margin, 8);
            dividerOffset = typedArray.getDimension(R.styleable.RatingBarView_divider_margin, 25);

            totalUnit = typedArray.getInteger(R.styleable.RatingBarView_total_unit, 100);
            selectedUnit = typedArray.getInteger(R.styleable.RatingBarView_selected_unit, 40);

            typedArray.recycle();
        }

    }
    private void measureView() {
        width = getWidth();
        height = getHeight();
        yPos = height-viewOffset;

if(isLeftAligned) {
    lineLength = width -dividerOffset;
    dividerLineWidth = lineLength+dividerOffset;
    viewDivider = dividerLineWidth;
    xPos = 0;

} else {
    lineLength = width;
    xPos = dividerOffset;
    dividerLineWidth = lineLength;
    viewDivider = 0;

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



        for(int i = 1; i <= totalUnit; i++) {

            canvas.drawLine(xPos, yPos, lineLength, yPos, i<=selectedUnit?lineSelectedPaint:linePaint);
            if( i % 10 == 0 ) {
                drawViewLeftDivider(canvas, viewDivider, yPos);
                if(i != totalUnit) {
                    yPos = yPos - viewOffset;
                    canvas.drawLine(0, yPos, dividerLineWidth, yPos, linePathPaint);
                }

            }

            yPos =  yPos - viewOffset;




        }
    }

    public void setSelectedUnit(int selectedUnit) {
        this.selectedUnit = selectedUnit;
        measureView();
        postInvalidate();
    }


    private void drawViewLeftDivider(Canvas canvas, float x, float y) {
        path.moveTo(x, y);
        if(isLeftAligned) {
            path.lineTo(x - linePathMargin, y);
            path.lineTo(x - linePathMargin, y + 9 * viewOffset);
            path.lineTo(x, y + 9 * viewOffset);
        } else {

            path.lineTo(x +linePathMargin, y);
            path.lineTo(x +linePathMargin, y + 9 * viewOffset);
            path.lineTo(x, y + 9 * viewOffset);
        }
        canvas.drawPath(path, linePathPaint);
    }
}
