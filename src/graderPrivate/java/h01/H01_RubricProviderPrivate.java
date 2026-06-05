package h01;

import org.sourcegrade.jagr.api.rubric.Criterion;
import org.sourcegrade.jagr.api.rubric.JUnitTestRef;
import org.sourcegrade.jagr.api.rubric.Rubric;
import org.sourcegrade.jagr.api.rubric.RubricProvider;

import static org.tudalgo.algoutils.tutor.general.jagr.RubricUtils.criterion;

public class H01_RubricProviderPrivate implements RubricProvider {

    private static final Criterion H1_1 = Criterion.builder()
        .shortDescription("H1.1 | Mehrere Referenzen auf einen Roboter")
        .maxPoints(1)
        .minPoints(0)
        .addChildCriteria(
            criterion(
                "RedRob wird korrekt über die vorhandene Referenz und eine neu erstellte Referenz bewegt.",
                1,
                JUnitTestRef.ofMethod(() -> MainTest.class.getDeclaredMethod("testRedRob0"))))
        .build();

    private static final Criterion H1_2 = Criterion.builder()
        .shortDescription("H1.2 | Eine Referenz auf mehrere Roboter")
        .maxPoints(1)
        .minPoints(0)
        .addChildCriteria(
            criterion(
                "BlueRob wird korrekt bewegt. Anschließend wird ein neuer Roboter erstellt und über dieselbe (alte) Referenz korrekt bewegt.",
                1,
                JUnitTestRef.ofMethod(() -> MainTest.class.getDeclaredMethod("testBlueRob0"))),
            criterion("Die verbindliche Anforderung wurde eingehalten",
                -1,
                JUnitTestRef.ofMethod(() -> MainTest.class.getDeclaredMethod("testVA1")))
        )
        .build();

    private static final Criterion H1_3 = Criterion.builder()
        .shortDescription("H1.3 | While-Schleife")
        .maxPoints(1)
        .minPoints(0)
        .addChildCriteria(
            criterion(
                "RedRob legt mit genau einer While-Schleife jeweils eine Münze auf allen geforderten Feldern ab, ohne zu crashen.",
                1,
                JUnitTestRef.ofMethod(() -> MainTest.class.getDeclaredMethod("testRedRob1"))),
            criterion("Die verbindliche Anforderung wurde eingehalten",
                -1,
                JUnitTestRef.ofMethod(() -> MainTest.class.getDeclaredMethod("testVA2")))
        )
        .build();

    private static final Criterion H1_4 = Criterion.builder()
        .shortDescription("H1.4 | For-Schleife")
        .maxPoints(1)
        .minPoints(0)
        .addChildCriteria(
            criterion(
                "BlueRob legt mit genau einer For-Schleife jeweils eine Münze auf allen geforderten Feldern ab, ohne zu crashen.",
                1,
                JUnitTestRef.ofMethod(() -> MainTest.class.getDeclaredMethod("testBlueRob1"))),
            criterion("Die verbindliche Anforderung wurde eingehalten",
                -1,
                JUnitTestRef.ofMethod(() -> MainTest.class.getDeclaredMethod("testVA3")))
        )
        .build();

    private static final Rubric RUBRIC = Rubric.builder()
        .title("H01 | Crossover")
        .addChildCriteria(H1_1, H1_2, H1_3, H1_4)
        .addChildCriteria(criterion("Die verbindliche Anforderung wurde eingehalten",
            -1,
            JUnitTestRef.ofMethod(() -> MainTest.class.getDeclaredMethod("testVA0"))))
        .build();

    @Override
    public Rubric getRubric() {
        return RUBRIC;
    }
}
