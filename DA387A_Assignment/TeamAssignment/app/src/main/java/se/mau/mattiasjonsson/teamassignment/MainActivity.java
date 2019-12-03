package se.mau.mattiasjonsson.teamassignment;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private int screenWidth, screenHeight;
    private Timer timer = new Timer();
    private int imageX, dx = 10;
    private Ball ball;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView2);


        WindowManager wm = getWindowManager();
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenHeight = size.y;
        screenWidth = size.x;

        ball = new Ball(100, Color.BLACK);
        imageView.setY(100);
        imageView.setBackgroundDrawable(ball);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    changePos();
                }
            });
            }
        },0, 20);

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        ball.jump();
                        Log.d("test", "jump");
                    }
                });
            }
        },0, 1000);

    }

    public void changePos(){
        imageX+=dx;
        if (imageX < 0 || imageX > (screenWidth - imageView.getWidth()))
            dx = -dx;
        imageView.setX(imageX);
    }

}
