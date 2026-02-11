import GameEngine.Core.gameObject.GameObject;
import GameEngine.Core.gameObject.Obj.*;
import GameEngine.Core.gameObject.Obj.Button;
import GameEngine.Core.gameObject.Obj.TextField;
import GameEngine.Core.util.Vector2;
import Helper.ColorPalette;
import Helper.List;

import java.awt.*;

public class UIManager extends GameObject {

    //<editor-fold desc="VARIABLES">

    //References
    private LogicManager logicManager;
    private List<Button> bigBlueButtons = new List<Button>();

    //Button Grid - RIGHT
    private int buttonCount = 3;
    private final int buttonMargin = 25;

    // Base Res
    private final int BASE_WIDTH = getScreenWidth();
    private final int BASE_HEIGHT = getScreenHeight();

    //Settings UI - LEFT
    private UIRect background1, background2, background3, background4;
    private Text highscoresDescriptionText, leaderBoardText, debugModeText, titleText, levelText, highscoresText, volumeText;
    private Slider buttonCountSlider, volumeSlider;
    private CheckBox debugModeCheckbox;
    private Dropdown modeDropdown;
    private TextField nameInputField;
    private Button startButton;

    //</editor-fold>

    //<editor-fold desc="GAME LOOP">

    @Override
    public void init() {
        logicManager = objectManager.get(LogicManager.class);
        setupBigButtons();
        setupLeftUI();
    }

    //</editor-fold>

