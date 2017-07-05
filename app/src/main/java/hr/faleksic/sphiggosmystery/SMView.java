package hr.faleksic.sphiggosmystery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class SMView extends SurfaceView implements Runnable {

    private volatile boolean running;
    private boolean debugging = true;
    Thread gameThread = null;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;
    Context context;
    long startFrameTime;
    long timeThisFrame;
    long fps;
    private LevelManager levelManager;
    InputController inputController;
    int width;
    int height;
    int frameWidth = 25;
    int frameHeight = 45;

    private Rect frameToDraw = new Rect(frameWidth*13, frameHeight*3, frameWidth*14, frameHeight*4);
    private RectF whereToDraw;

    public SMView(Context context, int screenWidth, int screenHeight) {
        super(context);

        this.context = context;
        ourHolder = getHolder();
        paint = new Paint();
        this.width = screenWidth;
        this.height = screenHeight;
        levelManager = new LevelManager(context, 1, screenWidth, screenHeight);
    }

    @Override
    public void run() {
        while(running) {
            startFrameTime = System.currentTimeMillis();

            update();
            draw();

            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }
        }
    }

    private void update() {
    }

    private void draw() {
        if (ourHolder.getSurface().isValid()){
            canvas = ourHolder.lockCanvas();

            canvas.drawBitmap(levelManager.getBackgroundImg(), 0, 0, null);
            canvas.drawBitmap(levelManager.getClosedDoor(), canvas.getWidth()/2-levelManager.getClosedDoor().getWidth()/2, 0, null);
            whereToDraw = new RectF(levelManager.getPlayer().getPositionX(), levelManager.getPlayer().getPositionY(),
                    levelManager.getPlayer().getPositionX() + (int)(canvas.getWidth()/6),
                    levelManager.getPlayer().getPositionY() + (int)(canvas.getHeight()*0.405));
            canvas.drawBitmap(levelManager.getPlayer().getBitmap(), frameToDraw, whereToDraw, null);
            canvas.drawBitmap(levelManager.getEnemy(), (int)(canvas.getWidth()/1.5), (int)(canvas.getHeight()*0.405), null);

            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e(SMView.class.getSimpleName(), "Failed to pause thread");
        }
    }
    public void resume() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }
}
