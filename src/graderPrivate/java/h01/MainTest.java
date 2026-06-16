package h01;

import fopbot.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.tudalgo.algoutils.tutor.general.SpoonUtils;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import spoon.reflect.code.*;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;
import spoon.reflect.visitor.Filter;

import java.util.List;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;

@TestForSubmission
public class MainTest {

    private static final CtType<?> CT_TYPE = SpoonUtils.getType("h01.Main");

    @BeforeAll
    public static void setup() {
        Main.delay = 0;
        Main.isVisible = false;
    }

    private void setupWorld(int width) {
        Main.worldHeight = 9;
        Main.worldWidth = width;
        Main.setupWorld();
    }

    private Context worldContext(int worldWidth) {
        return contextBuilder()
            .add("world width", worldWidth)
            .add("world height", 9)
            .build();
    }

    private CtMethod<?> getMethod(String name) {
        Filter<CtElement> filter = e -> e instanceof CtMethod<?> m && m.getSimpleName().equals(name);
        return CT_TYPE.filterChildren(filter).first();
    }


    // H1.1
    @ParameterizedTest
    @ValueSource(ints = {7, 9})
    public void testH1_1(int width) {
        setupWorld(width);
        Robot redRob = new Robot(1, 4, Direction.RIGHT, 7, RobotFamily.SQUARE_RED);
        Context ctx = worldContext(width);

        try {
            Main.h1_1(redRob);

            Context ctx1 = contextBuilder().add(ctx)
                .add("expected X", 3).add("actual X", redRob.getX())
                .add("expected Y", 4).add("actual Y", redRob.getY())
                .build();

            assertEquals(3, redRob.getX(), ctx1, r -> "redRob should be at X=3");
            assertEquals(4, redRob.getY(), ctx1, r -> "redRob should be at Y=4");

            long coinsAt14 = World.getGlobalWorld().getField(1, 4).getEntities().stream()
                .filter(e -> e instanceof Coin).count();
            assertEquals(1, (int) coinsAt14, ctx,
                r -> "A coin should have been placed at (1,4)");

            long coinsAt24 = World.getGlobalWorld().getField(2, 4).getEntities().stream()
                .filter(e -> e instanceof Coin).count();
            assertEquals(1, (int) coinsAt24, ctx,
                r -> "A coin should have been placed at (2,4)");
        } catch (Exception e) {
            fail(e, emptyContext(), r -> "h1_1(Robot redRob) threw an unexpected exception");
        }
    }

    @Test
    public void testH1_1_oneInstance() {
        Context ctx = worldContext(7);
        CtMethod<?> method = getMethod("h1_1");
        List<CtConstructorCall<?>> newCalls = method.filterChildren(e -> e instanceof CtConstructorCall<?>).list();
        assertEquals(0, newCalls.size(), ctx,
            r -> "Expected exactly one red robot. Did you create a new instance instead of a new reference?");
    }


    // H1.2
    @ParameterizedTest
    @ValueSource(ints = {7, 9})
    public void testH1_2(int width) {
        setupWorld(width);
        Robot blueRob = new Robot(width == 7 ? 3 : 4, 1, Direction.UP, 1, RobotFamily.SQUARE_BLUE);
        Context ctx = worldContext(width);

        try {
            Robot result = Main.h1_2(blueRob);

            int pos = width == 7 ? 3 : 4;

            // Erster Robot (Boxenstopp)
            Context ctx1 = contextBuilder().add(ctx)
                .add("expected X (blueRob1)", pos - 1).add("actual X", blueRob.getX())
                .add("expected Y (blueRob1)", 2).add("actual Y", blueRob.getY())
                .build();
            assertEquals(pos - 1, blueRob.getX(), ctx1, r -> "The first blueRob should be at X=%d during the pit stop".formatted(pos - 1));
            assertEquals(2, blueRob.getY(), ctx1, r -> "The first blueRob should be at Y=2 during the pit stop");

            // Zweiter Robot (neues Objekt)
            Context ctx2 = contextBuilder().add(ctx)
                .add("expected X (blueRob2)", pos).add("actual X", result.getX())
                .add("expected Y (blueRob2)", 3).add("actual Y", result.getY())
                .build();
            assertEquals(pos, result.getX(), ctx2, r -> "The second blueRob should be at X=%d".formatted(pos));
            assertEquals(3, result.getY(), ctx2, r -> "The second blueRob should be at Y=3");

            long coinsAtStart = World.getGlobalWorld().getField(pos, 1).getEntities().stream()
                .filter(e -> e instanceof Coin).count();
            assertEquals(1, (int) coinsAtStart, ctx,
                r -> "There should be a coin at (%d,1)".formatted(pos));

            long coinsAt22 = World.getGlobalWorld().getField(Main.worldWidth / 2, 2).getEntities().stream()
                .filter(e -> e instanceof Coin).count();
            assertEquals(1, (int) coinsAt22, ctx,
                r -> "A coin from the second robot should be located at (%d,2)".formatted(Main.worldWidth / 2));

        } catch (Exception e) {
            fail(e, emptyContext(), r -> "h1_2(Robot blueRob) threw an unexpected exception");
        }
    }

    @Test
    public void testH1_2_noNewLocalVariableVA2() {
        CtMethod<?> method = getMethod("h1_2");
        List<CtLocalVariable<?>> locals = method.filterChildren(e -> e instanceof CtLocalVariable<?> lv
            && !lv.getType().isPrimitive()).list();

        if (!locals.isEmpty()) {
            assertEquals(0, locals.size(), contextBuilder()
                    .add("Variable name", locals.getFirst().getSimpleName())
                    .add("Variable type", locals.getFirst().getType().getSimpleName())
                    .build(),
                r -> "You introduced an additional reference variable in H1.2, but you should reassign the existing one!");
        }
    }


