package h01;

import org.objectweb.asm.*;
import org.sourcegrade.jagr.api.rubric.Criterion;
import org.sourcegrade.jagr.api.rubric.JUnitTestRef;
import org.sourcegrade.jagr.api.rubric.Rubric;
import org.sourcegrade.jagr.api.rubric.RubricProvider;
import org.sourcegrade.jagr.api.testing.ClassTransformer;
import org.sourcegrade.jagr.api.testing.RubricConfiguration;

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
                    JUnitTestRef.ofMethod(() -> MainTest.class.getDeclaredMethod("testH1_1", int.class))),
                    JUnitTestRef.ofMethod(() -> MainTest.class.getDeclaredMethod("testH1_1_oneInstance")))
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
                JUnitTestRef.ofMethod(() -> MainTest.class.getDeclaredMethod("testH1_2", int.class))),
            criterion("Die verbindliche Anforderung wurde eingehalten",
                -1,
                JUnitTestRef.ofMethod(() -> MainTest.class.getDeclaredMethod("testH1_2_noNewLocalVariableVA2")))
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
                JUnitTestRef.ofMethod(() -> MainTest.class.getDeclaredMethod("testH1_3", int.class))),
            criterion("Die verbindliche Anforderung wurde eingehalten",
                -1,
                JUnitTestRef.ofMethod(() -> MainTest.class.getDeclaredMethod("testH1_3_usesWhileVA3")))
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
                JUnitTestRef.ofMethod(() -> MainTest.class.getDeclaredMethod("testH1_4", int.class))),
            criterion("Die verbindliche Anforderung wurde eingehalten",
                -1,
                JUnitTestRef.ofMethod(() -> MainTest.class.getDeclaredMethod("testH1_4_usesForVA4")))
        )
        .build();

    private static final Criterion SHEET_VA = Criterion.builder()
        .shortDescription("Verbindliche Anforderung für die gesamte Hausübung")
        .addChildCriteria(criterion("Die verbindliche Anforderung wurde eingehalten",
            -1,
            JUnitTestRef.ofMethod(() -> MainTest.class.getDeclaredMethod("testNoIfOrTernaryVA0", String.class))))
        .build();

    private static final Rubric RUBRIC = Rubric.builder()
        .title("H01 | Crossover")
        .addChildCriteria(H1_1, H1_2, H1_3, H1_4, SHEET_VA)
        .build();

    @Override
    public Rubric getRubric() {
        return RUBRIC;
    }

    @Override
    public void configure(RubricConfiguration configuration) {
        configuration.addTransformer(new ClassTransformer() {
            @Override
            public String getName() {
                return "setupWorldTransformer";
            }

            @Override
            public void transform(ClassReader reader, ClassWriter writer) {
                if (reader.getClassName().equals("h01/Main")) {
                    reader.accept(new ClassVisitor(Opcodes.ASM9, writer) {
                        @Override
                        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                            MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
                            if (name.equals("setupWorld") && descriptor.equals("()V")) {
                                return new MethodVisitor(Opcodes.ASM9, mv) {
                                    @Override
                                    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
                                        if (opcode == Opcodes.PUTSTATIC) {
                                            super.visitInsn(Opcodes.POP);
                                        } else {
                                            super.visitFieldInsn(opcode, owner, name, descriptor);
                                        }
                                    }
                                };
                            } else {
                                return mv;
                            }
                        }
                    }, 0);
                } else {
                    reader.accept(writer, 0);
                }
            }
        });
    }
}
