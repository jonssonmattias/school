package se.mau.mattiasjonsson.teamassignment;


import android.content.Context;
import android.graphics.Point;
import android.hardware.*;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.*;

import se.mau.mattiasjonsson.teamassignment.views.*;

public class GameActivity extends AppCompatActivity {

    private static boolean GAME_OVER;
    private static final int BALL_SIZE = 200,ENEMY_SPEED = 7, ENEMY_SIZE = 75, SHAKE_SLOP_TIME_MS = 500, SHAKE_COUNT_RESET_TIME_MS = 3000, STUCK_IN_MUD_TIME_MS=5000;
    private static final float FRAME_TIME = 1f, SHAKE_THRESHOLD_G_UNIT = 1.2F;

    private Ball ball;
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private HashMap<Enemy, Timer> timerList = new HashMap<>();
    private ViewGroup layout;
    private long lastLoopRun;
    private SensorManager sensorManager;
    private BallSensorListener ballSensorListener = new BallSensorListener();
    private float xPos, xMax, yMax, xVel, startX;
    private int score;
    private Timer enemyTimer = new Timer(), gameTimer = new Timer(), mudTimer = new Timer();
    private TextView tvScore, tvStuckInMud;
    private boolean isInMud, mIsShaken = false;
    private long mShakeTimeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GAME_OVER = false;

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        xMax = (float) size.x;
        yMax = (float) size.y;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(ballSensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);

        initComp();
        showBall();
        enemyLoop();
        gameLoop();
        setInMud();
    }

    private void initComp(){
        layout = (ConstraintLayout) findViewById(R.id.layout);
        tvScore = new TextView(this);
        tvScore.setLeft(0);
        tvScore.setTop(0);
        layout.addView(tvScore);
        tvStuckInMud = new TextView(this);
        tvStuckInMud.setY(1000);
        tvStuckInMud.setWidth((int)xMax);
        tvStuckInMud.setText("Free!!");
        tvStuckInMud.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        layout.addView(tvStuckInMud);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(ballSensorListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(ballSensorListener);
    }

    private void addEnemy() {
        Timer enemyTimer = new Timer();
        Enemy enemy = new Enemy(getApplicationContext());
        timerList.put(enemy, enemyTimer);
        enemies.add(enemy);
        showEnemy(enemy);
        moveEnemy(enemy, enemyTimer);
    }

    private void showEnemy(Enemy enemy) {
        layout.addView(enemy);
        enemy.setLayoutParams(new ConstraintLayout.LayoutParams(ENEMY_SIZE, ENEMY_SIZE));
        enemy.setSize(ENEMY_SIZE);
        enemy.setY(40);
        enemy.setX(new Random().nextInt((int) xMax - enemy.getWidth()));
    }

    private void showBall() {
        ball = new Ball(getApplicationContext());
        ball.setLayoutParams(new ConstraintLayout.LayoutParams(BALL_SIZE, BALL_SIZE));
        ball.setSize(BALL_SIZE);
        startX = xMax / 2 - BALL_SIZE / 2;
        ball.setX(startX);
        ball.setY(700);
        layout.addView(ball);
    }

    private void setInMud() {
        mudTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                isInMud = new Random().nextBoolean();
                Log.d("IsInMud", String.valueOf(isInMud));
            }
        }, STUCK_IN_MUD_TIME_MS, STUCK_IN_MUD_TIME_MS);
    }

    private void moveEnemy(final Enemy enemy, Timer enemyTimer) {
        enemyTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        changeEnemyPos(enemy);
                    }
                });
            }
        }, 0, 20);
    }

    private void changeEnemyPos(Enemy enemy) {
        int enemyY = (int) enemy.getY();
        enemyY += ENEMY_SPEED;
        enemy.setY(enemyY);
        if (enemy.getY() > yMax) {
            removeEnemy(enemy);
        }
    }

    private boolean checkCollisions() {
        for (Enemy enemy : enemies)
            if (intersects(ball, enemy))
                return true;
        return false;
    }

    private void removeEnemy(Enemy enemy) {
        timerList.remove(enemy);
        enemies.remove(enemy);
        layout.removeView(enemy);
    }

    private boolean isOutOfBounds() {
        float ballX = ball.getX();
        return (ballX < 0 || ballX > (xMax - ball.getSize()));
    }

    private boolean intersects(Ball ball, Enemy enemy) {
        return ball.getX() < enemy.getX() + enemy.getWidth() && ball.getX() + ball.getWidth() > enemy.getX() && ball.getY() < enemy.getY() + enemy.getHeight() && ball.getY() + ball.getHeight() > enemy.getY();
    }

    private boolean gameover(){
        return checkCollisions() || isOutOfBounds();
    }

    private float velocityThreshold(float velocity) {
        float maxVel = 5;
        if (velocity > maxVel) return maxVel;
        if (velocity < -maxVel) return -maxVel;
        return velocity;
    }

    private float tiltThreshold(float tilt) {
        if (tilt < -3) return -3;
        else if (tilt > 3) return 3;
        else return tilt;
    }

    private void enemyLoop() {
        enemyTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (new Date().getTime() - lastLoopRun > 40) {
                            addEnemy();
                            lastLoopRun = new Date().getTime();
                        }
                    }
                });
            }
        }, 0, 2000);
    }


    private void gameLoop() {
        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvScore.setText("SCORE: " + ++score);
                    }
                });
            }
        }, 0, 200);
    }

    /**
     * Allows the ball to move
     *
     * @param tilt
     */
    private void updateBall(float tilt) {
        tilt = tiltThreshold(tilt);
        xVel += (tilt * FRAME_TIME);
        xVel = velocityThreshold(xVel);
        xPos -= (xVel / 2) * FRAME_TIME;

        if(!gameover()) ball.setX(xPos + startX);
    }

    private float calculateGforce(float xAxis, float yAxis, float zAxis) {
        float gX = xAxis / SensorManager.GRAVITY_EARTH;
        float gY = yAxis / SensorManager.GRAVITY_EARTH;
        float gZ = zAxis / SensorManager.GRAVITY_EARTH;

        return (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);
    }

    private void phoneIsShaken(float gForce) {
        final long now = System.currentTimeMillis();
        if (gForce > SHAKE_THRESHOLD_G_UNIT && !mIsShaken) {

            // ignore shakes close to eachother
            if (mShakeTimeStamp + SHAKE_SLOP_TIME_MS > now)
                return;

            mShakeTimeStamp = now;
            mIsShaken = true;
            onShake();
        }
        // triggers when the user has stopped shaking the phone
        if (gForce < SHAKE_THRESHOLD_G_UNIT && mShakeTimeStamp + SHAKE_COUNT_RESET_TIME_MS < now)
            mIsShaken = false;
    }

    private void onShake() {
        isInMud = false;
        tvStuckInMud.setText("Free!!");
    }

    private GameActivity getGameActivity(){
        return this;
    }

    private class BallSensorListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                GAME_OVER = gameover();
                if (!GAME_OVER){
                    if (!isInMud)
                        updateBall(event.values[0]);
                    else {
                        tvStuckInMud.setText("Stuck in mud!!");
                        phoneIsShaken(calculateGforce(event.values[0], event.values[1], event.values[2]));
                    }
                }
                else {
                    enemyTimer.cancel();
                    gameTimer.cancel();
                    mudTimer.cancel();
                    getGameActivity().finish();
                }
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }
}