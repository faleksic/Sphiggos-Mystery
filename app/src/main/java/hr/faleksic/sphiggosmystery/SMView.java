package hr.faleksic.sphiggosmystery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class SMView extends SurfaceView implements Runnable {

    private volatile boolean running;
    Thread gameThread = null;
    private Canvas canvas;
    private SurfaceHolder ourHolder;
    Context context;
    long startFrameTime;
    long timeThisFrame;
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
    private int toxicNum = 2;
    int toxicBitmapIndex = 0;
    int doorBitmapIndex = 0;
    private boolean gameOverText = false;
    private boolean passedTest = false;
    private String whatWasWrong;
    private volatile boolean wrongPasscode = false;
    private volatile boolean correctPasscode = false;


    private static final String PLAYER_KEY = "player";
    private static final String SHEEP_KEY = "sheep";
    private static final String WOLF_KEY = "wolf";
    private static final String CABBAGE_KEY = "cabbage";
    private static final String BACKGROUND_KEY = "background";
    private static final String DOOR_KEY = "door";
    private static final String RULESBOX_KEY = "rulesBox";
    private static final String BOAT_KEY = "boat";
    private static final String TOXIC_KEY = "toxic";
    private static final String GAMEOVER_KEY = "gameOver";
    private static final String RETRY_KEY = "retry";
    private static final String RETRYCLICK_KEY = "retryClick";
    private static final String QUIT_KEY = "quit";
    private static final String QUITCLICK_KEY = "quitClick";

    public SMView(Context context) {
        super(context);
    }

    public SMView(Context context, int screenWidth, int screenHeight, int levelToStart) {
        super(context);

        this.context = context;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        ourHolder = getHolder();
        levelManager = new LevelManager(context, levelToStart, screenWidth, screenHeight);
        this.gameObjects = levelManager.getGameObjects();
        this.bitmaps = levelManager.getBitmaps();
        inputController = new InputController(screenWidth, screenHeight);

        int frameWidth = 25;
        int frameHeight = 45;
        if(levelManager.getLevel() != 8) {
            whereToDraw = new RectF(gameObjects.get(PLAYER_KEY).getPositionX(), gameObjects.get(PLAYER_KEY).getPositionY(),
                    gameObjects.get(PLAYER_KEY).getPositionX() + screenWidth / 6,
                    gameObjects.get(PLAYER_KEY).getPositionY() + (int) (screenHeight * 0.405));
            frameToDraw = new Rect(frameWidth * 13, frameHeight * 3, frameWidth * 14, frameHeight * 4);
        }

        time = System.currentTimeMillis();

        int i = 0;
        for(Map.Entry<String, GameObject> go : gameObjects.entrySet()) {
            if(Objects.equals(go.getKey(), TOXIC_KEY)) {
                toxicBitmapIndex = i;
            } else if(Objects.equals(go.getKey(), DOOR_KEY)) {
                doorBitmapIndex = i;
            }
            i++;
        }
    }

    @Override
    public void run() {
        while(running) {
            startFrameTime = System.currentTimeMillis();

            update();
            draw();

            timeThisFrame = System.currentTimeMillis() - startFrameTime;
        }
    }

    private void update() {
        if (levelManager.getLevel() == 1) {
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
        } else {
            if(wrongPasscode) {
                gameOver();
                wrongPasscode = false;
            }

            if(correctPasscode) {
                openDoor();
                correctPasscode = false;
            }
        }
        if (kill) {
            toxicAnimation();
        }
    }

    private void draw() {
        if (ourHolder.getSurface().isValid()) {
            canvas = ourHolder.lockCanvas();

            //drawing game objects
            int i = 0;
            for(Map.Entry<String, GameObject> go : gameObjects.entrySet()) {
                if(go.getValue().isVisiable() && i < bitmaps.length && bitmaps[i] != null) {
                    if(!Objects.equals(go.getKey(), PLAYER_KEY)) {
                        if(bitmaps[i] != null) {
                            canvas.drawBitmap(bitmaps[i], go.getValue().getPositionX(), go.getValue().getPositionY(), null);
                        }
                    } else {
                        if(bitmaps[i] != null) {
                            canvas.drawBitmap(bitmaps[i], frameToDraw, whereToDraw, null);
                        }
                    }
                }
                i++;
            }

            //checking are rules showed
            if(!showedRules && levelManager.getLevel() != 8) {
                if(numClicks > -1) {
                    if(numClicks < levelManager.getRulesText().size()) {
                        displayRules();
                    } else {
                        showedRules = true;
                        miniGame = true;
                        gameObjects.get(RULESBOX_KEY).setVisible(false);
                        showMiniGame(true);

                        if(levelManager.getLevel() != 1) {
                            final EditText editText = (EditText) ((Activity) context).findViewById(R.id.level2_edit_text);
                            ((Activity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    editText.setVisibility(View.VISIBLE);

                                    if(levelManager.getLevel() > 2 && levelManager.getLevel() != 6) {
                                        editText.setText("");
                                        editText.setInputType(InputType.TYPE_CLASS_TEXT);
                                        editText.setFilters(new InputFilter[] {});
                                    } else if(levelManager.getLevel() == 6) {
                                        editText.setText("");
                                        InputFilter[] FilterArray = new InputFilter[1];
                                        FilterArray[0] = new InputFilter.LengthFilter(1);
                                        editText.setFilters(FilterArray);
                                        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                                    }

                                    editText.setOnKeyListener(new OnKeyListener() {
                                        @Override
                                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                                            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                                                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                                                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                                String correctAnswer = "";
                                                switch (levelManager.getLevel()) {
                                                    case 2:
                                                        correctAnswer = getResources().getString(R.string.level2_answer);
                                                        whatWasWrong = getResources().getString(R.string.game_over_wrong_passcode);
                                                        break;
                                                    case 3:
                                                        correctAnswer = getResources().getString(R.string.level3_answer);
                                                        whatWasWrong = getResources().getString(R.string.game_over_wrong_answer);
                                                        break;
                                                    case 4:
                                                        correctAnswer = getResources().getString(R.string.level5_answer);
                                                        whatWasWrong = getResources().getString(R.string.game_over_wrong_answer);
                                                        break;
                                                    case 5:
                                                        correctAnswer = getResources().getString(R.string.level6_answer);
                                                        whatWasWrong = getResources().getString(R.string.game_over_wrong_answer);
                                                        break;
                                                    case 6:
                                                        correctAnswer = getResources().getString(R.string.level8_answer);
                                                        whatWasWrong = getResources().getString(R.string.game_over_wrong_answer);
                                                        break;
                                                    case 7:
                                                        correctAnswer = getResources().getString(R.string.level9_answer);
                                                        whatWasWrong = getResources().getString(R.string.game_over_wrong_answer);
                                                        break;
                                                }
                                                if (!Objects.equals(editText.getText().toString(), correctAnswer)) {

                                                    editText.setVisibility(GONE);
                                                    wrongPasscode = true;
                                                } else {
                                                    editText.setVisibility(GONE);
                                                    correctPasscode = true;
                                                }

                                                return true;
                                            }
                                            return false;
                                        }
                                    });
                                }
                            });
                        }
                    }
                }
            }

            //show game over text if rules are shown, mini game is not true and rules box is visible
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
        text += " " + context.getString(context.getResources().getIdentifier("game_over_text", "string", packageName));
        StaticLayout sl = new StaticLayout(text, tp,
                (int) (canvas.getWidth() * 0.9), Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
        canvas.translate((int)(canvas.getWidth() * 0.05), (int)(canvas.getHeight() - gameObjects.get(RULESBOX_KEY).getHeight() / 1.1));
        sl.draw(canvas);
    }

    private void displayWinText() {
        TextPaint tp = new TextPaint();
        tp.setColor(Color.rgb(0, 200, 0));
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
            } case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:{
                int i = 0;
                for (Map.Entry<String, GameObject> go : gameObjects.entrySet()) {
                    if (Objects.equals(go.getKey(), BACKGROUND_KEY)) {
                        String background1 = "";
                        String background2 = "";
                        switch (levelManager.getLevel()) {
                            case 2: {
                                background1 = "game_background2";
                                background2 = "game2_background";
                                break;
                            } case 3: {
                                background1 = "game_background3";
                                background2 = "game3_background";
                                break;
                            } case 4: {
                                background1 = "game_background5";
                                background2 = "game5_background";
                                break;
                            } case 5: {
                                background1 = "game_background6";
                                background2 = "game6_background";
                                break;
                            } case 6: {
                                background1 = "game_background8";
                                background2 = "game8_background";
                                break;
                            } case 7: {
                                background1 = "game_background9";
                                background2 = "game9_background";
                                break;
                            }
                        }
                        String background;
                        if (show) {
                            background = background2;
                        } else {
                            background = background1;
                        }
                        go.getValue().setBitmapName(background);
                        bitmaps[i] = go.getValue().prepareBitmap(context, go.getValue().getBitmapName());
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

                if(show) {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            EditText editText = (EditText)((Activity)context).findViewById(R.id.level2_edit_text);
                            editText.setVisibility(View.VISIBLE);
                        }
                    });
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
        String packageName = context.getPackageName();
        Wolf wolf = ((Wolf) gameObjects.get(WOLF_KEY));
        Sheep sheep = ((Sheep) gameObjects.get(SHEEP_KEY));
        Cabbage cabbage = ((Cabbage) gameObjects.get(CABBAGE_KEY));
        Boat boat = ((Boat) gameObjects.get(BOAT_KEY));

        if (sheep.isOtherSide() && cabbage.isOtherSide() && ((wolf.isInBoat() && boat.isStartingSide()) || (wolf.isStartSide() && boat.isStartingSide()))) {
            whatWasWrong = context.getString(context.getResources().getIdentifier("game_over_sheep_cabbage", "string", packageName));
            gameOver();
        } else if (sheep.isOtherSide() && wolf.isOtherSide() && ((cabbage.isInBoat() && boat.isStartingSide()) || (cabbage.isStartSide() && boat.isStartingSide()))) {
            whatWasWrong = context.getString(context.getResources().getIdentifier("game_over_wolf_sheep", "string", packageName));
            gameOver();
        } else if (sheep.isStartSide() && wolf.isStartSide() && ((cabbage.isInBoat() && !boat.isStartingSide()) || (cabbage.isOtherSide() && !boat.isStartingSide()))) {
            whatWasWrong = context.getString(context.getResources().getIdentifier("game_over_wolf_sheep", "string", packageName));
            gameOver();
        } else if (sheep.isStartSide() && cabbage.isStartSide() && ((wolf.isInBoat() && !boat.isStartingSide()) || (wolf.isOtherSide() && !boat.isStartingSide()))) {
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

    public void startLevel(boolean start) {
        if(start) {
            SharedPreferences.Editor editor = context.getSharedPreferences(getResources().getString(R.string.preference_file_key), Activity.MODE_PRIVATE).edit();
            editor.putInt(getResources().getString(R.string.level), 1).apply();
            numClicks = -1;
            levelManager = new LevelManager(context, 1, screenWidth, screenHeight);
            miniGame = false;
            toxicNum = 1;
            passedTest = false;
            showedRules = false;
            if (bitmaps != null) {
                for (int i = 0; i < bitmaps.length; i++) {
                    if (bitmaps[i] != null) {
                        bitmaps[i].recycle();
                        bitmaps[i] = null;
                    }
                }
                bitmaps = null;
            }
            this.gameObjects = levelManager.getGameObjects();
            this.bitmaps = levelManager.getBitmaps();

            int i = 0;
            for(Map.Entry<String, GameObject> go : gameObjects.entrySet()) {
                if(Objects.equals(go.getKey(), TOXIC_KEY)) {
                    toxicBitmapIndex = i;
                } else if(Objects.equals(go.getKey(), DOOR_KEY)) {
                    doorBitmapIndex = i;
                }
                i++;
            }
        }

    }

    public void startLevel() {
        if(levelManager.getLevel() < 8) {
            numClicks = -1;
            SharedPreferences preferences = context.getSharedPreferences(getResources().getString(R.string.preference_file_key), Activity.MODE_PRIVATE);
            if(preferences.getInt(getResources().getString(R.string.level), 0) < levelManager.getLevel() + 1) {
                SharedPreferences.Editor editor = context.getSharedPreferences(getResources().getString(R.string.preference_file_key), Activity.MODE_PRIVATE).edit();
                editor.putInt(getResources().getString(R.string.level), levelManager.getLevel() + 1).apply();
            }

            levelManager = new LevelManager(context, levelManager.getLevel() + 1, screenWidth, screenHeight);
            miniGame = false;
            toxicNum = 1;
            passedTest = false;
            showedRules = false;
            if (bitmaps != null) {
                for (int i = 0; i < bitmaps.length; i++) {
                    if (bitmaps[i] != null) {
                        bitmaps[i].recycle();
                        bitmaps[i] = null;
                    }
                }
                bitmaps = null;
            }
            this.gameObjects = levelManager.getGameObjects();
            this.bitmaps = levelManager.getBitmaps();

            int i = 0;
            for(Map.Entry<String, GameObject> go : gameObjects.entrySet()) {
                if(Objects.equals(go.getKey(), TOXIC_KEY)) {
                    toxicBitmapIndex = i;
                } else if(Objects.equals(go.getKey(), DOOR_KEY)) {
                    doorBitmapIndex = i;
                }
                i++;
            }
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    EditText editText = (EditText) ((Activity) context).findViewById(R.id.level2_edit_text);
                    if (editText != null) {
                        switch (levelManager.getLevel()) {
                            case 2:
                                editText.setHint(getResources().getString(R.string.level2_hint));
                                break;
                            case 3:
                                editText.setHint(getResources().getString(R.string.level3_hint));
                                break;
                            case 4:
                                editText.setHint(getResources().getString(R.string.level5_hint));
                                break;
                            case 5:
                                editText.setHint(getResources().getString(R.string.level6_hint));
                                break;
                        }
                    }
                }
            });
        }
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
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String syncConnPref = sharedPref.getString("pref_difficulty", "");

        if(Objects.equals(syncConnPref, "hard")) {
            startLevel(true);
        } else {
            if (levelManager.getLevel() == 1) {
                    gameObjects.put(BOAT_KEY, new Boat((int) (screenWidth * 0.3), (int) (screenHeight * 0.2), screenWidth / 2, (int) (screenHeight * 0.2), screenWidth));
                    gameObjects.put(WOLF_KEY, new Wolf((int) (screenWidth * 0.15), (int) (screenHeight * 0.1), (int) (screenWidth / 1.25), (int) (screenHeight * 0.3), screenWidth, screenHeight));
                    gameObjects.put(SHEEP_KEY, new Sheep((int) (screenWidth * 0.15), (int) (screenHeight * 0.1), (int) (screenWidth / 1.3), (int) (screenHeight * 0.1), screenWidth, screenHeight));
                    gameObjects.put(CABBAGE_KEY, new Cabbage((int) (screenWidth * 0.1), (int) (screenHeight * 0.1), (int) (screenWidth / 1.2), (int) (screenHeight * 0.5), screenWidth, screenHeight));
            }
            else {
                final EditText editText = (EditText) ((Activity) context).findViewById(R.id.level2_edit_text);
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        editText.setText("");
                    }

                });
            }

            showMiniGame(true);
            miniGame = true;
            toxicNum = 2;
            passedTest = false;
        }
    }

    public void quit() {
        context.startActivity(new Intent(context, MainActivity.class));
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
