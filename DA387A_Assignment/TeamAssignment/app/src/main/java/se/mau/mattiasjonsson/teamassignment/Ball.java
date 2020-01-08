package se.mau.mattiasjonsson.teamassignment;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

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
        int radius = size;
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        canvas.drawPaint(paint);
        paint.setColor(color);
        canvas.drawCircle(radius, radius, radius, paint);
    }

    @Override
    public void setAlpha(int alpha) { }

    @Override
    public void setColorFilter(ColorFilter colorFilter) { }

    @Override
    public int getOpacity() {return 0; }
}
