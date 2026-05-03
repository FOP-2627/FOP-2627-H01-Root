package h01;

import fopbot.*;
import fopbot.Robot;
import org.tudalgo.algoutils.student.annotation.DoNotTouch;

import java.awt.*;


/**
 * Main entry point in executing the program.
 */
public class Main {

    public static int delay = 100;
    public static int worldHeight;
    public static int worldWidth;

    public static void main(String[] args) {
        setupWorld(); //DO NOT TOUCH
        Robot redRob = new Robot(1, 4, Direction.RIGHT, 7, RobotFamily.SQUARE_RED);
        Robot blueRob = new Robot(worldWidth == 7 ? 3 : 4, 1, Direction.UP, 1, RobotFamily.SQUARE_BLUE);

        //TODO: H01.1 - mehrere Referenzen auf einen Robot
        redRob.putCoin();
        redRob.move();

        Robot redRob2 = redRob;
        redRob2.putCoin();
        redRob2.move();

        //TODO: H01.2 - eine Referenz für mehrere Roboter
        blueRob.putCoin();
        blueRob.move();

        //Boxenstopp
        blueRob.turnLeft();
        blueRob.move();

        // neuen Roboter erstellen
        blueRob = new Robot(worldWidth/2,2, Direction.UP, 8, RobotFamily.SQUARE_BLUE);
        blueRob.putCoin();
        blueRob.move();

        //TODO: H01.3
        while(redRob.isFrontClear()){
            redRob.putCoin();
            redRob.move();
        }

        //TODO: H01.4
        for(int i = 0; i < 5; i++){
            blueRob.putCoin();
            blueRob.move();
        }
    }

    @DoNotTouch
    public static void setupWorld(){
        worldWidth = (Math.random() < 0.5) ? 7 : 9;
        worldHeight = 9;
        World.setSize(worldWidth, worldHeight);

        // Feld für Boxenstopp
        World.getGlobalWorld().getField(worldWidth == 7 ? 2 : 3,2).setFieldColor(Color.MAGENTA);

        World.setDelay(delay);
        World.setVisible(true);
    }
}
