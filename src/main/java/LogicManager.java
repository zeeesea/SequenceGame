import GameEngine.Core.gameObject.GameObject;
import GameEngine.Core.util.Console.Console;

import java.awt.*;

public class LogicManager extends GameObject {

    //References
    private UIManager uiManager;

    @Override
    public void init() {
        uiManager = objectManager.get(UIManager.class);
        uiManager.onHEHEHEHA();
    }

    @Override
    public void update(double v) {

    }


    @Override
    public void draw(Graphics2D graphics2D) {

    }

    public void onClick() {
        Console.log("onClick");
    }

    @Override
    public void onCollision(GameObject gameObject) {

    }


}
