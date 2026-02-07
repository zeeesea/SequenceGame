import GameEngine.Core.gameObject.GameObject;
import GameEngine.Core.gameObject.Obj.Button;
import GameEngine.Core.util.Console.Console;
import GameEngine.Core.util.Timer.Timer;
import Helper.ButtonHelper;
import Helper.ColorPalette;
import Helper.List;

import java.awt.*;

public class LogicManager extends GameObject {

    // ----------------- References --------------------
    private UIManager uiManager;
    private List<Button> bigBlueButtons;
    private List<Button> sequence;

    // ----------------- Game Variables ----------------

    // Values --Normal Mode--
    private float defaultLightDuration = 0.28f;
    private float defaultFlashDelay = 0.45f;
    private float defaultLevelDelay = 0.6f;
    //Values --Speed Mode--
    private float speedLightDuration = 0.16f;
    private float speedFlashDelay = 0.24f;
    private float speedLevelDelay = 0.4f;

    private int level = 0;
    private float lightDuration = defaultLightDuration;
    private float flashDelay = defaultFlashDelay;
    private float levelDelay = defaultLevelDelay;
    Timer timer;


    // ----------------- Modes -------------------------
    private enum Mode {NONE,CLICK,SHOW}
    private enum GameMode {NORMAL,SPEED,REVERSE}
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
    private void lost(Button b) {
        for (Button bu : bigBlueButtons) {
            ButtonHelper.flash(bu, ColorPalette.WRONG_BUTTON);
        }
        clear();
        Console.log("LOST");
    }
    private void clear() {
        mode = Mode.NONE;
        sequence.clear();
        level = 0;
        updateLevel(level);
        bigBlueButtons = uiManager.getBigBlueButtons();
        ButtonHelper.setBigBlueButtons(bigBlueButtons.toArrayList());
    }
    public void startButtonSequence() {
        clear();
        mode = Mode.SHOW;
    }


    //Event Handlers
    public void onClickBig(Button b) {
        if (mode != Mode.CLICK || sequence.isEmpty() || !sequence.hasAccess()) return;
        Button expected = sequence.getContent();
        if (b == expected) {
            ButtonHelper.flash(b);
            if (gameMode == GameMode.REVERSE) {
                sequence.prev();
            } else {
                sequence.next();
            }
            //Check if sequence is finished
            if (!sequence.hasAccess()) {
                mode = Mode.NONE;
                Timer.create(() -> mode = Mode.SHOW, levelDelay, false).start();
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
        startButtonSequence();
    }
    public void onModeDropdownChanged(int index) {
        GameMode selectedMode = GameMode.values()[index];
        if (selectedMode == setGameMode) return;
        setGameMode = selectedMode;
        updateGameModeSettings();
        Console.log("GAMEMODE: " + setGameMode);
    }

    @Override
    public void onWindowResized(int width, int height) {
        uiManager.setupBigButtons();
        clear();
    }

    //Getter/Setter
    public void setUiManager(UIManager uiManager) {
        this.uiManager = uiManager;
    }
    public void setBigBlueButtons(List<Button> bigBlueButtons) {
        this.bigBlueButtons = bigBlueButtons;
        ButtonHelper.setBigBlueButtons(bigBlueButtons.toArrayList());
    }


    @Override
    public void onCollision(GameObject gameObject) {
    }
    @Override
    public void draw(Graphics2D graphics2D) {

    }
}
