import GameEngine.Core.gameObject.GameObject;
import GameEngine.Core.gameObject.Obj.Button;
import GameEngine.Core.util.Console.Console;
import GameEngine.Core.util.MathUtils;
import Helper.ColorPalette;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;



public class LogicManager extends GameObject {

    //References
    private UIManager uiManager;
    private ArrayList<Button> bigBlueButtons;
    Random rand = new Random();

    //Light Up Variables
    private float lightDuration = 0.4f;
    private float timer = 0f;
    private Button currentButton = null;
    private enum LightState {NONE, LIGHTUP, LIGHTDOWN}
    private LightState lightState = LightState.NONE;

    public LogicManager(UIManager uiManager, ArrayList<Button> bigBlueButtons) {
        this.uiManager = uiManager;
        this.bigBlueButtons = bigBlueButtons;
    }



    @Override
    public void update(double deltaTime) {
        updateButtonLight(deltaTime);
    }

    private void updateButtonLight(double deltaTime) {
        if (currentButton == null || lightState == LightState.NONE) return;

        timer += (float) deltaTime;
        float t = MathUtils.clamp(timer / lightDuration, 0f, 1f);

        if (lightState == LightState.LIGHTUP) {
            Color c = MathUtils.lerpColor(ColorPalette.BUTTON_BLUE,ColorPalette.BUTTON_LIGHT,t);
            currentButton.setColor(c);
            if(t >= 1f) {
                lightState = LightState.LIGHTDOWN;
                currentButton.setColor(ColorPalette.BUTTON_LIGHT);
                timer = 0f;
            }
        } else if (lightState == LightState.LIGHTDOWN) {
            Color c = MathUtils.lerpColor(ColorPalette.BUTTON_LIGHT, ColorPalette.BUTTON_BLUE, t);
            currentButton.setColor(c);
            if (t >= 1f) {
                lightState = LightState.NONE;
                currentButton.setColor(ColorPalette.BUTTON_BLUE);
                currentButton = null;
            }
        }
    }


    public void onClick(Button b) {
        Console.log("Button clicked: " + b.tag);
        buttonFlash(getRandomButton());
    }


    public Button getRandomButton(){
        return bigBlueButtons.get(rand.nextInt(bigBlueButtons.size()));
    }
    public void buttonFlash(Button b) {
        if (currentButton != null) currentButton.setColor(ColorPalette.BUTTON_BLUE);
        currentButton = b;
        timer = 0f;
        lightState = LightState.LIGHTUP;
    }
    //Getter/Setter
    public void setUiManager(UIManager uiManager) {
        this.uiManager = uiManager;
    }
    public void setBigBlueButtons(ArrayList<Button> bigBlueButtons) {
        this.bigBlueButtons = bigBlueButtons;
    }






    @Override
    public void init() {
    }
    @Override
    public void onCollision(GameObject gameObject) {

    }
    @Override
    public void draw(Graphics2D graphics2D) {

    }
}
