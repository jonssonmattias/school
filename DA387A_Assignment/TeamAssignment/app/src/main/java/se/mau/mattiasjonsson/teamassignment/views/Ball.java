package se.mau.mattiasjonsson.teamassignment.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;

public class Ball extends View {
    private Paint mPaintCircle;
    private float size;

    public Ball(Context context) {
        super(context);
        init(null);
    }

    public Ball(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public Ball(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public Ball(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public void init(AttributeSet attrs){
        mPaintCircle = new Paint();
        mPaintCircle.setColor(Color.parseColor("#00ccff"));
    }

    @Override
    public void setX(float x) {
        super.setX(x);
    }

    @Override
    public void setY(float y) {
        super.setY(y);
    }

    public float getSize() {return size;}

    public void setSize(float size) {this.size = size; }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaintCircle.setStyle(Paint.Style.FILL);
        mPaintCircle.setColor(Color.BLACK);
        canvas.drawPaint(mPaintCircle);
        canvas.drawRect(size, size, size, size, mPaintCircle);
    }
}
/*
public class Ball extends Drawable {
    private int size, color;
    private Canvas canvas;

    public Ball(int size, int color){
        this.size=size;
        this.color=color;
    }

    public void jump(){
        if(canvas!=null) {
            size = size * 2;
            draw(canvas);
            size = size / 2;
        }
    }

    public int getSize(){
        return size;
    }

    @Override
    public void draw(Canvas canvas) {
        this.canvas=canvas;
        float rectSize = (float)size;
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        canvas.drawPaint(paint);
        paint.setColor(color);
        canvas.drawRect(rectSize, rectSize, rectSize, rectSize, paint);
    }

    @Override
    public void setAlpha(int alpha) { }

    @Override
    public void setColorFilter(ColorFilter colorFilter) { }

    @Override
    public int getOpacity() {return 0; }
}

 */
