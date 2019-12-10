package se.mau.mattiasjonsson.teamassignment;

import android.graphics.Color;
import android.graphics.Point;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private int screenWidth, screenHeight;
    private Timer ballTimer = new Timer();
    private int ballX, dy=7, dx = 10;
    private Ball ball;
    private ImageView ivBall;
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<ImageView> ivEnemies = new ArrayList<>();
    private HashMap<Enemy,Timer> timerList = new HashMap<>();
    private ViewGroup layout;
    private long lastLoopRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivBall = findViewById(R.id.imageView2);
        layout = (ConstraintLayout) findViewById(R.id.layout);

        WindowManager wm = getWindowManager();
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenHeight = size.y;
        screenWidth = size.x;

        showBall();
        moveBall();
        loop();
    }

    private void addEnemy(){
        Timer enemyTimer = new Timer();
        ImageView ivEnemy = new ImageView(this);
        addContentView(ivEnemy, new ConstraintLayout.LayoutParams(75, 75));
        String enemyName = "enemy"+new Random().nextInt(1000000);
        Enemy enemy = new Enemy(enemyName);
        timerList.put(enemy, enemyTimer);
        enemies.add(enemy);
        ivEnemies.add(ivEnemy);
        showObstacle(ivEnemy, enemy);
        moveObstacle(ivEnemy, enemyTimer);
    }

    private void showObstacle(ImageView ivEnemy, Enemy enemy){
        ivEnemy.setY(40);
        ivEnemy.setX(new Random().nextInt(screenWidth-ivEnemy.getWidth()));
        ivEnemy.setBackgroundDrawable(enemy);
    }

    private void showBall(){
        ball = new Ball(100, Color.BLACK);
        ivBall.setY(700);
        ivBall.setBackgroundDrawable(ball);
    }

    private void moveBall(){
        ballTimer.schedule(new TimerTask() {
            @Override
            public void run() {runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    changeBallPos();
                }
            });
            }
        },0, 20);

        ballTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    //TODO: Make ball jump
                    }
                });
            }
        },0, 1000);
    }

    private void moveObstacle(final ImageView ivEnemy, Timer enemyTimer){
        enemyTimer.schedule(new TimerTask() {
            @Override
            public void run() {runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    changeObstaclePos(ivEnemy);
                }
            });
            }
        },0, 20);
    }

    private void changeObstaclePos(ImageView ivEnemy){
        int enemyY = (int)ivEnemy.getY();
        enemyY +=dy;
        ivEnemy.setY(enemyY);
        if(ivEnemy.getY()>screenHeight) {
            removeEnemy(ivEnemy);
        }
    }

    private void changeBallPos(){
        ballX +=dx;
        if (isOutOfBounds())
            dx = -dx;
        checkCollisions();
        ivBall.setX(ballX);
    }

    private void checkCollisions(){
        for(ImageView ivEnemy: ivEnemies)
            if(intersects(ivBall, ivEnemy)) {
                Log.d("Test", "Game Over");
                ballTimer.cancel();
            }
    }

    private void removeEnemy(ImageView ivEnemy){
        Enemy enemy = (Enemy)ivEnemy.getDrawable();
        timerList.remove(enemy);
        enemies.remove(enemy);
        layout.removeView(ivEnemy);
    }

    private boolean isOutOfBounds(){
        return(ballX < 0 || ballX > (screenWidth - ivBall.getWidth()));
    }

    private boolean intersects(ImageView ball, ImageView obstacle) {
        return ball.getX() < obstacle.getX() + obstacle.getWidth() && ball.getX() + ball.getWidth() > obstacle.getX() && ball.getY() < obstacle.getY() + obstacle.getHeight() && ball.getY() + ball.getHeight() > obstacle.getY();
    }

    private void loop() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (new Date().getTime() - lastLoopRun > 40) {
                        addEnemy();
                        lastLoopRun = new Date().getTime();
                        Log.d("test", "loop");
                    }
                }
            });
            }
        },0, 1000);
    }

}
