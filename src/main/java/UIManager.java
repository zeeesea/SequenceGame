import GameEngine.Core.gameObject.GameObject;
import GameEngine.Core.gameObject.Obj.Button;
import GameEngine.Core.gameObject.Obj.Slider;
import GameEngine.Core.util.Console.Console;
import GameEngine.Core.util.Vector2;
import Helper.ColorPalette;
import Helper.List;

import java.awt.*;
import java.util.ArrayList;

public class UIManager extends GameObject {
    //References
    private LogicManager logicManager;

    //UI Elements
    private List<Button> bigBlueButtons = new List<Button>();
    private Slider buttonCountSlider;

    //Button Grid - RIGHT
    private int buttonCount = 3;
    private int buttonMargin = 25;
    private int buttonGridSize = 650;


    private enum Mode {CLICK, SHOW}
    private enum GameMode {NORMAL,SPEED,REVERSE}


    @Override
    public void init() {
        logicManager = new LogicManager(this, bigBlueButtons);
        objectManager.add(logicManager);

        setupBigButtons();
        setupButtonCountSlider();
    }

    private void setupBigButtons() {
        int buttonSize;
        Vector2 gridOffset;

        // Remove old buttons
        for (int i = 0; i < bigBlueButtons.size(); i++) {
            Button b = bigBlueButtons.next().getContent();
            objectManager.remove(b);
        }
        bigBlueButtons.clear();

        //Calculations
        buttonSize = (buttonGridSize - (buttonMargin * (buttonCount - 1))) / buttonCount;

        gridOffset = new Vector2((float) getScreenWidth() / 2, (float) (getScreenHeight() - buttonGridSize) /2);

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
                        .border(ColorPalette.TEXT_ALT, 5)
                        .tag(Integer.toString(currentBtn))
                        .font(new Font("Arial", Font.BOLD, 26))
                        .textColor(Color.WHITE)
                        .onClick(this::onClick)
                        .build();
                bigBlueButtons.append(b);
                objectManager.add(b);

                x += buttonSize + buttonMargin;
            }
            y += buttonSize + buttonMargin;
            x = gridOffset.xToInt();
        }
    }
    private void setupButtonCountSlider() {
        buttonCountSlider = new Slider.Builder()
                .pos(new Vector2(300,300))
                .cornerRadius(10)
                .startValue(buttonCount)
                .borderColor(ColorPalette.TEXT_ALT)
                .handleShape(Slider.HandleShape.CIRCLE)
                .handleColor(ColorPalette.BUTTON_BLUE)
                .range(2, 5)
                .onDragEndValue(this::onCountSliderValueChange)
                .build();
        objectManager.add(buttonCountSlider);
    }



    private void onCountSliderValueChange(float v) {
        buttonCount = (int) v;
        Console.log(buttonCount);
        setupBigButtons();
    }

    @Override
    public void update(double v) {
    }
    private void onClick(Button b) {
        if (logicManager != null) {
            logicManager.onClick(b);
        }
    }
    @Override
    public void draw(Graphics2D graphics2D) {
    }
    @Override
    public void onCollision(GameObject gameObject) {

    }


    //Getter/Setter
}
