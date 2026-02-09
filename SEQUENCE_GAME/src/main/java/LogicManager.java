import GameEngine.Core.GameEngine;
import GameEngine.Core.audio.SFXEngine;
import GameEngine.Core.audio.SFXPlayer;
import GameEngine.Core.gameObject.GameObject;
import GameEngine.Core.gameObject.GameObjectManager;
import GameEngine.Core.gameObject.Obj.Button;
import GameEngine.Core.gameObject.Obj.Text;
import GameEngine.Core.util.Console.Console;
import GameEngine.Core.util.Timer.Timer;
import GameEngine.Core.util.Vector2;
import Helper.*;
import Helper.List;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ScheduledExecutorService;

public class LogicManager extends GameObject {

    // ----------------- References --------------------
    private UIManager uiManager;
    private List<Button> bigBlueButtons;
    private List<Button> sequence;
    SFXPlayer sfx = new SFXPlayer();

    // ----------------- Game Variables ----------------

    // Player
    private String name = "";
    private double averageClickSpeed;
    private long lastClickTime;
    private double totalClickTime;
    private int clickCount;

    private double gameTime;
    private long gameStartTime;
    private boolean gameActive;


    // Values -- Normal Mode --
    private final float defaultLightDuration = 0.28f;
    private final float defaultFlashDelay = 0.45f;
    private final float defaultLevelDelay = 0.6f;
    // Values  -- Speed Mode --
    private final float speedLightDuration = 0.16f;
    private final float speedFlashDelay = 0.24f;
    private final float speedLevelDelay = 0.4f;

    private int level = 0;
    private float lightDuration = defaultLightDuration;
    private float flashDelay = defaultFlashDelay;
    private float levelDelay = defaultLevelDelay;
    Timer timer;

    // Debug
    private String debugView = "";
    private boolean debugEnabled = false;
    public boolean debugWasEnabledThisGame = false;


    // ----------------- Modes -------------------------
    public enum Mode {NONE,CLICK,SHOW}
    private Mode mode = Mode.NONE;
    private Mode lastMode = Mode.NONE;
    private GameMode setGameMode = GameMode.NORMAL;
    private GameMode gameMode = GameMode.NORMAL;

    @Override
    public void init() {
        this.uiManager = objectManager.get(UIManager.class);
        this.bigBlueButtons = uiManager.getBigBlueButtons();
        this.sequence = new List<>();
        ButtonHelper.lightDuration = lightDuration;
        ButtonHelper.setBigBlueButtons(bigBlueButtons.toArrayList());
        Leaderboard.init();
        LeaderBoardUI.init(objectManager, getScreenWidth(), getScreenHeight());

        sfx.loadSFX("bum");
        sfx.loadSFX("blocked");
        sfx.loadSFX("wrong");
        sfx.loadSFX("levelup");
        objectManager.add(sfx);
    }

    @Override
    public void update(double deltaTime) {
        ButtonHelper.update(deltaTime);

        if (mode == Mode.SHOW) {
            if (lastMode != Mode.SHOW) {
                //Switched to SHOW mode
                updateGameModeSettings();
                level++;
                updateLevel(level);
                Console.log("SHOW MODE - LEVEL " + (level));
                sequence.toFirst();
                timer = Timer.create(this::showNextInSequence, flashDelay).start();
            }
        } else if (mode == Mode.CLICK) {
            if (lastMode != Mode.CLICK) {
                //Switched to CLICK mode
                Console.log("CLICK MODE - LEVEL " + (level));
                resetClickTimer(); // Timer für Reaktionszeit starten
                if (gameMode == GameMode.REVERSE) {
                    sequence.toLast();
                } else {
                    sequence.toFirst();
                }
            }
        } else {
            if (lastMode != Mode.NONE) {
                //Switched to NONE mode
                Console.log("NONE MODE");
            }
        }
        lastMode = mode;
    }

    private void updateGameModeSettings() {
        gameMode = setGameMode;
        switch (gameMode) {
            case NORMAL: {
                lightDuration = defaultLightDuration;
                flashDelay = defaultFlashDelay;
                levelDelay = defaultLevelDelay;
                break;
            }
            case SPEED: {
                lightDuration = speedLightDuration;
                flashDelay = speedFlashDelay;
                levelDelay = speedLevelDelay;
                break;
            }
        }
        ButtonHelper.lightDuration = lightDuration;
    }
    private void showNextInSequence() {
        if (sequence == null || sequence.isEmpty() || !sequence.hasAccess()) {
            addToSequence();
            return;
        }
        Button b = sequence.getContent();
        ButtonHelper.flash(b);
        sequence.next();
    }
    private void addToSequence() {
        if (sequence == null) return;
        Button random = ButtonHelper.getRandomButton();
        if (random != null) {
            sequence.append(random);
            ButtonHelper.flash(random);
            timer.stop();
            mode = Mode.CLICK;
        }
    }
    private void updateLevel(int lvl) {
        uiManager.getLevelText().setText("Level " + lvl);
    }

