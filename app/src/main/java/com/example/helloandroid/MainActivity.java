package com.example.helloandroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    MyView view;
    MyGravitySensorListener gravity;
    SensorManager sensorManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        view = new MyView(this);
        setContentView(view);
        gravity = new MyGravitySensorListener();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager == null) sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        if (sensor != null)
            sensorManager.registerListener(gravity, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(gravity);
    }

    public class MyView extends View
    {
        Paint background;
        Paint ball;
        int w,h;
        float x,y,vx,vy;
        float r;
        Timer timer;

        public MyView(Context context)
        {
            super(context);
        }

        private void firstTime()
        {
            w = getWidth();
            h = getHeight();
            x = w/2;
            y = h/2;
            vx = vy = 2;
            r = Math.min(x,y)/7;
            background = new Paint();
            background.setStyle(Paint.Style.FILL);
            background.setColor(Color.WHITE);
            ball = new Paint();
            ball.setStyle(Paint.Style.FILL);
            ball.setColor(0xFFCD5C5C);

            timer = new Timer("physics update");
            timer.schedule(new TimerTask() {
                public void run() {
                    float avx = Math.abs(vx);
                    float avy = Math.abs(vy);
                    float minv = 0.01f;
                    x += vx;
                    y += vy;
                    float dt = 0.1f;
                    if (x + r > w || x < r) vx = avx * Math.signum(r - x);
                    else vx += gravity.gx * dt;
                    if (y + r > h || y < r) vy = avy * Math.signum(r - y);
                    else vy += gravity.gy * dt;
                    float drag = 0.99f;
                    vx *= drag; vy *= drag;
                    invalidate();
                }
            }, 0, 15);
        }


        @Override
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);
            if (w == 0) firstTime();
            canvas.drawPaint(background);
            canvas.drawCircle(x, y, r, ball);
        }
    }

    public class MyGravitySensorListener implements SensorEventListener {
        float gx,gy,gz;
        @Override
        public final void onSensorChanged(SensorEvent event) {
            gx = -event.values[0];
            gy = event.values[1];
            gz = event.values[2];
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // do nothing
        }
    }
}