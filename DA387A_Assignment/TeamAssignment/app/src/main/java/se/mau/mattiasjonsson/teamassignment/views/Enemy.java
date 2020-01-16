package se.mau.mattiasjonsson.teamassignment.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class Enemy extends View {
    private Paint mPaint;
    private float size;

    public Enemy(Context context) {
        super(context);
        init();
    }

    public Enemy(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Enemy(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public Enemy(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init(){
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
    }

    public float getSize() {return size;}

    public void setSize(float size) {this.size = size; }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPaint(mPaint);
        canvas.drawRect(size,size,size,size, mPaint);
    }
}