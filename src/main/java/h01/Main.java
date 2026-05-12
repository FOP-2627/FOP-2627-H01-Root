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

    @DoNotTouch
    public static int runToSubtask = -1;
    public static int worldHeight;
    public static int worldWidth;

    public static void main(String[] args) {
        //Do Not Touch the following three lines
        setupWorld();
        Robot redRob = new Robot(1, 4, Direction.RIGHT, 7, RobotFamily.SQUARE_RED);
        Robot blueRob = new Robot(worldWidth == 7 ? 3 : 4, 1, Direction.UP, 1, RobotFamily.SQUARE_BLUE);

        //TODO: H01.1 - mehrere Referenzen auf einen Robot
        redRob.putCoin();
        redRob.move();

        Robot redRob2 = redRob;
        redRob2.putCoin();
        redRob2.move();

        if (runToSubtask == 0) return; // DO NOT TOUCH

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

        if (runToSubtask == 1) return; // DO NOT TOUCH

        //TODO: H01.3
        while(redRob.isFrontClear()){
            redRob.putCoin();
            redRob.move();
        }

        if (runToSubtask == 2) return; // DO NOT TOUCH

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
        World.getGlobalWorld().getField(worldWidth == 7 ? 2 : 3,2).setFieldColor(Color.ORANGE);
        World.placeHorizontalWall(worldWidth == 7 ? 2 : 3, 2);
        World.placeHorizontalWall(worldWidth == 7 ? 2 : 3, 1);
        World.placeVerticalWall(worldWidth == 7 ? 1 : 2, 2);

        // Zielfelder
        World.getGlobalWorld().getField(worldWidth == 7 ? 3 : 4,worldHeight-1).setFieldColor(Color.BLUE);
        World.placeVerticalWall(worldWidth == 7 ? 2 : 3, worldHeight-1);
        World.placeVerticalWall(worldWidth == 7 ? 3 : 4, worldHeight-1);

        World.getGlobalWorld().getField(worldWidth-1,worldHeight/2).setFieldColor(Color.RED);
        World.placeHorizontalWall(worldWidth-1, worldHeight/2);
        World.placeHorizontalWall(worldWidth-1, worldHeight/2-1);

        World.setDelay(delay);
        World.setVisible(true);
    }
}
