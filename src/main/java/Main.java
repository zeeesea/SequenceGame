import GameEngine.Core.GameEngine;
import GameEngine.Core.input.Input;
import Helper.ColorPalette;

import javax.swing.*;
import java.awt.*;


public class Main extends GameEngine {

    public static void main(String[] args) {
        GameEngine.launch(new Main());
    }

    @Override
    public void init() {
        setBackground(ColorPalette.BACKGROUND);
        setTitle("SEQUENCE GAME");
        setIconFromSprite("icon");

        objectManager.add(new UIManager());
        objectManager.add(new LogicManager());
    }

    @Override
    protected void update() {
        if (Input.getKeyDown(Input.KeyCode.ESCAPE)) {
            System.exit(0);
        } else if (Input.getKeyDown(Input.KeyCode.F11)) {
            toggleFullscreen();
        }
    }

    @Override
    protected void draw(Graphics2D graphics2D) {
    }
}

