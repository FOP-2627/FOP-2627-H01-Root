package h01;

import fopbot.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.SpoonUtils;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.visitor.Filter;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class MainTest {

    // TODO:
    //  - Not really related to the tests but what's the point of choosing the world width at random? It doesn't add much to the exercise but complicates tests.
    //  - The runToSubtask approach did not work well last year, it probably won't this year either. Needs to be replaced with own methods for each subtask.
    //    The way it is now might also lead to mistakes in previous subtasks having effects on subtasks below.
    //  - It looks like the tests can handle the random world width, but we should override the random value to always guarantee reproducible results
    //    (this way we can also test both cases).
    //  - Assertions should always provide context if there is any, e.g. world width and height or other information that helps students with debugging.

    private static CtMethod<?> mainMethod;
    private static List<CtIf> subtaskMarkers;

    @BeforeAll
    public static void setup() {
        Main.delay = 0;
        Main.isVisible = false;

        CtType<?> ctType = SpoonUtils.getType("h01.Main");
        Filter<? extends CtElement> mainMethodFilter = element -> element instanceof CtMethod<?> ctMethod && ctMethod.getSimpleName().equals("main");
        Filter<? extends CtElement> subtaskMarkerFilter = element -> element instanceof CtIf ctIf && ctIf.getCondition().toString().startsWith("runToSubtask == ");
        mainMethod = ctType.filterChildren(mainMethodFilter).first();
        subtaskMarkers = mainMethod.getBody().filterChildren(subtaskMarkerFilter).list();
    }

    @Test
    public void testRedRob0() {
        try {
            Main.runToSubtask = 0;
            Main.main(null);

            List<Robot> redRobots = World.getGlobalWorld().getAllFieldEntities()
                .stream()
                .filter(r -> r instanceof Robot)
                .map(r -> (Robot) r)
                .filter(robot -> robot.getRobotFamily() == RobotFamily.SQUARE_RED)
                .toList();

            Context context = contextBuilder()
                .add("world height", Main.worldHeight)
                .add("world width", Main.worldWidth)
                .build();

            assertEquals(1, redRobots.size(), context, r ->
                "Expected exactly one red robot. Did you create a new instance instead of a new reference?");
            Robot red = redRobots.getFirst();

            assertNotNull(red, context, r -> "The red robot was not found!");

            context = contextBuilder()
                .add(context)
                .add("starting position (X|Y)", "(%d|%d)".formatted(1, 4))
                .add("last position (X|Y) after execution", "(%d|%d)".formatted(red.getX(), red.getY()))
                .add("starting direction", Direction.RIGHT)
                .add("last direction", red.getDirection())
                .add("number of coins", 7)
                .add("number of coins after execution", red.getNumberOfCoins())
                .build();

            // position check
            assertEquals(3, red.getX(), context, r -> "redRob should be at X=3");
            assertEquals(4, red.getY(), context, r -> "redRob should be at Y=4");

            // coin Check
            long coinsAt14 = World.getGlobalWorld().getField(1, 4).getEntities().stream()
                .filter(e -> e instanceof Coin).count();
            assertEquals(1, (int) coinsAt14, context, r -> "A coin should have been placed at (1,4) before the first move");

            long coinsAt24 = World.getGlobalWorld().getField(2, 4).getEntities().stream()
                .filter(e -> e instanceof Coin).count();
            assertEquals(1, (int) coinsAt24, context, r -> "A coin should have been placed at (2,4) after the first move");

        } catch (Exception e) {
            fail(emptyContext(), r -> "Code execution failed with exception: " + e.getMessage());
        }
    }

    @Test
    public void testBlueRob0() {
        try {
            Main.runToSubtask = 1;
            Main.main(null);

            List<Robot> blue = World.getGlobalWorld().getAllFieldEntities()
                .stream()
                .filter(r -> r instanceof Robot)
                .map(r -> (Robot) r)
                .filter(robot -> robot.getRobotFamily() == RobotFamily.SQUARE_BLUE)
                .toList();

            Context context = contextBuilder()
                .add("world height", Main.worldHeight)
                .add("world width", Main.worldWidth)
                .build();

            assertEquals(2, blue.size(), context, r -> "Expected exactly two blue robots");
            assertNotNull(blue, context, r -> "The blue robot was not found!");

            Context context1 = contextBuilder()
                .add(context)
                .add("starting position first blueRob (X|Y)", "(%d|%d)".formatted(Main.worldWidth == 7 ? 3 : 4, 1))
                .add("last position first blueRob (X|Y) after execution", "(%d|%d)".formatted(blue.get(0).getX(), blue.get(0).getY()))
                .add("starting direction first blueRob", Direction.UP)
                .add("last direction first blueRob", blue.get(0).getDirection())
                .add("number of coins first blueRob", 1)
                .add("number of coins first blueRob after execution", blue.get(0).getNumberOfCoins())
                .build();

            Context context2 = contextBuilder()
                .add(context)
                .add("starting position second blueRob (X|Y)", "(%d|%d)".formatted(Main.worldWidth / 2, 2))
                .add("last position second blueRob (X|Y) after execution", "(%d|%d)".formatted(blue.get(1).getX(), blue.get(1).getY()))
                .add("starting direction second blueRob", Direction.UP)
                .add("last direction second blueRob", blue.get(1).getDirection())
                .add("number of coins", 8)
                .add("number of coins first blueRob after execution", blue.get(1).getNumberOfCoins())
                .build();

            int pos = Main.worldWidth == 7 ? 3 : 4;

            // position check boxen stop
            assertEquals(pos - 1, blue.get(0).getX(), context1, r -> "blueRob should be at X=3");
            assertEquals(2, blue.get(0).getY(), context1, r -> "blueRob should be at Y=2");

            // positon check blue2
            assertEquals(pos, blue.get(1).getX(), context2, r -> "blueRob should be at X=3");
            assertEquals(3, blue.get(1).getY(), context2, r -> "blueRob should be at Y=2");

            // coin Check
            long coinsAt14 = World.getGlobalWorld().getField(pos, 1).getEntities().stream()
                .filter(e -> e instanceof Coin).count();
            assertEquals(1, (int) coinsAt14, context, r -> "A coin should have been placed at (%d,1) before the first move".formatted(pos));

            long coinsAt24 = World.getGlobalWorld().getField(pos, 2).getEntities().stream()
                .filter(e -> e instanceof Coin).count();
            assertEquals(1, (int) coinsAt24, context, r -> "A coin should have been placed at (%d,2) after the first move".formatted(pos));

        } catch (Exception e) {
            fail(emptyContext(), r -> "Code execution failed with exception: " + e.getMessage());
        }
    }

    @Test
    public void testRedRob1() {
        try {
            Main.runToSubtask = 2;
            Main.main(null);

            List<Robot> redRobots = World.getGlobalWorld().getAllFieldEntities()
                .stream()
                .filter(r -> r instanceof Robot)
                .map(r -> (Robot) r)
                .filter(robot -> robot.getRobotFamily() == RobotFamily.SQUARE_RED)
                .toList();

            Context context = contextBuilder()
                .add("world height", Main.worldHeight)
                .add("world width", Main.worldWidth)
                .build();

            assertEquals(1, redRobots.size(), context, r ->
                "Expected exactly one red robot. Did you create a new instance instead of a new reference?");
            Robot red = redRobots.getFirst();

            assertNotNull(red, context, r -> "The red robot was not found!");

            context = contextBuilder()
                .add(context)
                .add("last position (X|Y) after execution", "(%d|%d)".formatted(red.getX(), red.getY()))
                .add("last direction", red.getDirection())
                .add("number of coins", 5)
                .add("number of coins after execution", red.getNumberOfCoins())
                .build();

            // position check
            assertEquals(Main.worldWidth - 1, red.getX(), context, r -> "redRob should be at X=%d".formatted(Main.worldWidth - 1));
            assertEquals(4, red.getY(), context, r -> "redRob should be at Y=4");

            // coin Check
            long coinsAt34 = World.getGlobalWorld().getField(3, 4).getEntities().stream()
                .filter(e -> e instanceof Coin).count();
            assertEquals(1, (int) coinsAt34, context, r -> "A coin should have been placed at (3,4) before the first move");

            long coinsAt44 = World.getGlobalWorld().getField(4, 4).getEntities().stream()
                .filter(e -> e instanceof Coin).count();
            assertEquals(1, (int) coinsAt44, context, r -> "A coin should have been placed at (4,4) after the first move");

            long coinsAt54 = World.getGlobalWorld().getField(5, 4).getEntities().stream()
                .filter(e -> e instanceof Coin).count();
            assertEquals(1, (int) coinsAt54, context, r -> "A coin should have been placed at (5,4) after the first move");

            if (Main.worldWidth == 9) {
                long coinsAt64 = World.getGlobalWorld().getField(6, 4).getEntities().stream()
                    .filter(e -> e instanceof Coin).count();
                assertEquals(1, (int) coinsAt64, context, r -> "A coin should have been placed at (6,4) after the first move");

                long coinsAt74 = World.getGlobalWorld().getField(7, 4).getEntities().stream()
                    .filter(e -> e instanceof Coin).count();
                assertEquals(1, (int) coinsAt74, context, r -> "A coin should have been placed at (7,4) after the first move");
            }

        } catch (Exception e) {
            fail(emptyContext(), r -> "Code execution failed with exception: " + e.getMessage());
        }
    }

    @Test
    public void testBlueRob1() {
        try {
            Main.runToSubtask = 3;
            Main.main(null);

            List<Robot> blue = World.getGlobalWorld().getAllFieldEntities()
                .stream()
                .filter(r -> r instanceof Robot)
                .map(r -> (Robot) r)
                .filter(robot -> robot.getRobotFamily() == RobotFamily.SQUARE_BLUE)
                .toList();

            Context context = contextBuilder()
                .add("world height", Main.worldHeight)
                .add("world width", Main.worldWidth)
                .build();

            assertEquals(2, blue.size(), emptyContext(), r -> "Expected exactly two blue robots");
            Robot blue1 = blue.get(1);

            assertNotNull(blue1, emptyContext(), r -> "The blue robot was not found!");

            context = contextBuilder()
                .add(context)
                .add("last position (X|Y) after execution", "(%d|%d)".formatted(blue1.getX(), blue1.getY()))
                .add("last direction", blue1.getDirection())
                .add("number of coins", 7)
                .add("number of coins after execution", blue1.getNumberOfCoins())
                .build();

            int pos = Main.worldWidth == 7 ? 3 : 4;

            // position check
            assertEquals(pos, blue1.getX(), context, r -> "blue should be at X=%d".formatted(pos));
            assertEquals(Main.worldHeight - 1, blue1.getY(), context, r -> "blue should be at Y=%d".formatted(Main.worldHeight - 1));

            // coin Check
            long coinsAt34 = World.getGlobalWorld().getField(pos, 3).getEntities().stream()
                .filter(e -> e instanceof Coin).count();
            assertEquals(1, (int) coinsAt34, context, r -> "A coin should have been placed at (%d,3) before the first move".formatted(pos));

            long coinsAt44 = World.getGlobalWorld().getField(pos, 4).getEntities().stream()
                .filter(e -> e instanceof Coin).count();
            assertEquals(1, (int) coinsAt44, context, r -> "A coin should have been placed at (%d,4) before the first move".formatted(pos));

            long coinsAt54 = World.getGlobalWorld().getField(pos, 5).getEntities().stream()
                .filter(e -> e instanceof Coin).count();
            assertEquals(1, (int) coinsAt54, context, r -> "A coin should have been placed at (%d,5) before the first move".formatted(pos));

            long coinsAt64 = World.getGlobalWorld().getField(pos, 6).getEntities().stream()
                .filter(e -> e instanceof Coin).count();
            assertEquals(1, (int) coinsAt64, context, r -> "A coin should have been placed at (%d,6) before the first move".formatted(pos));

            long coinsAt74 = World.getGlobalWorld().getField(pos, 7).getEntities().stream()
                .filter(e -> e instanceof Coin).count();
            assertEquals(1, (int) coinsAt74, context, r -> "A coin should have been placed at (%d,7) before the first move".formatted(pos));


        } catch (Exception e) {
            fail(emptyContext(), r -> "Code execution failed with exception: " + e.getMessage());
        }
    }

    @Test
    public void testVA0() {
        mainMethod.getBody().filterChildren(element -> element instanceof CtIf || element instanceof CtConditional)
            .list()
            .forEach(element -> {
                // Case 1: Standard if-statement
                if (element instanceof CtIf ctIf) {
                    if (!subtaskMarkers.contains(ctIf)) {
                        fail(contextBuilder()
                                .add("Illegal construct", "if-statement")
                                .add("Condition", ctIf.getCondition().toString())
                                .add("Line", ctIf.getPosition().getLine())
                                .build(),
                            r -> "You are not allowed to use if-statements in this Task!");
                    }
                }
                // Case 2: Ternary operator
                else if (element instanceof CtConditional<?> ternary) {
                    String conditionText = ternary.getCondition().toString();
                    boolean isSetupCode = conditionText.contains("worldWidth == 7");

                    if (!isSetupCode) {
                        fail(contextBuilder()
                                .add("Illegal construct", "ternary operator")
                                .add("Expression", ternary.toString())
                                .add("Line", ternary.getPosition().isValidPosition() ? ternary.getPosition().getLine() : "unknown")
                                .build(),
                            r -> "You are not allowed to use ternary operators in this Task!");
                    }
                }
            });
    }

    @Test
    public void testVA1() {
        Iterator<CtElement> iterator = mainMethod.getBody().descendantIterator();
        int currentBlock = 0;

        while (iterator.hasNext()) {
            CtElement element = iterator.next();

            if (element instanceof CtIf ctIf && subtaskMarkers.contains(ctIf)) {
                currentBlock++;
                continue;
            }

            if (currentBlock < 1) continue;
            if (currentBlock > 1) break;

            if (element instanceof CtLocalVariable<?> localVariable) {
                if (!localVariable.getType().isPrimitive()) {
                    fail(contextBuilder()
                            .add("Variable name", localVariable.getSimpleName())
                            .add("Variable type", localVariable.getType().getSimpleName())
                            .build(),
                        r -> "You introduced an additional reference variable in H1.2, but you should reassign the existing one!");
                }
            }
        }
    }

    @Test
    public void testVA2() {
        testVAs(2,
            ctLoop -> assertFalse(ctLoop instanceof CtDo || ctLoop instanceof CtFor || ctLoop instanceof CtForEach,
                contextBuilder().add("AST loop object", ctLoop).build(),
                r -> "Illegal loop found in block 3 (H1.3)"),
            i -> assertTrue(i > 0, emptyContext(), r -> "No loops found in block 3 (H1.3)"));
    }

    @Test
    public void testVA3() {
        testVAs(3,
            ctLoop -> assertFalse(ctLoop instanceof CtWhile || ctLoop instanceof CtDo || ctLoop instanceof CtForEach,
                contextBuilder().add("AST loop object", ctLoop).build(),
                r -> "Illegal loop found in block 2 (H1.4)"),
            i -> assertTrue(i > 0, emptyContext(), r -> "No loops found in block 2 (H1.4)"));
    }

    private void testVAs(int targetBlock, Consumer<? super CtLoop> ctLoopCallback, Consumer<Integer> counterCallback) {
        Iterator<CtElement> iterator = mainMethod.getBody().descendantIterator();
        int currentBlock = 0;
        int loopsInBlock = 0;

        while (iterator.hasNext()) {
            CtElement element = iterator.next();
            if (element instanceof CtIf ctIf && subtaskMarkers.contains(ctIf)) {
                currentBlock++;
            }
            if (currentBlock > targetBlock) {
                break;
            }
            if (currentBlock == targetBlock && element instanceof CtLoop ctLoop) {
                loopsInBlock++;
                ctLoopCallback.accept(ctLoop);
            }
        }

        counterCallback.accept(loopsInBlock);
    }
}