    //<editor-fold desc="SETUP">
    public void setupBigButtons() {
        int buttonSize;
        Vector2 gridOffset;

        // Remove old buttons
        bigBlueButtons.toFirst();
        for (int i = 0; i < bigBlueButtons.size(); i++) {
            Button b = bigBlueButtons.getContent();
            objectManager.remove(b);
            bigBlueButtons.next();
        }
        bigBlueButtons.clear();

        // Skalierungsfaktor berechnen
        float scaleX = (float) getScreenWidth() / BASE_WIDTH;
        float scaleY = (float) getScreenHeight() / BASE_HEIGHT;
        float scale = Math.min(scaleX, scaleY);

        // Dynamische Werte
        int buttonGridSize = (int) (650 * scale);
        int scaledMargin = (int) (buttonMargin * scale);
        int gridOffsetX = getScreenWidth() / 2 + (int)(50 * scaleX);

        //Calculations
        buttonSize = (buttonGridSize - (scaledMargin * (buttonCount - 1))) / buttonCount;

        gridOffset = new Vector2((float) gridOffsetX, (float) (getScreenHeight() - buttonGridSize) /2);

        int currentBtn = 0;
        //Create Buttons
        int y = gridOffset.yToInt();
        int x = gridOffset.xToInt();
        for (int i = 0; i < buttonCount; i++) {
            for (int j = 0; j < buttonCount; j++) {
                currentBtn++;
                Button b = new Button.Builder()
                        .cornerRadius(10)
                        .rect(new Rectangle(x,y, buttonSize, buttonSize))
                        .color(ColorPalette.BUTTON_BLUE)
                        .smoothHover(5, 100)
                        .border(ColorPalette.BORDER, 5)
                        .tag(Integer.toString(currentBtn))
                        .font(new Font("Arial", Font.BOLD, 26))
                        .textColor(Color.WHITE)
                        .onClick(this::onClickBig)
                        .build();
                bigBlueButtons.append(b);
                objectManager.add(b);

                x += buttonSize + buttonMargin;
            }
            y += buttonSize + buttonMargin;
            x = gridOffset.xToInt();
        }
    }
    private void setupLeftUI() {
        // Skalierungsfaktor berechnen
        float scaleX = (float) getScreenWidth() / BASE_WIDTH;
        float scaleY = (float) getScreenHeight() / BASE_HEIGHT;

        background1 = new UIRect.Builder()
                .rect(new Rectangle(scale(29, scaleX), scale(72, scaleY), scale(467, scaleX), scale(360, scaleY)))
                .fillColor(ColorPalette.BUTTON_BLUE)
                .borderColor(ColorPalette.BORDER)
                .cornerRadius(8)
                .borderWidth(5)
                .hasBorder(true)
                .hasFill(true)
                .renderOrder(-1)
                .build();
        objectManager.add(background1);

        background2 = new UIRect.Builder()
                .rect(new Rectangle(scale(28, scaleX), scale(446, scaleY), scale(464, scaleX), scale(268, scaleY)))
                .fillColor(ColorPalette.BUTTON_BLUE)
                .borderColor(ColorPalette.BORDER)
                .cornerRadius(8)
                .borderWidth(5)
                .hasBorder(true)
                .hasFill(true)
                .renderOrder(-1)
                .build();
        objectManager.add(background2);

        background4 = new UIRect.Builder()
                .rect(new Rectangle(scale(511, scaleX), scale(73, scaleY), scale(233, scaleX), scale(774, scaleY)))
                .fillColor(ColorPalette.BUTTON_BLUE)
                .borderColor(ColorPalette.BORDER)
                .cornerRadius(8)
                .borderWidth(5)
                .hasBorder(true)
                .hasFill(true)
                .renderOrder(-1)
                .build();
        objectManager.add(background4);

        background3 = new UIRect.Builder()
                .rect(new Rectangle(scale(29, scaleX), scale(736, scaleY), scale(462, scaleX), scale(110, scaleY)))
                .fillColor(ColorPalette.BUTTON_BLUE)
                .borderColor(ColorPalette.BORDER)
                .cornerRadius(8)
                .borderWidth(5)
                .hasBorder(true)
                .hasFill(true)
                .renderOrder(-1)
                .build();
        objectManager.add(background3);

        float fontScale = Math.min(scaleX, scaleY);

        titleText = new Text.Builder("Sequence Game")
                .position(new Vector2(scale(225, scaleX), scale(52, scaleY)))
                .color(ColorPalette.TEXT_MAIN)
                .font(new Font("Arial", Font.BOLD, scaleFont(35, fontScale)))
                .build();
        objectManager.add(titleText);

        levelText = new Text.Builder("Level 0")
                .position(new Vector2(scale(450, scaleX), scale(123, scaleY)))
                .color(ColorPalette.TEXT_MAIN)
                .font(new Font("Arial", Font.BOLD, scaleFont(29, fontScale)))
                .alignment(Text.TextAlignment.RIGHT)
                .build();
        objectManager.add(levelText);

        modeDropdown = new Dropdown.Builder()
                .pos(new Vector2(scale(80, scaleX), scale(90, scaleY)))
                .size(new Vector2(scale(230, scaleX), scale(50, scaleY)))
                .options("Normal", "Speed", "Reverse")
                .font(new Font("Arial", Font.BOLD, scaleFont(28, fontScale)))
                .textColor(ColorPalette.TEXT_MAIN)
                .backgroundColor(ColorPalette.BORDER)
                .hoverColor(ColorPalette.BACKGROUND)
                .selectedColor(ColorPalette.BUTTON_BLUE)
                .cornerRadius(10)
                .onIndexChanged(this::onModeDropdownChanged)
                .borderColor(ColorPalette.BORDER)
                .build();
        objectManager.add(modeDropdown);

        buttonCountSlider = new Slider.Builder()
                .pos(new Vector2(scale(50, scaleX), scale(160, scaleY)))
                .size(scale(424, scaleX), scale(21, scaleY))
                .cornerRadius(10)
                .startValue(buttonCount)
                .borderColor(ColorPalette.BORDER)
                .handleShape(Slider.HandleShape.CIRCLE)
                .fillColor(ColorPalette.BORDER)
                .backgroundColor(ColorPalette.BUTTON_LIGHT)
                .handleColor(ColorPalette.BUTTON_BLUE)
                .range(2, 10)
                .onDragEndValue(this::onCountSliderValueChange)
                .build();
        objectManager.add(buttonCountSlider);

        debugModeCheckbox = new CheckBox.Builder()
                .pos(new Vector2(scale(54, scaleX), scale(212, scaleY)))
                .label("Debug Mode")
                .boxSize(scale(25, fontScale))
                .boxColor(ColorPalette.BUTTON_LIGHT)
                .hoverBorderColor(ColorPalette.BORDER)
                .onChanged(this::onDebugModeChange)
                .checkedColor(ColorPalette.BORDER)
                .labelColor(ColorPalette.TEXT_MAIN)
                .labelFont(new Font("Arial", Font.BOLD, scaleFont(14, fontScale)))
                .build();
        objectManager.add(debugModeCheckbox);

        startButton = new Button.Builder()
                .rect(new Rectangle(scale(85, scaleX), scale(273, scaleY), scale(358, scaleX), scale(118, scaleY)))
                .color(ColorPalette.BUTTON_BLUE)
                .border(ColorPalette.BORDER, 5)
                .text("Start")
                .textColor(ColorPalette.TEXT_MAIN)
                .font(new Font("Arial", Font.BOLD, scaleFont(60, fontScale)))
                .tag("startButton")
                .cornerRadius(19)
                .onClick(this::onStartButtonClick)
                .smoothHover(10.0f, 150.0f)
                .build();
        objectManager.add(startButton);

        highscoresText = new Text.Builder("Highscores")
                .position(new Vector2(scale(42, scaleX), scale(486, scaleY)))
                .color(ColorPalette.TEXT_MAIN)
                .font(new Font("Arial", Font.BOLD, scaleFont(24, fontScale)))
                .build();
        objectManager.add(highscoresText);

        nameInputField = new TextField.Builder()
                .rect(new Rectangle(scale(55, scaleX), scale(513, scaleY), scale(300, scaleX), scale(60, scaleY)))
                .font(new Font("Arial", Font.BOLD, scaleFont(30, fontScale)))
                .textColor(ColorPalette.TEXT_MAIN)
                .backgroundColor(ColorPalette.BORDER)
                .borderColor(ColorPalette.BORDER)
                .focusedColor(ColorPalette.BORDER)
                .focusedBorderColor(ColorPalette.BORDER)
                .textColor(ColorPalette.TEXT_MAIN)
                .cornerRadius(8)
                .placeholder("Name")
                .build();
        objectManager.add(nameInputField);

        highscoresDescriptionText = new Text.Builder("Enter name and click button to submit Highscore")
                .position(new Vector2(scale(45, scaleX), scale(621, scaleY)))
                .color(ColorPalette.TEXT_MAIN)
                .font(new Font("Arial", Font.BOLD, scaleFont(16, fontScale)))
                .build();
        objectManager.add(highscoresDescriptionText);

        leaderBoardText = new Text.Builder("Leaderboard")
                .position(new Vector2(scale(529, scaleX), scale(118, scaleY)))
                .color(ColorPalette.TEXT_MAIN)
                .font(new Font("Arial", Font.BOLD, scaleFont(28, fontScale)))
                .build();
        objectManager.add(leaderBoardText);

        debugModeText = new Text.Builder("Enable debug mode for this feature")
                .position(new Vector2(scale(50, scaleX), scale(785, scaleY)))
                .color(ColorPalette.TEXT_MAIN)
                .font(new Font("Arial", Font.BOLD, scaleFont(16, fontScale)))
                .build();
        objectManager.add(debugModeText);

        volumeSlider = new Slider.Builder()
                .pos(new Vector2(scale(1200, scaleX), scale(820, scaleY)))
                .size(scale(300, scaleX), scale(17, scaleY))
                .cornerRadius(10)
                .startValue(buttonCount)
                .borderColor(ColorPalette.BORDER)
                .handleShape(Slider.HandleShape.CIRCLE)
                .fillColor(ColorPalette.BORDER)
                .backgroundColor(ColorPalette.BUTTON_LIGHT)
                .handleColor(ColorPalette.BUTTON_BLUE)
                .range(0, 1)
                .startValue(0.8f)
                .onDragEndValue(this::onVolumeChange)
                .build();
        objectManager.add(volumeSlider);

        volumeText = new Text.Builder("Volume")
                .position(new Vector2(scale(1200, scaleX), scale(812, scaleY)))
                .color(ColorPalette.TEXT_MAIN)
                .font(new Font("Arial", Font.BOLD, scaleFont(20, fontScale)))
                .build();
        objectManager.add(volumeText);
    }

