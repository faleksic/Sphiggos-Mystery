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

import java.util.LinkedHashMap;
import java.util.Map;
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
    private InputController inputController;
    private boolean showedRules = false;
    private int numClicks = -1;
    private LinkedHashMap<String, GameObject> gameObjects;
    private Bitmap[] bitmaps;
    private boolean miniGame = false;
    private Rect frameToDraw;
    private RectF whereToDraw;
    private int screenWidth;
    private int screenHeight;

    private static final String PLAYER_KEY = "player";
    private static final String SHEEP_KEY = "sheep";
    private static final String WOLF_KEY = "wolf";
    private static final String CABBAGE_KEY = "cabbage";
    private static final String BACKGROUND_KEY = "background";
    private static final String ENEMY_KEY = "enemy";
    private static final String DOOR_KEY = "door";
    private static final String RULESBOX_KEY = "rulesBox";
    private static final String BOAT_KEY = "boat";
    private static final String GAMEONTABLE_KEY = "gameOnTable";


    public SMView(Context context, int screenWidth, int screenHeight) {
        super(context);

        this.context = context;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        ourHolder = getHolder();
        paint = new Paint();
        levelManager = new LevelManager(context, 1, screenWidth, screenHeight);
        this.gameObjects = levelManager.getGameObjects();
        this.bitmaps = levelManager.getBitmaps();
        inputController = new InputController(screenWidth, screenHeight);

        int frameWidth = 25;
        int frameHeight = 45;
        whereToDraw = new RectF(gameObjects.get(PLAYER_KEY).getPositionX(), gameObjects.get(PLAYER_KEY).getPositionY(),
                gameObjects.get(PLAYER_KEY).getPositionX() + screenWidth / 6,
                gameObjects.get(PLAYER_KEY).getPositionY() + (int) (screenHeight * 0.405));
        frameToDraw = new Rect(frameWidth*13, frameHeight*3, frameWidth*14, frameHeight*4);
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
        if(((Sheep)gameObjects.get(SHEEP_KEY)).isMoving()) {
            Sheep sheep = (Sheep)gameObjects.get(SHEEP_KEY);
            if(sheep.isInBoat()) {
                if(sheep.getPositionX() < screenWidth/1.3) {
                    sheep.setPositionX(sheep.getPositionX() + (int)(screenWidth*0.005));
                }
                if(sheep.getPositionY() > screenHeight*0.1) {
                    sheep.setPositionY(sheep.getPositionY() - (int)(screenHeight*0.005));
                }
                if(sheep.getPositionX() >= screenWidth/1.3 && sheep.getPositionY() <= screenHeight*0.1) {
                    sheep.setInBoat(false);
                    sheep.setMoving(false);
                }

            } else {
                if(sheep.getPositionX() > screenWidth/1.7) {
                    sheep.setPositionX(sheep.getPositionX() - (int)(screenWidth*0.005));
                }
                if(sheep.getPositionY() < screenHeight*0.26) {
                    sheep.setPositionY(sheep.getPositionY() + (int)(screenHeight*0.005));
                }
                if(sheep.getPositionX() <= screenWidth/1.7 && sheep.getPositionY() >= screenHeight*0.26) {
                    sheep.setInBoat(true);
                    sheep.setMoving(false);
                }
            }
        }

    }

    private void draw() {
        if (ourHolder.getSurface().isValid()) {
            canvas = ourHolder.lockCanvas();

            //drawing game objects
            int i = 0;
            for(Map.Entry<String, GameObject> go : gameObjects.entrySet()) {
                if(go.getValue().isVisiable()) {
                    if(!Objects.equals(go.getKey(), PLAYER_KEY)) {
                        canvas.drawBitmap(bitmaps[i], go.getValue().getPositionX(), go.getValue().getPositionY(), null);
                    } else {
                        canvas.drawBitmap(bitmaps[i], frameToDraw, whereToDraw, null);
                    }
                }
                i++;
            }
            //checking are rules showed
            if(!showedRules) {
                if(numClicks > -1) {
                    if(numClicks < levelManager.getRulesText().size()) {
                        displayRules();
                    } else {
                        showedRules = true;
                        miniGame = true;
                        gameObjects.get(RULESBOX_KEY).setVisible(false);
                        showMiniGame();
                    }
                }
            }

            if(miniGame) {

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
            inputController.handleInput(event, this, levelManager);
        }
        return true;
    }

    private void displayRules() {
        if(!gameObjects.get(RULESBOX_KEY).isVisiable()) {
            gameObjects.get(RULESBOX_KEY).setVisible(true);
        }
        if(numClicks < levelManager.getRulesText().size()) {

            TextPaint tp = new TextPaint();
            tp.setColor(Color.BLACK);
            tp.setTextSize(20 * getResources().getDisplayMetrics().density);
            StaticLayout sl = new StaticLayout(levelManager.getRulesText().get(numClicks), tp,
                    (int) (canvas.getWidth() * 0.9), Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
            canvas.translate((int) (canvas.getWidth() * 0.05), (int) (canvas.getHeight() - gameObjects.get(RULESBOX_KEY).getHeight() / 1.1));
            sl.draw(canvas);
        }
    }

    private void showMiniGame() {
        int i = 0;
        for(Map.Entry<String, GameObject> go : gameObjects.entrySet()) {
            if(Objects.equals(go.getKey(), BACKGROUND_KEY)){
                go.getValue().setBitmapName("game1_background");
                bitmaps[i] = go.getValue().prepareBitmap(context, go.getValue().getBitmapName());
            } else if(Objects.equals(go.getKey(), SHEEP_KEY)
                    || Objects.equals(go.getKey(), WOLF_KEY)
                    || Objects.equals(go.getKey(), CABBAGE_KEY)
                    || Objects.equals(go.getKey(), BOAT_KEY)) {
                go.getValue().setVisible(true);
            } else {
                go.getValue().setVisible(false);
            }
            i++;
        }
    }

    public void moveSheep() {
        ((Sheep)gameObjects.get(SHEEP_KEY)).setMoving(true);
    }

    public void setNumCLicks(int numCLicks) {
        this.numClicks = numCLicks;
    }

    public int getNumCLicks() {
        return numClicks;
    }

    public boolean isShowedRules() {
        return showedRules;
    }

    public boolean isMiniGame() {
        return miniGame;
    }

    public void setMiniGame(boolean miniGame) {
        this.miniGame = miniGame;
    }
}
