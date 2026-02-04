import GameEngine.Core.gameObject.GameObject;
import GameEngine.Core.gameObject.Obj.Button;
import GameEngine.Core.util.Console.Console;
import Helper.ButtonHelper;
import Helper.List;

import java.awt.*;
import java.util.ArrayList;




public class LogicManager extends GameObject {

    //References
    private UIManager uiManager;
    private List<Button> bigBlueButtons;

    //Light Up Variables
    private float lightDuration = 2f;
    private float timer = 0f;
    private Button currentButton = null;
    private Button prevButton = null;
    private enum LightState {NONE, LIGHTUP, LIGHTDOWN}
    private LightState lightState = LightState.NONE;

    public LogicManager(UIManager uiManager, List<Button> bigBlueButtons) {
        this.uiManager = uiManager;
        this.bigBlueButtons = bigBlueButtons;
        ButtonHelper.setBigBlueButtons(bigBlueButtons.toArrayList());
    }



    @Override
    public void update(double deltaTime) {
        ButtonHelper.update(deltaTime);
    }


    public void onClick(Button b) {
        Console.log("Button clicked: " + b.tag);
        ButtonHelper.flash(ButtonHelper.getRandomButton());
    }



    //Getter/Setter
    public void setUiManager(UIManager uiManager) {
        this.uiManager = uiManager;
    }
    public void setBigBlueButtons(List<Button> bigBlueButtons) {
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
