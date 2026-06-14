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
                JUnitTestRef.and(
                    JUnitTestRef.ofMethod(() -> MainTest.class.getDeclaredMethod("testH01_1"))),
                    JUnitTestRef.ofMethod(() -> MainTest.class.getDeclaredMethod("testH01_1_oneInstance")))
        )
        .build();

    private static final Criterion H1_2 = Criterion.builder()
        .shortDescription("H1.2 | Eine Referenz auf mehrere Roboter")
        .maxPoints(1)
        .minPoints(0)
        .addChildCriteria(
            criterion(
                "BlueRob wird korrekt bewegt. Anschließend wird ein neuer Roboter erstellt und über dieselbe ([[[blueRob]]]) Referenz korrekt bewegt.",
                1,
                JUnitTestRef.ofMethod(() -> MainTest.class.getDeclaredMethod("testH01_2"))),
            criterion("Die verbindliche Anforderung wurde eingehalten",
                -1,
                JUnitTestRef.ofMethod(() -> MainTest.class.getDeclaredMethod("testH01_2_noNewLocalVariableVA2")))
        )
        .build();

    private static final Criterion H1_3 = Criterion.builder()
        .shortDescription("H1.3 | While-Schleife")
        .maxPoints(1)
        .minPoints(0)
        .addChildCriteria(
            criterion(
                "RedRob legt mit genau einer [[[while]]]-Schleife jeweils eine Münze auf allen geforderten Feldern ab, ohne zu crashen.",
                1,
                JUnitTestRef.ofMethod(() -> MainTest.class.getDeclaredMethod("testH01_3"))),
            criterion("Die verbindliche Anforderung wurde eingehalten",
                -1,
                JUnitTestRef.ofMethod(() -> MainTest.class.getDeclaredMethod("testH01_3_usesWhileVA3")))
        )
        .build();

    private static final Criterion H1_4 = Criterion.builder()
        .shortDescription("H1.4 | For-Schleife")
        .maxPoints(1)
        .minPoints(0)
        .addChildCriteria(
            criterion(
                "BlueRob legt mit genau einer [[[for]]]-Schleife jeweils eine Münze auf allen geforderten Feldern ab, ohne zu crashen.",
                1,
                JUnitTestRef.ofMethod(() -> MainTest.class.getDeclaredMethod("testH01_4"))),
            criterion("Die verbindliche Anforderung wurde eingehalten",
                -1,
                JUnitTestRef.ofMethod(() -> MainTest.class.getDeclaredMethod("testH01_4_usesForVA4")))
        )
        .build();

    private static final Criterion SHEET_VA = Criterion.builder()
        .shortDescription("Verbindliche Anforderung für die gesamte Hausübung")
        .addChildCriteria(criterion("Die verbindliche Anforderung wurde eingehalten",
            -1,
            JUnitTestRef.ofMethod(() -> MainTest.class.getDeclaredMethod("testNoIfOrTernaryVA0"))))
        .build();

    private static final Rubric RUBRIC = Rubric.builder()
        .title("H01 | Crossover")
        .addChildCriteria(H1_1, H1_2, H1_3, H1_4, SHEET_VA)
        .build();

    @Override
    public Rubric getRubric() {
        return RUBRIC;
    }
}
