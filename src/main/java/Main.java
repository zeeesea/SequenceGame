import GameEngine.Core.GameEngine;
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
        objectManager.add(new LogicManager());
    }

    @Override
    protected void update() {
    }

    @Override
    protected void draw(Graphics2D graphics2D) {
    }
}

