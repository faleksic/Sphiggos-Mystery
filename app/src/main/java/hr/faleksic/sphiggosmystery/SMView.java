package hr.faleksic.sphiggosmystery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
    private boolean miniGame;
    private boolean kill;
    private Rect frameToDraw;
    private RectF whereToDraw;
    private int screenWidth;
    private int screenHeight;
    private long time;
    private int toxicNum = 1;
    int toxicBitmapIndex = 0;
    int doorBitmapIndex = 0;
    private boolean gameOverText = false;
    private boolean passedTest = false;
    private String whatWasWrong;


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
    private static final String TOXIC_KEY = "toxic";
    private static final String GAMEOVER_KEY = "gameOver";
    private static final String RETRY_KEY = "retry";
    private static final String RETRYCLICK_KEY = "retryClick";
    private static final String QUIT_KEY = "quit";
    private static final String QUITCLICK_KEY = "quitClick";


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

        int i = 0;
        for(Map.Entry<String, GameObject> go : gameObjects.entrySet()) {
            if(Objects.equals(go.getKey(), TOXIC_KEY)) {
                toxicBitmapIndex = i;
            } else if(Objects.equals(go.getKey(), DOOR_KEY)) {
                doorBitmapIndex = i;
            }
            i++;
        }
        time = System.currentTimeMillis();
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
        switch (levelManager.getLevel()) {
            case 1: {
                //moving sheep in and out of the boat
                if (((Sheep) gameObjects.get(SHEEP_KEY)).isMoving()) {
                    gameObjects.get(SHEEP_KEY).update();

                }

                //moving wolf in and out of the boat
                if (((Wolf) gameObjects.get(WOLF_KEY)).isMoving()) {
                    gameObjects.get(WOLF_KEY).update();

                }

                //moving cabbage in and out of the boat
                if (((Cabbage) gameObjects.get(CABBAGE_KEY)).isMoving()) {
                    gameObjects.get(CABBAGE_KEY).update();

                }

                //moving boat
                if (((Boat) gameObjects.get(BOAT_KEY)).isMoving()) {
                    gameObjects.get(BOAT_KEY).update();
                }

                if (miniGame) {
                    checkGameOver();
                    checkWin();
                }

                if (kill) {
                    toxicAnimation();
                }
                break;
            } case 2: {

                break;
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
                        showMiniGame(true);
                    }
                }
            }

            //show game over text if rules are shown, mini game is not true and rules box is visiable
            if(gameOverText) {
                displayGameOverText();
            }

            if(passedTest) {
                displayWinText();
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
        if(System.currentTimeMillis() - time > 200) {
            if (levelManager != null) {
                inputController.handleInput(event, this, levelManager);
            }
            time = System.currentTimeMillis();
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
            tp.setAntiAlias(true);
            if(numClicks < levelManager.getRulesText().size()) {
                StaticLayout sl = new StaticLayout(levelManager.getRulesText().get(numClicks), tp,
                        (int) (canvas.getWidth() * 0.85), Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
                canvas.translate((int) (canvas.getWidth() * 0.05), (int) (canvas.getHeight() - gameObjects.get(RULESBOX_KEY).getHeight() / 1.1));
                sl.draw(canvas);
            }
        }
    }

    private void displayGameOverText() {
        TextPaint tp = new TextPaint();
        tp.setColor(Color.RED);
        tp.setTextSize(20 * getResources().getDisplayMetrics().density);
        tp.setAntiAlias(true);
        String packageName = context.getPackageName();
        String text = whatWasWrong;
        text += context.getString(context.getResources().getIdentifier("game_over_text", "string", packageName));
        StaticLayout sl = new StaticLayout(text, tp,
                (int) (canvas.getWidth() * 0.9), Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
        canvas.translate((int)(canvas.getWidth() * 0.05), (int)(canvas.getHeight() - gameObjects.get(RULESBOX_KEY).getHeight() / 1.1));
        sl.draw(canvas);
    }

    private void displayWinText() {
        TextPaint tp = new TextPaint();
        tp.setColor(Color.argb(1, 0, 100, 0));
        tp.setTextSize(20 * getResources().getDisplayMetrics().density);
        tp.setAntiAlias(true);
        String text = context.getString(context.getResources().getIdentifier("win_text", "string", context.getPackageName()));
        StaticLayout sl = new StaticLayout(text, tp,
                (int) (canvas.getWidth() * 0.9), Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
        canvas.translate((int)(canvas.getWidth() * 0.05), (int)(canvas.getHeight() - gameObjects.get(RULESBOX_KEY).getHeight() / 1.1));
        sl.draw(canvas);
    }

    private void showMiniGame(boolean show) {
        switch (levelManager.getLevel()) {
            case 1: {
                int i = 0;
                for (Map.Entry<String, GameObject> go : gameObjects.entrySet()) {
                    if (Objects.equals(go.getKey(), BACKGROUND_KEY)) {
                        String background = "game_background";
                        if (show) {
                            background = "game1_background";
                        }
                        go.getValue().setBitmapName(background);
                        bitmaps[i] = go.getValue().prepareBitmap(context, go.getValue().getBitmapName());
                    } else if (Objects.equals(go.getKey(), SHEEP_KEY)
                            || Objects.equals(go.getKey(), WOLF_KEY)
                            || Objects.equals(go.getKey(), CABBAGE_KEY)
                            || Objects.equals(go.getKey(), BOAT_KEY)) {
                        go.getValue().setVisible(show);
                    } else if (Objects.equals(go.getKey(), GAMEOVER_KEY)
                            || Objects.equals(go.getKey(), RETRY_KEY)
                            || Objects.equals(go.getKey(), RETRYCLICK_KEY)
                            || Objects.equals(go.getKey(), QUIT_KEY)
                            || Objects.equals(go.getKey(), QUITCLICK_KEY)
                            || Objects.equals(go.getKey(), TOXIC_KEY)) {
                        go.getValue().setVisible(false);
                    } else {
                        go.getValue().setVisible(!show);
                    }
                    i++;
                }
                break;
            } case 2: {
                int i = 0;
                for (Map.Entry<String, GameObject> go : gameObjects.entrySet()) {
                    if (Objects.equals(go.getKey(), BACKGROUND_KEY)) {
                        String background = "game_background";
                        if (show) {
                            background = "game2_background";
                        }
                        go.getValue().setBitmapName(background);
                        bitmaps[i] = go.getValue().prepareBitmap(context, go.getValue().getBitmapName());
                    } else if (Objects.equals(go.getKey(), SHEEP_KEY)) {
                        go.getValue().setVisible(show);
                    } else if (Objects.equals(go.getKey(), GAMEOVER_KEY)
                            || Objects.equals(go.getKey(), RETRY_KEY)
                            || Objects.equals(go.getKey(), RETRYCLICK_KEY)
                            || Objects.equals(go.getKey(), QUIT_KEY)
                            || Objects.equals(go.getKey(), QUITCLICK_KEY)
                            || Objects.equals(go.getKey(), TOXIC_KEY)) {
                        go.getValue().setVisible(false);
                    } else {
                        go.getValue().setVisible(!show);
                    }
                    i++;
                }
                break;
            }
        }
    }

    public void moveSheep() {
        Wolf wolf = ((Wolf)gameObjects.get(WOLF_KEY));
        Cabbage cabbage = ((Cabbage)gameObjects.get(CABBAGE_KEY));
        Sheep sheep = ((Sheep) gameObjects.get(SHEEP_KEY));
        Boat boat = ((Boat)gameObjects.get(BOAT_KEY));

        if(!(wolf.isInBoat() || wolf.isMoving()) && !(cabbage.isInBoat() || cabbage.isMoving()) && !boat.isMoving()) {
            sheep.setMoving(true);
            sheep.setBoatOnStartSide(boat.isStartingSide());
        }
    }

    public void moveWolf() {
        Cabbage cabbage = ((Cabbage)gameObjects.get(CABBAGE_KEY));
        Sheep sheep = ((Sheep) gameObjects.get(SHEEP_KEY));
        Wolf wolf = ((Wolf)gameObjects.get(WOLF_KEY));
        Boat boat = ((Boat)gameObjects.get(BOAT_KEY));

        if(!(sheep.isInBoat() || sheep.isMoving()) && !(cabbage.isInBoat() || cabbage.isMoving()) && !boat.isMoving()) {
            wolf.setMoving(true);
            wolf.setBoatOnStartSide(boat.isStartingSide());
        }
    }

    public void moveCabbage() {
        Wolf wolf = ((Wolf)gameObjects.get(WOLF_KEY));
        Sheep sheep = ((Sheep) gameObjects.get(SHEEP_KEY));
        Cabbage cabbage = ((Cabbage)gameObjects.get(CABBAGE_KEY));
        Boat boat = ((Boat)gameObjects.get(BOAT_KEY));

        if(!(sheep.isInBoat() || sheep.isMoving()) && !(wolf.isInBoat() || wolf.isMoving()) && !boat.isMoving()) {
            cabbage.setMoving(true);
            cabbage.setBoatOnStartSide(boat.isStartingSide());
        }
    }

    public void moveBoat() {
        Wolf wolf = ((Wolf)gameObjects.get(WOLF_KEY));
        Sheep sheep = ((Sheep) gameObjects.get(SHEEP_KEY));
        Cabbage cabbage = ((Cabbage)gameObjects.get(CABBAGE_KEY));
        Boat boat = ((Boat)gameObjects.get(BOAT_KEY));

        if(!sheep.isMoving() && !wolf.isMoving() && !cabbage.isMoving()) {
            //telling the boat who is inside
            if(wolf.isInBoat() || sheep.isInBoat() || cabbage.isInBoat()) {
                if(wolf.isInBoat()) {
                    boat.setPassenger(wolf);
                } else if(sheep.isInBoat()) {
                    boat.setPassenger(sheep);
                } else {
                    boat.setPassenger(cabbage);
                }
            }
            boat.setMoving(true);
        }
    }

    private void checkGameOver() {
        Wolf wolf = ((Wolf)gameObjects.get(WOLF_KEY));
        Sheep sheep = ((Sheep) gameObjects.get(SHEEP_KEY));
        Cabbage cabbage = ((Cabbage)gameObjects.get(CABBAGE_KEY));
        Boat boat = ((Boat)gameObjects.get(BOAT_KEY));
        String packageName = context.getPackageName();

        if(sheep.isOtherSide() && cabbage.isOtherSide() && ((wolf.isInBoat() && boat.isStartingSide()) || (wolf.isStartSide() && boat.isStartingSide()))) {
            whatWasWrong = context.getString(context.getResources().getIdentifier("game_over_sheep_cabbage", "string", packageName));
            gameOver();
        } else if(sheep.isOtherSide() && wolf.isOtherSide() && ((cabbage.isInBoat() && boat.isStartingSide()) || (cabbage.isStartSide() && boat.isStartingSide()))) {
            whatWasWrong = context.getString(context.getResources().getIdentifier("game_over_wolf_sheep", "string", packageName));
            gameOver();
        } else if(sheep.isStartSide() && wolf.isStartSide() && ((cabbage.isInBoat() && !boat.isStartingSide()) || (cabbage.isOtherSide() && !boat.isStartingSide()))) {
            whatWasWrong = context.getString(context.getResources().getIdentifier("game_over_wolf_sheep", "string", packageName));
            gameOver();
        } else if(sheep.isStartSide() && cabbage.isStartSide() && ((wolf.isInBoat() && !boat.isStartingSide()) || (wolf.isOtherSide() && !boat.isStartingSide()))) {
            gameOver();
            whatWasWrong = context.getString(context.getResources().getIdentifier("game_over_sheep_cabbage", "string", packageName));
        }
    }
    private void checkWin() {
        Wolf wolf = ((Wolf) gameObjects.get(WOLF_KEY));
        Sheep sheep = ((Sheep) gameObjects.get(SHEEP_KEY));
        Cabbage cabbage = ((Cabbage) gameObjects.get(CABBAGE_KEY));

        if(sheep.isOtherSide() && cabbage.isOtherSide() && wolf.isOtherSide()) {
            openDoor();
        }
    }

    public void startLevel() {
        numClicks = -1;
        levelManager = new LevelManager(context, levelManager.getLevel()+1, screenWidth, screenHeight);
        miniGame = false;
        toxicNum = 1;
        passedTest = false;
    }
    private void gameOver() {
        miniGame = false;
        gameOverText = true;
        showMiniGame(false);
    }

    public void killAnimation() {
        gameObjects.get(RULESBOX_KEY).setVisible(false);
        kill = true;
        gameOverText = false;
        time = System.currentTimeMillis();
        gameObjects.get(TOXIC_KEY).setBitmapName("toxic1");
        bitmaps[toxicBitmapIndex] = gameObjects.get(TOXIC_KEY).prepareBitmap(context, gameObjects.get(TOXIC_KEY).getBitmapName());
        gameObjects.get(TOXIC_KEY).setVisible(true);
    }

    private void toxicAnimation() {
        if (System.currentTimeMillis() - time > 250) {
            gameObjects.get(TOXIC_KEY).setBitmapName("toxic" + toxicNum);
            bitmaps[toxicBitmapIndex] =  gameObjects.get(TOXIC_KEY).prepareBitmap(context,  gameObjects.get(TOXIC_KEY).getBitmapName());
            toxicNum++;
            time = System.currentTimeMillis();
        }
        if(toxicNum == 11) {
            kill = false;
            gameObjects.get(GAMEOVER_KEY).setVisible(true);
            gameObjects.get(RETRY_KEY).setVisible(true);
            gameObjects.get(QUIT_KEY).setVisible(true);
        }
    }

    private void openDoor() {
        miniGame = false;
        gameObjects.get(DOOR_KEY).setBitmapName("door_opened");
        gameObjects.get(DOOR_KEY).setHeight((int)(screenHeight*0.425));
        passedTest = true;
        bitmaps[doorBitmapIndex] = gameObjects.get(DOOR_KEY).prepareBitmap(context, gameObjects.get(DOOR_KEY).getBitmapName());
        showMiniGame(false);
    }

    public void retry() {
        gameObjects.put(BOAT_KEY, new Boat((int)(screenWidth*0.3), (int)(screenHeight*0.2), screenWidth/2, (int)(screenHeight*0.2), screenWidth));
        gameObjects.put(WOLF_KEY, new Wolf((int)(screenWidth*0.15), (int)(screenHeight*0.1), (int)(screenWidth/1.25), (int)(screenHeight*0.3), screenWidth, screenHeight));
        gameObjects.put(SHEEP_KEY, new Sheep((int)(screenWidth*0.15), (int)(screenHeight*0.1), (int)(screenWidth/1.3), (int)(screenHeight*0.1), screenWidth, screenHeight));
        gameObjects.put(CABBAGE_KEY, new Cabbage((int)(screenWidth*0.1), (int)(screenHeight*0.1), (int)(screenWidth/1.2), (int)(screenHeight*0.5), screenWidth, screenHeight));
        showMiniGame(true);
        miniGame = true;
        toxicNum = 1;
        passedTest = false;
    }

    public void quit() {
        context.startActivity(new Intent(context.getApplicationContext(), MainActivity.class));
        ((Activity)context).finish();
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

    public int getToxicNum() {
        return toxicNum;
    }

    public boolean isGameOverText() {
        return gameOverText;
    }

    public boolean isPassedTest() {
        return passedTest;
    }
}