    // ----------------- Timer Methods ----------------
    private void startGameTimer() {
        gameStartTime = System.nanoTime();
        debugWasEnabledThisGame = debugEnabled;
        gameActive = true;
        gameTime = 0;
    }
    private void stopGameTimer() {
        if (gameActive) {
            gameTime = (System.nanoTime() - gameStartTime) / 1_000_000_000.0;
            gameActive = false;
        }
    }
    private void cancelGameTimer() {
        gameActive = false;
        gameTime = 0;
    }
    public double getCurrentGameTime() {
        if (gameActive) {
            return (System.nanoTime() - gameStartTime) / 1_000_000_000.0;
        }
        return gameTime;
    }

    // ----------------- Click Speed Methods ----------------
    private void startClickSpeedTimer() {
        lastClickTime = System.nanoTime();
        totalClickTime = 0;
        clickCount = 0;
        averageClickSpeed = 0;
    }
    private void recordClick() {
        long now = System.nanoTime();
        double clickTime = (now - lastClickTime) / 1_000_000.0;
        totalClickTime += clickTime;
        clickCount++;
        averageClickSpeed = totalClickTime / clickCount;
        lastClickTime = now;
    }
    private void resetClickTimer() {
        lastClickTime = System.nanoTime();
    }
    public double getAverageClickSpeed() {
        return averageClickSpeed;
    }
    // -------------------------------------------------

    private void lost(Button b) {
        stopGameTimer();
        sfx.play("wrong", 0.5f);
        for (Button bu : bigBlueButtons) {
            ButtonHelper.flash(bu, ColorPalette.WRONG_BUTTON);
        }

        if (!debugWasEnabledThisGame) {
            Leaderboard.addEntry(name, level, gameMode, gameStartTime, gameTime, uiManager.getButtonCount(), averageClickSpeed);
            LeaderBoardUI.refresh(objectManager, getScreenWidth(), getScreenHeight());
        }

        Console.log("LOST after " + String.format("%.2f", gameTime) + "s | Avg Click Speed: " + String.format("%.0f", averageClickSpeed) + "ms");
        clear();
    }
    private void clear() {
        if (gameActive) {
            cancelGameTimer();
        }
        mode = Mode.NONE;
        sequence.clear();
        level = 0;
        updateLevel(level);
        bigBlueButtons = uiManager.getBigBlueButtons();
        ButtonHelper.setBigBlueButtons(bigBlueButtons.toArrayList());
    }
    public void startButtonSequence() {
        clear();
        startGameTimer();
        startClickSpeedTimer();
        mode = Mode.SHOW;
    }

    //Event Handlers
    public void onClickBig(Button b) {
        if (mode != Mode.CLICK || sequence.isEmpty() || !sequence.hasAccess()) {
            sfx.play("blocked", 0.5f);
            return;
        }
        Button expected = sequence.getContent();
        if (b == expected) {
            recordClick(); // Klick-Geschwindigkeit messen
            ButtonHelper.flash(b);
            if (gameMode == GameMode.REVERSE) {
                sequence.prev();
            } else {
                sequence.next();
            }
            //Check if sequence is finished
            if (!sequence.hasAccess()) {
                mode = Mode.NONE;
                sfx.play("levelup");
                Timer.create(() -> mode = Mode.SHOW, levelDelay, false).start();
            } else {
                sfx.play("bum", 0.5f);
            }
        } else {
            lost(b);
        }

    }
    public void onCountSliderValueChange(float newButtonCount) {
        newButtonCount = Math.round(newButtonCount);
        if ((int)newButtonCount == uiManager.getButtonCount()) return;
        uiManager.setButtonCount((int)newButtonCount);
        uiManager.setupBigButtons();
        clear();
    }
    public void onStartButtonClick() {
        if ((name == null || name.isEmpty() || name.isBlank())) {
            sfx.play("blocked", 0.5f);
            return;
        }
        startButtonSequence();
    }
    public void onModeDropdownChanged(int index) {
        GameMode selectedMode = GameMode.values()[index];
        if (selectedMode == setGameMode) return;
        setGameMode = selectedMode;
        updateGameModeSettings();
        Console.log("GAMEMODE: " + setGameMode);
    }
    public void onNameUnfocus(String name) {
        this.name = name.trim();
    }
    @Override
    public void onWindowResized(int width, int height) {
        uiManager.setupBigButtons();
        LeaderBoardUI.refresh(objectManager, getScreenWidth(), getScreenHeight());
        clear();
    }
    public void onDebugModeChanged(boolean checked) {
        debugEnabled = checked;
        if (checked) {
            debugWasEnabledThisGame = true;
        }
    }

