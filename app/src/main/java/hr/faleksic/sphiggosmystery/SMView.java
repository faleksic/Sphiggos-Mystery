package hr.faleksic.sphiggosmystery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Objects;

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
    private InputController inputController = new InputController();
    private boolean showedRules = false;
    private int width;
    private int height;
    private int frameWidth = 25;
    private int frameHeight = 45;
    private int numCLicks = -1;
    private int playerIndex;
    private int rulesIndex;
    private ArrayList<GameObject> gameObjects;
    private Bitmap[] bitmaps;

    private Rect frameToDraw;
    private RectF whereToDraw;


    public SMView(Context context, int screenWidth, int screenHeight) {
        super(context);

        this.context = context;
        ourHolder = getHolder();
        paint = new Paint();
        this.width = screenWidth;
        this.height = screenHeight;
        levelManager = new LevelManager(context, 1, screenWidth, screenHeight);
        this.gameObjects = levelManager.getGameObjects();
        this.bitmaps = levelManager.getBitmaps();
        for(int i=0; i<gameObjects.size(); i++) {
            if(Objects.equals(gameObjects.get(i).getBitmapName(), "player")) {
                playerIndex = i;
            } else if(Objects.equals(gameObjects.get(i).getBitmapName(), "textbox")) {
                rulesIndex = i;
            }
        }
        whereToDraw = new RectF(gameObjects.get(playerIndex).getPositionX(), gameObjects.get(playerIndex).getPositionY(),
                gameObjects.get(playerIndex).getPositionX() + width / 6,
                gameObjects.get(playerIndex).getPositionY() + (int) (height * 0.405));
        frameToDraw = new Rect(frameWidth*13, frameHeight*3, frameWidth*14, frameHeight*4);
    }

    @Override
    public void run() {
        while(running) {
            startFrameTime = System.currentTimeMillis();

            draw();

            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }
        }
    }
    private void draw() {
        if (ourHolder.getSurface().isValid()) {
            canvas = ourHolder.lockCanvas();

            //drawing game objects
            int i = 0;
            for(GameObject go : gameObjects) {
                if(go.isVisiable()) {
                    if(!Objects.equals(go.getBitmapName(), "player")) {
                        canvas.drawBitmap(bitmaps[i], go.getPositionX(), go.getPositionY(), null);
                    } else {
                        canvas.drawBitmap(bitmaps[i], frameToDraw, whereToDraw, null);
                    }
                }
                i++;
            }

            if(!showedRules) {
                if(numCLicks > -1) {
                    if(numCLicks < levelManager.getRulesText().size()) {
                        displayRules();
                    } else {
                        showedRules = true;
                    }
                }
            }
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(levelManager != null) {
            inputController.handleInput(event, this);
        }
        return true;
    }

    private void displayRules(){


        TextPaint tp = new TextPaint();
        tp.setColor(Color.BLACK);
        tp.setTextSize(20 * getResources().getDisplayMetrics().density);
        StaticLayout sl = new StaticLayout(levelManager.getRulesText().get(numCLicks), tp,
                (int)(canvas.getWidth()*0.9), Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
        canvas.translate((int)(canvas.getWidth()*0.05), (int)(canvas.getHeight()-gameObjects.get(rulesIndex).getHeight()/1.1));
        sl.draw(canvas);
    }

    public void setNumCLicks(int numCLicks) {
        this.numCLicks = numCLicks;
    }

    public int getNumCLicks() {
        return numCLicks;
    }

    public boolean isShowedRules() {
        return showedRules;
    }
}
