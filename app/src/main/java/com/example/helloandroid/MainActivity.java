package com.example.helloandroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(new MyView2(this));
    }


    public class MyView2 extends View
    {
        Paint background;
        Paint ball;
        int w,h;
        float x,y,vx,vy;
        float r;
        Timer timer;

        public MyView2(Context context)
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
            timer = new Timer("animation");
            timer.schedule(new TimerTask() {
                public void run() {
                    x += vx; if (x > w || x < 0) vx = Math.abs(vx) * Math.signum(-x);
                    y += vy; if (y > h || y < 0) vy = Math.abs(vy) * Math.signum(-y);
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
}