    //Getter/Setter
    public void setUiManager(UIManager uiManager) {
        this.uiManager = uiManager;
    }
    public void setBigBlueButtons(List<Button> bigBlueButtons) {
        this.bigBlueButtons = bigBlueButtons;
        ButtonHelper.setBigBlueButtons(bigBlueButtons.toArrayList());
    }

    //Leaderboard
    private static class LeaderBoardUI {
        private static List<Text> entries = new List<>();
        private static final int MAX_ENTRIES = 15;
        private static final int MAX_NAME_LENGTH = 15;

        // Base values
        private static final int BASE_PANEL_X = 529;
        private static final int BASE_PANEL_Y = 150;
        private static final int BASE_ENTRY_HEIGHT = 32;
        private static final int BASE_FONT_SIZE = 18;

        // Reference resolution
        private static int BASE_WIDTH = 0;
        private static int BASE_HEIGHT = 0;

        private LeaderBoardUI() {
        }

        private static void init(GameObjectManager objectManager, int screenWidth, int screenHeight) {
            if (BASE_WIDTH == 0) {
                BASE_WIDTH = screenWidth;
                BASE_HEIGHT = screenHeight;
            }
            entries = new List<>();
            refresh(objectManager, screenWidth, screenHeight);
        }

        private static void refresh(GameObjectManager objectManager, int screenWidth, int screenHeight) {
            for (Text text : entries) {
                objectManager.remove(text);
            }
            entries.clear();

            float scaleX = (float) screenWidth / BASE_WIDTH;
            float scaleY = (float) screenHeight / BASE_HEIGHT;
            float fontScale = Math.min(scaleX, scaleY);

            int panelX = scale(BASE_PANEL_X, scaleX);
            int panelY = scale(BASE_PANEL_Y, scaleY);
            int entryHeight = scale(BASE_ENTRY_HEIGHT, fontScale);
            int fontSize = scaleFont(BASE_FONT_SIZE, fontScale);

            List<Leaderboard.PlayerEntry> leaderboard = Leaderboard.getLeaderboard();
            List<Leaderboard.PlayerEntry> list = Sort.sort(leaderboard);

            int y = panelY;
            int count = 0;

            for (Leaderboard.PlayerEntry entry : list) {
                if (count >= MAX_ENTRIES) break;

                String displayName = shortenName(entry.name);
                int highScore = getHighScore(entry);
                String gameMode = getGameMode(entry) != null ? "(" + getGameMode(entry) + ")" : "";
                String displayText = gameMode + " " + displayName + " - " + highScore;


                Text text = new Text.Builder(displayText)
                        .position(new Vector2(panelX, y))
                        .color(ColorPalette.TEXT_MAIN)
                        .font(new Font("Arial", Font.BOLD, fontSize))
                        .build();

                entries.append(text);
                objectManager.add(text);

                y += entryHeight;
                count++;
            }
        }

        private static int scale(int value, float scaleFactor) {
            return (int) (value * scaleFactor);
        }

        private static int scaleFont(int baseSize, float scaleFactor) {
            return Math.max(8, (int) (baseSize * scaleFactor));
        }

        private static String shortenName(String name) {
            if (name == null) return "???";
            if (name.length() <= MAX_NAME_LENGTH) {
                return name;
            }
            return name.substring(0, MAX_NAME_LENGTH - 2) + "..";
        }

        private static int getHighScore(Leaderboard.PlayerEntry entry) {
            if (entry.getHighScoreGame() == null) return 0;
            return entry.getHighScoreGame().score;
        }

        private static String getGameMode(Leaderboard.PlayerEntry entry) {
            if (entry.getHighScoreGame() == null) return null;
            String s = "";
            s += entry.getHighScoreGame().gameMode.getShort();
            s += entry.getHighScoreGame().buttonCount;
            return s;
        }
    }

    @Override
    public void onCollision(GameObject gameObject) {
    }
    @Override
    public void draw(Graphics2D graphics2D) {

    }
}
