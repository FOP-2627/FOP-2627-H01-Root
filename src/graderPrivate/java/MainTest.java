import fopbot.Coin;
import fopbot.Robot;
import fopbot.RobotFamily;
import fopbot.World;
import h01.Main;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.SpoonUtils;
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
            List<Robot> robots = World.getGlobalWorld().getAllFieldEntities()
                .stream()
                .filter(r -> r instanceof Robot)
                .map(r -> (Robot) r)
                .toList();

            Robot red = robots.stream()
                .filter(robot -> robot.getRobotFamily() == RobotFamily.SQUARE_RED)
                .findFirst()
                .orElse(null);

            assertNotNull(red, emptyContext(), r -> "The red robot was not found!");

            // position check
            assertEquals(3, red.getX(), emptyContext(), r -> "redRob should be at X=3");
            assertEquals(4, red.getY(), emptyContext(), r -> "redRob should be at Y=4");

            // coin Check
            long coinsAt14 = World.getGlobalWorld().getField(1, 4).getEntities().stream()
                .filter(e -> e instanceof Coin).count();
            assertEquals(1, (int) coinsAt14, emptyContext(), r -> "A coin should have been placed at (1,4) before the first move");

            long coinsAt24 = World.getGlobalWorld().getField(2, 4).getEntities().stream()
                .filter(e -> e instanceof Coin).count();
            assertEquals(1, (int) coinsAt24, emptyContext(), r -> "A coin should have been placed at (2,4) after the first move");

        } catch (Exception e) {
            fail(emptyContext(), r -> "Code execution failed with exception: " + e.getMessage());
        }
    }

    @Test
    public void testBlueRob0() {
        try {
            Main.runToSubtask = 1;
            Main.main(null);
            List<Robot> robots = World.getGlobalWorld().getAllFieldEntities()
                .stream()
                .filter(r -> r instanceof Robot)
                .map(r -> (Robot) r)
                .toList();

            Robot[] blue = robots.stream()
                .filter(robot -> robot.getRobotFamily() == RobotFamily.SQUARE_BLUE).toArray(Robot[]::new);

            assertNotNull(blue, emptyContext(), r -> "The blue robot was not found!");

            int pos = Main.worldWidth == 7 ? 3 : 4;

            // position check boxen stop
            assertEquals(pos - 1, blue[0].getX(), emptyContext(), r -> "blueRob should be at X=3");
            assertEquals(2, blue[0].getY(), emptyContext(), r -> "blueRob should be at Y=2");

            // positon check blue2
            assertEquals(pos, blue[1].getX(), emptyContext(), r -> "blueRob should be at X=3");
            assertEquals(3, blue[1].getY(), emptyContext(), r -> "blueRob should be at Y=2");

            // coin Check
            long coinsAt14 = World.getGlobalWorld().getField(pos, 1).getEntities().stream()
                .filter(e -> e instanceof Coin).count();
            assertEquals(1, (int) coinsAt14, emptyContext(), r -> "A coin should have been placed at (%d,1) before the first move".formatted(pos));

            long coinsAt24 = World.getGlobalWorld().getField(pos, 2).getEntities().stream()
                .filter(e -> e instanceof Coin).count();
            assertEquals(1, (int) coinsAt24, emptyContext(), r -> "A coin should have been placed at (%d,2) after the first move".formatted(pos));

        } catch (Exception e) {
            fail(emptyContext(), r -> "Code execution failed with exception: " + e.getMessage());
        }
    }

    @Test
    public void testRedRob1() {
        try {
            Main.runToSubtask = 2;
            Main.main(null);
            List<Robot> robots = World.getGlobalWorld().getAllFieldEntities()
                .stream()
                .filter(r -> r instanceof Robot)
                .map(r -> (Robot) r)
                .toList();

            Robot red = robots.stream()
                .filter(robot -> robot.getRobotFamily() == RobotFamily.SQUARE_RED)
                .findFirst()
                .orElse(null);

            assertNotNull(red, emptyContext(), r -> "The red robot was not found!");

            // position check
            assertEquals(Main.worldWidth - 1, red.getX(), emptyContext(), r -> "redRob should be at X=%d".formatted(Main.worldWidth));
            assertEquals(4, red.getY(), emptyContext(), r -> "redRob should be at Y=4");

            // coin Check
            long coinsAt34 = World.getGlobalWorld().getField(3, 4).getEntities().stream()
                .filter(e -> e instanceof Coin).count();
            assertEquals(1, (int) coinsAt34, emptyContext(), r -> "A coin should have been placed at (3,4) before the first move");

            long coinsAt44 = World.getGlobalWorld().getField(4, 4).getEntities().stream()
                .filter(e -> e instanceof Coin).count();
            assertEquals(1, (int) coinsAt44, emptyContext(), r -> "A coin should have been placed at (4,4) after the first move");

            long coinsAt54 = World.getGlobalWorld().getField(5, 4).getEntities().stream()
                .filter(e -> e instanceof Coin).count();
            assertEquals(1, (int) coinsAt54, emptyContext(), r -> "A coin should have been placed at (5,4) after the first move");

            if (Main.worldWidth == 9) {
                long coinsAt64 = World.getGlobalWorld().getField(6, 4).getEntities().stream()
                    .filter(e -> e instanceof Coin).count();
                assertEquals(1, (int) coinsAt64, emptyContext(), r -> "A coin should have been placed at (6,4) after the first move");

                long coinsAt74 = World.getGlobalWorld().getField(7, 4).getEntities().stream()
                    .filter(e -> e instanceof Coin).count();
                assertEquals(1, (int) coinsAt74, emptyContext(), r -> "A coin should have been placed at (7,4) after the first move");
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
            List<Robot> robots = World.getGlobalWorld().getAllFieldEntities()
                .stream()
                .filter(r -> r instanceof Robot)
                .map(r -> (Robot) r)
                .toList();

            Robot[] blue = robots.stream()
                .filter(robot -> robot.getRobotFamily() == RobotFamily.SQUARE_BLUE).toArray(Robot[]::new);

            Robot blue1 = blue[1];

            assertNotNull(blue1, emptyContext(), r -> "The blue robot was not found!");

            int pos = Main.worldWidth == 7 ? 3 : 4;

            // position check
            assertEquals(pos, blue1.getX(), emptyContext(), r -> "blue should be at X=%d".formatted(pos));
            assertEquals(Main.worldHeight - 1, blue1.getY(), emptyContext(), r -> "blue should be at Y=%d".formatted(Main.worldHeight - 1));

            // coin Check
            long coinsAt34 = World.getGlobalWorld().getField(pos, 3).getEntities().stream()
                .filter(e -> e instanceof Coin).count();
            assertEquals(1, (int) coinsAt34, emptyContext(), r -> "A coin should have been placed at (%d,3) before the first move".formatted(pos));

            long coinsAt44 = World.getGlobalWorld().getField(pos, 4).getEntities().stream()
                .filter(e -> e instanceof Coin).count();
            assertEquals(1, (int) coinsAt44, emptyContext(), r -> "A coin should have been placed at (%d,4) before the first move".formatted(pos));

            long coinsAt54 = World.getGlobalWorld().getField(pos, 5).getEntities().stream()
                .filter(e -> e instanceof Coin).count();
            assertEquals(1, (int) coinsAt54, emptyContext(), r -> "A coin should have been placed at (%d,5) before the first move".formatted(pos));

            long coinsAt64 = World.getGlobalWorld().getField(pos, 6).getEntities().stream()
                .filter(e -> e instanceof Coin).count();
            assertEquals(1, (int) coinsAt64, emptyContext(), r -> "A coin should have been placed at (%d,6) before the first move".formatted(pos));

            long coinsAt74 = World.getGlobalWorld().getField(pos, 7).getEntities().stream()
                .filter(e -> e instanceof Coin).count();
            assertEquals(1, (int) coinsAt74, emptyContext(), r -> "A coin should have been placed at (%d,7) before the first move".formatted(pos));


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
                        r -> "You introduced an additional reference variable in H01.2, but you should reassign the existing one!");
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
