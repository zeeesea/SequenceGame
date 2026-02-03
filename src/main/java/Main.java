import GameEngine.Core.GameEngine;
import GameEngine.Core.gameObject.Obj.Button;
import Helper.ColorPalette;

import java.awt.*;


public class Main extends GameEngine {

    public static void main(String[] args) {
        GameEngine.launch(new Main());
    }

    @Override
    public void init() {
        setBackground(ColorPalette.BACKGROUND);
        objectManager.add(new UIManager());
    }

    @Override
    protected void update() {
    }


    private void onClick(Button b) {

    }

    @Override
    protected void draw(Graphics2D graphics2D) {

    }
}

