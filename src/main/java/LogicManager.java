import GameEngine.Core.gameObject.GameObject;
import GameEngine.Core.gameObject.Obj.Button;
import GameEngine.Core.input.Input;
import GameEngine.Core.util.Console.Console;
import GameEngine.Core.util.Timer.Timer;
import Helper.ButtonHelper;
import Helper.List;

import java.awt.*;

public class LogicManager extends GameObject {

    //References
    private UIManager uiManager;
    private List<Button> bigBlueButtons;
    private List<Button> sequence;

    //Game Variables
    private int level = 0;
    private float lightDuration = 0.3f;
    private float flashDelay = 0.8f;
    Timer timer;


    //Modes
    private enum Mode {NONE,CLICK, SHOW}
    private enum GameMode {NORMAL,SPEED,REVERSE}
    private Mode mode = Mode.SHOW;
    private Mode lastMode = Mode.NONE;
    private GameMode gameMode = GameMode.NORMAL;


    @Override
    public void init() {
        this.uiManager = objectManager.get(UIManager.class);
        this.bigBlueButtons = uiManager.getBigBlueButtons();
        this.sequence = new List<>();
        ButtonHelper.lightDuration = lightDuration;
        ButtonHelper.setBigBlueButtons(bigBlueButtons.toArrayList());
    }

    @Override
    public void update(double deltaTime) {
        ButtonHelper.update(deltaTime);
        if(Input.getMouseButtonDown(Input.MouseCode.LEFT)) {
            mode = Mode.SHOW;
        }
        if (mode == Mode.SHOW) {
            if (lastMode != Mode.SHOW) {
                //Swichted to SHOW mode
                Console.log("SHOW MODE");
                level++;
                sequence.toFirst();
                timer = Timer.create(this::showNextInSequence, flashDelay).start();
            }
        } else if (mode == Mode.CLICK) {


        } else {

        }
        lastMode = mode;
    }


    private void showNextInSequence() {
        if (sequence == null || sequence.isEmpty() || !sequence.hasAccess()) {
            addToSequence();
            return;
        }
        Button b = sequence.getContent();
        ButtonHelper.flash(b);
        sequence.next();
    }
    private void addToSequence() {
        if (sequence == null) return;
        Button random = ButtonHelper.getRandomButton();
        if (random != null) {
            sequence.append(random);
            ButtonHelper.flash(random);
            timer.stop();
            mode = Mode.CLICK;
        }
    }



    //Event Handlers
    public void onClickBig(Button b) {
        // Falls das UI gerade rebuilt wurde
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
    public void onCollision(GameObject gameObject) {
    }
    @Override
    public void draw(Graphics2D graphics2D) {

    }
}
