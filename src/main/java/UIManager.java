import GameEngine.Core.GameEngine;
import GameEngine.Core.gameObject.GameObject;
import GameEngine.Core.gameObject.Obj.Button;
import GameEngine.Core.gameObject.Obj.Slider;
import GameEngine.Core.util.Console.Console;
import GameEngine.Core.util.Vector2;

import java.awt.*;
import java.util.ArrayList;

public class UIManager extends GameObject {
    //UI Elements
    private ArrayList<Button> bigBlueButtons = new ArrayList<>();
    private Slider buttonCountSlider;

    //Button Grid
    private int buttonCount = 3;
    private int buttonMargin = 25;
    private int buttonGridSize = 650;
    private int buttonSize;

    private int yOffset;
    private Vector2 gridOffset;


    private enum Mode {CLICK, SHOW}
    private enum GameMode{NORMAL,SPEED,REVERSE}

    @Override
    public void init() {
        setupBigButtons();
        setupButtonCountSlider();
    }

    private void setupBigButtons() {
        // Alte Buttons korrekt entfernen
        for (Button b : bigBlueButtons) {
            objectManager.remove(b);
        }
        bigBlueButtons.clear();

        //Calculations
        buttonSize = (buttonGridSize - (buttonMargin * (buttonCount - 1))) / buttonCount;

        yOffset = (getScreenHeight() - buttonGridSize) /2;
        gridOffset = new Vector2(getScreenWidth() / 2, yOffset);


        int y = gridOffset.yToInt();
        int x = gridOffset.xToInt();
        for (int i = 0; i < buttonCount; i++) {
            for (int j = 0; j < buttonCount; j++) {
                Button b = new Button.Builder()
                        .rect(new Rectangle(x,y, buttonSize, buttonSize))
                        .color(new Color(37, 115, 193))
                        .smoothHover(5, 100)
                        .font(new Font("Arial", Font.BOLD, 26))
                        .textColor(Color.WHITE)
                        .onClick(this::onClick)
                        .build();
                bigBlueButtons.add(b);
                objectManager.add(b);

                x += buttonSize + buttonMargin;
            }
            y += buttonSize + buttonMargin;
            x = gridOffset.xToInt();
        }
    }
    private void setupButtonCountSlider() {
        buttonCountSlider = new Slider.Builder()
                .position(300,300)
                .startValue(buttonCount)
                .range(2, 5)
                .onValueChanged(this::onCountSliderValueChange)
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
    }
    @Override
    public void draw(Graphics2D graphics2D) {
    }
    @Override
    public void onCollision(GameObject gameObject) {

    }
}
