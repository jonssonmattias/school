package se.mau.mattiasjonsson.teamassignment;


import android.content.Context;
import android.graphics.Point;
import android.hardware.*;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.*;

import se.mau.mattiasjonsson.teamassignment.views.*;

public class GameActivity extends AppCompatActivity {

    private static boolean GAME_OVER;
    private static final int dy=7, ENEMY_SIZE=75;
    private static final float FRAMETIME = 0.666f;

    private Ball ball;
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private HashMap<Enemy,Timer> timerList = new HashMap<>();
    private ViewGroup layout;
    private long lastLoopRun;
    private SensorManager sensorManager;
    private float xPos, xMax, yMax, xVel, startX;
    private int score;
    private Timer enemyTimer = new Timer(), gameTimer = new Timer();
    private TextView tvScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }

    @Override
    protected void onResume() {
        super.onResume();
        layout = (ConstraintLayout) findViewById(R.id.layout);
        tvScore = new TextView(this);
        tvScore.setLeft(0);
        tvScore.setTop(0);
        layout.addView(tvScore);
        GAME_OVER=false;

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        xMax = (float) size.x - 100;
        yMax = (float) size.y;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(new BallSensorListener(), sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);

        showBall();
        enemyLoop();
        gameLoop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("test","onDestroy");
        sensorManager.unregisterListener(new BallSensorListener());
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(new BallSensorListener());
    }

    private void addEnemy(){
        Timer enemyTimer = new Timer();
        Enemy enemy = new Enemy(getApplicationContext());
        timerList.put(enemy, enemyTimer);
        enemies.add(enemy);
        showEnemy(enemy);
        moveEnemy(enemy, enemyTimer);
    }

    private void showEnemy(Enemy enemy){
        layout.addView(enemy);
        enemy.setLayoutParams(new ConstraintLayout.LayoutParams(ENEMY_SIZE, ENEMY_SIZE));
        enemy.setSize(ENEMY_SIZE);
        enemy.setY(40);
        enemy.setX(new Random().nextInt((int)xMax - enemy.getWidth()));
    }

    private void showBall(){
        ball = findViewById(R.id.ball);
        ball.setSize(100);
        startX = xMax/2-100;
        ball.setX(startX);
        ball.setY(700);
    }

    private void moveEnemy(final Enemy enemy, Timer enemyTimer){
        enemyTimer.schedule(new TimerTask() {
            @Override
            public void run() {runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    changeEnemyPos(enemy);
                }
            });
            }
        },0, 20);
    }

    private void changeEnemyPos(Enemy enemy){
        int enemyY = (int)enemy.getY();
        enemyY +=dy;
        enemy.setY(enemyY);
        if(enemy.getY()> yMax) {
            removeEnemy(enemy);
        }
    }

    private boolean checkCollisions(){
        for(Enemy enemy: enemies)
            if(intersects(ball, enemy))
                return true;
        return false;
    }

    private void removeEnemy(Enemy enemy){
        timerList.remove(enemy);
        enemies.remove(enemy);
        layout.removeView(enemy);
    }

    private boolean isOutOfBounds(){
        float ballX = ball.getX();
        return(ballX < 0 || ballX > (xMax - ball.getSize()));
    }

    private boolean intersects(Ball ball, Enemy enemy) {
        return ball.getX() < enemy.getX() + enemy.getWidth() && ball.getX() + ball.getWidth() > enemy.getX() && ball.getY() < enemy.getY() + enemy.getHeight() && ball.getY() + ball.getHeight() > enemy.getY();
    }

    private boolean gameover(){
        return checkCollisions() || isOutOfBounds();
    }

    private float velocityThreshold(float velocity){
        float maxVel = 5;
        if(velocity>maxVel) return maxVel;
        if(velocity<-maxVel) return -maxVel;
        return velocity;
    }

    private float tiltThreshold(float tilt){
        if(tilt<-3) return -3;
        else if(tilt>3) return 3;
        else return tilt;
    }

    private void enemyLoop() {
        enemyTimer.schedule(new TimerTask() {
            @Override
            public void run() {runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (new Date().getTime() - lastLoopRun > 40) {
                        addEnemy();
                        lastLoopRun = new Date().getTime();
                    }
                }
            });
            }
        },0, 2000);
    }


    private void gameLoop(){
        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      Log.d("SCORE", ++score+"");
                                      tvScore.setText("SCORE: "+score);
                                  }
                              });
            }
        },0, 200);
    }

    private void updateBall(float tilt){
        tilt=tiltThreshold(tilt);
        xVel += (tilt * FRAMETIME);
        xVel = velocityThreshold(xVel);
        xPos -= (xVel / 2) * FRAMETIME;

        if(!gameover()) ball.setX(xPos + startX);
        else{
            GAME_OVER = true;
            enemyTimer.cancel();
            gameTimer.cancel();
            this.finish();
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
