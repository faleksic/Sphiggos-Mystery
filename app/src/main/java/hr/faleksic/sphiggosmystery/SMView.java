package hr.faleksic.sphiggosmystery;

import android.content.Context;
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
    private int numCLicks = 0;

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
            canvas.drawBitmap(levelManager.getGameOnTable(), (int)(canvas.getWidth()/2-levelManager.getGameOnTable().getWidth()/2), (int)(canvas.getHeight()*0.47), null);
            if(levelManager.isRules()) {
                displayRules();
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
            inputController.handleInput(event, levelManager, this);
        }
        return true;
    }

    private void displayRules(){
        canvas.drawBitmap(levelManager.getTextbox(), 0, canvas.getHeight()-levelManager.getTextbox().getHeight(), null);

        TextPaint tp = new TextPaint();
        tp.setColor(Color.BLACK);
        tp.setTextSize(20 * getResources().getDisplayMetrics().density);
        if(numCLicks < levelManager.getRulesText().size()) {
            StaticLayout sl = new StaticLayout(levelManager.getRulesText().get(numCLicks), tp,
                    (int)(canvas.getWidth()*0.9), Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
            canvas.translate((int)(canvas.getWidth()*0.05), (int)(canvas.getHeight()-levelManager.getTextbox().getHeight()/1.1));
            sl.draw(canvas);
            //canvas.drawText(levelManager.getRulesText().get(numCLicks), (int)(canvas.getWidth()*0.05), canvas.getHeight()-levelManager.getTextbox().getHeight()/2, paint);
        } else {
            levelManager.setRules(false);
            showedRules = true;
        }
    }

    public void setNumCLicks(int numCLicks) {
        this.numCLicks = numCLicks;
    }

    public boolean isShowedRules() {
        return showedRules;
    }
}
