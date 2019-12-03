package se.mau.mattiasjonsson.p1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

@SuppressLint("HeaderTextView")
public class HeaderTextView extends TextView {

    public HeaderTextView(Context context) {
        super(context);
    }

    public HeaderTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeaderTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void setText(CharSequence text, BufferType type) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < text.length(); i++)
            str.append( Character.toUpperCase(text.charAt(i)));
        super.setTextSize(18);
        super.setGravity(Gravity.CENTER);
        super.setTextColor(Color.BLACK);
        super.setText(str.subSequence(0, str.length()), type);
    }

}

