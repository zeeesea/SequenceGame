package Helper;

import GameEngine.Core.GameEngine;
import GameEngine.Tools.MainMenu.MainMenu;

public class RunMainMenu {
    public static void main(String[] args) {
        GameEngine.launch(new MainMenu());
    }
}