    private int scale(int value, float scaleFactor) {
        return (int) (value * scaleFactor);
    }
    private int scaleFont(int baseSize, float scaleFactor) {
        return Math.max(8, (int) (baseSize * scaleFactor));
    }
    //</editor-fold>

    //<editor-fold desc="EVENTS">

    private void onCountSliderValueChange(float v) {
        if (logicManager != null) {
            logicManager.onCountSliderValueChange(v);
        }
    }
    private void onClickBig(Button b) {
        if (logicManager != null) {
            logicManager.onClickBig(b);
        }
    }
    private void onStartButtonClick() {
        if (logicManager != null) {
            logicManager.onStartButtonClick();
        }
    }
    private void onModeDropdownChanged(int index) {
        if (logicManager != null) {
            logicManager.onModeDropdownChanged(index);
        }
    }
    private void onDebugModeChange(boolean checked) {
        if (logicManager != null) {
            logicManager.onDebugModeChange(checked);
        }
    }
    private void onVolumeChange(float v) {
        if (logicManager != null) {
            logicManager.onVolumeChange(v);
        }
    }
    //</editor-fold>

    //<editor-fold desc="GETTER/SETTER">

    public List<Button> getBigBlueButtons() {
        return bigBlueButtons;
    }
    public void setButtonCount(int buttonCount) {
        this.buttonCount = buttonCount;
    }
    public int getButtonCount() {
        return buttonCount;
    }

