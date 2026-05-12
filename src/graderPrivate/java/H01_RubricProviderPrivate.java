import org.sourcegrade.jagr.api.rubric.Criterion;
import org.sourcegrade.jagr.api.rubric.Rubric;
import org.sourcegrade.jagr.api.rubric.RubricProvider;

import static org.tudalgo.algoutils.tutor.general.jagr.RubricUtils.criterion;

public class H01_RubricProviderPrivate implements RubricProvider {

    private static final Criterion H1_1 = Criterion.builder()
        .shortDescription("H1.1 | Mehrere Referenzen auf einen Roboter")
        .maxPoints(1)
        .minPoints(0)
        .addChildCriteria(criterion(
            "Der Roboter wird korrekt über die vorhandene Referenz und eine neu erstellte Referenz bewegt."))
        .build();

    private static final Criterion H1_2 = Criterion.builder()
        .shortDescription("H1.2 | Eine Referenz auf mehrere Roboter")
        .maxPoints(1)
        .minPoints(0)
        .addChildCriteria(criterion(
            "Der Roboter wird korrekt bewegt. Anschließend wird ein neuer Roboter erstellt " +
                "und über dieselbe (alte) Referenz korrekt bewegt."))
        .build();

    private static final Criterion H1_3 = Criterion.builder()
        .shortDescription("H1.3 | While-Schleife")
        .maxPoints(1)
        .minPoints(0)
        .addChildCriteria(criterion(
            "Der Roboter legt mit genau einer While-Schleife " +
                "jeweils eine Münze auf allen geforderten Feldern ab, ohne zu crashen."))
        .build();

    private static final Criterion H1_4 = Criterion.builder()
        .shortDescription("H1.4 | For-Schleife")
        .maxPoints(1)
        .minPoints(0)
        .addChildCriteria(criterion(
            "Der Roboter legt mit genau einer For-Schleife " +
                "jeweils eine Münze auf allen geforderten Feldern ab, ohne zu crashen."))
        .build();

    private static final Rubric RUBRIC = Rubric.builder()
        .title("H01 | Crossover")
        .addChildCriteria(H1_1, H1_2, H1_3, H1_4)
        .build();

    @Override
    public Rubric getRubric() {
        return RUBRIC;
    }
}
