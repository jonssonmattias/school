package se.mau.mattiasjonsson.teamassignment;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class Enemy extends Drawable {
    private String name;

    public Enemy(String name){
        this.name = name;
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        canvas.drawPaint(paint);
    }

    public String getName() {
        return name;
    }

    @Override
    public void setAlpha(int alpha) { }
    @Override
    public void setColorFilter(ColorFilter colorFilter) { }
    @Override
    public int getOpacity() {return 0; }
}
