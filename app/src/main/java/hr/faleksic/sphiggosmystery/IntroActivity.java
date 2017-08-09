package hr.faleksic.sphiggosmystery;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class IntroActivity extends AppCompatActivity {

    private IntroScroll introScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();
        Point resolution = new Point();
        display.getSize(resolution);
        introScroll = new IntroScroll(this, resolution.x, resolution.y);
        setContentView(introScroll);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        introScroll.pause();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        introScroll.resume();
    }

    private class IntroScroll extends SurfaceView implements Runnable{

        private volatile boolean running;
        private volatile boolean stop;
        private Canvas canvas;
        private  Paint paint;
        private SurfaceHolder ourHolder;
        Context context;
        Thread gameThread = null;
        int x;
        int y;
        volatile int  num = 0;
        long startFrameTime;
        long timeThisFrame = 0;
        int taps = 0;

        public IntroScroll(Context context, int screenWidth, int screenHeight) {
            super(context);
            this.context = context;
            ourHolder = getHolder();
            paint = new Paint();
            stop = false;
            running = false;
            this.x = screenWidth;
            this.y = screenHeight;
        }

        @Override
        public void run() {
            startFrameTime = System.currentTimeMillis();
            while(running) {
                update();
                draw();
                timeThisFrame = System.currentTimeMillis();
            }
        }

        private void update() {
            if(!stop) {
                if (timeThisFrame - startFrameTime > 1) {
                    num += (int)(Math.ceil(y * 0.002));
                    startFrameTime = System.currentTimeMillis();
                }
            }
        }

        private  void draw() {
            if (ourHolder.getSurface().isValid()){
                canvas = ourHolder.lockCanvas();
                paint.setColor(Color.BLACK);
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL);

                if(canvas != null) {
                    canvas.drawPaint(paint);
                    TextPaint tp = new TextPaint();
                    tp.setColor(Color.WHITE);
                    tp.setTextSize(20 * getResources().getDisplayMetrics().density);
                    tp.setTextAlign(Paint.Align.CENTER);
                    tp.setAntiAlias(true);


                    StaticLayout sl = new StaticLayout(getResources().getString(R.string.intro), tp,
                            (int) (canvas.getWidth() / 1.33), Layout.Alignment.ALIGN_NORMAL, 1, 1, true);


                    canvas.translate(canvas.getWidth() / 2, (int) (canvas.getHeight() / 1.1) - num);

                    //if the y translate coordinate is higher than canvas height minus static layout minus 3/2 of the layout than stop text scroll
                    if (sl.getHeight() + sl.getHeight() * 0.33 < num) {
                        stop = true;
                        paint.setColor(Color.WHITE);
                        paint.setTextSize(20 * getResources().getDisplayMetrics().density);
                        canvas.drawText(getResources().getString(R.string.continue_text), x * 0.1f, sl.getHeight() + (int) (y * 0.2), paint);
                    }

                    sl.draw(canvas);
                    ourHolder.unlockCanvasAndPost(canvas);
                }
            }

        }
        public void pause() {
            running = false;
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                Log.e(IntroActivity.class.getSimpleName(), "Failed to pause thread");
            }
        }
        public void resume() {
            running = true;
            gameThread = new Thread(this);
            gameThread.start();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    if(stop) {
                        startActivity(new Intent(getApplicationContext(), GameActivity.class));
                        finish();
                    } else {
                        taps++;
                        if(taps == 3){
                            startActivity(new Intent(getApplicationContext(), GameActivity.class));
                            finish();
                        }
                    }
                    break;
            }
            return true;
        }
    }
}