    // H1.3
    @ParameterizedTest
    @ValueSource(ints = {7, 9})
    public void testH1_3(int width) {
        setupWorld(width);
        Context ctx = worldContext(width);

        Robot redRob = new Robot(3, 4, Direction.RIGHT, 5, RobotFamily.SQUARE_RED);

        try {
            Main.h1_3(redRob);

            Context ctx1 = contextBuilder().add(ctx)
                .add("expected X", Main.worldWidth - 1).add("actual X", redRob.getX())
                .add("expected Y", 4).add("actual Y", redRob.getY())
                .build();
            assertEquals(Main.worldWidth - 1, redRob.getX(), ctx1,
                r -> "redRob should be on the wall at X=%d".formatted(Main.worldWidth - 1));
            assertEquals(4, redRob.getY(), ctx1, r -> "redRob should stay at Y=4");

            for (int x = 3; x < Main.worldWidth - 1; x++) {
                final int fx = x;
                long coins = World.getGlobalWorld().getField(x, 4).getEntities().stream()
                    .filter(e -> e instanceof Coin).count();
                assertEquals(1, (int) coins, ctx,
                    r -> "There should be a coin at (%d,4)".formatted(fx));
            }

        } catch (Exception e) {
            fail(e, emptyContext(), r -> "h1_3(Robot redRob) threw an unexpected exception");
        }
    }

    @Test
    public void testH1_3_usesWhileVA3() {
        CtMethod<?> method = getMethod("h1_3");
        List<CtWhile> whileLoops = method.filterChildren(e -> e instanceof CtWhile).list();
        assertFalse(whileLoops.isEmpty(), emptyContext(),
            r -> "H1.3 must contain a while loop");

        List<CtFor> forLoops = method.filterChildren(e -> e instanceof CtFor).list();
        List<CtForEach> forEachLoops = method.filterChildren(e -> e instanceof CtForEach).list();
        List<CtDo> doLoops = method.filterChildren(e -> e instanceof CtDo).list();
        assertEquals(0, forLoops.size() + forEachLoops.size() + doLoops.size(), emptyContext(),
            r -> "H1.3 must use only a while loop; do not use a for/do-while loop!");
    }

    // H1.4
    @ParameterizedTest
    @ValueSource(ints = {7, 9})
    public void testH1_4(int width) {
        setupWorld(width);
        Context ctx = worldContext(width);

        Robot blueRob2 = new Robot(width == 7 ? 3 : 4, 3, Direction.UP, 7, RobotFamily.SQUARE_BLUE);
        int pos = width == 7 ? 3 : 4;

        try {
            Main.h1_4(blueRob2);

            Context ctx1 = contextBuilder().add(ctx)
                .add("expected X", pos).add("actual X", blueRob2.getX())
                .add("expected Y", Main.worldHeight - 1).add("actual Y", blueRob2.getY())
                .build();
            assertEquals(pos, blueRob2.getX(), ctx1,
                r -> "blueRob should be at X=%d".formatted(pos));
            assertEquals(Main.worldHeight - 1, blueRob2.getY(), ctx1,
                r -> "blueRob should be at Y=%d".formatted(Main.worldHeight - 1));

            for (int y = 3; y <= 7; y++) {
                final int fy = y;
                long coins = World.getGlobalWorld().getField(pos, y).getEntities().stream()
                    .filter(e -> e instanceof Coin).count();
                assertEquals(1, (int) coins, ctx,
                    r -> "There should be a coin at (%d,%d)".formatted(pos, fy));
            }

        } catch (Exception e) {
            fail(e, emptyContext(), r -> "h1_4(Robot blueRob2) threw an unexpected exception");
        }
    }

    @Test
    public void testH1_4_usesForVA4() {
        CtMethod<?> method = getMethod("h1_4");
        List<CtFor> forLoops = method.filterChildren(e -> e instanceof CtFor).list();
        assertFalse(forLoops.isEmpty(), emptyContext(),
            r -> "H1.4 must contain a for loop");

        List<CtWhile> whileLoops = method.filterChildren(e -> e instanceof CtWhile).list();
        List<CtForEach> forEachLoops = method.filterChildren(e -> e instanceof CtForEach).list();
        List<CtDo> doLoops = method.filterChildren(e -> e instanceof CtDo).list();
        assertEquals(0, whileLoops.size() + forEachLoops.size() + doLoops.size(), emptyContext(),
            r -> "H1.4 must use only a for loop; do not use while or do-while loops!");
    }

    // VA for entire H01
    @ParameterizedTest
    @ValueSource(strings = {"h1_1", "h1_2", "h1_3", "h1_4"})
    public void testNoIfOrTernaryVA0(String methodName) {
        CtMethod<?> method = getMethod(methodName);

        List<CtIf> ifs = method.filterChildren(e -> e instanceof CtIf).list();
        List<CtConditional<?>> ternaries = method.filterChildren(e -> e instanceof CtConditional<?>).list();

        Context ctx = contextBuilder()
            .add("Methode", methodName)
            .build();

        assertEquals(0, ifs.size() + ternaries.size(), ctx,
            r -> "The ‘%s’ method must not contain any if statements or ternary operators!".formatted(methodName));
    }
}
