package se.mau.mattiasjonsson.teamassignment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static boolean GAME_OVER = false;
    private static int dy=7;

    private Ball ball;
    private ImageView ivBall;
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private ArrayList<ImageView> ivEnemies = new ArrayList<>();
    private HashMap<Enemy,Timer> timerList = new HashMap<>();
    private ViewGroup layout;
    private long lastLoopRun;
    private SensorManager sensorManager;
    private float xPos, xMax, yMax, xVel, startX;

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
        xMax = (float) size.x - 100;
        yMax = (float) size.y;

        showBall();
        // moveBall();
        //     loop();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        sensorManager.registerListener(new BallSensorListener(), sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onStop() {
        sensorManager.unregisterListener(new BallSensorListener());
        super.onStop();
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
        ivEnemy.setX(new Random().nextInt((int)xMax -ivEnemy.getWidth()));
        ivEnemy.setBackgroundDrawable(enemy);
    }

    private void showBall(){
        ball = new Ball(100, Color.BLACK);
        Log.d("ballWidth", ball.getSize()+"");
        startX = xMax/2-100;
        ivBall.setY(700);
        ivBall.setX(startX);
        ivBall.setBackgroundDrawable(ball);
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
        if(ivEnemy.getY()> yMax) {
            removeEnemy(ivEnemy);
        }
    }

    private boolean checkCollisions(){
        for(ImageView ivEnemy: ivEnemies)
            if(intersects(ivBall, ivEnemy))
                return true;
        return false;
    }

    private void removeEnemy(ImageView ivEnemy){
        Enemy enemy = (Enemy)ivEnemy.getDrawable();
        timerList.remove(enemy);
        enemies.remove(enemy);
        layout.removeView(ivEnemy);
    }

    private boolean isOutOfBounds(){
        float ballX = ivBall.getX();
        return(ballX < 0 || ballX > (xMax - ball.getSize()));
    }

    private boolean intersects(ImageView ball, ImageView obstacle) {
        return ball.getX() < obstacle.getX() + obstacle.getWidth() && ball.getX() + ball.getWidth() > obstacle.getX() && ball.getY() < obstacle.getY() + obstacle.getHeight() && ball.getY() + ball.getHeight() > obstacle.getY();
    }

    private boolean gameover(){
        return checkCollisions() || isOutOfBounds();

    }

    private void velocityThreshold(float velocity){
        float maxVel = 5;
        if(xVel>maxVel) xVel=maxVel;
        if(xVel<-maxVel) xVel=-maxVel;
    }

    private float tiltThreshold(float tilt){
        if(tilt<-3) return -3;
        else if(tilt>3) return 3;
        else return tilt;
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

    private void updateBall(float tilt){
        tilt=tiltThreshold(tilt);
        float frameTime = 0.666f;
        xVel += (tilt * frameTime);

        velocityThreshold(xVel);
        xPos -= (xVel / 2) * frameTime;

        if(!gameover()) {
            ivBall.setX(xPos + startX);
            Log.d("xPos", xPos + "");
        }
        else{
            ivBall.setX(xMax/2);
            Log.d("", "Gameover");
            GAME_OVER = true;
        }
    }

    private class BallSensorListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                if(!GAME_OVER) updateBall(event.values[0]);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    }
}