    public Text getHighscoresText() {
        return highscoresText;
    }
    public Text getLevelText() {
        return levelText;
    }
    public Text gettitleText() {
        return titleText;
    }
    public Text getDebugModeText() {
        return debugModeText;
    }
    public Text getLeaderBoardText() {
        return leaderBoardText;
    }
    public Text getHighscoresDescriptionText() {
        return highscoresDescriptionText;
    }
    public TextField getNameInputField() {
        return nameInputField;
    }
    //</editor-fold>

    //<editor-fold desc="WINDOW RESIZE">

    @Override
    public void onWindowResized(int width, int height) {
        rebuildUI();
    }

    private void rebuildUI() {
        if (background1 != null) objectManager.remove(background1);
        if (background2 != null) objectManager.remove(background2);
        if (background3 != null) objectManager.remove(background3);
        if (background4 != null) objectManager.remove(background4);
        if (titleText != null) objectManager.remove(titleText);
        if (levelText != null) objectManager.remove(levelText);
        if (modeDropdown != null) objectManager.remove(modeDropdown);
        if (buttonCountSlider != null) objectManager.remove(buttonCountSlider);
        if (debugModeCheckbox != null) objectManager.remove(debugModeCheckbox);
        if (startButton != null) objectManager.remove(startButton);
        if (highscoresText != null) objectManager.remove(highscoresText);
        if (nameInputField != null) objectManager.remove(nameInputField);
        if (highscoresDescriptionText != null) objectManager.remove(highscoresDescriptionText);
        if (leaderBoardText != null) objectManager.remove(leaderBoardText);
        if (debugModeText != null) objectManager.remove(debugModeText);
        if (volumeSlider != null) objectManager.remove(volumeSlider);
        if (volumeText != null) objectManager.remove(volumeText);

        setupBigButtons();
        setupLeftUI();

        if (logicManager != null) {
            logicManager.setBigBlueButtons(bigBlueButtons);
        }
    }
    //</editor-fold>

    //<editor-fold desc="USELESS">

    @Override
    public void update(double v) {
    }


    @Override
    public void draw(Graphics2D graphics2D) {
    }
    @Override
    public void onCollision(GameObject gameObject) {
    }

    //</editor-fold>
}