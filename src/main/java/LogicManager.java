import GameEngine.Core.gameObject.GameObject;
import GameEngine.Core.gameObject.Obj.Button;
import GameEngine.Core.util.Console.Console;
import Helper.ButtonHelper;
import Helper.List;

import java.awt.*;

public class LogicManager extends GameObject {

    //References
    private UIManager uiManager;
    private List<Button> bigBlueButtons;

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

        // Falls das UI gerade rebuilt wurde: immer den aktuellen Stand rübergeben.
        ButtonHelper.setBigBlueButtons(bigBlueButtons.toArrayList());

        Button random = ButtonHelper.getRandomButton();
        if (random != null) {
            ButtonHelper.flash(random);
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
