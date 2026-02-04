package Helper;

import GameEngine.Core.gameObject.Obj.Button;
import GameEngine.Core.util.MathUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class ButtonHelper {
    private static final ArrayList<ButtonHelper> buttonHelpers = new ArrayList<ButtonHelper>();
    private static ArrayList<Button> bigBlueButtons;
    private static final Random rand = new Random();

    public static float lightDuration = 0.8f;

    private ButtonHelper(Button b) {
        this.b = b;
        timer = 0f;
        b.setColor(ColorPalette.BUTTON_BLUE);
        lightState = LightState.LIGHTUP;
    }
    public static void update(double deltaTime) {
        Iterator<ButtonHelper> iterator = buttonHelpers.iterator();
        while (iterator.hasNext()) {
            ButtonHelper bh = iterator.next();
            bh.updateButtonLight(deltaTime, iterator);
        }
    }
    public static void setBigBlueButtons(ArrayList<Button> bigBlueButtons) {
        ButtonHelper.bigBlueButtons = bigBlueButtons;
    }
    public static Button getRandomButton(){
        return bigBlueButtons.get(rand.nextInt(bigBlueButtons.size()));
    }
    public static void flash(Button b) {
        for (ButtonHelper bh : buttonHelpers) {
            if (bh.b == b) return;
        }
        ButtonHelper bh = new ButtonHelper(b);
        buttonHelpers.add(bh);
    }

    //Flash Variables
    private Button b;
    private float timer = 0f;

    private enum LightState {NONE, LIGHTUP, LIGHTDOWN}
    private LightState lightState;


    private void updateButtonLight(double deltaTime, Iterator<ButtonHelper> iterator) {
        if (b == null || lightState == LightState.NONE) {
            iterator.remove();
            return;
        }

        timer += (float) deltaTime;
        float t = MathUtils.clamp(timer / lightDuration, 0f, 1f);

        if (lightState == LightState.LIGHTUP) {
            Color c = MathUtils.lerpColor(ColorPalette.BUTTON_BLUE,ColorPalette.BUTTON_LIGHT,t);
            b.setColor(c);
            if(t >= 1f) {
                lightState = LightState.LIGHTDOWN;
                b.setColor(ColorPalette.BUTTON_LIGHT);
                timer = 0f;
            }
        } else if (lightState == LightState.LIGHTDOWN) {
            Color c = MathUtils.lerpColor(ColorPalette.BUTTON_LIGHT, ColorPalette.BUTTON_BLUE, t);
            b.setColor(c);
            if (t >= 1f) {
                lightState = LightState.NONE;
                b.setColor(ColorPalette.BUTTON_BLUE);
                b = null;
                iterator.remove();
            }
        }
    }
}
